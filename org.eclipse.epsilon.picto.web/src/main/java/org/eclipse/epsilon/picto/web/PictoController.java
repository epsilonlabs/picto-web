package org.eclipse.epsilon.picto.web;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/***
 * This controller class receives requests from clients and send back responses
 * to display the main pages of Picto Web. This controller is only used when a
 * client make a request from the browser's url bar. The class doesn't handle
 * requests from clicking TreeView node.
 * 
 * @author Alfa Yohannis
 *
 */

@Controller
public class PictoController {

  /***
   * A controller method to display the page that shows the Picto files under
   * Workspace. It uses the 'pictofiles' template under 'resource/templates' for
   * the page.
   * 
   * @param information
   * @param model
   * @return
   * @throws IOException
   */
  @GetMapping(value = "/")
  public String getPictoFiles(String information, String repo, String file, String branch, String revision, Model model)
      throws IOException {

    try {
      if (repo != null && file != null) {
        FileWatcher.pause();
        PictoRepository pictoRepo = new PictoRepository();
        if (repo != null && file != null && branch == null && revision == null) {
          pictoRepo.retrievePicto(file, repo);
        } else if (branch != null && revision == null) {
          pictoRepo.retrievePicto(file, repo, branch);
        } else if (branch != null && revision != null) {
          pictoRepo.retrievePicto(file, repo, branch, revision);
        }
        FileWatcher.unpause();

        String pictoFilePath = PictoApplication.WORKSPACE + repo.substring(repo.lastIndexOf("/") + 1, repo.length())
            + File.separator + file;
        pictoFilePath = pictoFilePath.replace("/", File.separator);

        PictoProject.createPictoProject(new File(new File(pictoFilePath).getAbsolutePath()));
      }
    } catch (IOException | GitAPIException e) {
      e.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    }

    List<String> pictoFiles = ProjectTreeToJson.getPictoFiles(PictoApplication.WORKSPACE).stream()
        .map(s -> s.replace("\\", "/")).collect(Collectors.toList());
    model.addAttribute("pictofiles", pictoFiles);
    return "pictofiles";
  }

  /***
   * This controller method displays Picto Web's main page consists of two panels:
   * the tree view panel on the left side and the view panel on the right side. It
   * uses the 'picto' template under 'resource/templates' for the page.
   * 
   * @param file
   * @param path
   * @param name
   * @param timestamp
   * @param model
   * @return
   * @throws Exception
   */
  @GetMapping(value = "/picto")
  public String getPicto(String file, String path, String repo, String timestamp, Model model) throws Exception {
    model.addAttribute("pictoName", file);

//    List<PictoProject>  x = PictoApplication.getPictoProjects();

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
      String treeResult = (timestamp == null) ? treePromiseView.getViewContent()
          : treePromiseView.getViewContent(timestamp);
      model.addAttribute("treeResponse", treeResult);
    } else {
      PromiseView treePromiseView = FileViewContentCache.getViewContentCache(file)
          .getPromiseView(FileViewContentCache.PICTO_TREE);
      String treeResult = (timestamp == null) ? treePromiseView.getViewContent()
          : treePromiseView.getViewContent(timestamp);
      model.addAttribute("treeResponse", treeResult);

      String viewResult = null;
      PromiseView promiseView = FileViewContentCache.getViewContentCache(file).getPromiseView(path);
      if (promiseView != null) {
//        System.out.println("FROM URL BAR");
        viewResult = promiseView.getViewContent(timestamp);
        model.addAttribute("viewResponse", viewResult);
      }
      model.addAttribute("selectedUri", path);
    }

    return "picto";
  }

}
