package org.nakedobjects.metamodel.spec.feature;

import java.util.List;

import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.commons.filters.Filters;


public interface NakedObjectAssociationContainer {

    /**
     * Get the field object representing the field with the specified field identifier.
     */
    NakedObjectAssociation getAssociation(String id);

    /**
     * Return all the fields that exist in an object of this specification, although they need not all be
     * accessible or visible.
     */
    NakedObjectAssociation[] getAssociations();

    /**
     * As per {@link #getAssociations()}, but returning a {@link List}.
     * @return
     */
    List<? extends NakedObjectAssociation> getAssociationList();

    /**
     * Return all {@link NakedObjectAssociation}s matching the supplied filter.
     * 
     * To get the statically visible fields (where any invisible and unauthorised fields have been removed)
     * use <tt>NakedObjectAssociationFilters#STATICALLY_VISIBLE_ASSOCIATIONS</tt>
     * 
     * @see Filters
     */
    NakedObjectAssociation[] getAssociations(Filter<NakedObjectAssociation> filter);

    /**
     * As per {@link #getAssociations(Filter)}, but returning a {@link List}.
     * @return
     */
    List<? extends NakedObjectAssociation> getAssociationList(Filter<NakedObjectAssociation> filter);

    /**
     * All {@link NakedObjectAssociation association}s that represent {@link OneToOneAssociation properties}.
     */
    List<OneToOneAssociation> getPropertyList();

    /**
     * All {@link NakedObjectAssociation association}s that represents {@link OneToManyAssociation collections}. 
     * @return
     */
    List<OneToManyAssociation> getCollectionList();


}
