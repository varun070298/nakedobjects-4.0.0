package org.nakedobjects.plugins.headless.viewer.internal;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.applib.events.CollectionMethodEvent;
import org.nakedobjects.applib.events.InteractionEvent;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;


abstract class AbstractCollectionInvocationHandler<T, C> extends DelegatingInvocationHandlerDefault<C> {

    private final List<Method> interceptedMethods = new ArrayList<Method>();
    private final List<Method> vetoedMethods = new ArrayList<Method>();
    private final String collectionName;
    private final OneToManyAssociation oneToManyAssociation;
    private final T domainObject;

    public AbstractCollectionInvocationHandler(
            final C collectionOrMapToProxy,
            final String collectionName,
            final DomainObjectInvocationHandler<T> handler,
            final RuntimeContext runtimeContext,
            final OneToManyAssociation otma) {
        super(collectionOrMapToProxy, handler.getHeadlessViewer(), handler.getExecutionMode(), runtimeContext);
        this.collectionName = collectionName;
        this.oneToManyAssociation = otma;
        this.domainObject = handler.getDelegate();
    }

    protected Method intercept(final Method method) {
        this.interceptedMethods.add(method);
        return method;
    }

    protected Method veto(final Method method) {
        this.vetoedMethods.add(method);
        return method;
    }

    public String getCollectionName() {
        return collectionName;
    }

    public OneToManyAssociation getCollection() {
        return oneToManyAssociation;
    }

    public T getDomainObject() {
        return domainObject;
    }

    @Override
    public Object invoke(final Object collectionObject, final Method method, final Object[] args) throws Throwable {

        // delegate
        final Object returnValueObj = delegate(method, args);

        if (interceptedMethods.contains(method)) {

        	resolveIfRequired(domainObject);
        	
            final InteractionEvent ev = new CollectionMethodEvent(getDelegate(), getCollection().getIdentifier(),
                    getDomainObject(), method.getName(), args, returnValueObj);
            notifyListeners(ev);
            return returnValueObj;
        }

        if (vetoedMethods.contains(method)) {
            throw new UnsupportedOperationException(String.format("Method '%s' may not be called directly.", method.getName()));
        }

        return returnValueObj;
    }

}
