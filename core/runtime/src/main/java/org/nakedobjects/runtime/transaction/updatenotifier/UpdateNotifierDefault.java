package org.nakedobjects.runtime.transaction.updatenotifier;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.lang.ToString;


public class UpdateNotifierDefault extends UpdateNotifierAbstract implements DebugInfo {
	
    private static final Logger LOG = Logger.getLogger(UpdateNotifierDefault.class);
    private final List<NakedObject> changes = new ArrayList<NakedObject>();
    private final List<NakedObject> disposals = new ArrayList<NakedObject>();

    
    ////////////////////////////////////////////////////
    // Constructor
    ////////////////////////////////////////////////////

    public UpdateNotifierDefault() {
        // does nothing
    }
    
    ////////////////////////////////////////////////////
    // Changed Objects
    ////////////////////////////////////////////////////

    public synchronized void addChangedObject(final NakedObject adapter) {
        ResolveState resolveState = adapter.getResolveState();
        if (!resolveState.isResolved() && ! adapter.isTransient()) {
            return;
        }
        
        if (LOG.isDebugEnabled()) {
            LOG.debug("mark as changed " + adapter);
        }
        if (!changes.contains(adapter)) {
            changes.add(adapter);
        }
    }

	public List<NakedObject> getChangedObjects() {
        if (changes.size() > 0) {
        	if (LOG.isDebugEnabled()) {
        		LOG.debug("dirty (changed) objects " + changes);
        	}
        }
		List<NakedObject> changedObjects = new ArrayList<NakedObject>();
		changedObjects.addAll(changes);
		
		changes.clear();
		
		return Collections.unmodifiableList(changedObjects);
	}
    
    ////////////////////////////////////////////////////
    // Disposed Objects
    ////////////////////////////////////////////////////

    public void addDisposedObject(final NakedObject adapter) {
    	if (LOG.isDebugEnabled()) {
    		LOG.debug("mark as disposed " + adapter);
    	}
        if (!disposals.contains(adapter)) {
            disposals.add(adapter);
        }
    }

	public List<NakedObject> getDisposedObjects() {
        if (disposals.size() > 0) {
        	if (LOG.isDebugEnabled()) {
        		LOG.debug("dirty (disposed) objects " + disposals);
        	}
        }
		List<NakedObject> disposedObjects = new ArrayList<NakedObject>();
		disposedObjects.addAll(disposals);
		
		disposals.clear();
		
		return Collections.unmodifiableList(disposedObjects);
	}
    
    
    ////////////////////////////////////////////////////
    // Empty, Clear
    ////////////////////////////////////////////////////

    public void ensureEmpty() {
        if (changes.size() > 0) {
            throw new NakedObjectException("Update notifier still has updates");
        }
    }

    public void clear() {
        changes.clear();
        disposals.clear();
    }
    
    
    ////////////////////////////////////////////////////
    // Debugging
    ////////////////////////////////////////////////////

    public void debugData(final DebugString debug) {
        debug.appendln("Changes");
        debugList(debug, changes);

        debug.appendln("Disposals");
        debugList(debug, disposals);
    }

    public String debugTitle() {
        return "Simple Update Notifier";
    }

    private void debugList(final DebugString debug, final List<NakedObject> list) {
        debug.indent();
        if (list.size() == 0) {
            debug.appendln("none");
        } else {
            for(NakedObject adapter: list) {
                debug.appendln(adapter.toString());
            }
        }
        debug.unindent();
    }

    
    ////////////////////////////////////////////////////
    // toString
    ////////////////////////////////////////////////////
    
    @Override
    public String toString() {
        return new ToString(this).append("changes", changes).toString();
    }

}

// Copyright (c) Naked Objects Group Ltd.
