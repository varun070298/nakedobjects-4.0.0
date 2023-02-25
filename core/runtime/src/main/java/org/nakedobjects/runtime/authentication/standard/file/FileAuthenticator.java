package org.nakedobjects.runtime.authentication.standard.file;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.lang.IoUtils;
import org.nakedobjects.metamodel.commons.resource.ResourceStreamSource;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.AuthenticationRequest;
import org.nakedobjects.runtime.authentication.AuthenticationRequestPassword;
import org.nakedobjects.runtime.authentication.standard.PasswordRequestAuthenticatorAbstract;

public class FileAuthenticator extends PasswordRequestAuthenticatorAbstract {
	
	private final ResourceStreamSource resourceStreamSource;

    public FileAuthenticator(final NakedObjectConfiguration configuration) {
    	super(configuration);
        this.resourceStreamSource = configuration.getResourceStreamSource();
    }

    public final boolean isValid(final AuthenticationRequest request) {
        final AuthenticationRequestPassword passwordRequest = (AuthenticationRequestPassword) request;
        final String username = passwordRequest.getName();
        if (username == null || username.equals("")) {
            return false;
        }
        final String password = passwordRequest.getPassword();
        Assert.assertNotNull(password);

        BufferedReader reader = null;
        try {
            InputStream readStream = resourceStreamSource.readResource(FileAuthenticationConstants.PASSWORDS_FILE);
            if (readStream == null) {
                throw new NakedObjectException("Failed to open password file: " + FileAuthenticationConstants.PASSWORDS_FILE + " from " + resourceStreamSource.getName());
            }
            reader = new BufferedReader(new InputStreamReader(readStream));
            String line;
            while ((line = reader.readLine()) != null) {
                if (commentedOutOrEmpty(line)) {
                    continue;
                }
                if (line.indexOf(':') == -1) {
                    throw new NakedObjectException("Invalid entry in password file - no colon (:) found on line: " + line);
                }
                final String name = line.substring(0, line.indexOf(':'));
                if (!name.equals(username)) {
                	continue;
                }
                
                return isPasswordValidForUser(request, password, line);
            }
        } catch (final IOException e) {
            throw new NakedObjectException("Failed to read password file: " + FileAuthenticationConstants.PASSWORDS_FILE + " from " + resourceStreamSource.getName());
        } finally {
        	IoUtils.closeSafely(reader);
        }

        return false;
    }

	private boolean commentedOutOrEmpty(String line) {
		return line.startsWith("#") || line.trim().length() == 0;
	}

	private boolean isPasswordValidForUser(
			final AuthenticationRequest request,
			final String password, String line) {
		int posFirstColon = line.indexOf(':');
		int posPasswordStart = posFirstColon + 1;
		
		int posSecondColonIfAny = line.indexOf(':', posPasswordStart);
		int posPasswordEnd = posSecondColonIfAny == -1 ? line.length() : posSecondColonIfAny;
		
		String parsedPassword = line.substring(posPasswordStart, posPasswordEnd);
		if (parsedPassword.equals(password)) {
		    if (posSecondColonIfAny != -1) {
		        setRoles(request, line.substring(posSecondColonIfAny + 1));
		    }
		    return true;
		} else {
		    return false;
		}
	}

    private final void setRoles(final AuthenticationRequest request, final String line) {
        final StringTokenizer tokens = new StringTokenizer(line, "|", false);
        String[] roles = new String[tokens.countTokens()];
        for (int i = 0; tokens.hasMoreTokens(); i++) {
            roles[i] = tokens.nextToken();
        }
        request.setRoles(Arrays.asList(roles));
    }

}

// Copyright (c) Naked Objects Group Ltd.
