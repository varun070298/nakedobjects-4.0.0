package org.nakedobjects.metamodel.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public final class ClassUtil {

    private ClassUtil() {}

    public static Object newInstance(final Class<?> type, final Class<?> constructorParamType, Object constructorArg) {
        return newInstance(type, new Class[]{constructorParamType}, new Object[]{constructorArg});
    }

    /**
     * Tries to instantiate using a constructor accepting the supplied arguments; if no such
     * constructor then falls back to trying the no-arg constructor.  
     */
    public static Object newInstance(final Class<?> type, final Class<?>[] constructorParamTypes, Object[] constructorArgs) {
        try {
            Constructor<?> constructor;
            try {
                constructor = type.getConstructor(constructorParamTypes);
                return constructor.newInstance(constructorArgs);
            } catch (NoSuchMethodException ex) {
                try {
                    constructor = type.getConstructor();
                    return constructor.newInstance();
                } catch (NoSuchMethodException e) {
                    throw new NakedObjectException(e);
                }
            }
        } catch (SecurityException ex) {
            throw new NakedObjectException(ex);
        } catch (final IllegalArgumentException e) {
            throw new NakedObjectException(e);
        } catch (final InstantiationException e) {
            throw new NakedObjectException(e);
        } catch (final IllegalAccessException e) {
            throw new NakedObjectException(e);
        } catch (final InvocationTargetException e) {
            throw new NakedObjectException(e);
        }
    }

    /**
     * Returns the supplied Class so long as it implements (or is a subclass of) the
     * required class, and also has either a constructor accepting the specified param type, 
     * or has a no-arg constructor.
     */
    public static Class<?> implementingClassOrNull(final Class<?> classCandidate, final Class<?> requiredClass, final Class<?> constructorParamType) {
        if (classCandidate == null) {
            return null;
        }
        if (!requiredClass.isAssignableFrom(classCandidate)) {
            return null;
        }
        try {
            try {
                classCandidate.getConstructor(new Class[] {constructorParamType});
            } catch (final NoSuchMethodException ex) {
                try {
                    classCandidate.getConstructor(new Class[]{});
                } catch (NoSuchMethodException e) {
                    return null;
                }
            }
        } catch (final SecurityException e) {
            throw null;
        }
        final int modifiers = classCandidate.getModifiers();
        if (!Modifier.isPublic(modifiers)) {
            return null;
        }
        return classCandidate;
    }

    public static Class<?> implementingClassOrNull(final String classCandidateName, final Class<?> requiredClass, final Class<?> constructorParamType) {
        if (classCandidateName == null) {
            return null;
        }
        Class<?> classCandidate = null;
        try {
            classCandidate = Class.forName(classCandidateName);
            return implementingClassOrNull(classCandidate, requiredClass, constructorParamType);
        } catch (final ClassNotFoundException e) {
            return null;
        }
    }

	public static boolean directlyImplements(final Class<?> cls, final Class<?> interfaceType) {
		for(Class<?> directlyImplementedInterface: cls.getInterfaces()) {
			if (directlyImplementedInterface == interfaceType) {
				return true;
			}
		}
		return false;
	}

}

// Copyright (c) Naked Objects Group Ltd.
