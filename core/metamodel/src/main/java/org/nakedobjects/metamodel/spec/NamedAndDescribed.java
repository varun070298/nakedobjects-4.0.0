package org.nakedobjects.metamodel.spec;

/**
 * Anything in the metamodel (which also includes peers in the reflector) that has a name and description.
 */
public interface NamedAndDescribed {

    /**
     * Return the name for this member - the field or action. This is based on the name of this member.
     * 
     * @see #getIdentifier()
     */
    String getName();

    /**
     * Returns a description of how the member is used - this complements the help text.
     * 
     * @see #getHelp()
     */
    String getDescription();

}
