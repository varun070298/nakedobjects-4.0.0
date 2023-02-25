package org.nakedobjects.remoting.facade;

import java.util.Properties;
import java.util.Vector;

import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.logging.Logger;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionType;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.remoting.client.transaction.ClientTransactionEvent;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.common.CollectionData;
import org.nakedobjects.remoting.data.common.EncodableObjectData;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.NullData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.data.query.PersistenceQueryData;
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
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoder;
import org.nakedobjects.runtime.context.NakedObjectsContext;


/**
 * previously called <tt>DistributionLogger</tt>.
 */
public class ServerFacadeLogger extends Logger implements ServerFacade {
    
    private static String PADDING = "      ";
    
    private final ObjectEncoderDecoder encoder;
    private final ServerFacade decorated;

    public ServerFacadeLogger(final ObjectEncoderDecoder encoder, final ServerFacade decorated, final String fileName) {
        super(fileName, false);
        this.encoder = encoder;
        this.decorated = decorated;
    }

    public ServerFacadeLogger(final ObjectEncoderDecoder encoder, final ServerFacade decorated) {
        this(encoder, decorated, null);
    }

    

    @Override
    protected Class getDecoratedClass() {
        return decorated.getClass();
    }

    //////////////////////////////////////////////////////////////////
    // init, shutdown
    //////////////////////////////////////////////////////////////////

    public void init() {
        decorated.init();
    }

    public void shutdown() {
        decorated.shutdown();
    }
    

    
    //////////////////////////////////////////////////////////////////
    // authentication, authorization
    //////////////////////////////////////////////////////////////////
    
    public OpenSessionResponse openSession(OpenSessionRequest request) {
        log("authenticate");
        return decorated.openSession(request);
    }

    public AuthorizationResponse authorizeUsability(AuthorizationRequestUsability request) {
        log("authoriseUsability");
        return decorated.authorizeUsability(request);
    }

    public AuthorizationResponse authorizeVisibility(AuthorizationRequestVisibility request) {
        log("authoriseVisibility");
        return decorated.authorizeVisibility(request);
    }

    //////////////////////////////////////////////////////////////////
    // session
    //////////////////////////////////////////////////////////////////

    public CloseSessionResponse closeSession(CloseSessionRequest request) {
    	AuthenticationSession session = request.getSession();
        log("close session " + session);
        CloseSessionResponse response = decorated.closeSession(request);
        return response;
    }


    //////////////////////////////////////////////////////////////////
    // setAssociation, setValue, clearAssociation, clearValue
    //////////////////////////////////////////////////////////////////

    public SetAssociationResponse setAssociation(
            SetAssociationRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	String fieldIdentifier = request.getFieldIdentifier();
    	IdentityData targetData = request.getTarget();
    	IdentityData associateData = request.getAssociate();

        log("set association " + fieldIdentifier + indentedNewLine() + "target: " + dump(targetData) + indentedNewLine()
                + "associate: " + dump(associateData));
        SetAssociationResponse response = decorated.setAssociation(request);
		final ObjectData[] changes = response.getUpdates();
        log("  <-- changes: " + dump(changes));
        return response;
    }

    public SetValueResponse setValue(
            final SetValueRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	String fieldIdentifier = request.getFieldIdentifier();
    	IdentityData target = request.getTarget();
    	EncodableObjectData value = request.getValue();
    	
        log("set value " + fieldIdentifier + indentedNewLine() + "target: " + dump(target) + indentedNewLine() + "value: "
                + value);
        SetValueResponse response = decorated.setValue(request);
		final ObjectData[] updates = response.getUpdates();
        log("  <-- changes: " + dump(updates));
        return response;
    }

    public ClearAssociationResponse clearAssociation(
            final ClearAssociationRequest request) {
    	String fieldIdentifier = request.getFieldIdentifier();
    	IdentityData target = request.getTarget();
    	IdentityData associate = request.getAssociate();
    	
        log("clear association " + fieldIdentifier + indentedNewLine() + "target: " + dump(target) + indentedNewLine()
                + "associate: " + dump(associate));
        ClearAssociationResponse response = decorated.clearAssociation(request);
		final ObjectData[] updates = response.getUpdates();
        log("  <-- changes: " + dump(updates));
        return response;
    }

    public ClearValueResponse clearValue(
    		final ClearValueRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	String fieldIdentifier = request.getFieldIdentifier();
    	IdentityData target = request.getTarget();
    	
        log("clear value " + fieldIdentifier + indentedNewLine() + "target: " + dump(target));
        ClearValueResponse response = decorated.clearValue(request);
		final ObjectData[] updates = response.getUpdates();
        log("  <-- changes: " + dump(updates));
        return response;
    }

    //////////////////////////////////////////////////////////////////
    // executeClientAction, executeServerAction 
    //////////////////////////////////////////////////////////////////

    public ExecuteServerActionResponse executeServerAction(
            final ExecuteServerActionRequest request) {

    	@SuppressWarnings("unused")
		AuthenticationSession session = request.getSession();
    	NakedObjectActionType actionType = request.getActionType();
    	String actionIdentifier = request.getActionIdentifier();
    	ReferenceData target = request.getTarget();
    	Data[] parameters = request.getParameters();
    	
        log("execute action " + actionIdentifier + "/" + actionType + indentedNewLine() + "target: " + dump(target)
                + indentedNewLine() + "parameters: " + dump(parameters));
        ExecuteServerActionResponse result;
        try {
            result = decorated.executeServerAction(request);
            log("  <-- returns: " + dump(result.getReturn()));
            log("  <-- persisted target: " + dump(result.getPersistedTarget()));
            log("  <-- persisted parameters: " + dump(result.getPersistedParameters()));
            log("  <-- updates: " + dump(result.getUpdates()));
            log("  <-- disposed: " + dump(result.getDisposed()));
        } catch (final RuntimeException e) {
            log("  <-- exception: " + e.getClass().getName() + " " + e.getMessage());
            throw e;
        }
        return result;
    }

    public ExecuteClientActionResponse executeClientAction(
    		ExecuteClientActionRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	ReferenceData[] data = request.getData();
    	int[] types = request.getTypes();
    	
        Vector complete = new Vector();
        StringBuffer str = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            str.append(indentedNewLine());
            str.append("[");
            str.append(i + 1);
            str.append("] ");
            switch (types[i]) {
            case ClientTransactionEvent.ADD:
                str.append("persisted: ");
                break;
            case ClientTransactionEvent.CHANGE:
                str.append("changed: ");
                break;
            case ClientTransactionEvent.DELETE:
                str.append("deleted: ");
                break;
            }
            dump(str, data[i], 3, complete);
        }
        log("execute client action " + str);

        /*
         * + indentedNewLine() + "changed: " + dump(changed, complete) + indentedNewLine() + "deleted: " +
         * dump(deleted, complete));
         */
        final ExecuteClientActionResponse results = decorated.executeClientAction(request);

        complete = new Vector();
        str = new StringBuffer();
        final ReferenceData[] persistedUpdates = results.getPersisted();
        final Version[] changedVersions = results.getChanged();
        for (int i = 0; i < persistedUpdates.length; i++) {
            str.append(indentedNewLine());
            str.append("[");
            str.append(i + 1);
            str.append("] ");
            switch (types[i]) {
            case ClientTransactionEvent.ADD:
                str.append("persisted: ");
                dump(str, persistedUpdates[i], 3, complete);
                break;
            case ClientTransactionEvent.CHANGE:
                str.append("changed: ");
                str.append(changedVersions[i]);
                break;
            }
        }
        log(" <--- execute client action results" + str);
        /*
         * log(" <-- persisted: " + dump(results.getPersisted())); log(" <-- changed: " +
         * dump(results.getChanged()));
         */
        return results;
    }

    
    //////////////////////////////////////////////////////////////////
    // getObject, resolveXxx 
    //////////////////////////////////////////////////////////////////

    public GetObjectResponse getObject(
    		GetObjectRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	Oid oid = request.getOid();
    	String specificationName = request.getSpecificationName();
    	
        log("get object " + oid);
        GetObjectResponse response = decorated.getObject(request);
		final ObjectData data = response.getObjectData();
        log(" <-- data: " + data);
        return response;
    }

    public ResolveFieldResponse resolveField(
    		ResolveFieldRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	IdentityData target = request.getTarget();
    	String fieldIdentifier = request.getFieldIdentifier();
    	
        log("resolve field " + fieldIdentifier + " - " + dump(target));
        ResolveFieldResponse response = decorated.resolveField(request);
		final Data result = response.getData();
        log(" <-- data: " + dump(result));
        return response;
    }

    public ResolveObjectResponse resolveImmediately(
    		ResolveObjectRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	IdentityData target = request.getTarget();
    	
        log("resolve immediately" + dump(target));
        ResolveObjectResponse response = decorated.resolveImmediately(request);
		final ObjectData objectData = response.getObjectData();
        log("  <-- data: " + dump(objectData));
        return response;

    }


    //////////////////////////////////////////////////////////////////
    // findInstances, hasInstances 
    //////////////////////////////////////////////////////////////////

    public FindInstancesResponse findInstances(
    		FindInstancesRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	PersistenceQueryData criteria = request.getCriteria();
    	
        log("find instances " + criteria);
        FindInstancesResponse response = decorated.findInstances(request);
		final ObjectData[] instances = response.getInstances();
        log(" <-- instances: " + dump(instances));
        return response;
    }

    public HasInstancesResponse hasInstances(
    		HasInstancesRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	String specificationName = request.getSpecificationName();
    	
        log("has instances " + specificationName);
        HasInstancesResponse response = decorated.hasInstances(request);
		final boolean hasInstances = response.hasInstances();
        log(" <-- instances: " + (hasInstances ? "yes" : "no"));
        return response;
    }

    //////////////////////////////////////////////////////////////////
    // getProperties 
    //////////////////////////////////////////////////////////////////

    public GetPropertiesResponse getProperties(GetPropertiesRequest request) {
        log("get properties");
        GetPropertiesResponse response = decorated.getProperties(request);
		final Properties properties = response.getProperties();
        log(" <-- data: " + properties);
        return response;
    }

    //////////////////////////////////////////////////////////////////
    // services 
    //////////////////////////////////////////////////////////////////

    public OidForServiceResponse oidForService(
    		OidForServiceRequest request) {
    	
    	AuthenticationSession session = request.getSession();
    	String serviceId = request.getServiceId();
    	
        log("oid for resource " + serviceId);
        
        OidForServiceResponse response = decorated.oidForService(request);
		final IdentityData oidData = response.getOidData();
        log(" <-- data: " + dump(oidData));
        return response;
    }

    
    //////////////////////////////////////////////////////////////////
    // Helpers 
    //////////////////////////////////////////////////////////////////

    private String dump(final Data data) {
        final StringBuffer str = new StringBuffer();
        dump(str, data, 1, new Vector());
        return str.toString();
    }

    private String dump(final Data[] data) {
        final StringBuffer str = new StringBuffer();
        for (int i = 0; i < data.length; i++) {
            str.append("\n    [");
            str.append(i + 1);
            str.append("] ");
            dump(str, data[i], 3, new Vector());
        }
        return str.toString();
    }

    private void dump(final StringBuffer str, final Data data, final int indent, final Vector complete) {
        if (data == null) {
            str.append("null");
        } else if (data instanceof NullData) {
            str.append("NULL (NullData object)");
        } else if (data instanceof EncodableObjectData) {
            final EncodableObjectData encodeableObjectData = ((EncodableObjectData) data);
            str.append("ValueData@" + Integer.toHexString(encodeableObjectData.hashCode()) + " " + encodeableObjectData.getType()
                    + ":" + encodeableObjectData.getEncodedObjectData());
        } else if (data instanceof IdentityData) {
            final IdentityData referenceData = (IdentityData) data;
            str.append("ReferenceData@" + Integer.toHexString(referenceData.hashCode()) + " " + referenceData.getType() + ":"
                    + referenceData.getOid() + ":" + referenceData.getVersion());
        } else if (data instanceof ObjectData) {
            dumpObjectData(str, data, indent, complete);
        } else if (data instanceof CollectionData) {
            dumpCollectionData(str, data, indent, complete);
        } else {
            str.append("Unknown: " + data);
        }
    }

    private void dumpCollectionData(final StringBuffer str, final Data data, final int indent, final Vector complete) {
        final CollectionData objectData = ((CollectionData) data);
        str.append("CollectionData@" + Integer.toHexString(objectData.hashCode()) + " " + objectData.getType() + ":"
                + objectData.getOid() + ":" + (objectData.hasAllElements() ? "A" : "-") + ":" + objectData.getVersion());
        final Object[] elements = objectData.getElements();
        for (int i = 0; elements != null && i < elements.length; i++) {
            str.append("\n");
            str.append(padding(indent));
            str.append(i + 1);
            str.append(") ");
            dump(str, (Data) elements[i], indent + 1, complete);
        }
    }

    private void dumpObjectData(final StringBuffer str, final Data data, final int indent, final Vector complete) {
        final ObjectData objectData = ((ObjectData) data);
        str.append("ObjectData@" + Integer.toHexString(objectData.hashCode()) + " " + objectData.getType() + ":"
                + objectData.getOid() + ":" + (objectData.hasCompleteData() ? "C" : "-") + ":" + objectData.getVersion());

        if (complete.contains(objectData)) {
            str.append(" (already detailed)");
            return;
        }

        complete.addElement(objectData);
        final NakedObjectSpecification spec = NakedObjectsContext.getSpecificationLoader().loadSpecification(data.getType());
        final NakedObjectAssociation[] fs = encoder.getFieldOrder(spec);
        final Object[] fields = objectData.getFieldContent();
        for (int i = 0; fields != null && i < fields.length; i++) {
            str.append("\n");
            str.append(padding(indent));
            str.append(i + 1);
            str.append(") ");
            str.append(fs[i].getId());
            str.append(": ");
            dump(str, (Data) fields[i], indent + 1, complete);
        }
    }

    private String indentedNewLine() {
        return "\n" + padding(2);
    }

    private String padding(final int indent) {
        final int length = indent * 3;
        while (length > PADDING.length()) {
            PADDING += PADDING;
        }
        return PADDING.substring(0, length);
    }


}
// Copyright (c) Naked Objects Group Ltd.
