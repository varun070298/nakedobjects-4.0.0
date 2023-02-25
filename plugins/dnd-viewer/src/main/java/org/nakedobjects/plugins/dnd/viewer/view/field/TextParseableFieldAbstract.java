package org.nakedobjects.plugins.dnd.viewer.view.field;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.TextParseableContent;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewSpecification;

public abstract class TextParseableFieldAbstract extends AbstractField {
    private static final Logger LOG = Logger.getLogger(TextField.class);

    protected TextParseableFieldAbstract(Content content, ViewSpecification design, ViewAxis axis) {
        super(content, design, axis);
    }

    @Override
    protected boolean provideClearCopyPaste() {
        return true;
    }
    
    @Override
    protected void pasteFromClipboard() {
        try {
            final String text = (String) getViewManager().getClipboard(String.class);
            final TextParseableContent content = (TextParseableContent) getContent();
            content.parseTextEntry(text);
            content.entryComplete();
            LOG.debug("pasted " + text);
        } catch (final Throwable e) {
            LOG.error("invalid paste operation " + e);
        }
    }

    @Override
    protected boolean cantClear() {
        final TextParseableContent field = (TextParseableContent) getContent();
        return !field.canClear();
    }

    @Override
    protected void clear() {
        try {
            final TextParseableContent content = (TextParseableContent) getContent();
            content.parseTextEntry("");
            content.entryComplete();
            LOG.debug("cleared");
        } catch (final Throwable e) {
            LOG.error("invalid paste operation " + e);
        }    
    }
    
    @Override
    protected void copyToClipboard() {
        final TextParseableContent content = (TextParseableContent) getContent();
        NakedObject object = content.getNaked();
        if (object != null) {
            String text = object.titleString();
            getViewManager().setClipboard(text, String.class);
            LOG.debug("copied " + text);
        }
    }

    
    public boolean isEmpty() {
        final TextParseableContent content = (TextParseableContent) getContent();
        return content.isEmpty();
    }


    @Override
    public Consent canChangeValue() {
        final TextParseableContent cont = (TextParseableContent) getContent();
        return cont.isEditable();
    }

    protected void saveValue(final NakedObject value) {
        parseEntry(value.titleString());
    }

    protected void parseEntry(final String entryText) {
        final TextParseableContent content = (TextParseableContent) getContent();
        content.parseTextEntry(entryText);
        content.entryComplete();
    }

}


// Copyright (c) Naked Objects Group Ltd.
