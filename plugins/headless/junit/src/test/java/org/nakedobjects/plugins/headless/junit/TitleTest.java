package org.nakedobjects.plugins.headless.junit;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class TitleTest extends AbstractTest {

    @Test
    public void shouldAppendToDocumentor() {
        assertThat(custJsVO.title(), equalTo("Richard Pawson"));
    }
}
