/*********************************************************************
* Copyright (c) 2008 The University of York.
*
* This program and the accompanying materials are made
* available under the terms of the Eclipse Public License 2.0
* which is available at https://www.eclipse.org/legal/epl-2.0/
*
* SPDX-License-Identifier: EPL-2.0
**********************************************************************/

package org.eclipse.epsilon.picto.web;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.stream.Collectors;

import org.eclipse.epsilon.picto.dom.PictoFactory;
import org.eclipse.epsilon.picto.dom.PictoPackage;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.event.ApplicationContextEvent;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

/***
 * The main class for Picto Web Application
 * 
 * @author Alfa Yohannis
 *
 */

@SpringBootApplication
public class PictoApplication implements ApplicationListener<ApplicationContextEvent>, Callable<Integer> {

  /***
   * Define the relative workspace target
   */
  public static final String WORKSPACE = ".." + File.separator + "workspace" + File.separator;
  public static final String TEMP = ".." + File.separator + "temp" + File.separator;

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

  private static CommandLine commandLine;

  @Option(names = { "-ni",
      "--non-incremental" }, description = "Use no incrementality. It implies that views generation "
          + "would be greedy. All views are (re)generated when a change happened. Options: false (default), true.")
  private static boolean nonIncremental = false;

  @Option(names = { "-nc",
      "--no-cache" }, description = "No-cache, every request always (re)generates a view. Options: false (default), true.")
  private static boolean isNoCache = false;

  /***
   * Main program launcher
   * 
   * @param args
   * @throws Exception
   */
  @Command(name = "Picto-Web", mixinStandardHelpOptions = true, version = "0.1.2-alpha", //
      description = "A High-performance Tool for Complex Model Exploration.")
  public static void main(String... args) throws Exception {

    PictoApplication.args = args;

    commandLine = new CommandLine(new PictoApplication());
    commandLine.execute(args);

    context = SpringApplication.run(PictoApplication.class, args);
    PictoFactory.eINSTANCE.eClass();
    PictoPackage.eINSTANCE.eClass();
    FileWatcher.scanPictoFiles();
    FileWatcher.startWatching();
    pictoWebOnLoadedListener.onLoaded();
  }

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

  public Integer call() throws Exception {
    return 0;
  }

  /**
   * @return the pictoProjects
   */
  public static List<PictoProject> getPictoProjects() {
    return pictoProjects;
  }

  public static void exit() throws IOException, InterruptedException {
    FileWatcher.stopWatching();
    FileViewContentCache.clear();
//    ExecutorService px = PromiseView.getPromiseExecutor();
//    px.shutdown();
//    context.close();
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

  public static boolean isNoCache() {
    return isNoCache;
  }

  public static void setNoCache(boolean isNoCache) {
    PictoApplication.isNoCache = isNoCache;
  }

  public static boolean isNonIncremental() {
    return nonIncremental;
  }

  public static void setNonIncremental(boolean nonIncremental) {
    PictoApplication.nonIncremental = nonIncremental;
  }

  public static PromisesGenerationListener getPromisesGenerationListener() {
    return promisesGenerationListener;
  }

  public static void setPromisesGenerationListener(PromisesGenerationListener promisesGenerationListener) {
    PictoApplication.promisesGenerationListener = promisesGenerationListener;
  }

}
