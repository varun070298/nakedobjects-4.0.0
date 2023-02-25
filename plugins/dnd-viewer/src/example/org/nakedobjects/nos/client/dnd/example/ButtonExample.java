package org.nakedobjects.viewer.dnd.example;

import org.nakedobjects.noa.reflect.Allow;
import org.nakedobjects.noa.reflect.Consent;
import org.nakedobjects.noa.reflect.NakedObjectActionType;
import org.nakedobjects.noa.reflect.Veto;
import org.nakedobjects.viewer.dnd.ButtonAction;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.Workspace;
import org.nakedobjects.viewer.dnd.action.Button;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.example.view.TestViews;


public class ButtonExample extends TestViews {

    public static void main(final String[] args) {
        new ButtonExample();
    }

    protected void views(final Workspace workspace) {
        ButtonAction action = new ButtonAction() {

            public Consent disabled(final View view) {
                return Allow.DEFAULT;
            }

            public void execute(final Workspace workspace, final View view, final Location at) {
                view.getFeedbackManager().setAction("Button 1 pressed");
            }

            public String getDescription(final View view) {
                return "Button that can be pressed";
            }

            public NakedObjectActionType getType() {
                return USER;
            }

            public String getName(final View view) {
                return "Action";
            }

            public boolean isDefault() {
                return true;
            }

            public String getHelp(final View view) {
                return null;
            }
        };

        View view = new Button(action, workspace);
        view.setLocation(new Location(100, 100));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);

        ButtonAction action2 = new ButtonAction() {

            public Consent disabled(final View view) {
                return Veto.DEFAULT;
            }

            public void execute(final Workspace workspace, final View view, final Location at) {
                view.getFeedbackManager().setViewDetail("Button 1 pressed");
            }

            public String getDescription(final View view) {
                return "Button that can't be pressed";
            }

            public NakedObjectActionType getType() {
                return USER;
            }

            public String getName(final View view) {
                return "Press Me Now!";
            }

            public boolean isDefault() {
                return false;
            }

            public String getHelp(final View view) {
                return null;
            }

        };

        View view2 = new Button(action2, workspace);
        view2.setLocation(new Location(200, 100));
        view2.setSize(view2.getRequiredSize(new Size()));
        workspace.addView(view2);
    }

}
// Copyright (c) Naked Objects Group Ltd.
