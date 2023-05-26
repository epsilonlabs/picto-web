package org.eclipse.epsilon.picto.web;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.eclipse.epsilon.picto.incrementality.AccessGraphResource;
import org.eclipse.epsilon.picto.incrementality.AccessRecordResource;
import org.eclipse.epsilon.picto.pictograph.Path;
import org.eclipse.epsilon.picto.pictograph.Property;

public class PictoCache {

	public static final String PICTO_TREE = "/";

	// map 'picto file' --> 'path' and 'view cache'
	private static final Map<String, PromiseViewCache> fileViewContentCache = new HashMap<String, PromiseViewCache>();

	// map 'picto file' --> 'access record resource' (e.g., accress graph resource)
	private static final Map<String, AccessRecordResource> fileAccessRecordResources = new HashMap<String, AccessRecordResource>();

	// map 'picto file' --> 'Root View Tree'
	private static final Map<String, JsTreeNode> rootJsTreeNodes = new HashMap<String, JsTreeNode>();

	// map 'picto file' --> 'View path to view tree maps'
	private static final Map<String, Map<String, JsTreeNode>> pathToJsTreeNodeMaps = new HashMap<String, Map<String, JsTreeNode>>();

	public static Map<String, PromiseViewCache> getMap() {
		return fileViewContentCache;
	}

	public static void clear() {
		fileViewContentCache.values().forEach(e -> e.clear());
		fileViewContentCache.clear();
		
		for (AccessRecordResource accessRecordResource : fileAccessRecordResources.values()) {
			accessRecordResource.clear();
		}
		fileAccessRecordResources.clear();

		pathToJsTreeNodeMaps.values().forEach(e -> e.clear());
		pathToJsTreeNodeMaps.clear();
		
		rootJsTreeNodes.clear();
	}

	public static JsTreeNode addCachedJsTreeNode(String pictoFilename, String path, JsTreeNode newJsTreeNode) {
		Map<String, JsTreeNode> map = pathToJsTreeNodeMaps.get(pictoFilename);
		if (map == null) {
			map = new HashMap<>();
			pathToJsTreeNodeMaps.put(pictoFilename, map);
		}
		JsTreeNode node = map.get(path);
		if (node == null) {
			node = newJsTreeNode;
			map.put(path, newJsTreeNode);
		}
		return node;
	}

	public static JsTreeNode getCachedJsTreeNode(String pictoFilename, String path) {
		Map<String, JsTreeNode> map = pathToJsTreeNodeMaps.get(pictoFilename);
		if (map == null) {
			map = new HashMap<>();
			pathToJsTreeNodeMaps.put(pictoFilename, map);
		}
		JsTreeNode node = map.get(path);
		return node;
	}

	public static void removeCachedJsTreeNode(String pictoFilename, Set<String> deletedPaths) {
		Map<String, JsTreeNode> map = pathToJsTreeNodeMaps.get(pictoFilename);
		for (String path : deletedPaths) {
			map.remove(path);
			JsTreeNode root = rootJsTreeNodes.get(pictoFilename);
			root.removeNode(path);
			System.console();
		}
	}

	/***
	 * 
	 * 
	 * @param pictoFilename
	 * @param rootJsTreeNode
	 * @return
	 */
	public static JsTreeNode cacheRootJsTreeNode(String pictoFilename, JsTreeNode rootJsTreeNode) {
		JsTreeNode jtn = rootJsTreeNodes.get(pictoFilename);
		if (jtn == null) {
			jtn = rootJsTreeNode;
			rootJsTreeNodes.put(pictoFilename, jtn);
		}
		return jtn;
	}

	public static PromiseViewCache addPictoFile(String pictoFilename) {
		PromiseViewCache map = getViewContentCache(pictoFilename);
		if (map == null) {
			map = new PromiseViewCache();
			fileViewContentCache.put(pictoFilename, map);
		}
		return map;

	}

	public static AccessRecordResource createAccessRecordResource(String pictoFilename) {
		AccessRecordResource accessResource = fileAccessRecordResources.get(pictoFilename);
		if (accessResource == null) {
			accessResource = new AccessGraphResource();
			fileAccessRecordResources.put(pictoFilename, accessResource);
		}
		return accessResource;
	}

	public static AccessRecordResource getAccessRecordResource(String pictoFilename) {
		return fileAccessRecordResources.get(pictoFilename);
	}

	public static PromiseViewCache getViewContentCache(String pictoFilename) {
		return fileViewContentCache.get(pictoFilename);
	}

	/***
	 * Remove all paths and properties that are flagged deleted.
	 */
	public static void removeDeletedPathsAndProperties() {

		Thread t = new Thread("RemoveDeletedPathsAndProperties") {
			public void run() {
				System.out.print("Removing deleted paths and properties ... ");

				for (Entry<String, AccessRecordResource> entry : fileAccessRecordResources.entrySet()) {
					AccessRecordResource accessRecordResource = entry.getValue();
					Map<String, Path> paths = accessRecordResource.getPaths();
					
//					while (AccessGraphResource.getExecutorService().getQueue().size() > 1
//							|| AccessGraphResource.getExecutorService().getActiveCount() > 1) {
//						try {
//							Thread.sleep(1000);
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}

					// remove deleted properties in each path
					paths.values().parallelStream().forEach(path -> {
						// remove properties from the path
						Iterator<Entry<String, Property>> propertyIterator = path.getProperties().entrySet().iterator();
						while (propertyIterator.hasNext()) {
							Entry<String, Property> propertyEntry = propertyIterator.next();
							Property property = propertyEntry.getValue();
							if (property.isDeleted())
								propertyIterator.remove();
						}
					});

					// remove deleted paths
					Iterator<Entry<String, Path>> pathIterator = paths.entrySet().iterator();
					while (pathIterator.hasNext()) {
						Entry<String, Path> pathEntry = pathIterator.next();
						Path path = pathEntry.getValue();
						if (path.isDeleted()) {
							pathIterator.remove();
							// remove path from the ViewTree
//					jsTreeNodeMap.remove(path.getName());
						}
					}
				}

				System.out.println("Done");
			}
//			} // run
		};
		AccessGraphResource.getExecutorService().submit(t);

	}

}
