package org.nakedobjects.runtime.authorization.standard;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authorization.AuthorizationManagerAbstract;
import org.nakedobjects.runtime.authorization.standard.noop.NoopAuthorizor;

public class AuthorizationManagerStandard extends AuthorizationManagerAbstract {

    private Authorizor authorizor;

    ///////////////////////////////////////////////////////////
    // Constructor
    ///////////////////////////////////////////////////////////
    
	public AuthorizationManagerStandard(final NakedObjectConfiguration configuration) {
    	super(configuration);
    	authorizor = new NoopAuthorizor(); // avoid null pointers
    }

    ///////////////////////////////////////////////////////////
    // init, shutddown
    ///////////////////////////////////////////////////////////
	
    public void init() {
        authorizor.init();
    }

    public void shutdown() {
        authorizor.shutdown();
    }
	
	
    ///////////////////////////////////////////////////////////
    // API
    ///////////////////////////////////////////////////////////

    public boolean isUsable(final AuthenticationSession session, NakedObject target, final Identifier identifier) {
        if (isPerspectiveMember(identifier)) {
            return true;
        }
        for (String roleName: session.getRoles()) {
            if (authorizor.isUsableInRole(roleName, identifier)) {
                return true;
            }
        }
        return false;
    }

    public boolean isVisible(final AuthenticationSession session, NakedObject target, final Identifier identifier) {
        if (isPerspectiveMember(identifier)) {
            return true;
        }
        for (String roleName: session.getRoles()) {
            if (authorizor.isVisibleInRole(roleName, identifier)) {
                return true;
            }
        }
        return false;
    }

    private boolean isPerspectiveMember(final Identifier identifier) {
        return (identifier.getClassName().equals(""));
    }
    
    
    ////////////////////////////////////////////////////
    // Dependencies (injected) 
    ////////////////////////////////////////////////////
    
    protected void setAuthorizor(final Authorizor authorisor) {
        this.authorizor = authorisor;
    }
}
