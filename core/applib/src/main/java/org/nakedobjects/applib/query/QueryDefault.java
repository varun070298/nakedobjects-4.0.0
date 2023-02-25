package org.nakedobjects.applib.query;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;


/**
 * Default implementation of {@link Query} that supports
 * parameter/argument values, along with a query name. 
 */
public class QueryDefault<T> extends QueryAbstract<T> {

	private static final long serialVersionUID = 1L;

	/**
	 * Convenience factory method, preferable to {@link #QueryDefault(Class, String, Object...) constructor}
	 * because will automatically genericize. 
	 */
	public static <Q> QueryDefault<Q> create(Class<Q> resultType, final String queryName,
			final Object... paramArgs) {
		return new QueryDefault<Q>(resultType, queryName, paramArgs);
	}

	/**
	 * Convenience factory method, preferable to {@link #QueryDefault(Class, String, Map) constructor}
	 * because will automatically genericize. 
	 */
	public static <Q> QueryDefault<Q> create(Class<Q> resultType, final String queryName,
			final Map<String, Object> argumentsByParameterName) {
		return new QueryDefault<Q>(resultType, queryName, argumentsByParameterName);
	}


	/**
	 * Converts a list of objects [a, 1, b, 2] into a map {a -> 1; b -> 2}
	 */
	private static Map<String, Object> asMap(Object[] paramArgs) {
		HashMap<String, Object> map = new HashMap<String, Object>();
		boolean param = true;
		String paramStr = null;
		for (Object paramArg : paramArgs) {
			if (param) {
				if (paramArg instanceof String) {
					paramStr = (String) paramArg;
				} else {
					throw new IllegalArgumentException(
							"Parameter must be a string");
				}
			} else {
				Object arg = paramArg;
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

	
	private final String queryName;
	private final Map<String, Object> argumentsByParameterName;

	public QueryDefault(Class<T> resultType, final String queryName,
			final Object... paramArgs) {
		this(resultType, queryName, asMap(paramArgs));
	}

	public QueryDefault(Class<T> resultType, final String queryName,
			final Map<String, Object> argumentsByParameterName) {
		super(resultType);
		this.queryName = queryName;
		this.argumentsByParameterName = argumentsByParameterName;
	}

	public String getQueryName() {
		return queryName;
	}

	public Map<String, Object> getArgumentsByParameterName() {
		return Collections.unmodifiableMap(argumentsByParameterName);
	}

	public String getDescription() {
		return getQueryName() + " with " + getArgumentsByParameterName();
	}

}
// Copyright (c) Naked Objects Group Ltd.
