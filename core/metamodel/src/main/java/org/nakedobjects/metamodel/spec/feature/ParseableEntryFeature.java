package org.nakedobjects.metamodel.spec.feature;


public interface ParseableEntryFeature extends OneToOneFeature {

    /**
     * The typical length of each line for this value, as a number of characters.
     */
    int getTypicalLineLength();

    /**
     * Returns the maximum number of characters for a value of this type.
     */
    int getMaximumLength();

    /**
     * Returns the number of lines for a multi-line value.
     */
    int getNoLines();

    /**
     * Determines if a multi-line value can be wrapped when displayed.
     */
    boolean canWrap();

}
