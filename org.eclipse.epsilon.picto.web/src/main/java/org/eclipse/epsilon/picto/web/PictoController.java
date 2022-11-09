package org.eclipse.epsilon.picto.web;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/***
 * Receive Requests and send back Responses
 * 
 * @author Alfa Yohannis
 *
 */

@Controller
public class PictoController {

  @GetMapping(value = "/")
  public String getPictoFiles(String information, Model model) throws IOException {
    List<String> pictoFiles = ProjectTreeToJson.getPictoFiles(PictoApplication.WORKSPACE).stream()
        .map(s -> s.replace("\\", "/")).collect(Collectors.toList());
    model.addAttribute("pictofiles", pictoFiles);
    return "pictofiles";
  }

  @GetMapping(value = "/picto")
  public String getPicto(String file, String path, String name, String hash, Model model) throws Exception {
    model.addAttribute("pictoName", file);

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
    if (path == null) {
      PromiseView treePromiseView = FileViewContentCache.getViewContentCache(file)
          .getPromiseView(FileViewContentCache.PICTO_TREE);
      String treeResult = (hash == null) ? treePromiseView.getViewContent() : treePromiseView.getViewContent(hash);
      model.addAttribute("treeResponse", treeResult);
    } else {
      PromiseView treePromiseView = FileViewContentCache.getViewContentCache(file)
          .getPromiseView(FileViewContentCache.PICTO_TREE);
      String treeResult = (hash == null) ? treePromiseView.getViewContent() : treePromiseView.getViewContent(hash);
      model.addAttribute("treeResponse", treeResult);

      String viewResult = null;
      PromiseView promiseView = FileViewContentCache.getViewContentCache(file).getPromiseView(path);
      if (promiseView != null) {
        viewResult = promiseView.getViewContent(hash);
        model.addAttribute("viewResponse", viewResult);
      }
      model.addAttribute("selectedUri", path);
    }

    return "picto";
  }

//	@MessageMapping("/treeview")
//	@SendTo("/topic/picto")
//	public PictoResponse getTreeView(PictoRequest message) throws Exception {
//		WebEglPictoSource source = new WebEglPictoSource();
//		File pictoFile = new File((new File(PictoApplication.WORKSPACE + message.getPictoFile())).getAbsolutePath());
//		source.transform(pictoFile);
//		String filename = pictoFile.getAbsolutePath()
//				.replace(new File(PictoApplication.WORKSPACE).getAbsolutePath() + File.separator, "")
//				.replace("\\", "/");
//		Object x = FileViewContentCache.getMap();
//		String result = FileViewContentCache.getElementViewContentMap(filename)
//				.getElementViewTree(FileViewContentCache.PICTO_TREE);
//		PictoResponse pictoResponse = new PictoResponse(result);
//		pictoResponse.setType("TreeView");
//
//		MessageChannel brokerChannel = context.getBean("brokerChannel", MessageChannel.class);
//		SimpMessagingTemplate messaging = new SimpMessagingTemplate(brokerChannel);
//		messaging.setMessageConverter(new MappingJackson2MessageConverter());
//		messaging.convertAndSend("/topic/picto/" + message.getPictoFile(), pictoResponse);
//
//		return pictoResponse;
//	}

//	@MessageMapping("/picto-web")
//	@SendTo("/topic/picto")
//	public PictoResponse sendBackFileUpdate(File modifiedFile) throws Exception {
//		WebEglPictoSource source = new WebEglPictoSource();
//		source.transform(modifiedFile);
//		String filename = modifiedFile.getAbsolutePath()
//				.replace(new File(PictoApplication.WORKSPACE).getAbsolutePath() + File.separator, "")
//				.replace("\\", "/");
//		String result = FileViewContentCache.getElementViewContentMap(filename)
//				.getElementViewTree(FileViewContentCache.PICTO_TREE);
//		PictoResponse pictoResponse = new PictoResponse(result);
//		pictoResponse.setType("TreeView");
//
//		MessageChannel brokerChannel = context.getBean("brokerChannel", MessageChannel.class);
//		SimpMessagingTemplate messaging = new SimpMessagingTemplate(brokerChannel);
//		messaging.setMessageConverter(new MappingJackson2MessageConverter());
//		messaging.convertAndSend("/topic/picto/" + modifiedFile.getName(), pictoResponse);
//
//		return pictoResponse;
//	}

//	@MessageMapping("/projecttree")
//	@SendTo("/topic/picto")
//	public PictoResponse getProjectTree(PictoRequest message) throws Exception {
//
//		String temp = ProjectTreeToJson.convert(PictoApplication.WORKSPACE);
//		PictoResponse pictoResponse = new PictoResponse(temp);
//		pictoResponse.setType("ProjectTree");
//		return pictoResponse;
//	}
//
//	@MessageMapping("/openfile")
//	@SendTo("/topic/picto")
//	public PictoResponse openFile(PictoRequest message) throws Exception {
//
//		String temp = Files.readString(Paths.get(PictoApplication.WORKSPACE + message.getCode()));
//		PictoResponse pictoResponse = new PictoResponse(temp);
//		pictoResponse.setType("OpenFile");
//		return pictoResponse;
//	}
}
