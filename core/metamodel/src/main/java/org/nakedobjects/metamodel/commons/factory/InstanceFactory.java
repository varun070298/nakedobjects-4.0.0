package org.nakedobjects.metamodel.commons.factory;

import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.lang.CastUtils;


public class InstanceFactory {

    public static Object createInstance(final String className) {
        return createInstance(className, (Class<?>)null, null);
    }

    public static Object createInstance(final Class<?> cls) {
        return createInstance(cls, (Class<?>)null, null);
    }

    public static <T> T createInstance(final String className, final Class<T> requiredClass) {
        return createInstance(className, (Class<T>)null, requiredClass);
    }

    public static <T> T createInstance(final Class<?> cls, final Class<T> requiredClass) {
        return createInstance(cls, (Class<T>)null, requiredClass);
    }

    public static <T> T createInstance(final String className, final String defaultTypeName, final Class<T> requiredType) {
        Class<? extends T> defaultType = null;
        if (defaultTypeName != null) {
        	try {
        		defaultType = CastUtils.cast(Thread.currentThread().getContextClassLoader().loadClass(defaultTypeName));
        		if (defaultType == null) {
        			throw new InstanceCreationClassException("Failed to load default type '" + defaultTypeName + "'");
        		}
        	} catch (final ClassNotFoundException e) {
        		throw new UnavailableClassException("The default type '" + defaultTypeName + "' cannot be found");
        	} catch (final NoClassDefFoundError e) {
        		throw new InstanceCreationClassException("Default type '" + defaultTypeName + "' found, but is missing a dependent class: " + e.getMessage(), e);
        	}
        }
        return createInstance(className, defaultType, requiredType);
    }

    public static <T> T createInstance(final Class<?> cls, final String defaultTypeName, final Class<T> requiredType) {
        Class<? extends T> defaultType = null;
        if (defaultTypeName != null) {
        	defaultType = loadClass(defaultTypeName, requiredType);
        	try {
        		defaultType = CastUtils.cast(Thread.currentThread().getContextClassLoader().loadClass(defaultTypeName));
        		if (defaultType == null) {
        			throw new InstanceCreationClassException("Failed to load default type '" + defaultTypeName + "'");
        		}
        	} catch (final ClassNotFoundException e) {
        		throw new UnavailableClassException("The default type '" + defaultTypeName + "' cannot be found");
        	} catch (final NoClassDefFoundError e) {
        		throw new InstanceCreationClassException("Default type '" + defaultTypeName + "' found, but is missing a dependent class: " + e.getMessage(), e);
        	}
        }
        return createInstance(cls, defaultType, requiredType);
    }

    public static <T> T createInstance(final String className, final Class<? extends T> defaultType, final Class<T> requiredType) {
        Assert.assertNotNull("Class to instantiate must be specified", className);
        Class<?> cls = null;
        try {
            cls = Thread.currentThread().getContextClassLoader().loadClass(className);
            if (cls == null) {
                throw new InstanceCreationClassException("Failed to load class '" + className + "'");
            }
            return createInstance(cls, defaultType, requiredType);
        } catch (final ClassNotFoundException e) {
            if (className.indexOf('.') == -1) {
                throw new UnavailableClassException("The component '" + className + "' cannot be found");
            }
            throw new UnavailableClassException("The class '" + className + "' cannot be found");
        } catch (final NoClassDefFoundError e) {
            throw new InstanceCreationClassException("Class '" + className + "' found , but is missing a dependent class: " + e.getMessage(), e);
        }
    }

    public static <T> T createInstance(final Class<?> cls, final Class<? extends T> defaultType, final Class<T> requiredType) {
        Assert.assertNotNull("Class to instantiate must be specified", cls);
        try {
            if (requiredType == null || requiredType.isAssignableFrom(cls)) {
                final Class<T> tClass = CastUtils.cast(cls);
                return tClass.newInstance();
            } else {
                throw new InstanceCreationClassException("Class '" + cls.getName() + "' is not of type '" + requiredType + "'");
            }
        } catch (final NoClassDefFoundError e) {
            throw new InstanceCreationClassException("Class '" + cls + "'found , but is missing a dependent class: " + e.getMessage(), e);
        } catch (final InstantiationException e) {
            throw new InstanceCreationException("Could not instantiate an object of class '" + cls.getName() + "'; "
                    + e.getMessage());
        } catch (final IllegalAccessException e) {
            throw new InstanceCreationException("Could not access the class '" + cls.getName() + "'; " + e.getMessage());
        }
    }

	public static Class<?> loadClass(
			String className) {
        Assert.assertNotNull("Class to instantiate must be specified", className);
    	try {
    		return Thread.currentThread().getContextClassLoader().loadClass(className);
    	} catch (final ClassNotFoundException e) {
    		throw new UnavailableClassException("The default type '" + className + "' cannot be found");
    	} catch (final NoClassDefFoundError e) {
    		throw new InstanceCreationClassException("Default type '" + className + "' found, but is missing a dependent class: " + e.getMessage(), e);
    	}
	}

	public static <R, T extends R> Class<T> loadClass(
			String className, Class<R> requiredType) {
        Assert.assertNotNull("Class to instantiate must be specified", className);
    	try {
    		Class<?> loadedClass = loadClass(className);
    		if (requiredType != null && !requiredType.isAssignableFrom(loadedClass)) {
    			throw new InstanceCreationClassException("Class '" + className + "' is not of type '" + requiredType + "'");
    		}
			return CastUtils.cast(loadedClass);
    	} catch (final NoClassDefFoundError e) {
    		throw new InstanceCreationClassException("Default type '" + className + "' found, but is missing a dependent class: " + e.getMessage(), e);
    	}
	}

}
// Copyright (c) Naked Objects Group Ltd.
