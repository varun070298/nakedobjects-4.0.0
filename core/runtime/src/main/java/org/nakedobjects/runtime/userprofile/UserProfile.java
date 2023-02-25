package org.nakedobjects.runtime.userprofile;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;


public class UserProfile implements DebugInfo {
	
    private final Options options = new Options();
    private final List<PerspectiveEntry> entries = new ArrayList<PerspectiveEntry>();
    private PerspectiveEntry entry;

    public UserProfile() {}

    public List<String> list() {
        List<String> list = new ArrayList<String>();
        for (PerspectiveEntry entry : entries) {
            list.add(entry.getName());
        }
        return list;
    }

    public void select(String name) {
        for (PerspectiveEntry entry : entries) {
            if (entry.getName().equals(name)) {
                this.entry = entry;
                break;
            }
        }
    }

    public PerspectiveEntry getPerspective() {
        if (entry == null) {
            if (entries.size() == 0) {
                throw new NakedObjectException("No perspective in user profile");
            } else {
                entry = entries.get(0);
            }
        }
        return entry;
    }

    public PerspectiveEntry getPerspective(String name) {
        for (PerspectiveEntry entry : entries) {
            if (entry.getName().equals(name)) {
                return entry;
            }
        }
        throw new NakedObjectException("No perspective " + name);
    }

    public void  addToPerspectives(PerspectiveEntry perspective) {
        PerspectiveEntry e = new PerspectiveEntry();
        e.copy(perspective);
        entries.add(e);
    }

    public void addToOptions(String name, String value) {
        options.addOption(name, value);
    }

    public Options getOptions() {
        return options;
    }

    public PerspectiveEntry newPerspective(String name) {
        entry = new PerspectiveEntry();
        entry.setName(name);
        entries.add(entry);
        return entry;
    }

    public void removeCurrent() {
        if (entries.size() > 1) {
            entries.remove(entry);
            entry = entries.get(0);
        }
    }

    public void copy(UserProfile template) {
        for (PerspectiveEntry entry : template.entries) {
            PerspectiveEntry e = new PerspectiveEntry();
            e.copy(entry);
            entries.add(e);
        }
        options.copy(template.getOptions());
    }

    public void saveObjects(List<NakedObject> objects) {
        entry.save(objects);
    }

    public void debugData(DebugString debug) {
        debug.appendTitle("Options");
        debug.indent();
        debug.append(options);
        debug.unindent();

        debug.appendTitle("Perspectives");
        for (PerspectiveEntry entry : entries) {
            entry.debugData(debug);
        }
    }
    
    public String debugTitle() {
        return toString();
    }
}

// Copyright (c) Naked Objects Group Ltd.
