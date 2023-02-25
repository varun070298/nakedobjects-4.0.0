package org.nakedobjects.metamodel.spec.feature;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.integration.junit4.JUnit4Mockery;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.facets.hide.HiddenFacet;

@RunWith(JMock.class)
public class NakedObjectAssociationFiltersTests {

    private Mockery mockery = new JUnit4Mockery();

    private AuthenticationSession mockSession;
    private NakedObject mockTarget;
    private NakedObjectAssociation mockAssociation;


    @Before
    public void setUp() throws Exception {
        mockSession = mockery.mock(AuthenticationSession.class);
        mockTarget = mockery.mock(NakedObject.class, "target");
        mockAssociation = mockery.mock(NakedObjectAssociation.class);
    }

    @After
    public void tearDown() throws Exception {
    }


    @Test
    public void shouldNotJustCheckIfAssociationContainsHiddenFacet() {
        mockery.checking(new Expectations(){{
            never(mockAssociation).containsFacet(HiddenFacet.class);
            allowing(mockAssociation).isVisible(with(any(AuthenticationSession.class)), with(any(NakedObject.class)));
        }});
        Filter<NakedObjectAssociation> filter = NakedObjectAssociationFilters.dynamicallyVisible(mockSession, mockTarget);
        filter.accept(mockAssociation);
    }

}

// Copyright (c) Naked Objects Group Ltd.
