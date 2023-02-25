package org.nakedobjects.metamodel.examples.facets.namefile;

import java.lang.reflect.Method;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.applib.Identifier;
import org.nakedobjects.metamodel.examples.facets.namefile.NameFileFacet;
import org.nakedobjects.metamodel.examples.facets.namefile.NameFileFacetFactory;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.MethodRemover;
import org.nakedobjects.metamodel.spec.identifier.Identified;

import static org.nakedobjects.metamodel.commons.matchers.NofMatchers.anInstanceOf;


@RunWith(JMock.class)
public class NameFileFacetFactoryProcessTest {

    private Mockery mockery = new JUnit4Mockery() {
        {
            setImposteriser(ClassImposteriser.INSTANCE);
        }
    };
    
    private NameFileFacetFactory facetFactory;
    private MethodRemover mockMethodRemover;
    private Identified mockFacetHolder;

    private Class<DomainObjectWithNameFileEntry> domainObjectWithNameFileEntryClass;
    private Method domainObjectWithNameFileEntryMethod;

    private Class<DomainObjectWithoutNameFileEntry> domainObjectWithoutNameFileEntryClass;
    private Method domainObjectWithoutNameFileEntryMethod;

    @Before
    public void setUp() throws Exception {
        facetFactory = new NameFileFacetFactory();
        mockMethodRemover = mockery.mock(MethodRemover.class);
        mockFacetHolder = mockery.mock(Identified.class);
        
        domainObjectWithNameFileEntryClass = DomainObjectWithNameFileEntry.class;
        domainObjectWithNameFileEntryMethod = domainObjectWithNameFileEntryClass.getMethod("getLastName");

        domainObjectWithoutNameFileEntryClass = DomainObjectWithoutNameFileEntry.class;
        domainObjectWithoutNameFileEntryMethod = domainObjectWithoutNameFileEntryClass.getMethod("getLastName");
    }

    @After
    public void tearDown() throws Exception {
        facetFactory = null;
        mockMethodRemover = null;
        mockFacetHolder = null;
    }

    @Test
    public void addsANameFileFacetForObjectIfEntryExists() {
        mockery.checking(new Expectations() {{
            one(mockFacetHolder).addFacet(with(anInstanceOf(NameFileFacet.class)));
        }});
        
        facetFactory.process(domainObjectWithNameFileEntryClass, mockMethodRemover, mockFacetHolder);
    }

    @Test
    public void doesNotAddsANameFileFacetForObjectIfEntryDoesNotExists() {
        mockery.checking(new Expectations() {{
            never(mockFacetHolder).addFacet(with(anInstanceOf(NameFileFacet.class)));
        }});
        
        facetFactory.process(domainObjectWithoutNameFileEntryClass, mockMethodRemover, mockFacetHolder);
    }
    
    @Test
    public void addsANameFileFacetForPropertyIfEntryExists() {
        mockery.checking(new Expectations() {{
            one(mockFacetHolder).getIdentifier();
            will(returnValue(Identifier.propertyOrCollectionIdentifier(domainObjectWithNameFileEntryClass, "lastName")));
            
            one(mockFacetHolder).addFacet(with(anInstanceOf(NameFileFacet.class)));
        }});
        
        facetFactory.process(domainObjectWithNameFileEntryClass, domainObjectWithNameFileEntryMethod, mockMethodRemover, mockFacetHolder);
    }
    
    @Test
    public void doesNotAddsANameFileFacetForPropertyIfEntryDoesNotExists() {
        mockery.checking(new Expectations() {{
            one(mockFacetHolder).getIdentifier();
            will(returnValue(Identifier.propertyOrCollectionIdentifier(domainObjectWithoutNameFileEntryClass, "lastName")));
            
            never(mockFacetHolder).addFacet(with(anInstanceOf(Facet.class)));
        }});
        
        facetFactory.process(domainObjectWithoutNameFileEntryClass, domainObjectWithoutNameFileEntryMethod, mockMethodRemover, mockFacetHolder);
    }
    
}
