package org.nakedobjects.metamodel.specloader.internal;

import org.nakedobjects.metamodel.adapter.Instance;
import org.nakedobjects.metamodel.adapter.MutableProposedHolder;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.InteractionInvocationMethod;
import org.nakedobjects.metamodel.consent.InteractionResultSet;
import org.nakedobjects.metamodel.facets.object.parseable.ParseableFacet;
import org.nakedobjects.metamodel.facets.propparam.multiline.MultiLineFacet;
import org.nakedobjects.metamodel.facets.propparam.typicallength.TypicalLengthFacet;
import org.nakedobjects.metamodel.facets.propparam.validate.maxlength.MaxLengthFacet;
import org.nakedobjects.metamodel.interactions.InteractionUtils;
import org.nakedobjects.metamodel.interactions.ValidityContext;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.spec.feature.OneToOneActionParameter;
import org.nakedobjects.metamodel.spec.feature.ParseableEntryActionParameter;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectActionParamPeer;


public class NakedObjectActionParameterParseable extends NakedObjectActionParameterAbstract implements
        ParseableEntryActionParameter {

    public NakedObjectActionParameterParseable(
            final int index,
            final NakedObjectActionImpl action,
            final NakedObjectActionParamPeer peer) {
        super(index, action, peer);
    }

    public int getNoLines() {
        final MultiLineFacet facet = getFacet(MultiLineFacet.class);
        return facet.numberOfLines();
    }

    public boolean canWrap() {
        final MultiLineFacet facet = getFacet(MultiLineFacet.class);
        return !facet.preventWrapping();
    }

    public int getMaximumLength() {
        final MaxLengthFacet facet = getFacet(MaxLengthFacet.class);
        return facet.value();
    }

    public int getTypicalLineLength() {
        final TypicalLengthFacet facet = getFacet(TypicalLengthFacet.class);
        return facet.value();
    }

    /**
     * Invoked when tab away, disables the OK button.
     * 
     * <p>
     * Assumed to be invoked {@link InteractionInvocationMethod#BY_USER by user}.
     */
    public String isValid(final NakedObject nakedObject, final Object proposedValue) {

        if (!(proposedValue instanceof String)) {
            return null;
        }
        final String proposedString = (String) proposedValue;

        final NakedObjectActionParameter nakedObjectActionParameter = getAction().getParameters()[getNumber()];
        if (!(nakedObjectActionParameter instanceof ParseableEntryActionParameter)) {
            return null;
        }
        final ParseableEntryActionParameter parameter = (ParseableEntryActionParameter) nakedObjectActionParameter;

        final NakedObjectSpecification parameterSpecification = parameter.getSpecification();
        final ParseableFacet p = parameterSpecification.getFacet(ParseableFacet.class);
        final NakedObject newValue = p.parseTextEntry(null, proposedString);

        final ValidityContext<?> ic = parameter.createProposedArgumentInteractionContext(getAuthenticationSession(),
                InteractionInvocationMethod.BY_USER, nakedObject, arguments(newValue), getNumber());

        final InteractionResultSet buf = new InteractionResultSet();
        InteractionUtils.isValidResultSet(parameter, ic, buf);
        if (buf.isVetoed()) {
            return buf.getInteractionResult().getReason();
        }
        return null;

    }

    /**
     * TODO: this is not ideal, because we can only populate the array for single argument, rather than the
     * entire argument set. Instead, we ought to do this in two passes, one to build up the argument set as a
     * single unit, and then validate each in turn.
     * 
     * @param proposedValue
     * @return
     */
    private NakedObject[] arguments(final NakedObject proposedValue) {
        final int parameterCount = getAction().getParameterCount();
        final NakedObject[] arguments = new NakedObject[parameterCount];
        arguments[getNumber()] = proposedValue;
        return arguments;
    }


    // /////////////////////////////////////////////////////////////
    // getInstance
    // /////////////////////////////////////////////////////////////
    
    public Instance getInstance(NakedObject nakedObject) {
        OneToOneActionParameter specification = this;
        return nakedObject.getInstance(specification);
    }


    
    // //////////////////////////////////////////////////////////////////////
    // get, set
    // //////////////////////////////////////////////////////////////////////

    /**
     * Gets the proposed value of the {@link Instance} (downcast as a
     * {@link MutableProposed}, wrapping the proposed value into a {@link NakedObject}.
     */
    public NakedObject get(NakedObject owner) {
        MutableProposedHolder proposedHolder = getProposedHolder(owner);
        Object proposed = proposedHolder.getProposed();
        return getRuntimeContext().adapterFor(proposed);
    }

    /**
     * Sets the proposed value of the {@link Instance} (downcast as a
     * {@link MutableProposed}, unwrapped the proposed value from a {@link NakedObject}.
     */
    public void set(NakedObject owner, NakedObject newValue) {
        MutableProposedHolder proposedHolder = getProposedHolder(owner);
        Object newValuePojo = newValue.getObject();
        proposedHolder.setProposed(newValuePojo);
    }

    private MutableProposedHolder getProposedHolder(NakedObject owner) {
        Instance instance = getInstance(owner);
        if(!(instance instanceof MutableProposedHolder)) {
            throw new IllegalArgumentException("Instance should implement MutableProposedHolder");
        }
        MutableProposedHolder proposedHolder = (MutableProposedHolder) instance;
        return proposedHolder;
    }


}
