package org.nakedobjects.remoting.exchange;

import java.util.UUID;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.remoting.facade.ServerFacade;

public interface Request extends Encodable {

    void execute(ServerFacade serverFacade);

    Object getResponse();
    void setResponse(Object response);

    /**
     * Unique identifier for the request.
     * 
     * <p>
     * REVIEW: rather than using a simple <tt>int</tt>, wouldn't a {@link UUID} be preferable?
     */
    int getId();

    AuthenticationSession getSession();
}
// Copyright (c) Naked Objects Group Ltd.
