package org.nakedobjects.remoting.facade;


import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
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


/**
 * previously called <tt>Distribution</tt>.
 */
public interface ServerFacade extends ApplicationScopedComponent {

	///////////////////////////////////////////////////////////////////////
	// Authentication
	///////////////////////////////////////////////////////////////////////

    OpenSessionResponse openSession(OpenSessionRequest request);
    CloseSessionResponse closeSession(CloseSessionRequest request);

    
	///////////////////////////////////////////////////////////////////////
	// Authorization
	///////////////////////////////////////////////////////////////////////

    AuthorizationResponse authorizeUsability(
    		AuthorizationRequestUsability request);
    AuthorizationResponse authorizeVisibility(
    		AuthorizationRequestVisibility request);

	///////////////////////////////////////////////////////////////////////
	// Misc
	///////////////////////////////////////////////////////////////////////

    GetPropertiesResponse getProperties(GetPropertiesRequest request);
    
	///////////////////////////////////////////////////////////////////////
	// Associations (Properties and Collections)
	///////////////////////////////////////////////////////////////////////

    SetAssociationResponse setAssociation(SetAssociationRequest request);
    ClearAssociationResponse clearAssociation(ClearAssociationRequest request);

    SetValueResponse setValue(SetValueRequest request);
    ClearValueResponse clearValue(ClearValueRequest request);
    
	///////////////////////////////////////////////////////////////////////
	// Actions
	///////////////////////////////////////////////////////////////////////

    ExecuteClientActionResponse executeClientAction(
    		ExecuteClientActionRequest request);

    ExecuteServerActionResponse executeServerAction(
            ExecuteServerActionRequest request);
    
	///////////////////////////////////////////////////////////////////////
	// getObject, resolve
	///////////////////////////////////////////////////////////////////////

    OidForServiceResponse oidForService(OidForServiceRequest request);

    GetObjectResponse getObject(GetObjectRequest request);

    ResolveObjectResponse resolveImmediately(ResolveObjectRequest request);

    ResolveFieldResponse resolveField(ResolveFieldRequest request);
    
    
	///////////////////////////////////////////////////////////////////////
	// findInstances, hasInstances
	///////////////////////////////////////////////////////////////////////

    FindInstancesResponse findInstances(FindInstancesRequest request);

    HasInstancesResponse hasInstances(HasInstancesRequest request);

}
// Copyright (c) Naked Objects Group Ltd.
