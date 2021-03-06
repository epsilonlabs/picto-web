package org.eclipse.epsilon.picto.incrementality;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

import org.eclipse.epsilon.eol.execute.context.Variable;
import org.eclipse.epsilon.picto.LazyEgxModule.LazyGenerationRuleContentPromise;

public class Util {

	@SuppressWarnings("unchecked")
	public static String getPath(LazyGenerationRuleContentPromise content) {
		Variable pathVar = content.getVariables().stream().filter(v -> v.getName().equals("path")).findFirst()
				.orElse(null);
		Collection<String> path = (pathVar != null) ? ((Collection) pathVar.getValue()) : null;
		String pathStr = "/" + String.join("/", path);
		return pathStr;
	}

	public static boolean equals(String value1, String value2) {
		boolean result = false;
		if (value1 != null) {
			result = value1.equals(value2);
		} else if (value2 != null) {
			result = value2.equals(value1);
		} else {
			result = value1 == value2;
		}
		return result;
	}

	public static boolean equals(Object value1, Object value2) {
		boolean result = false;
		if (value1 != null) {
			result = value1.equals(value2);
		} else if (value2 != null) {
			result = value2.equals(value1);
		} else {
			result = value1 == value2;
		}
		return result;
	}
}
