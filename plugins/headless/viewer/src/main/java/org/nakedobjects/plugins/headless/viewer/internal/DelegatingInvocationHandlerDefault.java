package org.nakedobjects.plugins.headless.viewer.internal;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import org.nakedobjects.applib.events.InteractionEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.plugins.headless.applib.HeadlessViewer;
import org.nakedobjects.plugins.headless.applib.HeadlessViewer.ExecutionMode;
import org.nakedobjects.runtime.persistence.container.DomainObjectContainerObjectChanged;
import org.nakedobjects.runtime.persistence.container.DomainObjectContainerResolve;



public class DelegatingInvocationHandlerDefault<T> implements DelegatingInvocationHandler<T> {

    private final T delegate;
    protected final HeadlessViewer headlessViewer;
    private final ExecutionMode executionMode;
    private final RuntimeContext runtimeContext;

    protected final Method equalsMethod;
    protected final Method hashCodeMethod;
    protected final Method toStringMethod;

	private DomainObjectContainerObjectChanged domainObjectContainerObjectChanged;
	private DomainObjectContainerResolve domainObjectContainerResolve;

	private boolean resolveObjectChangedEnabled;
	
    public DelegatingInvocationHandlerDefault(
            final T delegate,
            final HeadlessViewer headlessViewer,
            final ExecutionMode executionMode,
            final RuntimeContext runtimeContext) {
        if (delegate == null) {
            throw new IllegalArgumentException("delegate must not be null");
        }
        this.delegate = delegate;
        this.headlessViewer = headlessViewer;
        this.executionMode = executionMode;
        this.runtimeContext = runtimeContext;

        this.domainObjectContainerResolve = new DomainObjectContainerResolve();
        this.domainObjectContainerObjectChanged = new DomainObjectContainerObjectChanged();

        try {
            equalsMethod = delegate.getClass().getMethod("equals", new Class[] { Object.class });
            hashCodeMethod = delegate.getClass().getMethod("hashCode", new Class[] {});
            toStringMethod = delegate.getClass().getMethod("toString", new Class[] {});
        } catch (final NoSuchMethodException e) {
            // ///CLOVER:OFF
            throw new RuntimeException("An Object method could not be found: " + e.getMessage());
            // ///CLOVER:ON
        }
    }

	public boolean isResolveObjectChangedEnabled() {
		return resolveObjectChangedEnabled;
	}
    public void setResolveObjectChangedEnabled(boolean resolveObjectChangedEnabled) {
    	this.resolveObjectChangedEnabled = resolveObjectChangedEnabled;
    }


	protected void resolveIfRequired(final NakedObject targetAdapter) {
		resolveIfRequired(targetAdapter.getObject());
	}
	protected void resolveIfRequired(final Object domainObject) {
		if (resolveObjectChangedEnabled) {
			domainObjectContainerResolve.resolve(domainObject);
		}
	}
	protected void objectChangedIfRequired(final NakedObject targetAdapter) {
		objectChangedIfRequired(targetAdapter.getObject());
	}

	protected void objectChangedIfRequired(Object domainObject) {
		if(resolveObjectChangedEnabled) {
			domainObjectContainerObjectChanged.objectChanged(domainObject);
		}
	}



    public HeadlessViewer getHeadlessViewer() {
        return headlessViewer;
    }

    public T getDelegate() {
        return delegate;
    }

    public ExecutionMode getExecutionMode() {
		return executionMode;
	}

    protected Object delegate(final Method method, final Object[] args) throws IllegalArgumentException, IllegalAccessException,
            InvocationTargetException {

    	
        // commented out, hoping isn't needed...
//        if (method.equals(getHandlerMethod)) {
//            return this;
//        }

        return method.invoke(getDelegate(), args);
    }

    protected boolean isObjectMethod(final Method method) {
        return toStringMethod.equals(method) || hashCodeMethod.equals(method) || equalsMethod.equals(method);
    }

    // commented out, hoping isn't needed...
//    protected boolean isNakedObjectTestViewHandlerMethod(final Method method) {
//        return getHandlerMethod.equals(method);
//    }

    public Object invoke(final Object object, final Method method, final Object[] args) throws Throwable {

        return method.invoke(object, args);
    }

    protected InteractionEvent notifyListeners(final InteractionEvent interactionEvent) {
        headlessViewer.notifyListeners(interactionEvent);
        return interactionEvent;
    }

    
    // /////////////////////////////////////////////////////////////////
    // Dependencies (from constructor)
    // /////////////////////////////////////////////////////////////////

    protected RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}
    

    // /////////////////////////////////////////////////////////////////
    // Dependencies (from runtime context)
    // /////////////////////////////////////////////////////////////////

    protected SpecificationLoader getSpecificationLoader() {
        return runtimeContext.getSpecificationLoader();
    }

    protected AuthenticationSession getAuthenticationSession() {
        return runtimeContext.getAuthenticationSession();
    }
    
}
