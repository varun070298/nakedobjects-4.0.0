package org.nakedobjects.plugins.headless.viewer.internal;

import java.lang.reflect.InvocationHandler;


public interface DelegatingInvocationHandler<T> extends InvocationHandler {

    T getDelegate();

	public boolean isResolveObjectChangedEnabled();
    public void setResolveObjectChangedEnabled(boolean resolveObjectChangedEnabled);

}
