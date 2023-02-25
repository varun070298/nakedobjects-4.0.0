package org.nakedobjects.applib.events;

import java.util.EventObject;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.util.NameUtils;


/**
 * Represents an interaction with a domain object or a particular feature (property, collection, action) of a
 * domain object.
 * 
 * <p>
 * Many of the interactions are checks for {@link VisibilityEvent visibility},
 * {@link UsabilityEvent usability} and {@link ValidityEvent validity}.
 */
public abstract class InteractionEvent extends EventObject {

    private static final long serialVersionUID = 1L;

    private final Identifier identifier;
    private String reason;
    private Class<?> advisorClass;

    public InteractionEvent(final Object source, final Identifier identifier) {
        super(source);
        this.identifier = identifier;
    }


    /**
     * The domain object (pojo) against which the interaction occurred.
     */
    @Override
    public Object getSource() {
        return super.getSource();
    }

    /**
     * The {@link Identifier} of the feature of the object being interacted with.
     * 
     * <p>
     * Will be consistent with the subclass of {@link InteractionEvent}. So for example a
     * {@link PropertyModifyEvent} will have an {@link Identifier} that identifies the property being
     * modified.
     * 
     * @return
     */
    public Identifier getIdentifier() {
        return identifier;
    }

    /**
     * Convenience method that returns the {@link Identifier#getClassName() class name} of the
     * {@link #getIdentifier() identifier}.
     * 
     * @see #getIdentifier
     */
    public String getClassName() {
        return identifier.getClassName();
    }

    /**
     * As per {@link #getClassName()}, but {@link NameUtils#naturalName(String) naturalized}.
     */
    public String getClassNaturalName() {
        return NameUtils.naturalName(getClassName());
    }

    /**
     * Convenience method that returns the {@link Identifier#getMemberName() member name} of the
     * {@link #getIdentifier() identifier}.
     * 
     * @see #getIdentifier
     */
    public String getMemberName() {
        return identifier.getMemberName();
    }

    /**
     * As per {@link #getMemberName()}, but naturalized.
     */
    public String getMemberNaturalName() {
        return NameUtils.naturalName(getMemberName());
    }

    /**
     * Convenience method that returns the {@link Identifier#getClassName() class name} of the
     * {@link #getIdentifier() identifier}.
     */
    public String[] getMemberParameterNames() {
        return identifier.getMemberParameterNames();
    }

    /**
     * As per {@link #getMemberParameterName()}, but naturalized.
     */
    public String[] getMemberParameterNaturalNames() {
        return NameUtils.naturalNames(getMemberParameterNames());
    }

    /**
     * The reason, if any, that this interaction may have been vetoed or otherwise disallowed.
     * 
     * <p>
     * Intended to be {@link #setReason(String) set} as a result of consulting one of the facets.
     * 
     * @return
     */
    public String getReason() {
        return reason;
    }

    /**
     * The class of the (first) advisor, if any, that provided the {@link #getReason() reason} that this
     * interaction is {@link #isVeto() vetoed}.
     * 
     * @return
     */
    public Class<?> getAdvisorClass() {
        return advisorClass;
    }

    /**
     * Specify the {@link #getReason() reason} that this interaction has been vetoed and the
     * {@link #getAdvisorClass() class of the advisor} that did the veto.
     */
    public void advised(final String reason, final Class<?> advisorClass) {
        this.reason = reason;
        this.advisorClass = advisorClass;
    }

    /**
     * Whether this interaction has been vetoed (meaning that {@link #getReason()} and
     * {@link #getAdvisorClass()} will both be non-<tt>null</tt> and the {@link #getReason() reason}
     * non-empty.)
     * 
     * <p>
     * The interpretation of this depends on the subclass:
     * <ul>
     * <li>for {@link VisibilityEvent}, a veto means that the feature (property, collection, action) is
     * hidden</li>
     * <li>for {@link UsabilityEvent}, a veto means that the feature is disabled</li>
     * <li>for {@link ValidityEvent}, a veto means that the proposed modification (property value, object
     * added/removed, action argument) is invalid</li>
     * </ul>
     */
    public boolean isVeto() {
        return getReason() != null && getReason().length() > 0;
    }

}
