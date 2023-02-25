package org.nakedobjects.runtime.system;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.config.internal.PropertiesConfiguration;
import org.nakedobjects.metamodel.facetdecorator.FacetDecorator;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.naming.describedas.DescribedAsFacet;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacet;
import org.nakedobjects.metamodel.facets.object.ident.plural.PluralFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.JavaReflector;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutorIdentity;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistryDefault;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacetsJava5;
import org.nakedobjects.metamodel.specloader.traverser.SpecificationTraverserDefault;
import org.nakedobjects.metamodel.specloader.validator.MetaModelValidatorNoop;
import org.nakedobjects.runtime.authentication.AuthenticationManager;
import org.nakedobjects.runtime.authorization.AuthorizationManager;
import org.nakedobjects.runtime.context.NakedObjectsContextStatic;
import org.nakedobjects.runtime.imageloader.TemplateImageLoader;
import org.nakedobjects.runtime.persistence.PersistenceSessionFactory;
import org.nakedobjects.runtime.persistence.internal.RuntimeContextFromSession;
import org.nakedobjects.runtime.session.NakedObjectSessionFactory;
import org.nakedobjects.runtime.session.NakedObjectSessionFactoryDefault;
import org.nakedobjects.runtime.userprofile.UserProfileLoader;


@RunWith(JMock.class)
public abstract class JavaReflectorTestAbstract {

    private Mockery mockery = new JUnit4Mockery();

    protected NakedObjectSpecification specification;
    protected TemplateImageLoader mockTemplateImageLoader;
    protected PersistenceSessionFactory mockPersistenceSessionFactory;
    private UserProfileLoader mockUserProfileLoader;
    protected AuthenticationManager mockAuthenticationManager;
    protected AuthorizationManager mockAuthorizationManager;

	private List<Object> servicesList;



    @Before
    public void setUp() throws Exception {
        Logger.getRootLogger().setLevel(Level.OFF);

        PropertiesConfiguration configuration = new PropertiesConfiguration();

        mockTemplateImageLoader = mockery.mock(TemplateImageLoader.class);
        mockPersistenceSessionFactory = mockery.mock(PersistenceSessionFactory.class);
        mockUserProfileLoader = mockery.mock(UserProfileLoader.class);
        mockAuthenticationManager = mockery.mock(AuthenticationManager.class);
        mockAuthorizationManager = mockery.mock(AuthorizationManager.class);
        servicesList = Collections.emptyList();
        
        mockery.checking(new Expectations() {{
            ignoring(mockTemplateImageLoader);
            ignoring(mockPersistenceSessionFactory);
            ignoring(mockUserProfileLoader);
            ignoring(mockAuthenticationManager);
            ignoring(mockAuthorizationManager);
        }});
        

        final JavaReflector reflector = 
        	new JavaReflector(configuration, new ClassSubstitutorIdentity(), new CollectionTypeRegistryDefault(), new SpecificationTraverserDefault(), new ProgrammingModelFacetsJava5(), new HashSet<FacetDecorator>(), new MetaModelValidatorNoop());
        reflector.setRuntimeContext(new RuntimeContextFromSession());
        reflector.init();


        // not sure if this is needed since we have now moved Reflector out to global scope,
        // not specific to an ExecutionContext.
        NakedObjectSessionFactory executionContextFactory = 
            new NakedObjectSessionFactoryDefault(
                    DeploymentType.EXPLORATION, 
                    configuration, 
                    mockTemplateImageLoader, 
                    reflector, 
                    mockAuthenticationManager, 
                    mockAuthorizationManager, 
                    mockUserProfileLoader, 
                    mockPersistenceSessionFactory, servicesList);
        NakedObjectsContextStatic.createRelaxedInstance(executionContextFactory);
        NakedObjectsContextStatic.getInstance().getSessionInstance(); // cause an Execution Context to load


        specification = loadSpecification(reflector);
    }

    protected abstract NakedObjectSpecification loadSpecification(JavaReflector reflector);

    @Test
    public void testCollectionFacet() throws Exception {
        final Facet facet = specification.getFacet(CollectionFacet.class);
        Assert.assertNull(facet);
    }

    @Test
    public void testTypeOfFacet() throws Exception {
        final TypeOfFacet facet = specification.getFacet(TypeOfFacet.class);
        Assert.assertNull(facet);
    }

    @Test
    public void testNamedFaced() throws Exception {
        final Facet facet = specification.getFacet(NamedFacet.class);
        Assert.assertNotNull(facet);
    }

    @Test
    public void testPluralFaced() throws Exception {
        final Facet facet = specification.getFacet(PluralFacet.class);
        Assert.assertNotNull(facet);
    }

    @Test
    public void testDescriptionFacet() throws Exception {
        final Facet facet = specification.getFacet(DescribedAsFacet.class);
        Assert.assertNotNull(facet);
    }

}

// Copyright (c) Naked Objects Group Ltd.
