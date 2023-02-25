package org.nakedobjects.metamodel.specloader;

import org.nakedobjects.metamodel.commons.component.ApplicationScopedComponent;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContext;
import org.nakedobjects.metamodel.runtimecontext.RuntimeContextAware;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutor;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacets;
import org.nakedobjects.metamodel.specloader.traverser.SpecificationTraverser;
import org.nakedobjects.metamodel.specloader.validator.MetaModelValidator;

public interface NakedObjectReflector extends SpecificationLoader,
		ApplicationScopedComponent, RuntimeContextAware {

	/**
	 * The configured {@link ClassSubstitutor}.
	 */
	ClassSubstitutor getClassSubstitutor();

	/**
	 * The configured {@link RuntimeContext}.
	 */
	RuntimeContext getRuntimeContext();

	SpecificationTraverser getSpecificationTraverser();

	MetaModelValidator getMetaModelValidator();

	ProgrammingModelFacets getProgrammingModelFacets();
}
// Copyright (c) Naked Objects Group Ltd.
