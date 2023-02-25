package org.nakedobjects.plugins.headless.viewer.internal;

import static org.nakedobjects.metamodel.commons.lang.MethodUtils.getMethod;

import java.util.Collection;
import java.util.List;

import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;


class CollectionInvocationHandler<T, R> extends AbstractCollectionInvocationHandler<T, R> {

    public CollectionInvocationHandler(
            final R collectionToProxy,
            final String collectionName,
            final DomainObjectInvocationHandler<T> handler,
            final RuntimeContext runtimeContext,
            final OneToManyAssociation otma) {
        super(collectionToProxy, collectionName, handler, runtimeContext, otma);

        try {
            intercept(getMethod(collectionToProxy, "contains", Object.class));
            intercept(getMethod(collectionToProxy, "size"));
            intercept(getMethod(collectionToProxy, "isEmpty"));
            if (collectionToProxy instanceof List) {
                intercept(getMethod(collectionToProxy, "get", int.class));
            }
            veto(getMethod(collectionToProxy, "add", Object.class));
            veto(getMethod(collectionToProxy, "remove", Object.class));
            veto(getMethod(collectionToProxy, "addAll", Collection.class));
            veto(getMethod(collectionToProxy, "removeAll", Collection.class));
            veto(getMethod(collectionToProxy, "retainAll", Collection.class));
            veto(getMethod(collectionToProxy, "clear"));
        } catch (final NoSuchMethodException e) {
            // ///CLOVER:OFF
            throw new RuntimeException("A Collection method could not be found: " + e.getMessage());
            // ///CLOVER:ON
        }
    }

}
