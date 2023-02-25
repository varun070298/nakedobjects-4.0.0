package org.nakedobjects.remoting.facade;


import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.exchange.AuthorizationRequestUsability;
import org.nakedobjects.remoting.exchange.AuthorizationRequestVisibility;
import org.nakedobjects.remoting.exchange.ClearAssociationRequest;
import org.nakedobjects.remoting.exchange.ClearValueRequest;
import org.nakedobjects.remoting.exchange.CloseSessionRequest;
import org.nakedobjects.remoting.exchange.ExecuteClientActionRequest;
import org.nakedobjects.remoting.exchange.ExecuteServerActionRequest;
import org.nakedobjects.remoting.exchange.FindInstancesRequest;
import org.nakedobjects.remoting.exchange.GetObjectRequest;
import org.nakedobjects.remoting.exchange.GetPropertiesRequest;
import org.nakedobjects.remoting.exchange.GetPropertiesResponse;
import org.nakedobjects.remoting.exchange.HasInstancesRequest;
import org.nakedobjects.remoting.exchange.OidForServiceRequest;
import org.nakedobjects.remoting.exchange.OpenSessionRequest;
import org.nakedobjects.remoting.exchange.OpenSessionResponse;
import org.nakedobjects.remoting.exchange.AuthorizationResponse;
import org.nakedobjects.remoting.exchange.ClearAssociationResponse;
import org.nakedobjects.remoting.exchange.ClearValueResponse;
import org.nakedobjects.remoting.exchange.CloseSessionResponse;
import org.nakedobjects.remoting.exchange.ExecuteClientActionResponse;
import org.nakedobjects.remoting.exchange.ExecuteServerActionResponse;
import org.nakedobjects.remoting.exchange.FindInstancesResponse;
import org.nakedobjects.remoting.exchange.GetObjectResponse;
import org.nakedobjects.remoting.exchange.HasInstancesResponse;
import org.nakedobjects.remoting.exchange.OidForServiceResponse;
import org.nakedobjects.remoting.exchange.ResolveFieldRequest;
import org.nakedobjects.remoting.exchange.ResolveFieldResponse;
import org.nakedobjects.remoting.exchange.ResolveObjectRequest;
import org.nakedobjects.remoting.exchange.ResolveObjectResponse;
import org.nakedobjects.remoting.exchange.SetAssociationRequest;
import org.nakedobjects.remoting.exchange.SetAssociationResponse;
import org.nakedobjects.remoting.exchange.SetValueRequest;
import org.nakedobjects.remoting.exchange.SetValueResponse;
import org.nakedobjects.remoting.facade.ServerFacade;


/**
 * previously called <tt>DummyDistribution</tt>.
 */
public class DummyServerFacade implements ServerFacade {
    public ObjectData[] allInstances(final AuthenticationSession session, final String fullName, final boolean includeSubclasses) {
        return null;
    }

    public OpenSessionResponse openSession(OpenSessionRequest request) {
        return null;
    }

    public AuthorizationResponse authorizeUsability(AuthorizationRequestUsability request) {
        return new AuthorizationResponse(false);
    }

    public AuthorizationResponse authorizeVisibility(AuthorizationRequestVisibility request) {
        return new AuthorizationResponse(false);
    }

    public ClearAssociationResponse clearAssociation(
            ClearAssociationRequest request) {
        return null;
    }

    public ClearValueResponse clearValue(ClearValueRequest request) {
        return null;
    }

    public CloseSessionResponse closeSession(CloseSessionRequest request) {
    	return null;
    }

    public ExecuteServerActionResponse executeServerAction(
            ExecuteServerActionRequest request) {
        return null;
    }

    public ExecuteClientActionResponse executeClientAction(ExecuteClientActionRequest request) {
        return null;
    }

    public FindInstancesResponse findInstances(FindInstancesRequest request) {
        return null;
    }

    public HasInstancesResponse hasInstances(HasInstancesRequest request) {
        return null;
    }

    public GetObjectResponse getObject(GetObjectRequest request) {
        return null;
    }

    public GetPropertiesResponse getProperties(GetPropertiesRequest request) {
        return null;
    }

    public OidForServiceResponse oidForService(OidForServiceRequest request) {
        return null;
    }

    public ResolveFieldResponse resolveField(ResolveFieldRequest request) {
        return null;
    }

    public ResolveObjectResponse resolveImmediately(ResolveObjectRequest request) {
        return null;
    }

    public SetAssociationResponse setAssociation(
            SetAssociationRequest request) {
        return null;
    }

    public SetValueResponse setValue(
            SetValueRequest request) {
        return null;
    }

    public void init() {}

    public void shutdown() {}
}
// Copyright (c) Naked Objects Group Ltd.
