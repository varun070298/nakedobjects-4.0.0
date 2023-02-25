package org.nakedobjects.remoting.exchange;

import java.io.IOException;
import java.io.Serializable;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ReferenceData;

public class ExecuteServerActionResponse implements Encodable, Serializable{
    
    private static final long serialVersionUID = 1L;
    
    private final Data result;
    private final String[] messages;
    private final String[] warnings;
    private final ObjectData[] updatesData;
    private final ReferenceData[] disposedData;
    private final ObjectData persistedTarget;
    private final ObjectData[] persistedParameters;

    public ExecuteServerActionResponse(
            final Data result,
            final ObjectData[] updatesData,
            final ReferenceData[] disposed,
            final ObjectData persistedTarget,
            final ObjectData[] persistedParameters,
            final String[] messages,
            final String[] warnings) {
        this.result = result;
        this.updatesData = updatesData;
        this.disposedData = disposed;
        this.persistedTarget = persistedTarget;
        this.persistedParameters = persistedParameters;
        this.messages = messages;
        this.warnings = warnings;
        initialized();
    }

    public ExecuteServerActionResponse(final DataInputExtended input) throws IOException {
        this.result = input.readEncodable(Data.class);
        this.messages = input.readUTFs();
        this.warnings = input.readUTFs();
        this.updatesData = input.readEncodables(ObjectData.class);
        this.disposedData = input.readEncodables(ReferenceData.class);
        this.persistedTarget = input.readEncodable(ObjectData.class);
        this.persistedParameters = input.readEncodables(ObjectData.class);
        initialized();
    }

    public void encode(final DataOutputExtended output) throws IOException {
        output.writeEncodable(result);
        output.writeUTFs(messages);
        output.writeUTFs(warnings);
        output.writeEncodables(updatesData);
        output.writeEncodables(disposedData);
        output.writeEncodable(persistedTarget);
        output.writeEncodables(persistedParameters);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    //
    /////////////////////////////////////////////////////////

    /**
     * Return the Data for the result from executing the action.
     */
    public Data getReturn() {
        return result;
    }

    /**
     * Return the ObjectData for the target if it was persisited by the server.
     */
    public ObjectData getPersistedTarget() {
        return persistedTarget;
    }

    /**
     * Return the ObjectDatas for any of the parameters (in the same seqence as passed to the server) if they
     * were was persisited by the server.
     */
    public ObjectData[] getPersistedParameters() {
        return persistedParameters;
    }

    /**
     * Return the set of ObjectData for any objects that where changed by the server while executing the
     * action.
     */
    public ObjectData[] getUpdates() {
        return updatesData;
    }

    public ReferenceData[] getDisposed() {
        return disposedData;
    }

    /**
     * Return all messages created by the action.
     */
    public String[] getMessages() {
        return messages;
    }

    /**
     * Return all warnings created by the action.
     */
    public String[] getWarnings() {
        return warnings;
    }

}
// Copyright (c) Naked Objects Group Ltd.
