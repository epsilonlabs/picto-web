package org.eclipse.epsilon.picto.web;

import java.util.HashMap;
import java.util.Map;

public class FileViewContentCache {

	public static final String PICTO_TREE = "/";
	private static final Map<String, ViewContentCache> fileViewContentCache = new HashMap<String, ViewContentCache>();
	
	public static Map<String, ViewContentCache> getMap() {
		return fileViewContentCache;
	}

	public static ViewContentCache addPictoFile(String pictoFilename) {
		ViewContentCache map = getViewContentCache(pictoFilename);
		if (map == null) {
			map = new ViewContentCache();
			fileViewContentCache.put(pictoFilename, map);
		}
		return map;
	}

	public static ViewContentCache getViewContentCache(String pictoFilename) {
		return fileViewContentCache.get(pictoFilename);
	}
}
