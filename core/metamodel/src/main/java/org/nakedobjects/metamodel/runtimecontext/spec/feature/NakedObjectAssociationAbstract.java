package org.nakedobjects.metamodel.runtimecontext.spec.feature;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.facets.propcoll.derived.DerivedFacet;
import org.nakedobjects.metamodel.facets.properties.choices.PropertyChoicesFacet;
import org.nakedobjects.metamodel.facets.propparam.validate.mandatory.MandatoryFacet;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.identifier.Identified;


// TODO need to pull up the common methods. like getName(), from subclasses
public abstract class NakedObjectAssociationAbstract extends NakedObjectMemberAbstract implements NakedObjectAssociation {
    private final NakedObjectSpecification specification;

    public NakedObjectAssociationAbstract(
            final String associationId,
            final NakedObjectSpecification specification,
            final MemberType memberType,
            final Identified facetHolder, 
            final RuntimeContext runtimeContext) {
        super(associationId, facetHolder, memberType, runtimeContext);
        if (specification == null) {
            throw new IllegalArgumentException("field type for '" + associationId + "' must exist");
        }
        this.specification = specification;
    }

    public abstract NakedObject get(final NakedObject fromObject);

    /**
     * Return the specification of the object (or objects) that this field holds. For a value are one-to-one
     * reference this will be type that the accessor returns. For a collection it will be the type of element,
     * not the type of collection.
     */
    public NakedObjectSpecification getSpecification() {
        return specification;
    }

    public boolean isDerived() {
        return containsFacet(DerivedFacet.class);
    }

    public boolean hasChoices() {
        return containsFacet(PropertyChoicesFacet.class);
    }

    public boolean isMandatory() {
    	final MandatoryFacet mandatoryFacet = getFacet(MandatoryFacet.class);
    	return mandatoryFacet != null && !mandatoryFacet.isInvertedSemantics();
    }
    
    public abstract boolean isEmpty(final NakedObject adapter);

    public boolean isOneToOneAssociation() {
        return !isOneToManyAssociation();
    }

    public String getBusinessKeyName() {
        throw new NotYetImplementedException();
    }
    
    
    
}
// Copyright (c) Naked Objects Group Ltd.
