package org.nakedobjects.plugins.html.component;

import org.nakedobjects.metamodel.spec.NakedObjectSpecification;


public interface Form extends Component {

    void addField(
            NakedObjectSpecification type,
            String fieldLabel,
            String fieldDescription,
            String fieldId,
            String currentEntryTitle,
            int noLines,
            boolean wrap,
            int maxLength,
            int typicalLength,
            boolean required,
            String error);

    /*
     * REVIEW the form should be asked to create specific types, like see HTMLForm.addForm()
     * 
     * void addCheckBox(....)
     * 
     * void addPasswordField(....)
     * 
     * void addMultilineField(....)
     */

    void addLookup(
            String fieldLabel,
            String fieldDescription,
            String fieldId,
            int selectedIndex,
            String[] options,
            String[] ids,
            boolean required,
            String errorMessage);

    void addReadOnlyField(String fieldLabel, String title, String fieldDescription);

}

// Copyright (c) Naked Objects Group Ltd.
