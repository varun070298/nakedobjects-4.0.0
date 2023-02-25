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
public class ResolveStateIsDestroyedTest  {

    
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {false, GHOST},
                {false, NEW},
                {false, PART_RESOLVED},
                {false, RESOLVED},
                {false, RESOLVING},
                {false, RESOLVING_PART},
                {false, TRANSIENT},
                {true, DESTROYED},
                {false, UPDATING},
                {false, SERIALIZING_TRANSIENT},
                {false, SERIALIZING_GHOST},
                {false, SERIALIZING_PART_RESOLVED},
                {false, SERIALIZING_RESOLVED},
                {false, VALUE},
        });
    }
    

    private final boolean whetherIs;
    private final ResolveState state;
    
    public ResolveStateIsDestroyedTest(final boolean whetherIs, final ResolveState state) {
        this.whetherIs = whetherIs;
        this.state = state;
    }

    @Test
    public void testIsDestroyed() {
        assertThat(state.isDestroyed(), is(whetherIs));
    }

}
// Copyright (c) Naked Objects Group Ltd.
