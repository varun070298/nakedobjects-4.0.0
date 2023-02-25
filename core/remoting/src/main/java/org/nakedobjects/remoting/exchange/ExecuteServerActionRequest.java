package org.nakedobjects.remoting.exchange;

import java.io.IOException;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.facade.ServerFacade;

public class ExecuteServerActionRequest extends RequestAbstract {
    private static final long serialVersionUID = 1L;
    private final NakedObjectActionType actionType;
    private final String actionIdentifier;
    private final ReferenceData target;
    private final Data[] parameters;

    public ExecuteServerActionRequest(
            final AuthenticationSession session,
            final NakedObjectActionType actionType,
            final String actionIdentifier,
            final ReferenceData target,
            final Data[] parameters) {
        super(session);
        this.actionType = actionType;
        this.actionIdentifier = actionIdentifier;
        this.target = target;
        this.parameters = parameters;
        initialized();
    }

    public ExecuteServerActionRequest(final DataInputExtended input) throws IOException {
        super(input);
        this.actionType = NakedObjectActionType.valueOf(input.readUTF());
        this.actionIdentifier = input.readUTF();
        this.target = input.readEncodable(IdentityData.class);
        this.parameters = input.readEncodables(Data.class);
        initialized();
    }

    @Override
    public void encode(DataOutputExtended output)
    		throws IOException {
    	super.encode(output);
        output.writeUTF(actionType.name());
        output.writeUTF(actionIdentifier);
        output.writeEncodable(target);
        output.writeEncodables(parameters);
    }

	private void initialized() {
		// nothing to do
	}

    /////////////////////////////////////////////////////////
    // request data
    /////////////////////////////////////////////////////////

	public NakedObjectActionType getActionType() {
		return actionType;
	}
	
	public String getActionIdentifier() {
		return actionIdentifier;
	}
	
	public ReferenceData getTarget() {
		return target;
	}
	
	public Data[] getParameters() {
		return parameters;
	}
	
    /////////////////////////////////////////////////////////
    // execute, response
    /////////////////////////////////////////////////////////

	/**
	 * {@link #setResponse(Object) Sets a response} of a {@link ExecuteServerActionResponse}.
	 */
    public void execute(final ServerFacade serverFacade) {
        ExecuteServerActionResponse response = serverFacade.executeServerAction(this);
		setResponse(response);
    }

    /**
     * Downcasts.
     */
    @Override
    public ExecuteServerActionResponse getResponse() {
        return (ExecuteServerActionResponse) super.getResponse();
    }

    /////////////////////////////////////////////////////////
    // toString
    /////////////////////////////////////////////////////////

    @Override
    public String toString() {
        final ToString str = new ToString(this);
        str.append("method", actionIdentifier);
        str.append("target", target);
        return str.toString();
    }
}
// Copyright (c) Naked Objects Group Ltd.
