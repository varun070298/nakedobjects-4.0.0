package org.nakedobjects.metamodel.facets.object.validprops;

import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.propparam.validate.mandatory.MandatoryFacet;
import org.nakedobjects.metamodel.interactions.ObjectValidityContext;
import org.nakedobjects.metamodel.interactions.ValidatingInteractionAdvisor;


/**
 * Object-level {@link ValidatingInteractionAdvisor validator} that ensures that all {@link MandatoryFacet
 * mandatory} properties are entered prior to persisting the object.
 */
public interface ObjectValidPropertiesFacet extends Facet, ValidatingInteractionAdvisor {

    /**
     * The reason the object is invalid.
     * 
     * <p>
     * . If the object is actually valid, should return <tt>null</tt>.
     */
    public String invalidReason(ObjectValidityContext context);

}
