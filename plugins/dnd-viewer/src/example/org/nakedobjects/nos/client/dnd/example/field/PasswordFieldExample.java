package org.nakedobjects.viewer.dnd.example.field;

import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.ViewSpecification;
import org.nakedobjects.viewer.dnd.Workspace;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.example.ExampleViewSpecification;
import org.nakedobjects.viewer.dnd.example.view.TestViews;
import org.nakedobjects.viewer.dnd.view.field.PasswordField;


public class PasswordFieldExample extends TestViews {
    public static void main(final String[] args) {
        new PasswordFieldExample();
    }

    protected void views(final Workspace workspace) {
        View parent = new ParentView();

        Content content = new DummyTextParseableField("password");
        ViewSpecification specification = new ExampleViewSpecification();
        ViewAxis axis = null;

        PasswordField textField = new PasswordField(content, specification, axis);
        textField.setParent(parent);
        // textField.setMaxWidth(200);
        textField.setLocation(new Location(50, 20));
        textField.setSize(textField.getRequiredSize(new Size()));
        workspace.addView(textField);

        textField = new PasswordField(content, specification, axis);
        textField.setParent(parent);
        // textField.setMaxWidth(80);
        textField.setLocation(new Location(50, 80));
        textField.setSize(textField.getRequiredSize(new Size()));
        workspace.addView(textField);

        content = new DummyTextParseableField("pa");
        PasswordField view = new PasswordField(content, specification, axis);
        view.setParent(parent);
        // view.setMaxWidth(200);
        view.setLocation(new Location(50, 140));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);

        view = new PasswordField(content, specification, axis);
        view.setParent(parent);
        // view.setMaxWidth(80);
        view.setLocation(new Location(50, 250));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);
    }

    protected boolean showOutline() {
        return true;
    }
}
// Copyright (c) Naked Objects Group Ltd.
