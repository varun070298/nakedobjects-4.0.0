package org.nakedobjects.remoting.protocol.encoding;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.FacetHolder;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.remoting.data.Data;
import org.nakedobjects.remoting.data.DummyEncodeableObjectData;
import org.nakedobjects.remoting.data.DummyNullValue;
import org.nakedobjects.remoting.data.DummyObjectData;
import org.nakedobjects.remoting.data.common.NullData;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectEncoderDecoderDefault;
import org.nakedobjects.runtime.testdomain.Movie;
import org.nakedobjects.runtime.testdomain.Person;
import org.nakedobjects.runtime.testspec.MovieSpecification;
import org.nakedobjects.runtime.testspec.PersonSpecification;
import org.nakedobjects.runtime.testsystem.ProxyJunit4TestCase;
import org.nakedobjects.runtime.testsystem.TestProxyOid;
import org.nakedobjects.runtime.testsystem.TestProxyVersion;





public class ObjectEncoderImplTest extends ProxyJunit4TestCase {

    private ObjectEncoderDecoderDefault encoder;

    @Before
    public void setUp() throws Exception {
        encoder = new ObjectEncoderDecoderDefault();

        system.addSpecification(new MovieSpecification());
        system.addSpecification(new PersonSpecification());

        system.getSpecification(String.class).setupIsEncodeable();
        system.getSpecification(String.class).addFacet(new EncodableFacet() {

            public String toEncodedString(final NakedObject object) {
                return object.getObject().toString();
            }

            public NakedObject fromEncodedString(final String encodedData) {
                return getAdapterManager().adapterFor(encodedData);
            }

            public Class<? extends Facet> facetType() {
                return EncodableFacet.class;
            }

            public FacetHolder getFacetHolder() {
                return null;
            }

            public void setFacetHolder(final FacetHolder facetHolder) {}

            public boolean alwaysReplace() {
                return false;
            }

            public boolean isDerived() {
            	return false;
            }

            public boolean isNoop() {
                return false;
            }
            
        	public Facet getUnderlyingFacet() {
        		return null;
        	}
        	public void setUnderlyingFacet(Facet underlyingFacet) {
        		throw new UnsupportedOperationException();
        	}

        });

        /*
         * TestProxySpecification specification = new MovieSpecification(); specification.setupFields(new
         * NakedObjectField[] { new TestProxyField("name", system.getSpecification(String.class)), new
         * TestProxyField("person", system.getSpecification(Person.class)) });
         */
    }

    @After
    public void tearDown() throws Exception {
        system.shutdown();
    }

    @Test
    public void testRestoreCreatesNewAdapter() {
        final DummyObjectData data = new DummyObjectData(new TestProxyOid(5, true), Movie.class.getName(), true,
                new TestProxyVersion(3));
        final DummyEncodeableObjectData name = new DummyEncodeableObjectData("ET", "java.lang.String");
        final DummyNullValue reference = new DummyNullValue(Person.class.getName());
        // note - the order of the fields is by name, not the order that are defined in the specification
        data.setFieldContent(new Data[] { reference, name });

        final NakedObject object = encoder.decode(data);

        assertTrue(object.getObject() instanceof Movie);

        final Movie movie = (Movie) object.getObject();
        assertEquals("ET", movie.getName());
        assertEquals(new TestProxyOid(5, true), object.getOid());
        assertEquals(new TestProxyVersion(3), object.getVersion());
        
    }

    @Test
    public void testRestoreCreatesNewAdapterInUnresolvedState() {
        final DummyObjectData data = new DummyObjectData(new TestProxyOid(5, true), Movie.class.getName(), false,
                new TestProxyVersion(3));

        final NakedObject object = encoder.decode(data);

        assertTrue(object.getObject() instanceof Movie);

        assertEquals(new TestProxyOid(5, true), object.getOid());
        assertEquals(null, object.getVersion());
    }

    @Test
    public void testRestoreUpdatesExistingAdapter() {
        final Movie movie = new Movie();
        final NakedObject adapter = system.createPersistentTestObject(movie);
        adapter.changeState(ResolveState.RESOLVED);

        final DummyObjectData data = new DummyObjectData(adapter.getOid(), Movie.class.getName(), true, new TestProxyVersion(3));
        final DummyEncodeableObjectData name = new DummyEncodeableObjectData("ET", "java.lang.String");
        final DummyNullValue reference = new DummyNullValue(Person.class.getName());
        data.setFieldContent(new Data[] { reference, name });

        getTransactionManager().startTransaction();
        final NakedObject object = encoder.decode(data);
        getTransactionManager().endTransaction();

        assertEquals(new TestProxyVersion(3), object.getVersion());
        assertEquals("ET", movie.getName());
        assertEquals(movie, object.getObject());
    }

    @Test
    public void testRestoreIgnoredIfNoFieldData() {
        final Movie movie = new Movie();
        final NakedObject adapter = system.createPersistentTestObject(movie);
        adapter.changeState(ResolveState.RESOLVED);

        final DummyObjectData data = new DummyObjectData(adapter.getOid(), Movie.class.getName(), false, new TestProxyVersion(3));

        final NakedObject object = encoder.decode(data);

        assertEquals(movie, object.getObject());
        assertEquals(new TestProxyVersion(1), object.getVersion());
    }

    @Test
    public void testRestoreTransientObject() {
        final DummyObjectData movieData = new DummyObjectData(new TestProxyOid(-1), Movie.class.getName(), true, null);
        final NullData directorData = new DummyNullValue(Person.class.getName());
        final DummyEncodeableObjectData nameData = new DummyEncodeableObjectData("Star Wars", String.class.getName());
        movieData.setFieldContent(new Data[] { directorData, nameData });

        final NakedObject object = encoder.decode(movieData);
        final Movie movie = (Movie) object.getObject();

        assertEquals(movie, object.getObject());
        assertEquals(new TestProxyOid(-1), object.getOid());
        assertEquals(ResolveState.TRANSIENT, object.getResolveState());
        assertEquals(null, object.getVersion());

    }
}

// Copyright (c) Naked Objects Group Ltd.
