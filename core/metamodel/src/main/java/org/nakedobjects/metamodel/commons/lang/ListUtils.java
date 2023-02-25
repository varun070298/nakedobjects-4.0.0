package org.nakedobjects.metamodel.commons.lang;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;


public final class ListUtils {
    private static final String DEFAULT_DELIMITER = ",";

	private ListUtils() {}

    /**
     * Returns list1 with everything in list2, ignoring duplicates.
     */
    public static <T> List<T> combine(final List<T> list1, final List<T> list2) {
        for (final Iterator<T> iter = list2.iterator(); iter.hasNext();) {
            final T obj = iter.next();
            if (!(list1.contains(obj))) {
                list1.add(obj);
            }
        }
        return list1;
    }

    public static List<String> combine(final String[] array1, final String[] array2) {
        final List<String> prefixes = new ArrayList<String>();
        addNoDuplicates(array1, prefixes);
        addNoDuplicates(array2, prefixes);
        return prefixes;
    }

    private static void addNoDuplicates(final String[] array, final List<String> list) {
        for (int i = 0; i < array.length; i++) {
            if (!list.contains(array[i])) {
                list.add(array[i]);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public
    static List<Object> asList(final Object[] objectArray) {
        final List<Object> list = new ArrayList<Object>();
        for (int i = 0; i < objectArray.length; i++) {
            if (Collection.class.isAssignableFrom(objectArray[i].getClass())) {
                Collection collection = (Collection) objectArray[i];
                list.addAll(asList(collection.toArray()));
            } else {
                list.add(objectArray[i]);
            }
        }
        return list;
    }

    /**
     * @see #listToString(List, String)
	 * @see #stringToList(String)
     */
	public static String listToString(List<String> list) {
		return listToString(list, DEFAULT_DELIMITER);
	}

	/**
	 * @see #listToString(List, String)
	 * @see #stringToList(String)
	 */
	public static String listToString(List<String> list, String delimiter) {
		if (list.size() == 0) {
			return null;
		}
		StringBuilder buf = new StringBuilder();
		boolean first = true;
		for(String str: list) {
			if(first) {
				first = false;
			} else {
				buf.append(delimiter);
			}
			buf.append(str);
		}
		return buf.toString();
	}

	/**
	 * @see #stringToList(String, String)
	 * @see #listToString(List)
	 */
	public static List<String> stringToList(String commaSeparated) {
		return appendDelimitedStringToList(commaSeparated, new ArrayList<String>());
	}

	/**
	 * @see #stringToList(String)
	 * @see #listToString(List, String)
	 */
	public static List<String> stringToList(String delimited, String delimiter) {
		return appendDelimitedStringToList(delimited, delimiter, new ArrayList<String>());
	}

	/**
	 * @see #appendDelimitedStringToList(String, String, List)
	 */
	public static List<String> appendDelimitedStringToList(
			String commaSeparated, List<String> list) {
		return appendDelimitedStringToList(commaSeparated, DEFAULT_DELIMITER, list);
	}

	public static List<String> appendDelimitedStringToList(
			String delimited, String delimiter, List<String> list) {
		if (delimited == null) {
			return list;
		}
		String[] optionValues = delimited.split(delimiter);
		list.addAll(Arrays.asList(optionValues));
		return list;
	}

}

// Copyright (c) Naked Objects Group Ltd.
