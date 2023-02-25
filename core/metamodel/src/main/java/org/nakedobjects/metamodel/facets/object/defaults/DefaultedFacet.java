package org.nakedobjects.metamodel.facets.object.defaults;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.SingleValueFacet;
import org.nakedobjects.metamodel.facets.actions.defaults.ActionDefaultsFacet;
import org.nakedobjects.metamodel.facets.properties.defaults.PropertyDefaultFacet;


/**
 * Indicates that this class has a default.
 * 
 * <p>
 * The mechanism for providing a default will vary by the applib. In the Java applib, this is done by
 * implementing the DefaultProvider interface.
 * 
 * <p>
 * The rest of the framework does not used this directly, but instead we infer {@link PropertyDefaultFacet}
 * and {@link ActionDefaultsFacet} from the method's return type / parameter types, and copy over.
 */
public interface DefaultedFacet extends SingleValueFacet {

    /**
     * The default (as a pojo, not a {@link NakedObject}).
     * 
     * @return
     */
    Object getDefault();
}
