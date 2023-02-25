package org.nakedobjects.plugins.dnd.viewer.undo;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;


public class AssociateCommand implements Command {
    private final String description;
    private final OneToOneAssociation field;
    private final NakedObject object;
    private final NakedObject associatedObject;
    private final String name;

    public AssociateCommand(final NakedObject object, final NakedObject associatedObject, final OneToOneAssociation field) {
        this.description = "Clear association of " + associatedObject.titleString();
        this.name = "associate " + associatedObject.titleString();
        this.object = object;
        this.associatedObject = associatedObject;
        this.field = field;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void undo() {
        field.clearAssociation(object);
    }

    public void execute() {
        field.setAssociation(object, associatedObject);
    }
}
// Copyright (c) Naked Objects Group Ltd.
