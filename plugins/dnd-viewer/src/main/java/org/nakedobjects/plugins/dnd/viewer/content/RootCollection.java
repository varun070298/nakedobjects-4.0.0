package org.nakedobjects.plugins.dnd.viewer.content;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.image.ImageFactory;


public class RootCollection extends AbstractCollectionContent {
    private final NakedObject collection;

    public RootCollection(final NakedObject collection) {
        this.collection = collection;
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

    @Override
    public void debugDetails(final DebugString debug) {
        debug.appendln("collection", collection);
        super.debugDetails(debug);
    }

    @Override
    public NakedObject getCollection() {
        return collection;
    }

    @Override
    public boolean isCollection() {
        return true;
    }

    public String getHelp() {
        return "No help for this collection";
    }

    public String getIconName() {
        return null;
    }

    public String getId() {
        return "";
    }

    public NakedObject getNaked() {
        return collection;
    }

    public NakedObjectSpecification getSpecification() {
        return collection.getSpecification();
    }

    public boolean isTransient() {
        return collection != null;
    }

    public void setObject(final NakedObject object) {
        throw new NakedObjectException("Invalid call");
    }

    public String title() {
        return collection.titleString();
    }

    @Override
    public String windowTitle() {
        return collection.titleString();
    }

    @Override
    public String toString() {
        return "Root Collection: " + collection;
    }

    public NakedObject drop(final Content sourceContent) {
        return null;
    }

    public Consent canDrop(final Content sourceContent) {
        return Veto.DEFAULT;
    }

    @Override
    public Image getIconPicture(final int iconHeight) {
        // return ImageFactory.getInstance().loadObjectIcon(getSpecification(), "", iconHeight);
        return ImageFactory.getInstance().loadIcon("root-collection", iconHeight, null);
    }
}
// Copyright (c) Naked Objects Group Ltd.
