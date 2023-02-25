package org.nakedobjects.plugins.html.context;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.plugins.html.component.Block;
import org.nakedobjects.plugins.html.component.Component;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class ObjectHistory {
    private static final Logger LOG = Logger.getLogger(ObjectHistory.class);
    private static final int MAX = 8;
    private final List<HistoryEntry> history = new ArrayList<HistoryEntry>();

    private void add(final HistoryEntry entry) {
        history.remove(entry);
        history.add(entry);
        LOG.debug("added to history: " + entry);
        if (history.size() > MAX) {
            LOG.debug("purging from history: " + history.get(0));
            history.remove(0);
        }
    }

    public void debug(final DebugString debug) {
        for (int i = history.size() - 1; i >= 0; i--) {
            final HistoryEntry object = history.get(i);
            debug.appendln(object.toString());
        }
    }

    public void listObjects(final Context context, final Block navigation) {
        final Block taskBar = context.getComponentFactory().createBlock("history", null);
        taskBar.add(context.getComponentFactory().createHeading("History"));
        for (int i = history.size() - 1; i >= 0; i--) {
            final HistoryEntry item = history.get(i);
            Component icon;
            if (item.type == HistoryEntry.OBJECT) {
                final NakedObject object = context.getMappedObject(item.id);
                
				NakedObjectsContext.getPersistenceSession().resolveImmediately(object);
				
                icon = context.getComponentFactory().createObjectIcon(object, item.id, "item");
            } else if (item.type == HistoryEntry.COLLECTION) {
                final NakedObject object = context.getMappedCollection(item.id);
                icon = context.getComponentFactory().createCollectionIcon(object, item.id);
            } else {
                throw new UnknownTypeException(item);
            }
            taskBar.add(icon);
        }
        navigation.add(taskBar);
    }

    public void addObject(final String idString) {
        add(new HistoryEntry(idString, HistoryEntry.OBJECT));
    }

    public void addCollection(final String idString) {
        add(new HistoryEntry(idString, HistoryEntry.COLLECTION));
    }

    public Iterator<HistoryEntry> elements() {
        return history.iterator();
    }

    public void remove(String existingId) {
        for (HistoryEntry entry : history) {
            if (entry.id.equals(existingId)) {
                history.remove(entry);
                break;
            }
        }
        
    }
}

// Copyright (c) Naked Objects Group Ltd.
