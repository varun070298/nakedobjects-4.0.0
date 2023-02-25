package org.nakedobjects.metamodel.facets.collections;

import org.nakedobjects.metamodel.facets.FacetFactoryAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacetDefaultToObject;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacetInferredFromArray;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacetInferredFromGenerics;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContextAware;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistry;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistryAware;


public class CollectionFacetFactory extends FacetFactoryAbstract implements CollectionTypeRegistryAware, RuntimeContextAware {

    private CollectionTypeRegistry collectionTypeRegistry;
	private RuntimeContext runtimeContext;

    public CollectionFacetFactory() {
        super(NakedObjectFeatureType.OBJECTS_ONLY);
    }

    
    @Override
    public boolean process(final Class<?> cls, final MethodRemover methodRemover, final FacetHolder holder) {
        if (collectionTypeRegistry.isCollectionType(cls)) {
            final TypeOfFacet typeOfFacet = holder.getFacet(TypeOfFacet.class);
            Class<?> collectionElementType;
            if (typeOfFacet != null) {
                collectionElementType = typeOfFacet.value();
            } else {
                collectionElementType = collectionElementType(cls);
                holder.addFacet(collectionElementType != Object.class ? new TypeOfFacetInferredFromGenerics(
                        collectionElementType, holder, getSpecificationLoader()) : new TypeOfFacetDefaultToObject(holder, getSpecificationLoader()));
            }
            holder.addFacet(new JavaCollectionFacet(holder, getRuntimeContext()));
            return true;
        }
        if (collectionTypeRegistry.isArrayType(cls)) {
            holder.addFacet(new JavaArrayFacet(holder, getRuntimeContext()));
            holder.addFacet(new TypeOfFacetInferredFromArray(cls.getComponentType(), holder, getSpecificationLoader()));
            return true;
        }

        return false;
    }

    private Class<?> collectionElementType(final Class<?> cls) {
        return Object.class;
    }

    
    ////////////////////////////////////////////////////////////////
    // Dependencies (injected)
    ////////////////////////////////////////////////////////////////
    
    /**
     * Injected since {@link CollectionTypeRegistryAware}.
     */
    public void setCollectionTypeRegistry(final CollectionTypeRegistry collectionTypeRegistry) {
        this.collectionTypeRegistry = collectionTypeRegistry;
    }

    
    /**
     * As per {@link #setRuntimeContext(RuntimeContext)}
     */
    public RuntimeContext getRuntimeContext() {
		return runtimeContext;
	}

    /**
     * Injected since {@link RuntimeContextAware}.
     */
	public void setRuntimeContext(final RuntimeContext runtimeContext) {
		this.runtimeContext = runtimeContext;
	}


}
