package org.nakedobjects.viewer.dnd.example.border;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.reflect.Allow;
import org.nakedobjects.noa.reflect.Consent;
import org.nakedobjects.noa.reflect.NakedObjectActionType;
import org.nakedobjects.noa.reflect.Veto;
import org.nakedobjects.viewer.dnd.ButtonAction;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.ViewSpecification;
import org.nakedobjects.viewer.dnd.Workspace;
import org.nakedobjects.viewer.dnd.border.ButtonBorder;
import org.nakedobjects.viewer.dnd.content.RootObject;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.example.ExampleViewSpecification;
import org.nakedobjects.viewer.dnd.example.view.TestObjectView;
import org.nakedobjects.viewer.dnd.example.view.TestViews;


public class ButtonBorderExample extends TestViews {

    public static void main(final String[] args) {
        new ButtonBorderExample();
    }

    protected void views(final Workspace workspace) {
        ButtonAction[] actions = new ButtonAction[] { new ButtonAction() {

            public Consent disabled(final View view) {
                return Allow.DEFAULT;
            }

            public void execute(final Workspace workspace, final View view, final Location at) {
                view.getFeedbackManager().addMessage("Button 1 pressed");
            }

            public String getDescription(final View view) {
                return "Button that can be pressed";
            }

            public String getName(final View view) {
                return "Action";
            }

            public NakedObjectActionType getType() {
                return USER;
            }

            public boolean isDefault() {
                return true;
            }

            public String getHelp(final View view) {
                return null;
            }
        },

        new ButtonAction() {

            public Consent disabled(final View view) {
                return Veto.DEFAULT;
            }

            public void execute(final Workspace workspace, final View view, final Location at) {
                view.getFeedbackManager().addMessage("Button 2 pressed");
            }

            public String getDescription(final View view) {
                return "Button that can't be pressed";
            }

            public String getName(final View view) {
                return "Disabled";
            }

            public NakedObjectActionType getType() {
                return USER;
            }

            public boolean isDefault() {
                return false;
            }

            public String getHelp(final View view) {
                return null;
            }
        } };

        NakedObject object = createExampleObjectForView();
        Content content = new RootObject(object);
        ViewSpecification specification = new ExampleViewSpecification();
        ViewAxis axis = null;

        View view = new ButtonBorder(actions, new TestObjectView(content, specification, axis, 200, 80, "VIEW in border"));

        view.setLocation(new Location(100, 100));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);

    }
}
// Copyright (c) Naked Objects Group Ltd.
