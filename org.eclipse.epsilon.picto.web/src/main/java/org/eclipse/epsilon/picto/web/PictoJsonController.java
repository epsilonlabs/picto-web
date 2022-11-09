package org.eclipse.epsilon.picto.web;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.epsilon.picto.dom.PictoPackage;
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

/***
 * Receive Requests and send back Responses
 * 
 * @author Alfa Yohannis
 *
 */
@RestController
@RequestMapping("/pictojson")
public class PictoJsonController {

  @Autowired
  private ApplicationContext context;

  @Autowired
  public SimpMessagingTemplate template;

  public PictoJsonController() {
    FileWatcher.setResponseController(this);
  }

  @GetMapping(path = "/picto", produces = MediaType.APPLICATION_JSON_VALUE)
  public String getPictoJson(String file, String path, String name, String hash, Model model) throws Exception {
    PromiseViewCache promiseViewCache = FileViewContentCache.getViewContentCache(file);
    String result = "";
    if (promiseViewCache != null) {
      PromiseView promiseView = promiseViewCache.getPromiseView(path);
      if (promiseView != null)
        result = promiseView.getViewContent(hash);
    }
    return result;
  }

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
      // Map<String, String> modifiedObjects = source.transform(modifiedFilePath);
      Map<String, String> modifiedObjects = source.generatePromises(modifiedFilePath, pictoProject);
      System.out.println("PICTO: number of modified objects = " + modifiedObjects.size());

      String pictoFilePath = pictoProject.getPictoFile().getAbsolutePath()
          .replace(new File(PictoApplication.WORKSPACE).getAbsolutePath(), "").replace("\\", "/");

      MessageChannel brokerChannel = context.getBean("brokerChannel", MessageChannel.class);
      for (Entry<String, String> entry : modifiedObjects.entrySet()) {
        String path = entry.getKey();
        String content = FileViewContentCache.getViewContentCache(pictoFilePath).getPromiseView(path).getViewContent();
        SimpMessagingTemplate messaging = new SimpMessagingTemplate(brokerChannel);
        String topicName = "/topic/picto" + pictoFilePath;
        messaging.convertAndSend(topicName, content.getBytes());
      }
    }

  }

}
