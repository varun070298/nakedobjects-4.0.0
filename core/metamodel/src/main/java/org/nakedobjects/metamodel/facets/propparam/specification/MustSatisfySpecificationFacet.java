package org.nakedobjects.metamodel.facets.propparam.specification;

import java.util.List;

import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.applib.spec.Specification;
import org.nakedobjects.applib.util.ReasonBuffer;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.ProposedHolder;
import org.nakedobjects.metamodel.interactions.ValidatingInteractionAdvisor;
import org.nakedobjects.metamodel.interactions.ValidityContext;

public class MustSatisfySpecificationFacet extends FacetAbstract implements ValidatingInteractionAdvisor {

    public static Class<? extends Facet> type() {
        return MustSatisfySpecificationFacet.class;
    }

    private final List<Specification> specifications;

    public MustSatisfySpecificationFacet(final List<Specification> specifications, final FacetHolder holder) {
        super(type(), holder, false);
        this.specifications = specifications;
    }

    public String invalidates(final ValidityContext<? extends ValidityEvent> validityContext) {
        if (!(validityContext instanceof ProposedHolder)) {
            return null;
        }
        ProposedHolder proposedHolder = (ProposedHolder) validityContext;
        final NakedObject targetNO = proposedHolder.getProposed();
        final Object targetObject = targetNO.getObject();
        ReasonBuffer buf = new ReasonBuffer();
        for(Specification specification: specifications) {
            buf.append(specification.satisfies(targetObject));
        }
        return buf.getReason();
    }

}
