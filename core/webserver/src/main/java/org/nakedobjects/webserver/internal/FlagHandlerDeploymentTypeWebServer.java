package org.nakedobjects.webserver.internal;

import org.nakedobjects.runtime.options.standard.OptionHandlerDeploymentType;
import org.nakedobjects.runtime.system.DeploymentType;

import static org.nakedobjects.runtime.options.Constants.TYPE_SERVER;
import static org.nakedobjects.runtime.options.Constants.TYPE_SERVER_EXPLORATION;
import static org.nakedobjects.runtime.options.Constants.TYPE_SERVER_PROTOTYPE;


public class FlagHandlerDeploymentTypeWebServer extends OptionHandlerDeploymentType {
    
    public FlagHandlerDeploymentTypeWebServer(boolean primeIfDefaulted) {
        super(primeIfDefaulted);
    }
    
    public FlagHandlerDeploymentTypeWebServer() {
        super();
    }

    protected String types() {
        String types = TYPE_SERVER_EXPLORATION + "; " + TYPE_SERVER_PROTOTYPE + " (default); " + TYPE_SERVER;
        return types;
    }

    protected DeploymentType defaultType() {
        return DeploymentType.SERVER_PROTOTYPE;
    }
}
