package org.eclipse.epsilon.picto.web;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;
import org.springframework.context.event.ContextClosedEvent;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.ContextStartedEvent;
import org.springframework.context.event.ContextStoppedEvent;
import org.springframework.stereotype.Component;

/***
 * The main class for Picto Web Application
 * 
 * @author Alfa Yohannis
 *
 */
@SpringBootApplication
@Component
public class PictoApplication implements ApplicationListener<ApplicationContextEvent> {

  /***
   * Define the relative workspace target
   */
  public static final String WORKSPACE = ".." + File.separator + "workspace" + File.separator;

  /***
   * This contains the visualised Picto file's target
   */
  public static final List<File> PICTO_FILES = new ArrayList<File>();

  /***
   * To keep the arguments accessible.
   */
  public static String[] args;

  private static List<PictoProject> pictoProjects = new ArrayList<PictoProject>();

  private static ConfigurableApplicationContext context;

  /***
   * Initialise Picto Application
   * 
   * @param args
   * @throws Exception
   */
  public PictoApplication() throws Exception {
    String workDir = System.getProperty("user.dir");
    System.out.println("PICTO - Default Picto Application directory: " + workDir);
    System.out.println("PICTO - Workspace directory: " + (new File(WORKSPACE)).getAbsolutePath());
  }

  /***
   * Main program launcher
   * 
   * @param args
   * @throws Exception
   */
  public static void main(String[] args) throws Exception {
    PictoApplication.args = args;
    FileWatcher.scanPictoFiles();
    FileWatcher.startWatching();
    context = SpringApplication.run(PictoApplication.class, args);
  }

  /**
   * @return the pictoProjects
   */
  public static List<PictoProject> getPictoProjects() {
    return pictoProjects;
  }

  public static void exit() throws IOException, InterruptedException {
    FileWatcher.stopWatching();
    SpringApplication.exit(context, new ExitCodeGenerator() {
      @Override
      public int getExitCode() {
        return 0;
      }
    });
  }

  @Override
  public void onApplicationEvent(ApplicationContextEvent event) {
    if (event instanceof ContextStartedEvent) {
      System.out.println("PICTO: started - " + Timestamp.from(Instant.now()).toString());
    } else if (event instanceof ContextRefreshedEvent) {
      if (args != null) {
        synchronized (args) {
          args.notify();
        }
      }
      System.out.println("PICTO: loaded - " + Timestamp.from(Instant.now()).toString());
    } else if (event instanceof ContextStoppedEvent) {
      System.out.println("PICTO: stopped - " + Timestamp.from(Instant.now()).toString());
    } else if (event instanceof ContextClosedEvent) {
      System.out.println("PICTO: closed - " + Timestamp.from(Instant.now()).toString());
    }

  }
}
