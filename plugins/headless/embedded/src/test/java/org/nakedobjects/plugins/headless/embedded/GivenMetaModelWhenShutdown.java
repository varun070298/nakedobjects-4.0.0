package org.nakedobjects.plugins.headless.embedded;

import java.util.TreeSet;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facetdecorator.FacetDecorator;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutor;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistry;
import org.nakedobjects.metamodel.specloader.progmodelfacets.ProgrammingModelFacets;
import org.nakedobjects.plugins.headless.embedded.EmbeddedContext;
import org.nakedobjects.plugins.headless.embedded.NakedObjectsMetaModel;
import org.nakedobjects.plugins.headless.embedded.dom.claim.ClaimRepositoryImpl;
import org.nakedobjects.plugins.headless.embedded.dom.employee.EmployeeRepositoryImpl;

@RunWith(JMock.class)
public class GivenMetaModelWhenShutdown {

	private Mockery mockery = new JUnit4Mockery();
	
	private NakedObjectConfiguration mockConfiguration;
	private ProgrammingModelFacets mockProgrammingModelFacets;
	private FacetDecorator mockFacetDecorator;
	private ClassSubstitutor mockClassSubstitutor;
	private CollectionTypeRegistry mockCollectionTypeRegistry;
	private EmbeddedContext mockContext;
	
	private NakedObjectsMetaModel metaModel;
	
	
	@Before
	public void setUp() {
		mockContext = mockery.mock(EmbeddedContext.class);
		mockConfiguration = mockery.mock(NakedObjectConfiguration.class);
		mockProgrammingModelFacets = mockery.mock(ProgrammingModelFacets.class);
		mockCollectionTypeRegistry = mockery.mock(CollectionTypeRegistry.class);
		mockFacetDecorator = mockery.mock(FacetDecorator.class);
		mockClassSubstitutor = mockery.mock(ClassSubstitutor.class);
	
		metaModel = new NakedObjectsMetaModel(mockContext, EmployeeRepositoryImpl.class, ClaimRepositoryImpl.class);
	}
	

	@Test
	public void shouldSucceedWithoutThrowingAnyExceptions() {
		metaModel.init();
	}

	@Test(expected=IllegalStateException.class)
	public void shouldNotBeAbleToChangeConfiguration() {
		metaModel.init();
		metaModel.setConfiguration(mockConfiguration);
	}
	
	@Test(expected=IllegalStateException.class)
	public void shouldNotBeAbleToChangeProgrammingModelFacets() {
		metaModel.init();
		metaModel.setProgrammingModelFacets(mockProgrammingModelFacets);
	}
	
	@Test(expected=IllegalStateException.class)
	public void shouldNotBeAbleToChangeCollectionTypeRegistry() {
		metaModel.init();
		metaModel.setCollectionTypeRegistry(mockCollectionTypeRegistry);
	}

	@Test(expected=IllegalStateException.class)
	public void shouldNotBeAbleToChangeClassSubstitutor() {
		metaModel.init();
		metaModel.setClassSubstitutor(mockClassSubstitutor);
	}

	@Test(expected=IllegalStateException.class)
	public void shouldNotBeAbleToChangeFacetDecorators() {
		metaModel.init();
		metaModel.setFacetDecorators(new TreeSet<FacetDecorator>());
	}

	@Test(expected=UnsupportedOperationException.class)
	public void shouldNotBeAbleToAddToFacetDecorators() {
		metaModel.init();
		metaModel.getFacetDecorators().add(mockFacetDecorator);
	}

	@Test(expected=IllegalStateException.class)
	public void shouldNotBeAbleToInitializeAgain() {
		metaModel.init();
		//
		metaModel.init();
	}


}
