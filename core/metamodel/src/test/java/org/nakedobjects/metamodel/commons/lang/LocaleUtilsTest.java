package org.nakedobjects.metamodel.commons.lang;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;

public class LocaleUtilsTest {


    @Test
    public void canFindEnGB() throws Exception {
        assertThat(LocaleUtils.findLocale("en_GB"), is(not(nullValue())));
    
    }
}
