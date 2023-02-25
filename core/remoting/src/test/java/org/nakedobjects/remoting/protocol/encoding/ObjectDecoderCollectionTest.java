package org.nakedobjects.remoting.protocol.encoding;

import java.util.Collection;
import java.util.Enumeration;
import java.util.Vector;

import org.apache.commons.collections.CollectionUtils;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacetAbstract;
import org.nakedobjects.metamodel.testspec.TestProxySpecification;
import org.nakedobjects.remoting.data.DummyCollectionData;
import org.nakedobjects.remoting.data.DummyObjectData;
import org.nakedobjects.remoting.data.common.CollectionData;
import org.nakedobjects.remoting.data.common.ObjectData;
import org.nakedobjects.remoting.data.common.ReferenceData;
import org.nakedobjects.remoting.protocol.encoding.internal.FieldOrderCache;
import org.nakedobjects.remoting.protocol.encoding.internal.ObjectDeserializer;
import org.nakedobjects.runtime.persistence.adaptermanager.ObjectToNakedObjectTransformer;
import org.nakedobjects.runtime.testsystem.ProxyJunit3TestCase;
import org.nakedobjects.runtime.testsystem.TestPojo;
import org.nakedobjects.runtime.testsystem.TestProxyOid;
import org.nakedobjects.runtime.testsystem.TestProxyVersion;


public class ObjectDecoderCollectionTest extends ProxyJunit3TestCase {

    private ObjectDeserializer deserializer;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        FieldOrderCache fieldOrderCache = null;  // TODO: should provide a mock here?
		deserializer = new ObjectDeserializer(fieldOrderCache);

        final TestProxySpecification specification = system.getSpecification(Vector.class);
        specification.addFacet(new CollectionFacetAbstract(specification) {

            public void init(final NakedObject collection, final NakedObject[] initData) {
                for (int i = 0; i < initData.length; i++) {
                    collectionOfUnderlying(collection).add(initData[i].getObject());
                }
            }

            @SuppressWarnings("unchecked")
            public Collection<NakedObject> collection(NakedObject nakedObjectRepresentingCollection) {
                Collection<Object> collection = collectionOfUnderlying(nakedObjectRepresentingCollection);
                return CollectionUtils.collect(collection, new ObjectToNakedObjectTransformer());
            }

            public NakedObject firstElement(final NakedObject collection) {
                throw new NotYetImplementedException();
            }

            public int size(final NakedObject collection) {
                return collectionOfUnderlying(collection).size();
            }

            @SuppressWarnings("unchecked")
            private Collection<Object> collectionOfUnderlying(final NakedObject collectionNO) {
                return (Collection<Object>) collectionNO.getObject();
            }

        });
    }

    public void testRecreateEmptyCollection() {
        final TestProxyOid collectionOid = new TestProxyOid(123);
        final ReferenceData[] elementData = null;
        final CollectionData data = new DummyCollectionData(collectionOid, Vector.class.getName(), TestPojo.class.getName(),
                elementData, new TestProxyVersion());

        final NakedObject naked = deserializer.deserialize(data);

        final Vector restoredCollection = (Vector) naked.getObject();
        assertEquals(0, restoredCollection.size());

        final CollectionFacet facet = naked.getSpecification().getFacet(CollectionFacet.class);
        assertEquals(0, facet.size(naked));
    }

    public void testRecreateCollection() {
        final ReferenceData elements[] = new ObjectData[2];
        final TestProxyOid element0Oid = new TestProxyOid(345, true);
        elements[0] = new DummyObjectData(element0Oid, TestPojo.class.getName(), false, new TestProxyVersion(3));
        final TestProxyOid element1Oid = new TestProxyOid(678, true);
        elements[1] = new DummyObjectData(element1Oid, TestPojo.class.getName(), false, new TestProxyVersion(7));

        final TestProxyOid collectionOid = new TestProxyOid(123);
        final CollectionData data = new DummyCollectionData(collectionOid, Vector.class.getName(), TestPojo.class.getName(),
                elements, new TestProxyVersion());

        final NakedObject naked = deserializer.deserialize(data);

        final Vector restoredCollection = (Vector) naked.getObject();
        assertEquals(2, restoredCollection.size());

        final CollectionFacet facet = naked.getSpecification().getFacet(CollectionFacet.class);
        final Enumeration elements2 = facet.elements(naked);
        final NakedObject element0 = (NakedObject) elements2.nextElement();
        final NakedObject element2 = (NakedObject) elements2.nextElement();

        assertNotNull(element0.getObject());
        assertNotNull(element2.getObject());

        assertEquals(TestPojo.class, element0.getObject().getClass());
        assertEquals(TestPojo.class, element2.getObject().getClass());

        assertEquals(new TestProxyOid(678, true), element2.getOid());
        assertEquals(new TestProxyOid(345, true), element0.getOid());

        // version not set as there is no field data for elements
        // assertEquals(new DummyVersion(3), naked.elementAt(0).getVersion());
        // assertEquals(new DummyVersion(7), naked.elementAt(1).getVersion());
    }
}
// Copyright (c) Naked Objects Group Ltd.
