
package org.nakedobjects.runtime.options.standard;

import org.nakedobjects.runtime.system.DeploymentType;

import static org.nakedobjects.runtime.options.Constants.TYPE_CLIENT;
import static org.nakedobjects.runtime.options.Constants.TYPE_EXPLORATION;
import static org.nakedobjects.runtime.options.Constants.TYPE_PROTOTYPE;
import static org.nakedobjects.runtime.options.Constants.TYPE_SERVER;
import static org.nakedobjects.runtime.options.Constants.TYPE_SERVER_EXPLORATION;
import static org.nakedobjects.runtime.options.Constants.TYPE_SERVER_PROTOTYPE;
import static org.nakedobjects.runtime.options.Constants.TYPE_SINGLE_USER;


public class OptionHandlerDeploymentTypeNakedObjects extends OptionHandlerDeploymentType {
  
    protected String types() {
        String types = TYPE_EXPLORATION + "; " + TYPE_PROTOTYPE + " (default); " + TYPE_SINGLE_USER + "; " + TYPE_CLIENT + "; "
        + TYPE_SERVER_EXPLORATION + "; " + TYPE_SERVER_PROTOTYPE + "; " + TYPE_SERVER;
        return types;
    }
    
    protected DeploymentType defaultType() {
        return DeploymentType.PROTOTYPE;
    }

}
