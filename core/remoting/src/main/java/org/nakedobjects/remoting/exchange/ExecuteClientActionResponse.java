package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ReferenceData;


public class ExecuteClientActionResponse implements Encodable, Serializable {

    private static final long serialVersionUID = 1L;
    private final ReferenceData[] madePersistent;
    private final Version[] changedVersion;
    private final ObjectData[] updates;

    public ExecuteClientActionResponse(final ReferenceData[] madePersistent, final Version[] changedVersion, final ObjectData[] updates) {
        this.madePersistent = madePersistent;
        this.changedVersion = changedVersion;
        this.updates = updates;
        instantiated();
    }

    public ExecuteClientActionResponse(final DataInputExtended input) throws IOException {
    	this.madePersistent = input.readEncodables(ReferenceData.class);
		this.changedVersion = input.readEncodables(Version.class);
		this.updates = input.readEncodables(ObjectData.class);
		instantiated();
    }

    public void encode(final DataOutputExtended output) throws IOException {
    	output.writeEncodables(madePersistent);
        output.writeEncodables(changedVersion);
        output.writeEncodables(updates);
    }

	private void instantiated() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////

    /**
     * Return the ObjectDatas for the persisted objects (in the same seqence as passed to the server). This is
     * used to update the client's copies with the new OIDs and Versions
     */
    public ReferenceData[] getPersisted() {
        return madePersistent;
    }

    /**
     * Return the Versions for the objects that were saved by the server for the action. These are used to
     * update the versions of the client's copies so they align with the servers copies.
     */
    public Version[] getChanged() {
        return changedVersion;
    }

    /**
     * Return the set of ObjectData for any objects that where changed by the server while executing the
     * action.
     */
    public ObjectData[] getUpdates() {
        return updates;
    }

}
// Copyright (c) Naked Objects Group Ltd.
