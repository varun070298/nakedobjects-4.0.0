package org.nakedobjects.metamodel.specloader;

import java.util.Set;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facetdecorator.FacetDecorator;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutor;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistry;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacets;
import org.nakedobjects.metamodel.specloader.traverser.SpecificationTraverser;
import org.nakedobjects.metamodel.specloader.validator.MetaModelValidator;


public class JavaReflector extends NakedObjectReflectorAbstract {

    @SuppressWarnings("unused")
    private final static Logger LOG = Logger.getLogger(JavaReflector.class);

    // /////////////////////////////////////////////////////////////
    // constructor
    // /////////////////////////////////////////////////////////////

    public JavaReflector(
            final NakedObjectConfiguration configuration,
            final ClassSubstitutor classSubstitutor,
            final CollectionTypeRegistry collectionTypeRegistry,
            final SpecificationTraverser specificationTraverser,
            final ProgrammingModelFacets programmingModelFacets, 
            final Set<FacetDecorator> facetDecorators, 
            final MetaModelValidator metaModelValidator) {
        super(configuration, classSubstitutor, collectionTypeRegistry, specificationTraverser, programmingModelFacets, facetDecorators, metaModelValidator);
    }


}
// Copyright (c) Naked Objects Group Ltd.
