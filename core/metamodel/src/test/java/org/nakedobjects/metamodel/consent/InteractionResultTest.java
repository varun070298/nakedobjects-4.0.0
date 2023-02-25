package org.nakedobjects.metamodel.consent;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class InteractionResultTest {

    private InteractionResult result;

    @Before
    public void setUp() throws Exception {
        result = new InteractionResult(null);
    }

    @After
    public void tearDown() throws Exception {
        result = null;
    }

    @Test
    public void shouldHaveNullReasonWhenJustInstantiated() {
        assertEquals(null, result.getReason());
    }

    @Test
    public void shouldBeEmptyWhenJustInstantiated() {
        assertFalse(result.isVetoing());
        assertTrue(result.isNotVetoing());
    }

    @Test
    public void shouldHaveNonNullReasonWhenAdvisedWithNonNull() {
        result.advise("foo", InteractionAdvisorFacet.NOOP);
        assertEquals("foo", result.getReason());
    }

    @Test
    public void shouldConcatenateAdviseWhenAdvisedWithNonNull() {
        result.advise("foo", InteractionAdvisorFacet.NOOP);
        result.advise("bar", InteractionAdvisorFacet.NOOP);
        assertEquals("foo; bar", result.getReason());
    }

    @Test
    public void shouldNotBeEmptyWhenAdvisedWithNonNull() {
        result.advise("foo", InteractionAdvisorFacet.NOOP);
        assertTrue(result.isVetoing());
        assertFalse(result.isNotVetoing());
    }

    @Test
    public void shouldBeEmptyWhenAdvisedWithNull() {
        result.advise(null, InteractionAdvisorFacet.NOOP);
        assertTrue(result.isNotVetoing());
        assertFalse(result.isVetoing());
        assertEquals(null, result.getReason());
    }


}

// Copyright (c) Naked Objects Group Ltd.
