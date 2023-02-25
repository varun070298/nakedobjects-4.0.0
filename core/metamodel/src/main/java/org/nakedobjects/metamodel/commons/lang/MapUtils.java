package org.nakedobjects.metamodel.commons.lang;

import java.util.HashMap;
import java.util.Map;

public final class MapUtils {
	
	private MapUtils() {}
	
	/**
	 * Converts a list of objects [a, 1, b, 2] into a map {a -> 1; b -> 2}
	 */
	public static Map<String, String> asMap(String... paramArgs) {
		HashMap<String, String> map = new HashMap<String, String>();
		boolean param = true;
		String paramStr = null;
		for (String paramArg : paramArgs) {
			if (param) {
				if (paramArg instanceof String) {
					paramStr = (String) paramArg;
				} else {
					throw new IllegalArgumentException(
							"Parameter must be a string");
				}
			} else {
				String arg = paramArg;
				map.put(paramStr, arg);
				paramStr = null;
			}
			param = !param;
		}
		if (paramStr != null) {
			throw new IllegalArgumentException(
					"Must have equal number of parameters and arguments");
		}
		return map;
	}


}
