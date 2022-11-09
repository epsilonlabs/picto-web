package org.eclipse.epsilon.picto.web;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.epsilon.picto.incrementality.AccessGraphResource;
import org.eclipse.epsilon.picto.incrementality.AccessRecordResource;

public class FileViewContentCache {

	public static final String PICTO_TREE = "/";
	
	//map 'picto file' --> 'path' and 'view cache' 
	private static final Map<String, PromiseViewCache> fileViewContentCache = new HashMap<String, PromiseViewCache>();
	
	//map  'picto file' --> 'access record resource' (e.g., accress graph resource)
	private static final Map<String, AccessRecordResource> fileAccessRecordResources = new HashMap<String, AccessRecordResource>();

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
}
