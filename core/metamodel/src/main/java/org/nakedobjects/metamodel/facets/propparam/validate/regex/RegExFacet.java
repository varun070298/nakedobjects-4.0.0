package org.nakedobjects.metamodel.facets.propparam.validate.regex;

import org.nakedobjects.metamodel.facets.MultipleValueFacet;
import org.nakedobjects.metamodel.facets.propparam.validate.mask.MaskFacet;
import org.nakedobjects.metamodel.interactions.ValidatingInteractionAdvisor;


/**
 * Whether the (string) property or a parameter must correspond to a specific regular expression.
 * 
 * <p>
 * In the standard Naked Objects Programming Model, corresponds to the <tt>@RegEx</tt> annotation.
 * 
 * @see MaskFacet
 */
public interface RegExFacet extends MultipleValueFacet, ValidatingInteractionAdvisor {

    public String validation();

    public String format();

    public boolean caseSensitive();

    public boolean doesNotMatch(String proposed);

    public String format(String text);

}
