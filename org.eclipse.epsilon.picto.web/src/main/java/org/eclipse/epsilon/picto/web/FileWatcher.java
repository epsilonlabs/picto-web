/*********************************************************************
* Copyright (c) 2022 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.epsilon.picto.web;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.beans.factory.annotation.Autowired;

/***
 * Monitor file changes.
 * 
 * @author Alfa Yohannis
 *
 */

public class FileWatcher extends Thread {

	public static FileWatcher FILE_WATCHER;

	@Autowired
	public PictoJsonController pictoJsonController;

	private boolean isRunning = false;
	private static boolean isPaused = false;

	private WatchService watcher;

	public FileWatcher() {
		this.setName(FileWatcher.class.getSimpleName());
	}

	public FileWatcher(PictoJsonController pictoJsonController) {
		this.pictoJsonController = pictoJsonController;
		this.setName(FileWatcher.class.getSimpleName());
	}

	public boolean isPaused() {
		return isPaused;
	}

	public static void pause() {
		isPaused = true;
	}

	public static void unpause() {
		isPaused = false;
	}

	public void terminate() throws IOException {
		isRunning = false;
		if (watcher != null)
			watcher.close();
	}

//  @MessageMapping("/gs-guide-websocket")
	public void notifyFileChange(File modifiedFile) throws Exception {
		if (this.pictoJsonController != null) {
			this.pictoJsonController.sendChangesToBroker(modifiedFile);
		} else {
			System.out.println("No PictoJsonController attached");
		}
	}

	@Override
	public void run() {
		try {
//      // System.out.println("YYYYY");
			HashMap<WatchKey, Path> keys = new HashMap<WatchKey, Path>();
			watcher = FileSystems.getDefault().newWatchService();
			registerDirectory(watcher, PictoApplication.WORKSPACE, keys);

			isRunning = true;

			firstWhile: while (isRunning) {

				WatchKey key;
				Path path;
				try {
					key = watcher.take();
					Thread.sleep(100);
					path = keys.get(key);
					if (path == null) {
						System.err.println("WatchKey not recognised!!");
						continue;
					}
				} catch (Exception ex) {
					continue;
				}

				if (isPaused) {
					continue;
				}

				List<WatchEvent<?>> events = key.pollEvents();
				for (WatchEvent<?> event : events) {

					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path filePath = ev.context();

					if (filePath.toString().endsWith(".picto")
//              || filePath.toString().endsWith(".egx")
//              || filePath.toString().endsWith(".egl") 
							|| filePath.toString().endsWith(".flexmi") || filePath.toString().endsWith(".model")
							|| filePath.toString().endsWith(".emf") || filePath.toString().endsWith(".xmi")) {

						File modifiedFile = new File(path.toString() + File.separator + filePath.toString());

						System.out.println("Modified file: " + modifiedFile.getAbsolutePath());

						boolean isDocumentValid = false;
						int counter = 0;
						while (!isDocumentValid) {
							try {
								counter += 1;
								DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
								DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
								documentBuilder.parse(modifiedFile);
								isDocumentValid = true;
								break;
							} catch (Exception e) {
								System.out.println("Error reading " + modifiedFile.getAbsolutePath());
								Thread.sleep(1000);
								System.out.println("Retry to read");
								
							}
							if (counter == 3) {
								System.out.println("Cannot read the file. Cancel further processes.");
								continue firstWhile;
							}
						}

						try {
							this.notifyFileChange(modifiedFile);
							break;
						} catch (Exception e) {
							e.printStackTrace();
							System.console();
						}
					}
				}

				boolean valid = key.reset();
				if (!valid) {
					// System.out.println("Picto: FileWatcher is not valid anymore!");
					break;
				}

			}
			watcher.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void registerDirectory(WatchService watcher, String directory, HashMap<WatchKey, Path> keys)
			throws Exception {
		Path dir = Paths.get(directory).toAbsolutePath();
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		keys.put(key, dir);
		// System.out.println("PICTO: Watch Service registered for dir: " + dir);

		File file = new File(directory).getAbsoluteFile();

		Files.list(Paths.get(file.toURI())).forEach(p -> {
			if (p.toFile().isDirectory()) {
				try {
					registerDirectory(watcher, p.toFile().getAbsolutePath(), keys);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});

//		for (File f : file.listFiles()) {
//			if (f.isDirectory()) {
//				registerDirectory(watcher, f.getAbsolutePath(), keys);
//			}
//		}
	}

	public static void scanPictoFiles() throws Exception {
		scanPictoFiles(PictoApplication.WORKSPACE);
	}

	private static void scanPictoFiles(String directory) throws Exception {
		Path path = (new File(directory)).getAbsoluteFile().toPath();
		Files.list(path).forEach(p -> {
//		for (File f : Files.list(file)) {
			File f = p.toFile();
			if (f.isDirectory()) {
				try {
					scanPictoFiles(f.getAbsolutePath());
				} catch (Exception e) {
				}
			}
			if (f.isFile() && f.getName().endsWith(".picto")) {
				// System.out.println("PICTO: Found Picto file: " + file.getAbsolutePath());
				try {
					PictoProject.createPictoProject(f);
				} catch (Exception e) {
				}
			}
		});
	}

	public PictoJsonController getPictoJsonController() {
		return pictoJsonController;
	}

	public void setPictoJsonController(PictoJsonController pictoJsonController) {
		this.pictoJsonController = pictoJsonController;
	}

	public static void startWatching() {
		if (FILE_WATCHER == null)
			FILE_WATCHER = new FileWatcher();
		FILE_WATCHER.start();
	}

	public static void setResponseController(PictoJsonController pictoJsonController) {
		if (FILE_WATCHER == null)
			FILE_WATCHER = new FileWatcher();
		FILE_WATCHER.setPictoJsonController(pictoJsonController);
	}

	public static void startWatching(PictoJsonController pictoJsonController) {
		if (FILE_WATCHER == null)
			FILE_WATCHER = new FileWatcher(pictoJsonController);
		FILE_WATCHER.start();
	}

	public static void stopWatching() throws IOException, InterruptedException {
		if (FILE_WATCHER != null) {
			FILE_WATCHER.terminate();
			FILE_WATCHER = null;
		}
	}
}
