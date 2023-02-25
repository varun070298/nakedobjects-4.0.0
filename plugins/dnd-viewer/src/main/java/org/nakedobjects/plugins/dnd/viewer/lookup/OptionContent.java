package org.nakedobjects.plugins.dnd.viewer.lookup;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.viewer.content.AbstractContent;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.image.ImageFactory;


public class OptionContent extends AbstractContent {
    private final NakedObject nakedObject;

    public OptionContent(final NakedObject nakedObject) {
        this.nakedObject = nakedObject;
    }

    public Consent canDrop(final Content sourceContent) {
        return Veto.DEFAULT;
    }

    public void debugDetails(final DebugString debug) {}

    public NakedObject drop(final Content sourceContent) {
        return null;
    }

    public String getDescription() {
        return null;
    }

    public String getHelp() {
        return null;
    }

    public String getIconName() {
        return nakedObject.getIconName();
    }

    public Image getIconPicture(final int iconHeight) {
        if (nakedObject instanceof NakedObject) {
            final NakedObject nakedObject = this.nakedObject;
            if (nakedObject == null) {
                return ImageFactory.getInstance().loadIcon("empty-field", iconHeight, null);
            }
            final NakedObjectSpecification specification = nakedObject.getSpecification();
            Image icon = ImageFactory.getInstance().loadIcon(specification, iconHeight, null);
            if (icon == null) {
                icon = ImageFactory.getInstance().loadDefaultIcon(iconHeight, null);
            }
            return icon;
        } else {
            return null;
        }
    }

    public String getId() {
        return "OptionContent " + nakedObject;
    }

    public NakedObject getNaked() {
        return nakedObject;
    }

    public NakedObject[] getOptions() {
        return null;
    }

    public NakedObjectSpecification getSpecification() {
        return nakedObject.getSpecification();
    }

    public boolean isOptionEnabled() {
        return false;
    }

    public boolean isTransient() {
        return false;
    }

    public void parseTextEntry(final String entryText) {}

    public String title() {
        return nakedObject.titleString();
    }
}

// Copyright (c) Naked Objects Group Ltd.
