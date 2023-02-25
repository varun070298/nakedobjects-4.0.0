package org.nakedobjects.viewer.dnd.example.field;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.Oid;
import org.nakedobjects.noa.adapter.ResolveState;
import org.nakedobjects.noa.adapter.Version;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.noa.reflect.Allow;
import org.nakedobjects.noa.reflect.Consent;
import org.nakedobjects.noa.reflect.NakedObjectAction;
import org.nakedobjects.noa.reflect.NakedObjectActionInstance;
import org.nakedobjects.noa.reflect.NakedObjectActionType;
import org.nakedobjects.noa.reflect.OneToManyAssociation;
import org.nakedobjects.noa.reflect.OneToManyAssociationInstance;
import org.nakedobjects.noa.reflect.OneToOneAssociation;
import org.nakedobjects.noa.reflect.OneToOneAssociationInstance;
import org.nakedobjects.noa.reflect.listeners.ObjectListener;
import org.nakedobjects.noa.spec.NakedObjectSpecification;
import org.nakedobjects.noa.util.DebugString;
import org.nakedobjects.noa.util.NotImplementedException;
import org.nakedobjects.nof.core.util.AsString;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.TextParseableContent;
import org.nakedobjects.viewer.dnd.UserActionSet;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.ViewSpecification;
import org.nakedobjects.viewer.dnd.Workspace;
import org.nakedobjects.viewer.dnd.drawing.Image;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.example.ExampleViewSpecification;
import org.nakedobjects.viewer.dnd.example.view.TestViews;
import org.nakedobjects.viewer.dnd.view.field.SingleLineTextField;
import org.nakedobjects.viewer.dnd.view.field.WrappedTextField;


class DummyTextParseableField implements TextParseableContent {
    private NakedObject object = new NakedObject() {

        public String asEncodedString(Object object) {
            return null;
        }

        public String getIconName() {
            return null;
        }

        public Object getObject() {
            return null;
        }

        public NakedObjectSpecification getSpecification() {
            return null;
        }

        public void parseTextEntry(Object original, final String text) {
            DummyTextParseableField.this.text = text;
        }

        public Object restoreFromEncodedString(final String data) {
            return null;
        }

        public String titleString() {
            return text;
        }

        public String toString() {
            AsString str = new AsString(this);
            str.append("text", text);
            return str.toString();
        }

        public void setMask(String mask) {}

        public int defaultTypicalLength() {
            return 0;
        }

        public void addObjectListener(ObjectListener listener) {}

        public void fireChangedEvent() {}

        public NakedObjectActionInstance getActionInstance(NakedObjectAction action) {
            return null;
        }

        public NakedObjectActionInstance[] getActionInstances(NakedObjectActionType type) {
            return null;
        }

        public OneToManyAssociationInstance getOneToManyAssociation(OneToManyAssociation field) {
            return null;
        }

        public OneToManyAssociationInstance[] getOneToManyAssociationInstances() {
            return null;
        }

        public OneToOneAssociationInstance getOneToOneAssociation(OneToOneAssociation field) {
            return null;
        }

        public OneToOneAssociationInstance[] getOneToOneAssociationInstances() {
            return null;
        }

        public void removeObjectListener(ObjectListener listener) {}

        public void changeState(ResolveState newState) {}

        public void checkLock(Version version) {}

        public Oid getOid() {
            return null;
        }

        public ResolveState getResolveState() {
            return null;
        }

        public Version getVersion() {
            return null;
        }

        public void setOptimisticLock(Version version) {}

        public void replacePojo(Object pojo) {
            throw new NotImplementedException();
        }
        
        public TypeOfFacet getTypeOfFacet() {
            return null;
        }
        
        public void setTypeOfFacet(TypeOfFacet typeOfFacet) {}

    };
    private String text;

    public DummyTextParseableField(final String text) {
        this.text = text;
    }

    public Consent canDrop(final Content sourceContent) {
        return null;
    }

    public void clear() {}

    public void contentMenuOptions(UserActionSet options) {}

    public void debugDetails(final DebugString debug) {}

    public NakedObject drop(final Content sourceContent) {
        return null;
    }

    public void entryComplete() {}

    public String getDescription() {
        return null;
    }

    public String getHelp() {
        return null;
    }

    public String getIconName() {
        return null;
    }

    public Image getIconPicture(int iconHeight) {
        return null;
    }

    public String getId() {
        return null;
    }

    public NakedObject getNaked() {
        return getObject();
    }

    public NakedObject getObject() {
        return object;
    }

    public int getMaximumLength() {
        return 0;
    }

    public int getTypicalLineLength() {
        return 0;
    }

    public NakedObjectSpecification getSpecification() {
        return null;
    }

    public boolean isCollection() {
        return false;
    }

    public Consent isEditable() {
        return Allow.DEFAULT;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean isObject() {
        return false;
    }

    public boolean isPersistable() {
        return false;
    }

    public boolean isTransient() {
        return false;
    }

    public boolean isTextParseable() {
        return true;
    }

    public void parseTextEntry(final String entryText) {}

    public String title() {
        return null;
    }

    public void viewMenuOptions(UserActionSet options) {}

    public String windowTitle() {
        return null;
    }

    public NakedObject[] getOptions() {
        return null;
    }

    public boolean isOptionEnabled() {
        return false;
    }

    public int getNoLines() {
        return 1;
    }

    public boolean canClear() {
        return false;
    }

    public boolean canWrap() {
        return false;
    }

    public String titleString(NakedObject value) {
        return null;
    }

}

public class TextFieldExample extends TestViews {
    private static final String LONG_TEXT = "Naked Objects - a framework that exposes behaviourally complete business\n"
            + "objects directly to the user. Copyright (C) 2000 - 2005 Naked Objects Group  Ltd\n" + "\n"
            + "This program is free software; you can redistribute it and/or modify it under\n"
            + "the terms of the GNU General Public License as published by the Free Software\n"
            + "Foundation; either version 2 of the License, or (at your option) any later " + "version.";

    private static final String SHORT_TEXT = "Short length of text for small field";

    public static void main(final String[] args) {
        new TextFieldExample();
    }

    protected void views(final Workspace workspace) {
        View parent = new ParentView();

        TextParseableContent content = new DummyTextParseableField(SHORT_TEXT);
        ViewSpecification specification = new ExampleViewSpecification();
        ViewAxis axis = null;

        SingleLineTextField textField = new SingleLineTextField(content, specification, axis, true);
        textField.setParent(parent);
        textField.setWidth(200);
        textField.setLocation(new Location(50, 20));
        textField.setSize(textField.getRequiredSize(new Size()));
        workspace.addView(textField);

        textField = new SingleLineTextField(content, specification, axis, false);
        textField.setParent(parent);
        textField.setWidth(80);
        textField.setLocation(new Location(50, 80));
        textField.setSize(textField.getRequiredSize(new Size()));
        workspace.addView(textField);

        content = new DummyTextParseableField(LONG_TEXT);
        WrappedTextField view = new WrappedTextField(content, specification, axis, false);
        view.setParent(parent);
        view.setNoLines(5);
        view.setWidth(200);
        view.setWrapping(false);
        view.setLocation(new Location(50, 140));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);

        view = new WrappedTextField(content, specification, axis, true);
        view.setParent(parent);
        view.setNoLines(8);
        view.setWidth(500);
        view.setWrapping(false);
        view.setLocation(new Location(50, 250));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);
    }
}

// Copyright (c) Naked Objects Group Ltd.
