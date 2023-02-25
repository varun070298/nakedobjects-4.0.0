package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;


public interface TextParseableContent extends Content {

    void clear();

    boolean canClear();

    boolean canWrap();

    void entryComplete();

    int getMaximumLength();

    int getNoLines();

    int getTypicalLineLength();

    Consent isEditable();

    boolean isEmpty();

    String titleString(NakedObject value);
}

// Copyright (c) Naked Objects Group Ltd.
