package org.nakedobjects.metamodel.facets.actcoll.typeof;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;

import org.nakedobjects.applib.annotation.TypeOf;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.FacetUtil;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.java5.AnnotationBasedFacetFactoryAbstract;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistry;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistryAware;


public class TypeOfAnnotationFacetFactory extends AnnotationBasedFacetFactoryAbstract implements CollectionTypeRegistryAware {
    private CollectionTypeRegistry collectionTypeRegistry;

    public TypeOfAnnotationFacetFactory() {
        super(NakedObjectFeatureType.COLLECTIONS_AND_ACTIONS);
    }

    @Override
    public boolean process(Class<?> cls, final Method method, final MethodRemover methodRemover, final FacetHolder holder) {

        final TypeOf annotation = getAnnotation(method, TypeOf.class);

        final Class<?> methodReturnType = method.getReturnType();
        if (!collectionTypeRegistry.isCollectionType(methodReturnType) && !collectionTypeRegistry.isArrayType(methodReturnType)) {
            return false;
        }

        final Class<?> returnType = method.getReturnType();
        if (returnType.isArray()) {
            final Class<?> componentType = returnType.getComponentType();
            FacetUtil.addFacet(new TypeOfFacetInferredFromArray(componentType, holder, getSpecificationLoader()));
            return false;
        }

        if (annotation != null) {
            return FacetUtil.addFacet(new TypeOfFacetViaAnnotation(annotation.value(), holder, getSpecificationLoader()));
        }

        final Type type = method.getGenericReturnType();
        if (!(type instanceof ParameterizedType)) {
            return false;
        }

        final ParameterizedType parameterizedType = (ParameterizedType) type;
        final Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (actualTypeArguments.length == 0) {
            return false;
        }

        final Object actualTypeArgument = actualTypeArguments[0];
        if (actualTypeArgument instanceof Class) {
            final Class<?> actualType = (Class<?>) actualTypeArgument;
            return FacetUtil.addFacet(new TypeOfFacetInferredFromGenerics(actualType, holder, getSpecificationLoader()));
        }

        if (actualTypeArgument instanceof TypeVariable) {
            @SuppressWarnings("unused")
			final TypeVariable<?> typeVariable = (TypeVariable<?>) actualTypeArgument;
            // TODO: what to do here?
            return false;
        }

        return false;
    }

    public void setCollectionTypeRegistry(final CollectionTypeRegistry collectionTypeRegistry) {
        this.collectionTypeRegistry = collectionTypeRegistry;
    }

}
