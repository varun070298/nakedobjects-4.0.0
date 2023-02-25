package org.nakedobjects.metamodel.specloader;

import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facetdecorator.FacetDecorator;
import org.nakedobjects.metamodel.facets.FacetFactory;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutor;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutorIdentity;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacets;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacetsJava5;
import org.nakedobjects.metamodel.specloader.traverser.SpecificationTraverser;
import org.nakedobjects.metamodel.specloader.traverser.SpecificationTraverserDefault;
import org.nakedobjects.metamodel.specloader.validator.MetaModelValidator;
import org.nakedobjects.metamodel.specloader.validator.MetaModelValidatorNoop;

public final class ReflectorConstants {
    
    /**
     * Key used to lookup implementation of {@link ClassSubstitutor} in {@link NakedObjectConfiguration}.
     */
    public static final String CLASS_SUBSTITUTOR_CLASS_NAME = ConfigurationConstants.ROOT + "reflector.class-substitutor";
    public static final String CLASS_SUBSTITUTOR_CLASS_NAME_DEFAULT = ClassSubstitutorIdentity.class.getName();

    /**
     * Key used to lookup implementation of {@link SpecificationTraverser} in {@link NakedObjectConfiguration}.
     */
    public static final String SPECIFICATION_TRAVERSER_CLASS_NAME = ConfigurationConstants.ROOT + "reflector.traverser";
    public static final String SPECIFICATION_TRAVERSER_CLASS_NAME_DEFAULT = SpecificationTraverserDefault.class.getName();

	

    /**
     * Key used to lookup implementation of {@link ProgrammingModelFacets} in {@link NakedObjectConfiguration}.
     * 
     * @see #FACET_FACTORY_INCLUDE_CLASS_NAME_LIST
     * @see #FACET_FACTORY_EXCLUDE_CLASS_NAME_LIST
     */
    public static final String PROGRAMMING_MODEL_FACETS_CLASS_NAME = ConfigurationConstants.ROOT + "reflector.facets";
    public static final String PROGRAMMING_MODEL_FACETS_CLASS_NAME_DEFAULT = ProgrammingModelFacetsJava5.class.getName();

    /**
     * Key used to lookup comma-separated list of {@link FacetFactory}s to include
     * (over and above those specified by {@link #PROGRAMMING_MODEL_FACETS_CLASS_NAME}.
     * 
     * @see #PROGRAMMING_MODEL_FACETS_CLASS_NAME
     * @see #FACET_FACTORY_EXCLUDE_CLASS_NAME_LIST
     */
	public static final String FACET_FACTORY_INCLUDE_CLASS_NAME_LIST = ConfigurationConstants.ROOT + "reflector.facets.include";
	
    /**
     * Key used to lookup comma-separated list of {@link FacetFactory}s to exclude
     * (that might otherwise be included specified by the {@link #PROGRAMMING_MODEL_FACETS_CLASS_NAME}.
     * 
     * @see #PROGRAMMING_MODEL_FACETS_CLASS_NAME
     * @see #FACET_FACTORY_INCLUDE_CLASS_NAME_LIST
     */
	public static final String FACET_FACTORY_EXCLUDE_CLASS_NAME_LIST = ConfigurationConstants.ROOT + "reflector.facets.exclude";
    

    /**
     * Key used to lookup comma-separated list of {@link FacetDecorator}s.
     */
	public static final String FACET_DECORATOR_CLASS_NAMES = ConfigurationConstants.ROOT + "reflector.facet-decorators";
	
    /**
     * Key used to lookup implementation of {@link MetaModelValidator} in {@link NakedObjectConfiguration}.
     */
    public static final String META_MODEL_VALIDATOR_CLASS_NAME = ConfigurationConstants.ROOT + "reflector.validator";
    public static final String META_MODEL_VALIDATOR_CLASS_NAME_DEFAULT = MetaModelValidatorNoop.class.getName();

	
    
    private ReflectorConstants() {
    }

}
