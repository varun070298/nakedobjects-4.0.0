package org.nakedobjects.metamodel.specloader.classsubstitutor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.nakedobjects.applib.DomainObjectContainer;



public abstract class ClassSubstitutorAbstract implements ClassSubstitutor {

    private final Set<Class<?>> classesToIgnore = new HashSet<Class<?>>();

    
    /**
     * Will implicitly ignore the {@link DomainObjectContainer}.
     */
    public ClassSubstitutorAbstract() {
    	ignore(DomainObjectContainer.class);
    }
    
    
    ///////////////////////////////////////////////////////////////////
    // init, shutdown
    ///////////////////////////////////////////////////////////////////

    /**
     * Default implementation does nothing.
     */
    public void init() {}
    
    /**
     * Default implementation does nothing.
     */
    public void shutdown() {}
    

    ///////////////////////////////////////////////////////////////////
    // ClassSubstitutor impl.
    ///////////////////////////////////////////////////////////////////


    /**
     * Hook method for subclasses to override if required.
     * 
     * <p>
     * Default implementation will either return the class, unless
     * has been registered as to be {@link #ignore(Class) ignore}d, in
     * which case returns <tt>null</tt>.
     */
    public Class<?> getClass(final Class<?> cls) {
        if (shouldIgnore(cls)) {
            return null;
        }
        return cls;
    }

    
    private boolean shouldIgnore(Class<?> cls) {
        if (cls.isArray()) {
            return shouldIgnore(cls.getComponentType());
        }
        return classesToIgnore.contains(cls);
    }

    // ////////////////////////////////////////////////////////////////////
    // ignoring
    // ////////////////////////////////////////////////////////////////////

    /**
     * For any classes registered as ignored, {@link #getClass(Class)}
     * will return <tt>null</tt>. 
     */
    protected boolean ignore(Class<?> q) {
        return classesToIgnore.add(q);
    }

    public Set<Class<?>> getIgnoredClasses() {
		return Collections.unmodifiableSet(classesToIgnore);
	}
    
    // ////////////////////////////////////////////////////////////////////
    // injectInto
    // ////////////////////////////////////////////////////////////////////

    public void injectInto(Object candidate) {
        if (ClassSubstitutorAware.class.isAssignableFrom(candidate.getClass())) {
            ClassSubstitutorAware cast = ClassSubstitutorAware.class.cast(candidate);
            cast.setClassInstrumentor(this);
        }
    }

    
}
