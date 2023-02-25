package org.nakedobjects.metamodel.adapter;

import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Specification;

/**
 * Naked objects are adapters to domain objects, where the application is written in terms of domain objects
 * and those objects are represented within the NOF through these adapter, and not directly.
 * 
 * <p>
 * This interface defines just those aspects of the adapter that are of interest to the
 * meta-model (hence the 'MM' suffix), while the {@link NakedObject} subtype defines additional properties
 * used by the framework (<tt>PersistenceSession</tt>s et al) for tracking the persistence state
 * of the object.  In theory it ought to be possible to move {@link NakedObject} interface out of
 * the metamodel module.  However, this would require either lots of genericizing or lots of downcasting
 * to ensure that the framework is only ever handed or deals with the {@link NakedObject}s.
 * 
 * @see NakedObject
 */
public interface NakedObjectMM extends Instance {


	/**
	 * Refines {@link Instance#getSpecification()}.
	 */
	NakedObjectSpecification getSpecification();


    /**
     * Returns the adapted domain object, the POJO, that this adapter represents with the NOF.
     */
    Object getObject();

	
    /**
     * Returns the title to display this object with, which is usually got from the wrapped
     * {@link #getObject() domain object}.
     */
    String titleString();

    /**
     * Return an {@link Instance} of the specified {@link Specification} with respect
     * to this {@link NakedObject}.
     * 
     * <p>
     * If called with {@link NakedObjectSpecification}, then just returns <tt>this</tt>). 
     * If called for other subinterfaces, then should provide an appropriate
     * {@link Instance} implementation.
     * 
     * <p>
     * Designed to be called in a double-dispatch design from {@link Specification#getInstance(NakedObject)}.
     * 
     * <p>
     * Note: this method will throw an {@link UnsupportedOperationException} unless the
     * extended <tt>PojoAdapterXFactory</tt> is configured.  (That is, only
     * <tt>PojoAdapterX</tt> provides support for this; the regular <tt>PojoAdapter</tt>
     * does not currently.
     *   
     * @param nakedObject
     * @return
     */
    Instance getInstance(Specification specification);


    /**
     * Whether the object is persisted.
     * 
     * <p>
     * Note: not necessarily the reciprocal of {@link #isTransient()};
     * standalone adapters (with {@link ResolveState#VALUE}) report as neither persistent or transient. 
     */
    boolean isPersistent();
    
    /**
     * Whether the object is transient.
     * 
     * <p>
     * Note: not necessarily the reciprocal of {@link #isPersistent()};
     * standalone adapters (with {@link ResolveState#VALUE}) report as neither persistent or transient. 
     */
    boolean isTransient();
    

    /**
     * Sometimes it is necessary to manage the replacement of the underlying domain object (by another
     * component such as an object store). This method allows the adapter to be kept while the domain object
     * is replaced.
     */
    void replacePojo(Object pojo);


    /**
     * Gets the type of facet for this object, defaulting to the facet held by the underlying specification.
     * This is needed by collections that can have an element type but is not held by the collection, such as
     * generic collections that lose the type information through type erasure.
     * 
     * REVIEW should this and the {@link #setTypeOfFacet(TypeOfFacet)} be more generic allowing other facets
     * to be added to adapters.
     */
    TypeOfFacet getTypeOfFacet();

    /**
     * Sets the element type facet, typically by copying it from the method or association information.
     * 
     * @see #getTypeOfFacet()
     */
    void setTypeOfFacet(TypeOfFacet typeOfFacet);


}
