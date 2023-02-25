package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;


public interface ContentFactory {

    Content createRootContent(NakedObject object);

    Content createFieldContent(NakedObjectAssociation field, NakedObject object);
}
// Copyright (c) Naked Objects Group Ltd.
