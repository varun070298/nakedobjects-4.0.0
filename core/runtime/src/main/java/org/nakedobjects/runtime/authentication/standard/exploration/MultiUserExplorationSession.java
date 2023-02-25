package org.nakedobjects.runtime.authentication.standard.exploration;

import java.io.IOException;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.nakedobjects.metamodel.authentication.AuthenticationSessionAbstract;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.lang.ToString;
import org.nakedobjects.runtime.authentication.standard.SimpleSession;


public final class MultiUserExplorationSession extends AuthenticationSessionAbstract implements Encodable {
	
	private static final long serialVersionUID = 1L;

    private final Set<SimpleSession> sessions = new LinkedHashSet<SimpleSession>();;
    private SimpleSession selectedSession;
    

	//////////////////////////////////////////////////////
	// Constructors
	//////////////////////////////////////////////////////

    public MultiUserExplorationSession(Set<SimpleSession> sessions, String code) {
    	super("unused", code);
        this.sessions.addAll(sessions);
        initialized();
    }
    
    public MultiUserExplorationSession(DataInputExtended input) throws IOException {
    	super(input);
    	sessions.addAll(Arrays.asList(input.readEncodables(SimpleSession.class)));
    	selectedSession = input.readEncodable(SimpleSession.class);
    	initialized();
    }
    
    @Override
    public void encode(DataOutputExtended output)
    		throws IOException {
    	super.encode(output);
    	output.writeEncodables(sessions.toArray());
    	output.writeEncodable(selectedSession);
    }
    
	private void initialized() {
		if (selectedSession == null && sessions.size() > 0) {
			selectedSession = sessions.iterator().next();
		}
	}

	
	//////////////////////////////////////////////////////
	// Overriding API
	//////////////////////////////////////////////////////

	@Override
    public String getUserName() {
        return selectedSession.getUserName();
    }

	@Override
	public boolean hasUserNameOf(String userName) {
		for(SimpleSession session: sessions) {
			if (session.hasUserNameOf(userName)) {
				return true;
			}
		}
		return false;
	}
	
	@Override
    public List<String> getRoles() {
    	return selectedSession.getRoles();
    }

	
	//////////////////////////////////////////////////////
	// not API
	//////////////////////////////////////////////////////
	
    public void setCurrentSession(String name) {
        for (SimpleSession user : this.sessions) {
            if (user.getUserName().equals(name)) {
                selectedSession = user;
                break;
            }
        }
    }

    public Set<String> getUserNames() {
        Set<String> users = new LinkedHashSet<String>();
        for (SimpleSession user : sessions) {
            users.add(user.getUserName());
        }
        return users;
    }


	//////////////////////////////////////////////////////
	// toString
	//////////////////////////////////////////////////////

    
    @Override
    public String toString() {
        return new ToString(this).append("name", getUserNames()).append("userCount", sessions.size()).toString();
    }


}
// Copyright (c) Naked Objects Group Ltd.
