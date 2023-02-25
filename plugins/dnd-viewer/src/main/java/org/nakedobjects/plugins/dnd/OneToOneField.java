package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.consent.Consent;


public interface OneToOneField extends FieldContent, ObjectContent {
    Consent isEditable();
}

// Copyright (c) Naked Objects Group Ltd.
