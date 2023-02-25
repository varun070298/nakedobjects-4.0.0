package org.nakedobjects.metamodel.consent;

import org.nakedobjects.metamodel.interactions.InteractionContext;

/**
 * Powertype for the {@link InteractionContext} hierarchy.
 * 
 */
public enum InteractionContextType {

    /**
     * Persisting the object.
     */
    OBJECT_VALIDATE("Saving or updating object"),
    /**
     * Accessing the object's title.
     */
    OBJECT_TITLE("Reading object's title"),
    /**
     * Determining whether the property of the object is visible (or has been hidden).
     */
    PROPERTY_VISIBLE("View property"),
    /**
     * Determining whether the property of the object is either readable or modifiable (or has been disabled).
     */
    PROPERTY_USABLE("Use property"),
    /**
     * Reading the current value of the property of the object.
     */
    PROPERTY_READ("Read property"),
    /**
     * Modifying (or attempting to modify) the value of a property.
     */
    PROPERTY_MODIFY("Modify property"),
    /**
     * Determining whether the collection of the object is visible (or has been hidden).
     */
    COLLECTION_VISIBLE("View collection"),
    /**
     * Determining whether the collection of the object is either readable or modifiable (or has been
     * disabled).
     */
    COLLECTION_USABLE("Use collection"),
    /**
     * Reading the contents of the collection.
     */
    COLLECTION_READ("Read contents of collection"),
    /**
     * Adding to (or attempting to add to) a collection.
     */
    COLLECTION_ADD_TO("Add to collection"),
    /**
     * Removing from (or attempting to remove from) a collection.
     */
    COLLECTION_REMOVE_FROM("Remove from collection"),
    /**
     * Whether the action of the object is visible (or has been hidden).
     */
    ACTION_VISIBLE("View action"),
    /**
     * Whether the action of the object is usable (or has been disabled).
     */
    ACTION_USABLE("Use action"),
    /**
     * Whether this particular proposed argument for an action invocation is valid (or if it is in fact
     * invalid).
     * 
     * <p>
     * For example, ensuring that a regular expression match or number range is correct.
     */
    ACTION_PROPOSED_ARGUMENT("Proposed argument"),
    /**
     * Invoking (or attempting to invoke) an action.
     * 
     * <p>
     * Even if each of the {@link #ACTION_PROPOSED_ARGUMENT proposed arguments} are valid, it may not be
     * possible to invoke the action if there the arguments together are invalid (for example,
     * <tt>startDate &gt; endDate</tt>).
     */
    ACTION_INVOKE("Invoke action"),

    /**
     * Parsing a value (could be an property or an action argument).
     */
    PARSE_VALUE("Parsing value");

    private final String description;

    private InteractionContextType(final String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}

// Copyright (c) Naked Objects Group Ltd.
