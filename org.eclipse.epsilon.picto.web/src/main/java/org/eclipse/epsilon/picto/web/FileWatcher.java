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
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;

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

  private WatchService watcher;

  public FileWatcher() {
    this.setName(FileWatcher.class.getSimpleName());
  }

  public FileWatcher(PictoJsonController pictoJsonController) {
    this.pictoJsonController = pictoJsonController;
    this.setName(FileWatcher.class.getSimpleName());
  }

  public void terminate() throws IOException {
    isRunning = false;
    if (watcher != null)
      watcher.close();
  }

  @MessageMapping("/gs-guide-websocket")
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
//      System.out.println("YYYYY");
      HashMap<WatchKey, Path> keys = new HashMap<WatchKey, Path>();
      watcher = FileSystems.getDefault().newWatchService();
      registerDirectory(watcher, PictoApplication.WORKSPACE, keys);

      isRunning = true;

      while (isRunning) {

        WatchKey key;
        Path path;
        try {
          key = watcher.take();
          Thread.sleep(100);
          path = keys.get(key);
//          System.out.println("XXX: " + path);
          if (path == null) {
            System.err.println("WatchKey not recognized!!");
            continue;
          }
        } catch (Exception ex) {
          // ex.printStackTrace();
          return;
        }

        List<WatchEvent<?>> events = key.pollEvents();
        for (WatchEvent<?> event : events) {
          
          @SuppressWarnings("unchecked")
          WatchEvent<Path> ev = (WatchEvent<Path>) event;
          Path filePath = ev.context();

          if (filePath.toString().endsWith(".picto")
//              || filePath.toString().endsWith(".egx")
//              || filePath.toString().endsWith(".egl") 
              || filePath.toString().endsWith(".flexmi")
              || filePath.toString().endsWith(".model") || filePath.toString().endsWith(".emf")
              || filePath.toString().endsWith(".xmi")) {
            // System.out.println("Picto: " + filePath + " has changed!!!");

            
            File modifiedFile = new File(path.toString() + File.separator + filePath.toString());
            
//            System.out.println("Modified file: " + modifiedFile.getAbsolutePath());
            this.notifyFileChange(modifiedFile);
          }
        }

        boolean valid = key.reset();
        if (!valid) {
          System.out.println("Picto: FileWatcher is not valid anymore!");
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
    System.out.println("PICTO: Watch Service registered for dir: " + dir);

    File file = new File(directory);
    for (File f : file.listFiles()) {
      if (f.isDirectory()) {
        registerDirectory(watcher, f.getAbsolutePath(), keys);
      }
    }
  }

  public static void scanPictoFiles() throws Exception {
    scanPictoFiles(PictoApplication.WORKSPACE);
  }

  private static void scanPictoFiles(String directory) throws Exception {
    File file = new File(directory);
    for (File f : file.listFiles()) {
      if (f.isDirectory()) {
        scanPictoFiles(f.getAbsolutePath());
      }
      if (f.isFile() && f.getName().endsWith(".picto")) {
        System.out.println("PICTO: Found Picto file: " + file.getAbsolutePath());
        PictoProject.createPictoProject(f);
      }
    }
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
