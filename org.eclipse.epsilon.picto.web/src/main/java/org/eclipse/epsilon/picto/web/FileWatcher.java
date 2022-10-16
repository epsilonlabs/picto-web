package org.eclipse.epsilon.picto.web;

import static java.nio.file.StandardWatchEventKinds.ENTRY_CREATE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_DELETE;
import static java.nio.file.StandardWatchEventKinds.ENTRY_MODIFY;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;

/***
 * Monitor file changes.
 * 
 * @author Alfa Yohannis
 *
 */

public class FileWatcher extends Thread {

	@Autowired
	public PictoJsonController pictoJsonController;
	/**
	 * 
	 */
	private boolean isRunning = false;

	public FileWatcher() {
		this.setName(FileWatcher.class.getSimpleName());
	}

	public FileWatcher(PictoJsonController pictoJsonController) {
		this.pictoJsonController = pictoJsonController;
		this.setName(FileWatcher.class.getSimpleName());
	}

	public void terminate() {
		isRunning = false;
	}

	@MessageMapping("/gs-guide-websocket")
	public void notifyFileChange(File modelFile) throws Exception {
		if (this.pictoJsonController != null) {
			this.pictoJsonController.sendBackFileUpdate(modelFile);
		} else {
			System.out.println("No PictoJsonController attached");
		}
	}

	@Override
	public void run() {
		try {
			HashMap<WatchKey, Path> keys = new HashMap<WatchKey, Path>();
			WatchService watcher = FileSystems.getDefault().newWatchService();
			registerDirectory(watcher, PictoApplication.WORKSPACE, keys);
			isRunning = true;

			while (isRunning) {

				WatchKey key;
				Path path;
				try {
					key = watcher.take();
					path = keys.get(key);
					if (path == null) {
						System.err.println("WatchKey not recognized!!");
						continue;
					}
				} catch (InterruptedException ex) {
					return;
				}

				for (WatchEvent<?> event : key.pollEvents()) {
//					WatchEvent.Kind<?> kind = event.kind();
//					System.out.println(kind);

					@SuppressWarnings("unchecked")
					WatchEvent<Path> ev = (WatchEvent<Path>) event;
					Path filePath = ev.context();
					Path parent = ev.context().getParent();

					if (filePath.toString().endsWith(".picto") || filePath.toString().endsWith(".egx")
							|| filePath.toString().endsWith(".egl") || filePath.toString().endsWith(".flexmi")
							|| filePath.toString().endsWith(".model") || filePath.toString().endsWith(".emf")
							|| filePath.toString().endsWith(".xmi")) {
						System.out.println("Picto: " + filePath + " has changed!!!");

						File modelFile = new File(path.toString() + File.separator + filePath.toString());
						this.notifyFileChange(modelFile);
					}
				}

				boolean valid = key.reset();
				if (!valid) {
					System.out.println("Picto: FileWatcher is not valid anymore!");
					break;
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void registerDirectory(WatchService watcher, String directory, HashMap<WatchKey, Path> keys)
			throws IOException {
		Path dir = Paths.get(directory).toAbsolutePath();
		WatchKey key = dir.register(watcher, ENTRY_CREATE, ENTRY_DELETE, ENTRY_MODIFY);
		keys.put(key, dir);
		System.out.println("PICTO: Watch Service registered for dir: " + dir);

		File file = new File(directory);
		for (File f : file.listFiles()) {
			if (f.isDirectory()) {
				registerDirectory(watcher, f.getAbsolutePath(), keys);
			}
		}
	}
}