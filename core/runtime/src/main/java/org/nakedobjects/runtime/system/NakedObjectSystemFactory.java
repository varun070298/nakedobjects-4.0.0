package org.nakedobjects.runtime.system;

import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;

/**
 * Pluggable mechanism for creating {@link NakedObjectsSystem}s.
 */
public interface NakedObjectSystemFactory extends ApplicationScopedComponent {

	NakedObjectsSystem createSystem(
			final DeploymentType deploymentType);

}
