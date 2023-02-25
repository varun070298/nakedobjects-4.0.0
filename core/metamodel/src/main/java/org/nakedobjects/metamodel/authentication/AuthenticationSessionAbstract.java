package org.nakedobjects.metamodel.authentication;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.nakedobjects.metamodel.commons.encoding.DataInputExtended;
import org.nakedobjects.metamodel.commons.encoding.DataInputStreamExtended;
import org.nakedobjects.metamodel.commons.encoding.DataOutputExtended;
import org.nakedobjects.metamodel.commons.encoding.Encodable;
import org.nakedobjects.metamodel.commons.lang.ToString;

public abstract class AuthenticationSessionAbstract implements AuthenticationSession, Serializable {

    private static final long serialVersionUID = 1L;

    private final String name;
    private final List<String> roles = new ArrayList<String>();
    private final String code;
    
    private final Map<String, Object> attributeByName = new HashMap<String, Object>();

    
	/////////////////////////////////////////////////////////
	// Constructor, encode
	/////////////////////////////////////////////////////////

    @SuppressWarnings("unchecked")
	public AuthenticationSessionAbstract(String name, String code) {
    	this(name, Collections.EMPTY_LIST, code);
    }

    public AuthenticationSessionAbstract(String name, List<String> roles, String code) {
    	this.name = name;
    	this.roles.addAll(roles);
    	this.code = code;
    	initialized();
	}

    public AuthenticationSessionAbstract(final DataInputExtended input) throws IOException {
    	this.name = input.readUTF();
        this.roles.addAll(Arrays.asList(input.readUTFs()));
        this.code = input.readUTF();
        initialized();
    }


	public void encode(final DataOutputExtended output) throws IOException {
        output.writeUTF(getUserName());
        output.writeUTFs(roles.toArray(new String[]{}));
        output.writeUTF(code);
    }

	private void initialized() {
		// nothing to do
	}

	/////////////////////////////////////////////////////////
	// User Name
	/////////////////////////////////////////////////////////

    public String getUserName() {
        return name;
    }

	public boolean hasUserNameOf(final String userName) {
		return userName == null ? false : userName.equals(getUserName());
	}

	
	/////////////////////////////////////////////////////////
	// Roles
	/////////////////////////////////////////////////////////

    /**
     * Can be overridden.
     */
	public List<String> getRoles() {
		return Collections.unmodifiableList(roles);
	}
    
	
	/////////////////////////////////////////////////////////
	// Code
	/////////////////////////////////////////////////////////

    public String getValidationCode() {
        return code;
    }

	
	/////////////////////////////////////////////////////////
	// Attributes
	/////////////////////////////////////////////////////////
	
	
	public Object getAttribute(String attributeName) {
		return attributeByName.get(attributeName);
	}


	public void setAttribute(String attributeName, Object attribute) {
		attributeByName.put(attributeName, attribute);
	}


	/////////////////////////////////////////////////////////
	// toString
	/////////////////////////////////////////////////////////

    @Override
    public String toString() {
        return new ToString(this).append("name", getUserName()).append("code", getValidationCode()).toString();
    }



}
