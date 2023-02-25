package org.nakedobjects.metamodel.examples.facets.jsr303;

import java.util.Set;

import javax.validation.InvalidConstraint;
import javax.validation.ValidationProviderFactory;
import javax.validation.Validator;

import org.nakedobjects.applib.events.ValidityEvent;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetAbstract;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.interactions.ProposedHolder;
import org.nakedobjects.metamodel.interactions.ValidatingInteractionAdvisor;
import org.nakedobjects.metamodel.interactions.ValidityContext;
import org.nakedobjects.metamodel.spec.identifier.Identified;


public class Jsr303PropertyValidationFacet extends FacetAbstract implements ValidatingInteractionAdvisor {

    public static Class<? extends Facet> type() {
        return Jsr303PropertyValidationFacet.class;
    }

    private final Identified identifierHolder;
    
    public Jsr303PropertyValidationFacet(final FacetHolder holder) {
        super(type(), holder, false);
        identifierHolder = (holder instanceof Identified) ? (Identified)holder : null;
    }

    @SuppressWarnings("unchecked")
    public String invalidates(final ValidityContext<? extends ValidityEvent> validityContext) {
        final Validator validator = getTargetValidator(validityContext);
        final Object proposed = getProposed(validityContext);
        
        if (validator == null || proposed == null || identifierHolder == null) {
            return null;
        }
        
        final String memberName = identifierHolder.getIdentifier().getMemberName();
        Set<InvalidConstraint<?>> constraints = validator.validateValue(memberName, proposed);
        return asString(memberName, constraints);
    }

    private Validator<?> getTargetValidator(final ValidityContext<? extends ValidityEvent> validityContext) {
        final NakedObject targetNO = validityContext.getTarget();
        final Object targetObject = targetNO.getObject();
        final Class<?> cls = targetObject.getClass();
        return ValidationProviderFactory.createValidator(cls);
    }

    private Object getProposed(final ValidityContext<? extends ValidityEvent> validityContext) {
        if (!(validityContext instanceof ProposedHolder)) {
            return null;
        }
        final ProposedHolder propertyModifyContext = (ProposedHolder) validityContext;
        final NakedObject proposedNO = propertyModifyContext.getProposed();
        if (proposedNO == null) {
            return null;
        }
        return proposedNO.getObject();
    }


    private String asString(final String memberName, final Set<InvalidConstraint<?>> constraints) {
        if (constraints.isEmpty()) {
            return null;
        }
        final StringBuilder buf = new StringBuilder(memberName + " is invalid: ");
        boolean first = true;
        for (final InvalidConstraint<?> constraint : constraints) {
            if (!first) {
                buf.append("; ");
            } else {
                first = false;
            }
            buf.append(constraint.getMessage());
        }
        return buf.toString();
    }

}
