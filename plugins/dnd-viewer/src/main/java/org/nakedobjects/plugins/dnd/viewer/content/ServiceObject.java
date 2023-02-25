package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.exceptions.UnexpectedCallException;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.image.ImageFactory;


public class ServiceObject extends AbstractContent {
    private final NakedObject adapter;

    public ServiceObject(final NakedObject adapter) {
        this.adapter = adapter;
    }

    public Consent canClear() {
        return Veto.DEFAULT;
    }

    public Consent canSet(final NakedObject dragSource) {
        return Veto.DEFAULT;
    }

    public void clear() {
        throw new NakedObjectException("Invalid call");
    }

    public void debugDetails(final DebugString debug) {
        debug.appendln("service", adapter);
    }

    public NakedObject getNaked() {
        return adapter;
    }

    public String getDescription() {
        final String specName = getSpecification().getSingularName();
        final String objectTitle = getObject().titleString();
        return specName + (specName.equalsIgnoreCase(objectTitle) ? "" : ": " + objectTitle) + " "
                + getSpecification().getDescription();
    }

    public String getHelp() {
        return "";
    }

    public String getId() {
        return "";
    }

    public NakedObject getObject() {
        return adapter;
    }

    public NakedObject[] getOptions() {
        return null;
    }

    public NakedObjectSpecification getSpecification() {
        return adapter.getSpecification();
    }

    @Override
    public boolean isObject() {
        return false;
    }

    public boolean isOptionEnabled() {
        return false;
    }

    public boolean isTransient() {
        return adapter != null && adapter.isTransient();
    }

    public void setObject(final NakedObject object) {
        throw new NakedObjectException("Invalid call");
    }

    public String title() {
        return adapter.titleString();
    }

    @Override
    public String toString() {
        return "Service Object [" + adapter + "]";
    }

    @Override
    public String windowTitle() {
        return (isTransient() ? "UNSAVED " : "") + getSpecification().getSingularName();
    }

    public Consent canDrop(final Content sourceContent) {
        NakedObjectAction action = actionFor(sourceContent);
        if (action == null) {
            return Veto.DEFAULT;
        } else {
            NakedObject source = sourceContent.getNaked();
            final Consent parameterSetValid = action.isProposedArgumentSetValid(adapter, new NakedObject[] { source });
            parameterSetValid.setDescription("Execute '" + action.getName() + "' with " + source.titleString());
            return parameterSetValid;
        }
      }

    private NakedObjectAction actionFor(final Content sourceContent) {
        NakedObjectAction action;
        action = adapter.getSpecification().getObjectAction(NakedObjectActionConstants.USER, null,
                new NakedObjectSpecification[] { sourceContent.getSpecification() });
        return action;
    }

    public NakedObject drop(final Content sourceContent) {
        NakedObjectAction action = actionFor(sourceContent);
        NakedObject source = sourceContent.getNaked();
        return action.execute(adapter, new NakedObject[] { source });
    }

    public String getIconName() {
        final NakedObject object = getObject();
        return object == null ? null : object.getIconName();
    }

    public Image getIconPicture(final int iconHeight) {
        final NakedObject nakedObject = getObject();
        final NakedObjectSpecification specification = nakedObject.getSpecification();
        final Image icon = ImageFactory.getInstance().loadIcon(specification, iconHeight, null);
        return icon;
    }

    public void parseTextEntry(final String entryText) {
        throw new UnexpectedCallException();
    }

    @Override
    public void viewMenuOptions(final UserActionSet options) {}

}
// Copyright (c) Naked Objects Group Ltd.
