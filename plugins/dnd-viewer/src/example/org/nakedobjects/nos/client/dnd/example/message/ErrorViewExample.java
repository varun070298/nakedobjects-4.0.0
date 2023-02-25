package org.nakedobjects.viewer.dnd.example.message;

import org.nakedobjects.noa.NakedObjectRuntimeException;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.Workspace;
import org.nakedobjects.viewer.dnd.drawing.Location;
import org.nakedobjects.viewer.dnd.drawing.Size;
import org.nakedobjects.viewer.dnd.example.view.TestViews;
import org.nakedobjects.viewer.dnd.view.message.DetailedMessageViewSpecification;
import org.nakedobjects.viewer.dnd.view.message.ExceptionMessageContent;
import org.nakedobjects.viewer.dnd.view.message.MessageDialogSpecification;



public class ErrorViewExample extends TestViews {

    public static void main(final String[] args) {
        new ErrorViewExample();
    }

    protected void views(final Workspace workspace) {
        Object object = new NakedObjectRuntimeException("The test exception message");
        Content content = new ExceptionMessageContent((Throwable) object);
        ViewAxis axis = null;

        View view = new MessageDialogSpecification().createView(content, axis);
        view.setLocation(new Location(100, 30));
        view.setSize(view.getRequiredSize(new Size()));
        workspace.addView(view);

        View view2 = new DetailedMessageViewSpecification().createView(content, axis);
        view2.setLocation(new Location(100, 260));
        view2.setSize(view2.getMaximumSize());
        workspace.addView(view2);
    }

}
// Copyright (c) Naked Objects Group Ltd.
