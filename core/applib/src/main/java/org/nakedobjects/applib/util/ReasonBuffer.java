package org.nakedobjects.applib.util;

/**
 * Helper class to create properly concatenated reason string for use in method that return {@link String}s
 * with reasons.
 * 
 * <p>
 * If no reasons are specified {@link #getReason()} will return <code>null</code> , otherwise it will return
 * a {@link String} with all the valid reasons concatenated with a semi-colon separating each one.
 */
public class ReasonBuffer {
    StringBuffer reasonBuffer = new StringBuffer();

    /**
     * Append a reason to the list of existing reasons.
     */
    public void append(final String reason) {
        if (reason != null) {
            if (reasonBuffer.length() > 0) {
                reasonBuffer.append("; ");
            }
            reasonBuffer.append(reason);
        }
    }

    /**
     * Append a reason to the list of existing reasons if the condition flag is true.
     */
    public void appendOnCondition(final boolean condition, final String reason) {
        if (condition) {
            append(reason);
        }
    }

    /**
     * Return the combined set of reasons, or <code>null</code> if there are none.
     */
    public String getReason() {
        return reasonBuffer.length() == 0 ? null : reasonBuffer.toString();
    }

}

// Copyright (c) Naked Objects Group Ltd.
