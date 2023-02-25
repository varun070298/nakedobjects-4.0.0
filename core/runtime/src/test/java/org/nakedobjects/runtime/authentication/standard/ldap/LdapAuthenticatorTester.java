package org.nakedobjects.runtime.authentication.standard.ldap;

import org.nakedobjects.runtime.authentication.AuthenticationRequestPassword;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class LdapAuthenticatorTester {

    public static void main(final String[] args) {
        final LdapAuthenticator auth = new LdapAuthenticator(NakedObjectsContext.getConfiguration());

        AuthenticationRequestPassword req = new AuthenticationRequestPassword("unauth", "pass");
        try {
            System.out.println("unauth auth=" + auth.isValid(req));
        } catch (final Exception e) {
            System.out.println("unauth failed authentication!");
            e.printStackTrace();
        }
        req = new AuthenticationRequestPassword("joe", "pass");
        try {
            System.out.println("joe auth=" + auth.isValid(req));
        } catch (final Exception e) {
            System.out.println("joe auth failed!!");
            e.printStackTrace();
        }
        req = new AuthenticationRequestPassword("joe", "wrongpass");
        try {
            System.out.println("joe wrongpass auth=" + auth.isValid(req));
        } catch (final Exception e) {
            System.out.println("joe wrongpass auth failed!!");
            e.printStackTrace();
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
