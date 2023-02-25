package org.nakedobjects.remoting.client;

import org.easymock.MockControl;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.remoting.NakedObjectsRemoteException;
import org.nakedobjects.remoting.client.ClientConnection;
import org.nakedobjects.remoting.data.DummyIdentityData;
import org.nakedobjects.remoting.data.common.IdentityData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.exchange.ClearAssociationRequest;
import org.nakedobjects.remoting.exchange.ClearAssociationResponse;
import org.nakedobjects.remoting.exchange.HasInstancesRequest;
import org.nakedobjects.remoting.exchange.HasInstancesResponse;
import org.nakedobjects.remoting.exchange.OidForServiceRequest;
import org.nakedobjects.remoting.exchange.OidForServiceResponse;
import org.nakedobjects.remoting.exchange.Request;
import org.nakedobjects.remoting.exchange.RequestAbstract;
import org.nakedobjects.remoting.exchange.ResponseEnvelope;
import org.nakedobjects.remoting.facade.ServerFacade;
import org.nakedobjects.remoting.facade.proxy.ServerFacadeProxy;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.testsystem.TestProxyOid;
import org.nakedobjects.runtime.testsystem.TestProxySession;


public class CommandClientTest extends ProxyJunit3TestCase {
    private MockControl control;
    private ServerFacade serverFacade;
    private ServerFacadeProxy serverFacadeProxy;
    private TestProxySession session;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        control = MockControl.createControl(ServerFacade.class);
        serverFacade = (ServerFacade) control.getMock();
        ClientConnection connection = new ClientConnection() {
            public ResponseEnvelope executeRemotely(final Request request) {
                request.execute(serverFacade);
                return new ResponseEnvelope(request);
            }

            public void init() {}

            public void shutdown() {}
        };
        serverFacadeProxy = new ServerFacadeProxy(connection);
        session = new TestProxySession();
    }

    public void testOidForService() {
        OidForServiceRequest request = new OidForServiceRequest(session, "domain.Service");
		serverFacade.oidForService(request );
        final IdentityData data = new DummyIdentityData();
        control.setReturnValue(new OidForServiceResponse(data));

        control.replay();
        OidForServiceResponse response = serverFacadeProxy.oidForService(request);
		final IdentityData ret = response.getOidData();
        control.verify();

        assertEquals(data, ret);
    }

    public void testHasInstances() {
        HasInstancesRequest request = new HasInstancesRequest(session, "pkg.Class");
		serverFacade.hasInstances(request);
        final boolean data = true;
        control.setReturnValue(new HasInstancesResponse(data));

        control.replay();
        HasInstancesResponse response = serverFacadeProxy.hasInstances(request);
		final boolean ret = response.hasInstances();
        control.verify();

        assertEquals(data, ret);
    }

    public void testOutOfSequence() {
        ClientConnection connection = new ClientConnection() {
            public ResponseEnvelope executeRemotely(final Request request) {
                // create a response based on another request so id is different
                return new ResponseEnvelope(new RequestAbstract((AuthenticationSession) null) {
                    private static final long serialVersionUID = 1L;

                    public void execute(final ServerFacade serverFacade) {}
                });
            }

            public void init() {}

            public void shutdown() {}
        };
		serverFacadeProxy = new ServerFacadeProxy(connection);

        try {
            OidForServiceRequest request = new OidForServiceRequest(session, "domain.Service");
            serverFacadeProxy.oidForService(request);
            fail();
        } catch (final NakedObjectsRemoteException e) {
            assertTrue(e.getMessage().startsWith("Response out of sequence"));
        }
    }

    public void testClearAssociation() {
        final DummyIdentityData target = new DummyIdentityData(new TestProxyOid(1), "class 1", null);
        final DummyIdentityData associate = new DummyIdentityData(new TestProxyOid(2), "class 2", null);
        ClearAssociationRequest request = new ClearAssociationRequest(session, "fieldname", target, associate);
		serverFacade.clearAssociation(request);
        final ObjectData[] data = new ObjectData[2];
        control.setReturnValue(new ClearAssociationResponse(data));

        control.replay();
        ClearAssociationResponse response = serverFacadeProxy.clearAssociation(request);
		final ObjectData[] ret = response.getUpdates();
        control.verify();

        assertEquals(data, ret);
    }

}

// Copyright (c) Naked Objects Group Ltd.
