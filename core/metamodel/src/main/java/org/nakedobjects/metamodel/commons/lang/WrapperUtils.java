package org.nakedobjects.metamodel.commons.lang;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;



public final class WrapperUtils {

    private WrapperUtils() {}

    private static Map<Class<?>, Class<?>> wrapperClasses = new HashMap<Class<?>, Class<?>>();

    static {
        wrapperClasses.put(boolean.class, Boolean.class);
        wrapperClasses.put(byte.class, Byte.class);
        wrapperClasses.put(char.class, Character.class);
        wrapperClasses.put(short.class, Short.class);
        wrapperClasses.put(int.class, Integer.class);
        wrapperClasses.put(long.class, Long.class);
        wrapperClasses.put(float.class, Float.class);
        wrapperClasses.put(double.class, Double.class);
    }


	public static Class<?> wrap(final Class<?> primitiveClass) {
	    return wrapperClasses.get(primitiveClass);
	}


	public static Class<?>[] wrapAsNecessary(final Class<?>[] classes) {
	    final List<Class<?>> wrappedClasses = new ArrayList<Class<?>>();
	    for (final Class<?> cls : classes) {
	        if (cls.isPrimitive()) {
	            wrappedClasses.add((Class<?>) wrap(cls));
	        } else {
	            wrappedClasses.add(cls);
	        }
	    }
	    return wrappedClasses.toArray(new Class[] {});
	}

}
