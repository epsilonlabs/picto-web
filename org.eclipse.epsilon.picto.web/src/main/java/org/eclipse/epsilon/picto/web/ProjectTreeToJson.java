package org.eclipse.epsilon.picto.web;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

/***
 * Convert a filepath to a json tree structure that conforms to JsTree.com's
 * tree structure.
 * 
 * @author Alfa Yohannis
 *
 */
public class ProjectTreeToJson {

	public static void main(String[] args) throws IOException, GitAPIException {

		String json = ProjectTreeToJson.convert("");
		System.out.println(json);

	}

	public static List<String> getPictoFiles(String path) throws IOException {
		
		List<String> paths = Files.walk(Paths.get(path)).filter(p -> !Files.isDirectory(p)).map(p -> p.toString())
				.filter(f -> f.toLowerCase().endsWith(".picto")).collect(Collectors.toList());
		
		paths = paths.stream().map(s -> s.replace(PictoApplication.WORKSPACE, "").replace("\\", "/"))
				.collect(Collectors.toList());
		
		return paths;
	}

	/***
	 * Convert a filepath to a json tree structure that conforms to JsTree.com's
	 * tree structure.
	 * 
	 * @param target
	 * @return
	 * @throws IOException
	 * @throws GitAPIException
	 */
	public static String convert(String rootPath) throws IOException {
		List<String> paths = getPictoFiles(rootPath);

		ObjectMapper mapper = new ObjectMapper();
		ArrayNode root = mapper.createArrayNode();

		for (String path : paths) {
			File f = new File(PictoApplication.WORKSPACE + "/" + path);

			Repository repository = PictoRepository.getRepo(f);

			String repoUrl = null;
			String branch = null;
			if (repository != null) {
				repoUrl = repository.getConfig().getString("remote", "origin", "url");
				branch = repository.getBranch();
			}

			System.out.println(path);
			String[] fragments = path.split("\\/|\\\\");

			ObjectNode parent = null;
			for (int i = 0; i < fragments.length; i++) {
				String fragment = fragments[i];
				if (fragment.equals("..") || fragment.equals(".") || fragment.equals("workspace"))
					continue;

				ObjectNode fileNode = null;
				if (parent == null) {
					JsonNode node = contains(root, fragment);
					if (node != null) {
						parent = (ObjectNode) node;
						continue;
					}
				} else {
					ArrayNode children = (ArrayNode) parent.get("children");
					JsonNode node = contains(children, fragment);
					if (node != null) {
						parent = (ObjectNode) node;
						continue;
					}
				}

				fileNode = mapper.createObjectNode();
				if (i < fragments.length - 1) {
					fileNode.put("text", fragment);
					fileNode.put("icon", "icons/folder.gif");
//					ObjectNode stateNode = fileNode.putObject("state");
//					stateNode.put("opened", true);
					fileNode.putArray("children");
				} else {
					fileNode.put("text", fragment);
					fileNode.put("icon", "icons/document.gif");
					if (repoUrl != null)
						fileNode.put("repo", repoUrl);
					if (branch != null)
						fileNode.put("branch", branch);
				}
				if (parent != null) {
					ArrayNode children = (ArrayNode) parent.get("children");
					children.add(fileNode);
				} else {
					root.add(fileNode);
				}
				parent = fileNode;
			}
		}

		String json = mapper.writerWithDefaultPrettyPrinter().writeValueAsString(root);
		return json;
	}

	private static JsonNode contains(ArrayNode root, String fragment) {
		Iterator<JsonNode> iterator = root.iterator();
		while (iterator.hasNext()) {
			JsonNode temp = iterator.next();
			if (fragment.equals(temp.get("text").asText())) {
				return temp;
			}
		}
		return null;
	}
}
