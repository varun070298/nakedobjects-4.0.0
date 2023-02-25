package org.nakedobjects.metamodel.examples.facets.namefile;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.metamodel.examples.facets.namefile.NameFileParser;





public class NameFileParserParsingTest {

    private NameFileParser parser;

    @Before
    public void setUp() throws Exception {
        parser = new NameFileParser();
    }

    @After
    public void tearDown() throws Exception {
        parser = null;
    }

    @Test
    public void canFindResourceWhenExists() throws Exception {
        parser.parse();
    }

    @Test
    public void getName() throws Exception {
        parser.parse();
        assertThat(parser.getName(DomainObjectWithNameFileEntry.class), is("Customer"));
    }

    @Test
    public void getPropertyName() throws Exception {
        parser.parse();
        assertThat(parser.getMemberName(DomainObjectWithNameFileEntry.class, "lastName"), is("surname"));
    }

}
