package org.nakedobjects.remoting.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.SocketException;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.remoting.exchange.OpenSessionRequest;
import org.nakedobjects.remoting.exchange.Request;
import org.nakedobjects.remoting.exchange.ResponseEnvelope;
import org.nakedobjects.remoting.transport.socket.server.Worker;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.system.internal.monitor.Monitor;

/**
 * Standard processing for processing an inbound {@link Request} and generating
 * an outbound {@link ResponseEnvelope} (or some sort of {@link Exception}).
 * 
 * <p>
 * Used by the (socket transport) {@link Worker} and originally inlined; now
 * factored out so can be reused by other transports, notably http.
 */
public class ServerConnectionHandler {

	private static final Logger LOG = Logger
			.getLogger(ServerConnectionHandler.class);
	private static final Logger ACCESS_LOG = Logger.getLogger("access_log");

	private final ServerConnection connection;

	private String debugRequest;
	private String debugAuthSession;
	private String debugResponse;
	private String debugContextId;

	private DebugInfo[] debugSessionInfo;

	private long responseTime;

	public ServerConnectionHandler(ServerConnection connection) {
		this.connection = connection;
	}

	// /////////////////////////////////////////////////////
	// handleRequest
	// /////////////////////////////////////////////////////

	public void handleRequest() throws IOException {
		final long start = System.currentTimeMillis();
		final Request request = connection.readRequest();
		AuthenticationSession authenticationSession = null;
		try {
			authenticationSession = openSessionIfNotAuthenticateRequest(request);

			monitorRequest(authenticationSession, request);
			executeRequest(request);

			sendResponse(request);

		} catch (final Exception e) {
			sendExceptionResponse(e);

		} finally {
			closeSessionIfNotAuthenticateRequest(authenticationSession);
			calcResponseTime(start);
		}
	}

	private AuthenticationSession openSessionIfNotAuthenticateRequest(
			final Request request) {
		AuthenticationSession authenticationSession;
		if (LOG.isDebugEnabled()) {
			debugRequest = request.getId() + " - " + request.toString();
		}
		authenticationSession = request.getSession();

		if (authenticationSession == null) {
			if (LOG.isDebugEnabled()) {
				debugAuthSession = "(none)";
				debugContextId = "(none)";
			}

			if (!(request instanceof OpenSessionRequest)) {
				throw new NakedObjectException(
						"AuthenticationSession required for all requests (except the initial Authenticate request)");
			}
		} else {
			NakedObjectsContext.openSession(authenticationSession);

			if (LOG.isDebugEnabled()) {
				debugAuthSession = authenticationSession.toString();
				debugContextId = NakedObjectsContext.getSessionId();
				debugSessionInfo = NakedObjectsContext.debugSession();
			}
		}
		return authenticationSession;
	}

	private void monitorRequest(
			final AuthenticationSession authenticationSession,
			final Request request) {
		String userName = authenticationSession != null ? authenticationSession
				.getUserName() : "**AUTHENTICATING**";
		String message = "{" + userName + "|" + this + "}  "
				+ request.toString();
		ACCESS_LOG.info(message);
		Monitor.addEvent("REQUEST", message, debugSessionInfo);
	}

	private void executeRequest(final Request request) {
		request.execute(connection.getServerFacade());
	}

	private void sendResponse(final Request request) throws IOException {
		ResponseEnvelope response = new ResponseEnvelope(request);

		if (LOG.isDebugEnabled()) {
			debugResponse = response.toString();
			LOG.debug("sending " + debugResponse);
		}
		connection.sendResponse(response);
	}

	private void sendExceptionResponse(final Exception e) throws IOException {
		LOG.error("error during remote request", e);
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));

		if (LOG.isDebugEnabled()) {
			debugResponse = sw.toString();
		}

		connection.sendResponse(e);
	}

	private void calcResponseTime(final long start) {
		responseTime = System.currentTimeMillis() - start;
	}

	private void closeSessionIfNotAuthenticateRequest(
			AuthenticationSession authenticationSession) {
		if (authenticationSession == null) {
			return;
		}
		NakedObjectsContext.closeSession();
	}

	// /////////////////////////////////////////////////////
	// Debug
	// /////////////////////////////////////////////////////

	public void debug(final DebugString debug) {
		debug.appendln("context Id", debugContextId);
		debug.appendln("authSession", debugAuthSession);
		debug.appendln("request", debugRequest);
		debug.appendln("response", debugResponse);
		debug.appendln("duration", responseTime / 1000.0f + " secs.");

		// TODO: the code below was commented out (by Rob, presumably?); I've
		// reinstated it but disabled it so no change in behaviour
		if (false) {
			debugSessionInfo(debug);
		}
	}

	private void debugSessionInfo(final DebugString debug) {
		try {
			if (debugSessionInfo != null) {
				for (DebugInfo info : debugSessionInfo) {
					debug.appendTitle(info.debugTitle());
					info.debugData(debug);
				}
			}
		} catch (RuntimeException e) {
			debug.appendException(e);
		}
	}

}

// Copyright (c) Naked Objects Group Ltd.
