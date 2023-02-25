package org.nakedobjects.plugins.headless.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Ignore;
import org.junit.Test;


public class DefaultAndChoicesTest extends AbstractTest {

    @Test
    public void defaults() {
        final Object[] defaultPlaceOrder = custJsVO.defaultPlaceOrder();
        assertThat(defaultPlaceOrder.length, is(2));
    }

    @Ignore("not yet tested")
    @Test
    public void choicesDefaults() {
    // not tested.
    }

}
