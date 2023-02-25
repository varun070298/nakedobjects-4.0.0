package org.nakedobjects.plugins.dnd;

import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;


public interface OneToManyField extends FieldContent, CollectionContent {
    OneToManyAssociation getOneToManyAssociation();
}

// Copyright (c) Naked Objects Group Ltd.
