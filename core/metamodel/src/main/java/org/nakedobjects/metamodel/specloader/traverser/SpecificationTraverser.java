package org.nakedobjects.metamodel.specloader.traverser;

import java.lang.reflect.Method;
import java.util.List;

import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;

public interface SpecificationTraverser extends ApplicationScopedComponent {

	void traverseTypes(Method method, List<Class<?>> discoveredTypes);

	void traverseReferencedClasses(
			NakedObjectSpecification noSpec,
			List<Class<?>> discoveredTypes) throws ClassNotFoundException;

}
