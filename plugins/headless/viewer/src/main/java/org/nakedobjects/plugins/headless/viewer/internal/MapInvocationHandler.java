package org.nakedobjects.plugins.headless.viewer.internal;

import static org.nakedobjects.metamodel.commons.lang.MethodUtils.getMethod;

import java.util.Map;

import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;


class MapInvocationHandler<T, C> extends AbstractCollectionInvocationHandler<T, C> {

    public MapInvocationHandler(
            final C collectionToProxy,
            final String collectionName,
            final DomainObjectInvocationHandler<T> handler,
            final RuntimeContext runtimeContext,
            final OneToManyAssociation otma) {
        super(collectionToProxy, collectionName, handler, runtimeContext, otma);

        try {
            intercept(getMethod(collectionToProxy, "containsKey", Object.class));
            intercept(getMethod(collectionToProxy, "containsValue", Object.class));
            intercept(getMethod(collectionToProxy, "size"));
            intercept(getMethod(collectionToProxy, "isEmpty"));
            veto(getMethod(collectionToProxy, "put", Object.class, Object.class));
            veto(getMethod(collectionToProxy, "remove", Object.class));
            veto(getMethod(collectionToProxy, "putAll", Map.class));
            veto(getMethod(collectionToProxy, "clear"));
        } catch (final NoSuchMethodException e) {
            // ///CLOVER:OFF
            throw new RuntimeException("A Collection method could not be found: " + e.getMessage());
            // ///CLOVER:ON
        }
    }

}
