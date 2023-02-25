package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;


public interface ObjectParameter extends ParameterContent {

    Consent canSet(final NakedObject dragSource);

    void setObject(final NakedObject object);

}

// Copyright (c) Naked Objects Group Ltd.
