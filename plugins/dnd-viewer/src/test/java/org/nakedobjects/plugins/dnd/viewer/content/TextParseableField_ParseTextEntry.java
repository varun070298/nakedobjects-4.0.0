package org.nakedobjects.plugins.dnd.viewer.content;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.InvalidEntryException;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.consent.Allow;
import org.nakedobjects.metamodel.consent.Veto;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.object.parseable.ParseableFacet;
import org.nakedobjects.metamodel.interactions.ValidatingInteractionAdvisor;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.plugins.dnd.viewer.content.TextParseableFieldImpl;
import org.nakedobjects.runtime.testsystem.TestProxySystem;


@RunWith(JMock.class)
public class TextParseableField_ParseTextEntry {

    private final Mockery context = new JUnit4Mockery();

    private NakedObject mockParent;
    private NakedObject mockChild;
    private OneToOneAssociation mockOtoa;
    private NakedObjectSpecification mockOtoaSpec;
    private ParseableFacet mockParseableFacet;
    private NakedObject mockParsedText;
    private TextParseableFieldImpl fieldImpl;

    private ValidatingInteractionAdvisor mockValidatingInteractionAdvisorFacet;

    private TestProxySystem system;

    @Before
    public void setUp() throws Exception {
        system = new TestProxySystem();
        system.init();

        mockParent = context.mock(NakedObject.class, "parent");
        mockChild = context.mock(NakedObject.class, "child");
        mockOtoa = context.mock(OneToOneAssociation.class);
        mockOtoaSpec = context.mock(NakedObjectSpecification.class);
        mockParseableFacet = context.mock(ParseableFacet.class);
        mockParsedText = context.mock(NakedObject.class, "parsedText");
        mockValidatingInteractionAdvisorFacet = context.mock(ValidatingInteractionAdvisor.class);

        context.checking(new Expectations() {
            {
                allowing(mockOtoa).getIdentifier();

                allowing(mockOtoa).getSpecification();
                will(returnValue(mockOtoaSpec));

                one(mockOtoaSpec).getFacet(ParseableFacet.class);
                will(returnValue(mockParseableFacet));
            }
        });

        fieldImpl = new TextParseableFieldImpl(mockParent, mockChild, mockOtoa);
    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void parsedTextIsValidForSpecAndCorrespondingObjectValidAsAssociation() {

        context.checking(new Expectations() {
            {
                one(mockParseableFacet).parseTextEntry(mockChild, "foo");
                will(returnValue(mockParsedText));

                atLeast(1).of(mockOtoa).isAssociationValid(mockParent, mockParsedText);
                will(returnValue(Allow.DEFAULT));

                one(mockOtoa).isMandatory();
            }
        });

        fieldImpl.parseTextEntry("foo");
    }

    @Test(expected = InvalidEntryException.class)
    public void parsedTextIsNullWhenMandatoryThrowsException() {

        context.checking(new Expectations() {
            {
                one(mockParseableFacet).parseTextEntry(mockChild, "foo");
                will(returnValue(null));

                atLeast(1).of(mockOtoa).isAssociationValid(mockParent, null);
                will(returnValue(Allow.DEFAULT));

                one(mockOtoa).isMandatory();
                will(returnValue(true));
            }
        });

        fieldImpl.parseTextEntry("foo");
    }

    @Test
    public void parsedTextIsValidAccordingToSpecificationFacet() {

        context.checking(new Expectations() {
            {
                one(mockParseableFacet).parseTextEntry(mockChild, "foo");
                will(returnValue(mockParsedText));

                atLeast(1).of(mockOtoa).isAssociationValid(mockParent, mockParsedText);
                will(returnValue(Allow.DEFAULT));

                allowing(mockOtoa).isMandatory();
                will(returnValue(true));
            }
        });

        fieldImpl.parseTextEntry("foo");
    }

    @Test(expected = InvalidEntryException.class)
    public void parsedTextIsInvalidAccordingToSpecification() {

        context.checking(new Expectations() {
            {
                allowing(mockParseableFacet).parseTextEntry(mockChild, "foo");
                will(returnValue(mockParsedText));

                atLeast(1).of(mockOtoa).isAssociationValid(mockParent, mockParsedText);
                will(returnValue(Veto.DEFAULT));

                allowing(mockOtoa).isMandatory();
                will(returnValue(true));
            }
        });

        fieldImpl.parseTextEntry("foo");
    }

    @Test(expected = IllegalArgumentException.class)
    public void parsedTextIsInvalidAccordingToAssociation() {

        context.checking(new Expectations() {
            {
                allowing(mockParseableFacet).parseTextEntry(mockChild, "foo");
                will(returnValue(mockParsedText));

                allowing(mockOtoa).getFacets(with(any(Filter.class)));
                will(returnValue(new Facet[] {}));

                one(mockOtoa).isAssociationValid(mockParent, mockParsedText);
                will(returnValue(Veto.DEFAULT));

                allowing(mockOtoa).isMandatory();
                allowing(returnValue(true));
            }
        });

        fieldImpl.parseTextEntry("foo");
    }

}
