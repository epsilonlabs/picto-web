package org.eclipse.epsilon.picto.web;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

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

	private static final String SLASH_STR = "/";

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
	@GetMapping(value = SLASH_STR)
	public String getPictoFiles(String information, String repo, String file, String branch, String revision,
			Model model) throws IOException {

		retrieveRepo(file, repo, branch, revision);

		String files = ProjectTreeToJson.convert(PictoApplication.WORKSPACE);
		model.addAttribute("pictofiles", files);

		return "pictofiles";
	}

	/***
	 * Retrieve source code from a remote/local Git repository.
	 * 
	 * @param repo
	 * @param file
	 * @param branch
	 * @param revision
	 * @throws InterruptedException
	 */
	private void retrieveRepo(String file, String repo, String branch, String revision) {

		FileWatcher.pause();

		try {
			if (repo != null && file != null) {

				PictoRepository pictoRepo = new PictoRepository();
				if (repo != null && file != null && branch == null && revision == null) {
					pictoRepo.retrievePicto(file, repo);
				} else if (branch != null && revision == null) {
					pictoRepo.retrievePicto(file, repo, branch);
				} else if (branch != null && revision != null) {
					pictoRepo.retrievePicto(file, repo, branch, revision);
				}

				if (!file.startsWith(SLASH_STR))
					file = SLASH_STR + file;

				String pictoFilePath = PictoApplication.WORKSPACE;

				String projectName = File.separator + repo.substring(repo.lastIndexOf(SLASH_STR) + 1, repo.length());
				if (repo != null) {
					String hash = PictoRepository.generateHash(repo, branch);
					if (file.startsWith(projectName)) {
						file = file.replaceFirst(projectName, SLASH_STR + hash);
					}
					if (!file.startsWith(projectName) && !file.startsWith(SLASH_STR + hash)) {
						pictoFilePath = pictoFilePath + projectName;
						pictoFilePath = pictoFilePath.replaceFirst(projectName, hash);
					}
				}
				pictoFilePath = pictoFilePath + file;
				pictoFilePath = pictoFilePath.replace(SLASH_STR, File.separator);

				// /home/alfa/projects/picto-web/org.eclipse.epsilon.picto.web/../workspace
				// /picto-web/examples/pos/pos.picto

				File pictoFile = new File(new File(pictoFilePath).getAbsolutePath());
				PictoProject.createPictoProject(pictoFile);

			}
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

		FileWatcher.unpause();
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
	public String getPicto(String file, String path, String repo, String branch, String revision, String timestamp,
			Model model) throws Exception {
		model.addAttribute("pictoName", file);

//  http://localhost:8081/picto?file=/picto-web/examples/pos/pos.picto&repo=https://github.com/epsilonlabs/picto-web&branch=optimised-detection

		retrieveRepo(file, repo, branch, revision);

		if (PictoCache.getViewContentCache(file) == null) {
			String filePath = PictoApplication.WORKSPACE + file;
			if (repo != null) {
				String projectName = File.separator + repo.substring(repo.lastIndexOf(SLASH_STR) + 1, repo.length());
				String hash = PictoRepository.generateHash(repo, branch);
				filePath = filePath.replaceFirst(projectName, hash);
			}

			File modifiedFile = new File(new File(filePath).getAbsolutePath());
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

		if (repo != null) {
			String projectName = repo.substring(repo.lastIndexOf(SLASH_STR) + 1, repo.length());
			String hash = PictoRepository.generateHash(repo, branch);
			file = file.replaceFirst(projectName, hash);
		}

		if (path == null) {
			PromiseView treePromiseView = PictoCache.getViewContentCache(file).getPromiseView(PictoCache.PICTO_TREE);
			String treeResult = (timestamp == null) ? treePromiseView.getViewContent()
					: treePromiseView.getViewContent(timestamp);
			model.addAttribute("treeResponse", treeResult);
		} else {
			PromiseView treePromiseView = PictoCache.getViewContentCache(file).getPromiseView(PictoCache.PICTO_TREE);
			String treeResult = (timestamp == null) ? treePromiseView.getViewContent()
					: treePromiseView.getViewContent(timestamp);
			model.addAttribute("treeResponse", treeResult);

			String viewResult = null;
			PromiseView promiseView = PictoCache.getViewContentCache(file).getPromiseView(path);
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
