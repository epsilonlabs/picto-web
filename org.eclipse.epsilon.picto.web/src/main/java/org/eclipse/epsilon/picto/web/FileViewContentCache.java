package org.eclipse.epsilon.picto.web;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.epsilon.picto.incrementality.AccessGraphResource;
import org.eclipse.epsilon.picto.incrementality.AccessRecordResource;

public class FileViewContentCache {

	public static final String PICTO_TREE = "/";
	private static final Map<String, ViewContentCache> fileViewContentCache = new HashMap<String, ViewContentCache>();
	private static final Map<String, AccessRecordResource> fileAccessRecordResources = new HashMap<String, AccessRecordResource>();

	public static Map<String, ViewContentCache> getMap() {
		return fileViewContentCache;
	}

	public static void clear() {
		fileViewContentCache.values().forEach(e -> e.clear());
		fileViewContentCache.clear();
		for (AccessRecordResource accessRecordResource : fileAccessRecordResources.values()) {
			accessRecordResource.clear();
		}
		fileAccessRecordResources.clear();
	}

	public static ViewContentCache addPictoFile(String pictoFilename) {
		ViewContentCache map = getViewContentCache(pictoFilename);
		if (map == null) {
			map = new ViewContentCache();
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

	public static ViewContentCache getViewContentCache(String pictoFilename) {
		return fileViewContentCache.get(pictoFilename);
	}
}
