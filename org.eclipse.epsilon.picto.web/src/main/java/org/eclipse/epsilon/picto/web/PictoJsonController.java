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
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

/***
 * A controller to receive JSON requests from clicking the TreeView nodes inside
 * the TreeView panel on the left side ('getPictoJson' method). This controller
 * also sends back JSON objects to clients via a STOMP broker server
 * ('sendChangesToBroker' method) triggered by (1) Request from clicking
 * TreeView nodes, and (2) Modifying a file monitored by Picto Web.
 * 
 * @author Alfa Yohannis
 *
 */
@RestController
@RequestMapping("/pictojson")
public class PictoJsonController {

  /*** The ApplicationContext of Spring Boot ***/
  @Autowired
  private ApplicationContext context;

  /***
   * The constructor to attach the controller to the FileWatcher that monitors
   * changes on files so that new views can be send to the client via a STOMP
   * broker server.
   * 
   */
  public PictoJsonController() {
    FileWatcher.setResponseController(this);
  }

  /***
   * This method receives the JSON requests from clients that come by clicking the
   * TreeView nodes on the left panel.
   * 
   * @param file
   * @param path
   * @param name
   * @param timestamp
   * @param model
   * @return
   * @throws Exception
   */
  @GetMapping(path = "/picto", produces = MediaType.APPLICATION_JSON_VALUE)
  public String getPictoJson(String file, String path, String timestamp, Model model) throws Exception {
    if (FileViewContentCache.getViewContentCache(file) == null) {
      File modifiedFile = new File(new File(PictoApplication.WORKSPACE + file).getAbsolutePath());
      Set<PictoProject> affectedPictoProjects = new HashSet<>();
      for (PictoProject project : PictoApplication.getPictoProjects()) {
        if (project.getFiles().contains(modifiedFile)) {
          affectedPictoProjects.add(project);
        }
      }
      for (PictoProject pictoProject : affectedPictoProjects) {
        WebEglPictoSource source = new WebEglPictoSource();
        source.generatePromises(file, pictoProject);
      }
    }

    PromiseViewCache promiseViewCache = FileViewContentCache.getViewContentCache(file);
    String result = "";
    if (promiseViewCache != null) {
      PromiseView promiseView = promiseViewCache.getPromiseView(path);
      if (promiseView != null)
//        System.out.println("FROM CLICKING NODE");
        result = promiseView.getViewContent(timestamp);
    }

    return result;
  }

  /***
   * This method sends new views to clients via a STOMP broker server triggered by
   * (1) Clicking on the TreeView nodes on the left panel of clients, and (2)
   * Modifying files monitored by Picto Web.
   * 
   * @param modifiedFile
   * @throws Exception
   */
  @MessageMapping("/picto-web")
  @SendTo("/topic/picto")
  public void sendChangesToBroker(File modifiedFile) throws Exception {

    Set<PictoProject> affectedPictoProjects = new HashSet<>();
    for (PictoProject project : PictoApplication.getPictoProjects()) {
      if (project.getFiles().contains(modifiedFile)) {
        affectedPictoProjects.add(project);
      }
    }
    for (PictoProject pictoProject : affectedPictoProjects) {

      String modifiedFilePath = modifiedFile.getAbsolutePath()
          .replace(new File(PictoApplication.WORKSPACE).getAbsolutePath(), "").replace("\\", "/");

      WebEglPictoSource source = new WebEglPictoSource();

      Set<String> modifiedObjects = source.generatePromises(modifiedFilePath, pictoProject, true);

      String pictoFilePath = pictoProject.getPictoFile().getAbsolutePath()
          .replace(new File(PictoApplication.WORKSPACE).getAbsolutePath(), "").replace("\\", "/");

      MessageChannel brokerChannel = context.getBean("brokerChannel", MessageChannel.class);
      System.out.println("Sending " + modifiedObjects.size() + " views");

      PictoResponse response = new PictoResponse();
      response.setType("newviews");
      response.setFilename(pictoFilePath);

      ObjectMapper mapper = new ObjectMapper();
      String affectedViews = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(modifiedObjects);
      response.setContent(affectedViews);
      String content = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(response);

      SimpMessagingTemplate messaging = new SimpMessagingTemplate(brokerChannel);
      String topicName = "/topic/picto" + pictoFilePath;
      messaging.convertAndSend(topicName, content.getBytes());
    }
  }

}
