package org.nakedobjects.plugins.dnd.viewer.border;

class SaveState {
    StringBuffer missingFields = new StringBuffer();
    StringBuffer invalidFields = new StringBuffer();

    void addMissingField(final String parameterName) {
        if (missingFields.length() > 0) {
            missingFields.append(", ");
        }
        missingFields.append(parameterName);
    }

    void addInvalidField(final String parameterName) {
        if (invalidFields.length() > 0) {
            invalidFields.append(", ");
        }
        invalidFields.append(parameterName);
    }

    String getMessage() {
        String error = "";
        if (missingFields.length() > 0) {
            if (error.length() > 0) {
                error += "; ";
            }
            error += "Fields needed: " + missingFields;
        }
        if (invalidFields.length() > 0) {
            if (error.length() > 0) {
                error += "; ";
            }
            error += "Invalid fields: " + invalidFields;
        }
        return error;
    }
}

// Copyright (c) Naked Objects Group Ltd.
