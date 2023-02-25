package org.nakedobjects.metamodel.java5;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

import org.nakedobjects.metamodel.facets.FacetFactory;
import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public abstract class AnnotationBasedFacetFactoryAbstract extends FacetFactoryAbstract implements AnnotationBasedFacetFactory {

    public AnnotationBasedFacetFactoryAbstract(final NakedObjectFeatureType[] featureTypes) {
        super(featureTypes);
    }

    /**
     * Always returns <tt>false</tt>; {@link FacetFactory}s that look for annotations won't recognize methods
     * with prefixes.
     */
    public boolean recognizes(final Method method) {
        return false;
    }

    /**
     * For convenience of the several annotations that apply only to {@link String}s.
     */
    protected boolean isString(final Class<?> cls) {
        return cls.equals(String.class);
    }

    protected org.nakedobjects.metamodel.facets.When decodeWhen(final org.nakedobjects.applib.annotation.When when) {
        if (when == org.nakedobjects.applib.annotation.When.ALWAYS) {
            return org.nakedobjects.metamodel.facets.When.ALWAYS;
        }
        if (when == org.nakedobjects.applib.annotation.When.ONCE_PERSISTED) {
            return org.nakedobjects.metamodel.facets.When.ONCE_PERSISTED;
        }
        if (when == org.nakedobjects.applib.annotation.When.UNTIL_PERSISTED) {
            return org.nakedobjects.metamodel.facets.When.UNTIL_PERSISTED;
        }
        return org.nakedobjects.metamodel.facets.When.NEVER;
    }

    

    /**
     * Searches for annotation on provided class, and if not found for the superclass.
     * 
     * <p>
     * Added to allow bytecode-mangling libraries such as CGLIB to be supported.
     */
	protected <T extends Annotation> T getAnnotation(final Class<?> cls, Class<T> annotationClass) {
		if (cls == null) {
			return null;
		}
		T annotation = cls.getAnnotation(annotationClass);
		if (annotation != null) {
			return annotation;
		}
		
		// search superclasses
		Class<?> superclass = cls.getSuperclass();
		if (superclass != null) {
			try {
				T annotationFromSuperclass = getAnnotation(superclass, annotationClass);
				if (annotationFromSuperclass != null) {
					return annotationFromSuperclass;
				}
			} catch (SecurityException e) {
				// fall through
			}
		}
		
		// search implemented interfaces
		Class<?>[] interfaces = cls.getInterfaces();
		for(Class<?> iface: interfaces) {
			T annotationFromInterface = getAnnotation(iface, annotationClass);
			if (annotationFromInterface != null) {
				return annotationFromInterface;
			}
		}
		return null;
	}

	
    /**
     * Searches for annotation on provided method, and if not found for any inherited methods up
     * from the superclass.
     * 
     * <p>
     * Added to allow bytecode-mangling libraries such as CGLIB to be supported.
     */
	protected <T extends Annotation> T getAnnotation(final Method method, Class<T> annotationClass) {
		if (method == null) {
			return null;
		}
		T annotation = method.getAnnotation(annotationClass);
		if (annotation != null) {
			return annotation;
		}

		Class<?> methodDeclaringClass = method.getDeclaringClass();
		
		// search superclasses
		Class<?> superclass = methodDeclaringClass.getSuperclass();
		if (superclass != null) {
			try {
				Method parentClassMethod = superclass.getMethod(method.getName(), method.getParameterTypes());
				return getAnnotation(parentClassMethod, annotationClass);
			} catch (SecurityException e) {
				// fall through
			} catch (NoSuchMethodException e) {
				// fall through
			}
		}
		
		// search implemented interfaces
		Class<?>[] interfaces = methodDeclaringClass.getInterfaces();
		for(Class<?> iface: interfaces) {
			try {
				Method ifaceMethod = iface.getMethod(method.getName(), method.getParameterTypes());
				return getAnnotation(ifaceMethod, annotationClass);
			} catch (SecurityException e) {
				// fall through
			} catch (NoSuchMethodException e) {
				// fall through
			}
		}
		return null;
	}

    /**
     * Searches for annotation on provided method, and if not found for any inherited methods up
     * from the superclass.
     * 
     * <p>
     * Added to allow bytecode-mangling libraries such as CGLIB to be supported.
     */
	protected boolean isAnnotationPresent(final Method method, Class<? extends Annotation> annotationClass) {
		if (method == null) {
			return false;
		}
		boolean present = method.isAnnotationPresent(annotationClass);
		if (present) {
			return true;
		}

		Class<?> methodDeclaringClass = method.getDeclaringClass();
		
		// search superclasses
		Class<?> superclass = methodDeclaringClass.getSuperclass();
		if (superclass != null) {
			try {
				Method parentClassMethod = superclass.getMethod(method.getName(), method.getParameterTypes());
				return isAnnotationPresent(parentClassMethod, annotationClass);
			} catch (SecurityException e) {
				// fall through
			} catch (NoSuchMethodException e) {
				// fall through
			}
		}

		// search implemented interfaces
		Class<?>[] interfaces = methodDeclaringClass.getInterfaces();
		for(Class<?> iface: interfaces) {
			try {
				Method ifaceMethod = iface.getMethod(method.getName(), method.getParameterTypes());
				return isAnnotationPresent(ifaceMethod, annotationClass);
			} catch (SecurityException e) {
				// fall through
			} catch (NoSuchMethodException e) {
				// fall through
			}
		}
		return false;
	}

	
    /**
     * Searches for parameter annotations on provided method, and if not found for any inherited methods up
     * from the superclass.
     * 
     * <p>
     * Added to allow bytecode-mangling libraries such as CGLIB to be supported.
     */
	protected Annotation[][] getParameterAnnotations(final Method method) {
		if (method == null) {
			return noParamAnnotationsFor(method);
		}
		Annotation[][] allParamAnnotations = method.getParameterAnnotations();
		
		boolean foundAnnotationsForAnyParameter = false;
		for(Annotation[] singleParamAnnotations: allParamAnnotations) {
			if (singleParamAnnotations.length > 0) {
				foundAnnotationsForAnyParameter = true;
				break;
			}
		}
		if (foundAnnotationsForAnyParameter) {
			return allParamAnnotations;
		}

		Class<?> methodDeclaringClass = method.getDeclaringClass();
		
		// search superclasses
		Class<?> superclass = methodDeclaringClass.getSuperclass();
		if (superclass != null) {
			try {
				Method parentClassMethod = superclass.getMethod(method.getName(), method.getParameterTypes());
				return getParameterAnnotations(parentClassMethod);
			} catch (SecurityException e) {
				// fall through
			} catch (NoSuchMethodException e) {
				// fall through
			}
		}
		
		// search implemented interfaces
		Class<?>[] interfaces = methodDeclaringClass.getInterfaces();
		for(Class<?> iface: interfaces) {
			try {
				Method ifaceMethod = iface.getMethod(method.getName(), method.getParameterTypes());
				return getParameterAnnotations(ifaceMethod);
			} catch (SecurityException e) {
				// fall through
			} catch (NoSuchMethodException e) {
				// fall through
			}
		}
		
		return noParamAnnotationsFor(method);
	}

	private Annotation[][] noParamAnnotationsFor(final Method method) {
		return new Annotation[method.getParameterTypes().length][0];
	}

}
