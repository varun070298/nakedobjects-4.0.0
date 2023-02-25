package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;


public interface FieldContent extends Content {

    String getFieldName();

    NakedObjectAssociation getField();

    boolean isMandatory();
    
    Consent isEditable();

    NakedObject getParent();
}
// Copyright (c) Naked Objects Group Ltd.
