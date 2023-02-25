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
public class ResolveStateSerializeFromTest  {

    
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {GHOST, SERIALIZING_GHOST},
                {NEW, null},
                {PART_RESOLVED, SERIALIZING_PART_RESOLVED},
                {RESOLVED, SERIALIZING_RESOLVED},
                {RESOLVING, null},
                {RESOLVING_PART, null},
                {TRANSIENT, SERIALIZING_TRANSIENT},
                {DESTROYED, null},
                {UPDATING, null},
                {SERIALIZING_TRANSIENT, null},
                {SERIALIZING_GHOST, null},
                {SERIALIZING_PART_RESOLVED, null},
                {SERIALIZING_RESOLVED, null},
                {VALUE, null},
        });
    }
    

    private final ResolveState state;
    private final ResolveState serializeFrom;
    
    public ResolveStateSerializeFromTest(final ResolveState state, final ResolveState serializeFrom) {
        this.serializeFrom = serializeFrom;
        this.state = state;
    }

    @Test
    public void testSerializeFrom() {
        assertThat(state.serializeFrom(), is(serializeFrom));
    }

}
// Copyright (c) Naked Objects Group Ltd.
