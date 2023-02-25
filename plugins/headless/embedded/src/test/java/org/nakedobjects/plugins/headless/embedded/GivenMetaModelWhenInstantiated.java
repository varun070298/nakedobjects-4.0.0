package org.nakedobjects.plugins.headless.embedded;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.plugins.headless.embedded.EmbeddedContext;
import org.nakedobjects.plugins.headless.embedded.NakedObjectsMetaModel;


@RunWith(JMock.class)
public class GivenMetaModelWhenInstantiated {
	
	private Mockery mockery = new JUnit4Mockery();
	
	private EmbeddedContext mockContext;
	
	private NakedObjectsMetaModel metaModel;
	
	@Before
	public void setUp() {
		mockContext = mockery.mock(EmbeddedContext.class);
		metaModel = new NakedObjectsMetaModel(mockContext);
	}


	@Test
	public void shouldDefaultConfiguration() {
		assertThat(metaModel.getConfiguration(), is(notNullValue()));
	}

	@Test
	public void shouldDefaultClassSubstitutor() {
		assertThat(metaModel.getClassSubstitutor(), is(notNullValue()));
	}

	@Test
	public void shouldDefaultProgrammingModelFacets() {
		assertThat(metaModel.getProgrammingModelFacets(), is(notNullValue()));
	}
	
	@Test
	public void shouldDefaultCollectionTypeRegistry() {
		assertThat(metaModel.getCollectionTypeRegistry(), is(notNullValue()));
	}

	@Test
	public void shouldDefaultFacetDecorators() {
		assertThat(metaModel.getFacetDecorators(), is(notNullValue()));
	}

	@Test
	public void shouldHaveNoFacetDecorators() {
		assertThat(metaModel.getFacetDecorators().size(), is(0));
	}
	
	@Test(expected=IllegalStateException.class)
	public void shouldNotBeAbleToShutdown() {
		metaModel.shutdown();
	}

}
