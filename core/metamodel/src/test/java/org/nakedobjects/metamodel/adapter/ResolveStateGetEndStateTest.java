package org.nakedobjects.metamodel.adapter;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.nakedobjects.metamodel.adapter.ResolveState.DESTROYED;
import static org.nakedobjects.metamodel.adapter.ResolveState.GHOST;
import static org.nakedobjects.metamodel.adapter.ResolveState.NEW;
import static org.nakedobjects.metamodel.adapter.ResolveState.PART_RESOLVED;
import static org.nakedobjects.metamodel.adapter.ResolveState.RESOLVED;
import static org.nakedobjects.metamodel.adapter.ResolveState.RESOLVING;
import static org.nakedobjects.metamodel.adapter.ResolveState.RESOLVING_PART;
import static org.nakedobjects.metamodel.adapter.ResolveState.SERIALIZING_GHOST;
import static org.nakedobjects.metamodel.adapter.ResolveState.SERIALIZING_PART_RESOLVED;
import static org.nakedobjects.metamodel.adapter.ResolveState.SERIALIZING_RESOLVED;
import static org.nakedobjects.metamodel.adapter.ResolveState.SERIALIZING_TRANSIENT;
import static org.nakedobjects.metamodel.adapter.ResolveState.TRANSIENT;
import static org.nakedobjects.metamodel.adapter.ResolveState.UPDATING;
import static org.nakedobjects.metamodel.adapter.ResolveState.VALUE;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(Parameterized.class)
public class ResolveStateGetEndStateTest  {

    
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {NEW, null},
                {GHOST, null},
                {TRANSIENT, null},
                {RESOLVING_PART, PART_RESOLVED},
                {PART_RESOLVED, null},
                {RESOLVING, RESOLVED},
                {RESOLVED, null},
                {UPDATING, RESOLVED},
                {SERIALIZING_TRANSIENT, TRANSIENT},
                {SERIALIZING_PART_RESOLVED, PART_RESOLVED},
                {SERIALIZING_RESOLVED, RESOLVED},
                {SERIALIZING_GHOST, GHOST},
                {VALUE, null},
                {DESTROYED, null},
        });
    }
    
    private final ResolveState from;
    private final ResolveState to;
    
    public ResolveStateGetEndStateTest(final ResolveState from, final ResolveState to) {
        this.from = from;
        this.to = to;
    }

    @Test
    public void testGetEndState() {
        assertThat(from.getEndState(), is(to));
    }

}
// Copyright (c) Naked Objects Group Ltd.
