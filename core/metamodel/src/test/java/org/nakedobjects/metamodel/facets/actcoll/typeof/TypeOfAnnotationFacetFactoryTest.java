package org.nakedobjects.metamodel.facets.actcoll.typeof;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.nakedobjects.applib.annotation.TypeOf;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.specloader.collectiontyperegistry.CollectionTypeRegistryDefault;


public class TypeOfAnnotationFacetFactoryTest extends AbstractFacetFactoryTest {

    private TypeOfAnnotationFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new TypeOfAnnotationFacetFactory();
        facetFactory.setCollectionTypeRegistry(new CollectionTypeRegistryDefault());
    }

    @Override
    protected void tearDown() throws Exception {
        facetFactory = null;
        super.tearDown();
    }

    @Override
    public void testFeatureTypes() {
        final NakedObjectFeatureType[] featureTypes = facetFactory.getFeatureTypes();
        assertFalse(contains(featureTypes, NakedObjectFeatureType.OBJECT));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.PROPERTY));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testTypeOfFacetInferredForActionWithJavaUtilCollectionReturnType() {
        class Order {}
        class Customer {
            @TypeOf(Order.class)
            public Collection someAction() {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(TypeOfFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TypeOfFacetViaAnnotation);
        final TypeOfFacetViaAnnotation typeOfFacetViaAnnotation = (TypeOfFacetViaAnnotation) facet;
        assertEquals(Order.class, typeOfFacetViaAnnotation.value());

        assertNoMethodsRemoved();
    }

    public void testTypeOfFacetInferredForCollectionWithJavaUtilCollectionReturnType() {
        class Order {}
        class Customer {
            @TypeOf(Order.class)
            public Collection getOrders() {
                return null;
            }
        }
        final Method accessorMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, accessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(TypeOfFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TypeOfFacetViaAnnotation);
        final TypeOfFacetViaAnnotation typeOfFacetViaAnnotation = (TypeOfFacetViaAnnotation) facet;
        assertEquals(Order.class, typeOfFacetViaAnnotation.value());

        assertNoMethodsRemoved();
    }

    public void testTypeOfFacetInferredForActionWithGenericCollectionReturnType() {
        class Order {}
        class Customer {
            public Collection<Order> someAction() {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(TypeOfFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TypeOfFacetInferredFromGenerics);
        final TypeOfFacetInferredFromGenerics typeOfFacetInferredFromGenerics = (TypeOfFacetInferredFromGenerics) facet;
        assertEquals(Order.class, typeOfFacetInferredFromGenerics.value());

    }

    public void testTypeOfFacetInferredForCollectionWithGenericCollectionReturnType() {
        class Order {}
        class Customer {
            public Collection<Order> getOrders() {
                return null;
            }
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(TypeOfFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TypeOfFacetInferredFromGenerics);
        final TypeOfFacetInferredFromGenerics typeOfFacetInferredFromGenerics = (TypeOfFacetInferredFromGenerics) facet;
        assertEquals(Order.class, typeOfFacetInferredFromGenerics.value());

    }

    public void testTypeOfFacetInferredForActionWithJavaUtilListReturnType() {
        class Order {}
        class Customer {
            @TypeOf(Order.class)
            public List someAction() {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(TypeOfFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TypeOfFacetViaAnnotation);
        final TypeOfFacetViaAnnotation typeOfFacetViaAnnotation = (TypeOfFacetViaAnnotation) facet;
        assertEquals(Order.class, typeOfFacetViaAnnotation.value());

        assertNoMethodsRemoved();
    }

    public void testTypeOfFacetInferredForCollectionWithJavaUtilListReturnType() {
        class Order {}
        class Customer {
            @TypeOf(Order.class)
            public List getOrders() {
                return null;
            }
        }
        final Method accessorMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, accessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(TypeOfFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TypeOfFacetViaAnnotation);
        final TypeOfFacetViaAnnotation typeOfFacetViaAnnotation = (TypeOfFacetViaAnnotation) facet;
        assertEquals(Order.class, typeOfFacetViaAnnotation.value());

        assertNoMethodsRemoved();
    }

    public void testTypeOfFacetInferredForActionWithJavaUtilSetReturnType() {
        class Order {}
        class Customer {
            @TypeOf(Order.class)
            public Set someAction() {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(TypeOfFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TypeOfFacetViaAnnotation);
        final TypeOfFacetViaAnnotation typeOfFacetViaAnnotation = (TypeOfFacetViaAnnotation) facet;
        assertEquals(Order.class, typeOfFacetViaAnnotation.value());

        assertNoMethodsRemoved();
    }

    public void testTypeOfFacetInferredForCollectionWithJavaUtilSetReturnType() {
        class Order {}
        class Customer {
            @TypeOf(Order.class)
            public Set getOrders() {
                return null;
            }
        }
        final Method accessorMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, accessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(TypeOfFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TypeOfFacetViaAnnotation);
        final TypeOfFacetViaAnnotation typeOfFacetViaAnnotation = (TypeOfFacetViaAnnotation) facet;
        assertEquals(Order.class, typeOfFacetViaAnnotation.value());

        assertNoMethodsRemoved();
    }

    public void testTypeOfFacetInferredForActionWithArrayReturnType() {
        class Order {}
        class Customer {
            public Order[] someAction() {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(TypeOfFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TypeOfFacetInferredFromArray);
        final TypeOfFacetInferredFromArray typeOfFacetInferredFromArray = (TypeOfFacetInferredFromArray) facet;
        assertEquals(Order.class, typeOfFacetInferredFromArray.value());

        assertNoMethodsRemoved();
    }

    public void testTypeOfFacetIsInferredForCollectionFromOrderArray() {
        class Order {}
        class Customer {
            public Order[] getOrders() {
                return null;
            }
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(TypeOfFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TypeOfFacetInferredFromArray);
        final TypeOfFacetInferredFromArray typeOfFacetInferredFromArray = (TypeOfFacetInferredFromArray) facet;
        assertEquals(Order.class, typeOfFacetInferredFromArray.value());

    }

    public void testTypeOfAnnotationIgnoredForActionIfReturnTypeIsntACollectionType() {
        class Order {}
        class Customer {
            @TypeOf(Order.class)
            public Customer someAction() {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        assertNull(facetHolder.getFacet(TypeOfFacet.class));

        assertNoMethodsRemoved();
    }

}

// Copyright (c) Naked Objects Group Ltd.
