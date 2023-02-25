package org.nakedobjects.metamodel.authentication;

import java.util.List;

import org.nakedobjects.metamodel.commons.encoding.Encodable;


/**
 * The representation within the system of an authenticated user.
 */
public interface AuthenticationSession extends Encodable {

	/**
     * The name of the authenticated user; for display purposes only.
     */
    public String getUserName();

	public boolean hasUserNameOf(String userName);

    /**
     * The roles this user belongs to
     */
    public List<String> getRoles();

    /**
     * A unique code given to this session during authentication.
     * 
     * <p>
     * This can be used to confirm that this session has been properly created and the user has been
     * authenticated. It should return an empty string (<tt>""</tt>) if this is unauthenticated user (i.e., as
     * created within an exploration system).
     */
    public String getValidationCode();
    
    /**
     * For viewers (in particular) to store additional attributes, analogous to an
     * <tt>HttpSession</tt>.
     */
    public Object getAttribute(String attributeName);
    
    /**
     * @see #getAttribute(String)
     */
    public void setAttribute(String attributeName, Object attribute);

}
// Copyright (c) Naked Objects Group Ltd.
