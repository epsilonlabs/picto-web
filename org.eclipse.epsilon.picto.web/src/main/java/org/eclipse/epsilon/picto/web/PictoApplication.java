package org.eclipse.epsilon.picto.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.epsilon.picto.dom.PictoFactory;
import org.eclipse.epsilon.picto.dom.PictoPackage;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

/***
 * The main class for Picto Web Application
 * 
 * @author Alfa Yohannis
 *
 */
@SpringBootApplication
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

  private static boolean modelModificationRegeneratesAllViews = false;

  private static boolean eachRequestAlwaysRegeneratesView = false;

  private static List<PictoProject> pictoProjects = new ArrayList<PictoProject>();

  private static ConfigurableApplicationContext context;

  private static PictoWebOnLoadedListener pictoWebOnLoadedListener = new PictoWebOnLoadedListener() {
    @Override
    public void onLoaded() {
    }
  };

  private static PromisesGenerationListener promisesGenerationListener = new PromisesGenerationListener() {
    @Override
    public void onGenerated(java.util.Set<String> invalidatedViews) {
    };
  };



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
    context = SpringApplication.run(PictoApplication.class, args);
    PictoFactory.eINSTANCE.eClass();
    PictoPackage.eINSTANCE.eClass();
    FileWatcher.scanPictoFiles();
    FileWatcher.startWatching();
    pictoWebOnLoadedListener.onLoaded();
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

  public static void setPictoWebOnLoadedListener(PictoWebOnLoadedListener listener) {
    PictoApplication.pictoWebOnLoadedListener = listener;
  }

  @Override
  public void onApplicationEvent(ApplicationContextEvent event) {
//    if (event instanceof ContextStartedEvent) {
//      System.out.println("PICTO: started - " + Timestamp.from(Instant.now()).toString());
//    } else if (event instanceof ContextRefreshedEvent) {
//      System.out.println("PICTO: loaded - " + Timestamp.from(Instant.now()).toString());
//    } else if (event instanceof ContextStoppedEvent) {
//      System.out.println("PICTO: stopped - " + Timestamp.from(Instant.now()).toString());
//    } else if (event instanceof ContextClosedEvent) {
//      System.out.println("PICTO: context closed - " + Timestamp.from(Instant.now()).toString());
//    }
  }

  public static boolean getModelModificationRegeneratesAllViews() {
    return modelModificationRegeneratesAllViews;
  }

  public static void setModelModificationRegeneratesAllViews(boolean modelModificationRegeneratesAllViews) {
    PictoApplication.modelModificationRegeneratesAllViews = modelModificationRegeneratesAllViews;
  }

  public static boolean getEachRequestAlwaysRegeneratesView() {
    return eachRequestAlwaysRegeneratesView;
  }

  public static void setEachRequestAlwaysRegeneratesView(boolean eachRequestAlwaysRegeneratesView) {
    PictoApplication.eachRequestAlwaysRegeneratesView = eachRequestAlwaysRegeneratesView;
  }

  public static PromisesGenerationListener getPromisesGenerationListener() {
    return promisesGenerationListener;
  }

  public static void setPromisesGenerationListener(PromisesGenerationListener promisesGenerationListener) {
    PictoApplication.promisesGenerationListener = promisesGenerationListener;
  }


}
