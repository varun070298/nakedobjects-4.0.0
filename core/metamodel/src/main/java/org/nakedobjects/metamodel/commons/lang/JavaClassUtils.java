package org.nakedobjects.metamodel.commons.lang;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public final class JavaClassUtils {

    private static final String JAVA_CLASS_PREFIX = "java.";

    private static Map<String, Class<?>> builtInClasses = new HashMap<String, Class<?>>();

    static {
        put(void.class);
        put(boolean.class);
        put(char.class);
        put(byte.class);
        put(short.class);
        put(int.class);
        put(long.class);
        put(float.class);
        put(double.class);
    }


	private static void put(Class<?> cls) {
		builtInClasses.put(cls.getName(), cls);
	}

	
    private JavaClassUtils() {}

    
	public static Class<?> getBuiltIn(String name) {
		return builtInClasses.get(name);
	}
    
    public static String[] getInterfaces(final Class<?> type) {
        final Class<?>[] interfaces = type.getInterfaces();
        final Class<?>[] nakedInterfaces = new Class[interfaces.length];
        int validInterfaces = 0;
        for (int i = 0; i < interfaces.length; i++) {
            nakedInterfaces[validInterfaces++] = interfaces[i];
        }

        final String[] interfaceNames = new String[validInterfaces];
        for (int i = 0; i < validInterfaces; i++) {
            interfaceNames[i] = nakedInterfaces[i].getName();
        }

        return interfaceNames;
    }

    public static String getSuperclass(final Class<?> type) {
        final Class<?> superType = type.getSuperclass();

        if (superType == null) {
            return null;
        }
        return superType.getName();
    }

    public static boolean isAbstract(final Class<?> type) {
        return Modifier.isAbstract(type.getModifiers());
    }

    public static boolean isFinal(final Class<?> type) {
        return Modifier.isFinal(type.getModifiers());
    }

    public static boolean isPublic(final Class<?> type) {
        return Modifier.isPublic(type.getModifiers());
    }

    public static boolean isJavaClass(final Class<?> type) {
        return type.getName().startsWith(JAVA_CLASS_PREFIX) || type.getName().startsWith("sun.");
    }

    public static boolean isStatic(final Method method) {
        return Modifier.isStatic(method.getModifiers());
    }
    
    public static boolean isPublic(final Method method) {
        return Modifier.isPublic(method.getModifiers());
    }
    
    public static List<Class<?>> toClasses(List<Object> objectList) {
	    List<Class<?>> classList = new ArrayList<Class<?>>();
	    for (Object service : objectList) {
	    classList.add(service.getClass());
	    }
	    return classList;
    }

    

}
