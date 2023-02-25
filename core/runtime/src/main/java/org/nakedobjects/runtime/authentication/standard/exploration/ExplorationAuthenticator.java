package org.nakedobjects.runtime.authentication.standard.exploration;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.runtime.authentication.AuthenticationRequest;
import org.nakedobjects.runtime.authentication.standard.AuthenticatorAbstract;
import org.nakedobjects.runtime.authentication.standard.SimpleSession;
import org.nakedobjects.runtime.system.DeploymentType;


/**
 * Creates a session suitable for {@link DeploymentType#EXPLORATION exploration} mode.
 * 
 * <p>
 * If the {@link NakedObjectConfiguration} contains the key
 * {@value ExplorationAuthenticatorConstants#NAKEDOBJECTS_USERS} then returns a
 * {@link MultiUserExplorationSession} which encapsulates the details of several users (and their roles).
 * Viewers that are aware of this capability can offer the convenient ability to switch between these users.
 * For viewers that are not aware, the {@link MultiUserExplorationSession} appears as a regular
 * {@link SimpleSession session}, with the Id of the first user listed.
 * 
 * <p>
 * The format of the {@value ExplorationAuthenticatorConstants#NAKEDOBJECTS_USERS} key should be:
 * 
 * <pre>
 * &amp;lt:userName&gt; [:&lt;role&gt;[|&lt;role&gt;]...], &lt;userName&gt;...
 * </pre>
 */
public class ExplorationAuthenticator extends AuthenticatorAbstract {

    private final Set<SimpleSession> registeredSessions = new LinkedHashSet<SimpleSession>();;
    private final String users;

    // //////////////////////////////////////////////////////////////////
    // Constructor
    // //////////////////////////////////////////////////////////////////

    public ExplorationAuthenticator(final NakedObjectConfiguration configuration) {
        super(configuration);
        users = getConfiguration().getString(ExplorationAuthenticatorConstants.NAKEDOBJECTS_USERS);
        if (users != null) {
            registeredSessions.addAll(parseUsers(users));
        }
    }

    private List<SimpleSession> parseUsers(String users) {
        final List<SimpleSession> registeredUsers = new ArrayList<SimpleSession>();

        final StringTokenizer st = new StringTokenizer(users, ",");
        while (st.hasMoreTokens()) {
            final String token = st.nextToken();
            int end = token.indexOf(':');
            List<String> roles = new ArrayList<String>();
            final String userName;
            if (end == -1) {
                userName = token.trim();
            } else {
                userName = token.substring(0, end).trim();
                String roleList = token.substring(end + 1);
                final StringTokenizer st2 = new StringTokenizer(roleList, "|");
                while (st2.hasMoreTokens()) {
                    final String role = st2.nextToken().trim();
                    roles.add(role);
                }
            }
            registeredUsers.add(createSimpleSession(userName, roles));
        }
        return registeredUsers;
    }

    private SimpleSession createSimpleSession(final String userName, List<String> roles) {
        return new SimpleSession(userName, roles.toArray(new String[roles.size()]));
    }

    // //////////////////////////////////////////////////////////////////
    // API
    // //////////////////////////////////////////////////////////////////

    /**
     * Can authenticate if a {@link AuthenticationRequestExploration}.
     */
    public final boolean canAuthenticate(final AuthenticationRequest request) {
        return request instanceof AuthenticationRequestExploration;
    }

    /**
     * Valid providing running in {@link DeploymentType#isExploring() exploration} mode.
     */
    public final boolean isValid(final AuthenticationRequest request) {
        return getDeploymentType().isExploring();
    }

    @Override
    public AuthenticationSession authenticate(final AuthenticationRequest request, final String code) {
        AuthenticationRequestExploration authenticationRequestExploration = (AuthenticationRequestExploration) request;
        if (!authenticationRequestExploration.isDefaultUser()) {
            registeredSessions.add(createSimpleSession(authenticationRequestExploration.getName(),
                    authenticationRequestExploration.getRoles()));
        }
        if (registeredSessions.size() > 1) {
            return new MultiUserExplorationSession(registeredSessions, code);
        } else if (registeredSessions.size() == 1) {
            return registeredSessions.iterator().next();
        } else {
            return new ExplorationSession(code);
        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
