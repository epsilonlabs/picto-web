package org.eclipse.epsilon.picto.web;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.eclipse.epsilon.picto.dom.Picto;
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

  public final FileWatcher FILE_WATCHER = new FileWatcher(this);

  public PictoJsonController() {
    PictoPackage.eINSTANCE.eClass();
    FILE_WATCHER.start();
  }

  @GetMapping(path = "/picto", produces = MediaType.APPLICATION_JSON_VALUE)
  public String getPictoJson(String file, String path, String name, Model model) throws Exception {
    ViewContentCache x = FileViewContentCache.getViewContentCache(file);
    String result = "";
    if (x != null)
      result = x.getViewContentCache(path);
    return result;
  }

  @MessageMapping("/picto-web")
  @SendTo("/topic/picto")
  public void sendChangesToBroker(File modifiedFile) throws Exception {

    List<Object[]> list = new ArrayList<>();  
    for (PictoProject project : PictoProject.getPictoProjects()) {
      if (project.getFiles().contains(modifiedFile)) {
        list.add(new Object[]{modifiedFile, project});
      }
    }
    if (list.size() == 0) {
      return;
    }
    
    String modifiedFilePath = modifiedFile.getAbsolutePath()
        .replace(new File(PictoApplication.WORKSPACE).getAbsolutePath(), "").replace("\\", "/");

    WebEglPictoSource source = new WebEglPictoSource();
    //Map<String, String> modifiedObjects = source.transform(modifiedFilePath);
    Map<String, String> modifiedObjects = source.transform(modifiedFilePath);
    System.out.println("PICTO: number of modified objects = " + modifiedObjects.size());

    File pictoFile = modifiedFile;
    if (modifiedFile.getAbsolutePath().endsWith(".model") || modifiedFile.getAbsolutePath().endsWith(".flexmi")
        || modifiedFile.getAbsolutePath().endsWith(".xmi") || modifiedFile.getAbsolutePath().endsWith(".emf")) {
//			pictoFile = new File(modifiedFile.getAbsolutePath() + ".picto");
      modifiedFilePath += ".picto";
    }

    MessageChannel brokerChannel = context.getBean("brokerChannel", MessageChannel.class);
    for (Entry<String, String> entry : modifiedObjects.entrySet()) {
      SimpMessagingTemplate messaging = new SimpMessagingTemplate(brokerChannel);
//		messaging.setMessageConverter(new MappingJackson2MessageConverter());
      String topicName = "/topic/picto" + modifiedFilePath;
      messaging.convertAndSend(topicName, entry.getValue().getBytes());
    }

//		return modifiedObjects;
  }

}
