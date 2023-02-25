package org.nakedobjects.metamodel.spec.feature;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.filters.AbstractFilter;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.facets.hide.HiddenFacet;


public class NakedObjectAssociationFilters {

    private NakedObjectAssociationFilters() {}

    /**
     * Filters only fields that are for properties (ie 1:1 associations)
     */
    public final static Filter<NakedObjectAssociation> PROPERTIES = new AbstractFilter<NakedObjectAssociation>() {
        @Override
        public boolean accept(final NakedObjectAssociation property) {
            return property.isOneToOneAssociation();
        }
    };

    /**
     * Returns all fields (that is, filters out nothing).
     */
    public final static Filter<NakedObjectAssociation> ALL = new AbstractFilter<NakedObjectAssociation>() {
        @Override
        public boolean accept(final NakedObjectAssociation property) {
            return true;
        }
    };

    /**
     * Filters only fields that are for collections (ie 1:m associations)
     */
    public final static Filter<NakedObjectAssociation> COLLECTIONS = new AbstractFilter<NakedObjectAssociation>() {
        @Override
        public boolean accept(final NakedObjectAssociation property) {
            return property.isOneToManyAssociation();
        }
    };

    /**
     * Filters only properties that are visible statically, ie have not been unconditionally hidden at compile time.  Note this list will include 
     * properties marked as hidden once persisted and until persisted, but not those marked hidden always.  
     */
    public static final Filter<NakedObjectAssociation> STATICALLY_VISIBLE_ASSOCIATIONS = new AbstractFilter<NakedObjectAssociation>() {
        @Override
        public boolean accept(final NakedObjectAssociation property) {
            return !property.containsFacet(HiddenFacet.class);
        }
    };

    /**
     * Filters only properties that are visible statically, ie have not been hidden at compile time.
     */
    public static Filter<NakedObjectAssociation> dynamicallyVisible(final AuthenticationSession session, final NakedObject target) {
        return new AbstractFilter<NakedObjectAssociation>() {
            @Override
            public boolean accept(final NakedObjectAssociation nakedObjectAssociation) {
                final Consent visible = nakedObjectAssociation.isVisible(session, target);
                return visible.isAllowed();
            }
        };
    }

}
