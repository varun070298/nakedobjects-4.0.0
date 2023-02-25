package org.nakedobjects.runtime.authentication.standard;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.nakedobjects.metamodel.authentication.AuthenticationSessionAbstract;
import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;


public final class SimpleSession extends AuthenticationSessionAbstract {
    
	private static final long serialVersionUID = 1L;

    /////////////////////////////////////////////////////////////////
    // Constructor, encode
    /////////////////////////////////////////////////////////////////

	/**
     * as per {@link #SimpleSession(String, List)}.
     */
    public SimpleSession(final String name, final String[] roles) {
        this(name, Arrays.asList(roles));
    }
    
    /**
     * Defaults {@link #getValidationCode()} to empty string (<tt>""</tt>).
     */
    public SimpleSession(final String name, final List<String> roles) {
        this(name, roles, "");
    }

    public SimpleSession(final String name, final String[] roles, final String code) {
        this(name, Arrays.asList(roles), code);
    }

    public SimpleSession(final String name, final List<String> roles, final String code) {
    	super(name, roles, code);
    }

    public SimpleSession(final DataInputExtended input) throws IOException {
    	super(input);
    }

    
    /////////////////////////////////////////////////////////////////
    // equals, hashCode
    /////////////////////////////////////////////////////////////////
    
    
    @Override
    public boolean equals(Object obj) {
    	if (obj == this) {
			return true;
		}
    	if (obj == null) {
			return false;
		}
    	if (obj.getClass() != getClass()) {
    		return false;
    	}
    	SimpleSession other = (SimpleSession) obj;
    	return equals(other);
    }
    
    public boolean equals(SimpleSession other) {
    	if (other == this) {
			return true;
		}
    	if (other == null) {
			return false;
		}
    	return getUserName().equals(other.getUserName());
    }

    @Override
    public int hashCode() {
    	return getUserName().hashCode();
    }
    
}
// Copyright (c) Naked Objects Group Ltd.
