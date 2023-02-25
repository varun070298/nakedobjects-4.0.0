package org.nakedobjects.metamodel.facets.collections;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.nakedobjects.applib.security.UserMemento;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.When;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacetInferredFromSupportingMethods;
import org.nakedobjects.metamodel.facets.actions.DescribedAsFacetViaMethod;
import org.nakedobjects.metamodel.facets.actions.NamedFacetViaMethod;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionAddToFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionAddToFacetViaAccessor;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionAddToFacetViaMethod;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionClearFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionClearFacetViaAccessor;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionClearFacetViaMethod;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionRemoveFromFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionRemoveFromFacetViaAccessor;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionRemoveFromFacetViaMethod;
import org.nakedobjects.metamodel.facets.collections.validate.CollectionValidateAddToFacet;
import org.nakedobjects.metamodel.facets.collections.validate.CollectionValidateAddToFacetViaMethod;
import org.nakedobjects.metamodel.facets.collections.validate.CollectionValidateRemoveFromFacet;
import org.nakedobjects.metamodel.facets.collections.validate.CollectionValidateRemoveFromFacetViaMethod;
import org.nakedobjects.metamodel.facets.disable.DisableForSessionFacet;
import org.nakedobjects.metamodel.facets.disable.DisableForSessionFacetViaMethod;
import org.nakedobjects.metamodel.facets.disable.DisabledFacet;
import org.nakedobjects.metamodel.facets.disable.DisabledFacetAlways;
import org.nakedobjects.metamodel.facets.hide.HiddenFacet;
import org.nakedobjects.metamodel.facets.hide.HiddenFacetAbstract;
import org.nakedobjects.metamodel.facets.hide.HiddenFacetAlways;
import org.nakedobjects.metamodel.facets.hide.HideForSessionFacet;
import org.nakedobjects.metamodel.facets.hide.HideForSessionFacetViaMethod;
import org.nakedobjects.metamodel.facets.naming.describedas.DescribedAsFacet;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacet;
import org.nakedobjects.metamodel.facets.propcoll.access.PropertyAccessorFacet;
import org.nakedobjects.metamodel.facets.propcoll.access.PropertyAccessorFacetViaAccessor;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class CollectionFieldMethodsFacetFactoryTest extends AbstractFacetFactoryTest {

    private CollectionFieldMethodsFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new CollectionFieldMethodsFacetFactory();
        facetFactory.setSpecificationLoader(reflector);
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
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testPropertyAccessorFacetIsInstalledForJavaUtilCollectionAndMethodRemoved() {
        class Customer {
            public Collection getOrders() {
                return null;
            }
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(CustomerStatic.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertyAccessorFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertyAccessorFacetViaAccessor);
        final PropertyAccessorFacetViaAccessor propertyAccessorFacetViaAccessor = (PropertyAccessorFacetViaAccessor) facet;
        assertEquals(collectionAccessorMethod, propertyAccessorFacetViaAccessor.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(collectionAccessorMethod));
    }

    public void testPropertyAccessorFacetIsInstalledForJavaUtilListAndMethodRemoved() {
        class Customer {
            public List getOrders() {
                return null;
            }
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(CustomerStatic.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertyAccessorFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertyAccessorFacetViaAccessor);
        final PropertyAccessorFacetViaAccessor propertyAccessorFacetViaAccessor = (PropertyAccessorFacetViaAccessor) facet;
        assertEquals(collectionAccessorMethod, propertyAccessorFacetViaAccessor.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(collectionAccessorMethod));
    }

    public void testPropertyAccessorFacetIsInstalledForJavaUtilSetAndMethodRemoved() {
        class Customer {
            public Set getOrders() {
                return null;
            }
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertyAccessorFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertyAccessorFacetViaAccessor);
        final PropertyAccessorFacetViaAccessor propertyAccessorFacetViaAccessor = (PropertyAccessorFacetViaAccessor) facet;
        assertEquals(collectionAccessorMethod, propertyAccessorFacetViaAccessor.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(collectionAccessorMethod));
    }

    public void testPropertyAccessorFacetIsInstalledForObjectArrayAndMethodRemoved() {
        class Customer {
            public Object[] getOrders() {
                return null;
            }
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertyAccessorFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertyAccessorFacetViaAccessor);
        final PropertyAccessorFacetViaAccessor propertyAccessorFacetViaAccessor = (PropertyAccessorFacetViaAccessor) facet;
        assertEquals(collectionAccessorMethod, propertyAccessorFacetViaAccessor.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(collectionAccessorMethod));
    }

    public void testPropertyAccessorFacetIsInstalledForOrderArrayAndMethodRemoved() {
        class Order {}
        class Customer {
            public Order[] getOrders() {
                return null;
            }
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertyAccessorFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertyAccessorFacetViaAccessor);
        final PropertyAccessorFacetViaAccessor propertyAccessorFacetViaAccessor = (PropertyAccessorFacetViaAccessor) facet;
        assertEquals(collectionAccessorMethod, propertyAccessorFacetViaAccessor.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(collectionAccessorMethod));
    }

    public void testAddToFacetIsInstalledViaAccessorIfNoExplicitAddToMethodExists() {
        class Customer {
            public Collection getOrders() {
                return null;
            }
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(CollectionAddToFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof CollectionAddToFacetViaAccessor);
        final CollectionAddToFacetViaAccessor collectionAddToFacetViaAccessor = (CollectionAddToFacetViaAccessor) facet;
        assertEquals(collectionAccessorMethod, collectionAddToFacetViaAccessor.getMethods().get(0));
    }

    public void testCannotInferTypeOfFacetIfNoExplicitAddToOrRemoveFromMethods() {
        class Customer {
            public Collection getOrders() {
                return null;
            }
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        assertNull(facetHolder.getFacet(TypeOfFacet.class));
    }

    public void testRemoveFromFacetIsInstalledViaAccessorIfNoExplicitRemoveFromMethodExists() {
        class Customer {
            public Collection getOrders() {
                return null;
            }
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(CollectionRemoveFromFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof CollectionRemoveFromFacetViaAccessor);
        final CollectionRemoveFromFacetViaAccessor collectionRemoveFromFacetViaAccessor = (CollectionRemoveFromFacetViaAccessor) facet;
        assertEquals(collectionAccessorMethod, collectionRemoveFromFacetViaAccessor.getMethods().get(0));
    }

    public void testAddToFacetIsInstalledAndMethodRemoved() {
        class Order {}
        class Customer {
            public Collection getOrders() {
                return null;
            }

            public void addToOrders(final Order o) {};
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");
        final Method addToMethod = findMethod(Customer.class, "addToOrders", new Class[] { Order.class });

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(CollectionAddToFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof CollectionAddToFacetViaMethod);
        final CollectionAddToFacetViaMethod collectionAddToFacetViaMethod = (CollectionAddToFacetViaMethod) facet;
        assertEquals(addToMethod, collectionAddToFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(addToMethod));
    }

    public void testCanInferTypeOfFacetFromExplicitAddToMethod() {
        class Order {}
        class Customer {
            public Collection getOrders() {
                return null;
            }

            public void addToOrders(final Order o) {};
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");
        final Method addToMethod = findMethod(Customer.class, "addToOrders", new Class[] { Order.class });

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(TypeOfFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TypeOfFacetInferredFromSupportingMethods);
        final TypeOfFacetInferredFromSupportingMethods typeOfFacetInferredFromSupportingMethods = (TypeOfFacetInferredFromSupportingMethods) facet;
        assertEquals(Order.class, typeOfFacetInferredFromSupportingMethods.value());
    }

    public void testRemoveFromFacetIsInstalledAndMethodRemoved() {
        class Order {}
        class Customer {
            public Collection getOrders() {
                return null;
            }

            public void removeFromOrders(final Order o) {};
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");
        final Method removeFromMethod = findMethod(Customer.class, "removeFromOrders", new Class[] { Order.class });

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(CollectionRemoveFromFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof CollectionRemoveFromFacetViaMethod);
        final CollectionRemoveFromFacetViaMethod collectionRemoveFromFacetViaMethod = (CollectionRemoveFromFacetViaMethod) facet;
        assertEquals(removeFromMethod, collectionRemoveFromFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(removeFromMethod));
    }

    public void testCanInferTypeOfFacetFromExplicitRemoveFromMethod() {
        class Order {}
        class Customer {
            public Collection getOrders() {
                return null;
            }

            public void removeFromOrders(final Order o) {};
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");
        final Method removeFromMethod = findMethod(Customer.class, "removeFromOrders", new Class[] { Order.class });

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(TypeOfFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof TypeOfFacetInferredFromSupportingMethods);
        final TypeOfFacetInferredFromSupportingMethods typeOfFacetInferredFromSupportingMethods = (TypeOfFacetInferredFromSupportingMethods) facet;
        assertEquals(Order.class, typeOfFacetInferredFromSupportingMethods.value());
    }

    public void testClearFacetIsInstalledAndMethodRemoved() {
        class Order {}
        class Customer {
            public Collection getOrders() {
                return null;
            }

            public void clearOrders() {};
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");
        final Method clearMethod = findMethod(Customer.class, "clearOrders");

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(CollectionClearFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof CollectionClearFacetViaMethod);
        final CollectionClearFacetViaMethod collectionClearFacetViaMethod = (CollectionClearFacetViaMethod) facet;
        assertEquals(clearMethod, collectionClearFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(clearMethod));
    }

    public void testClearFacetIsInstalledViaAccessorIfNoExplicitClearMethod() {
        class Order {}
        class Customer {
            public Collection getOrders() {
                return null;
            }
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(CollectionClearFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof CollectionClearFacetViaAccessor);
        final CollectionClearFacetViaAccessor collectionClearFacetViaAccessor = (CollectionClearFacetViaAccessor) facet;
        assertEquals(collectionAccessorMethod, collectionClearFacetViaAccessor.getMethods().get(0));
    }

    public void testValidateAddToFacetIsInstalledAndMethodRemoved() {
        class Order {}
        class Customer {
            public Collection getOrders() {
                return null;
            }

            public void addToOrders(final Order o) {};

            public String validateAddToOrders(final Order o) {
                return null;
            };
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");
        final Method validateAddToMethod = findMethod(Customer.class, "validateAddToOrders", new Class[] { Order.class });

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(CollectionValidateAddToFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof CollectionValidateAddToFacetViaMethod);
        final CollectionValidateAddToFacetViaMethod collectionValidateAddToFacetViaMethod = (CollectionValidateAddToFacetViaMethod) facet;
        assertEquals(validateAddToMethod, collectionValidateAddToFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(validateAddToMethod));
    }

    public void testValidateRemoveFromFacetIsInstalledAndMethodRemoved() {
        class Order {}
        class Customer {
            public Collection getOrders() {
                return null;
            }

            public void removeFromOrders(final Order o) {};

            public String validateRemoveFromOrders(final Order o) {
                return null;
            };
        }
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");
        final Method validateRemoveFromMethod = findMethod(Customer.class, "validateRemoveFromOrders",
                new Class[] { Order.class });

        facetFactory.process(Customer.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(CollectionValidateRemoveFromFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof CollectionValidateRemoveFromFacetViaMethod);
        final CollectionValidateRemoveFromFacetViaMethod collectionValidateRemoveFromFacetViaMethod = (CollectionValidateRemoveFromFacetViaMethod) facet;
        assertEquals(validateRemoveFromMethod, collectionValidateRemoveFromFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(validateRemoveFromMethod));
    }
    
    public void testMethodFoundInSuperclass() {
        class Order {}
        class Customer {
            public Collection getOrders() {
                return null;
            }
        }
        
        class CustomerEx extends Customer {
        }
        
        
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");
 
        facetFactory.process(CustomerEx.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertyAccessorFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertyAccessorFacetViaAccessor);
        final PropertyAccessorFacetViaAccessor collectionAccessorFacetViaMethod = (PropertyAccessorFacetViaAccessor) facet;
        assertEquals(collectionAccessorMethod, collectionAccessorFacetViaMethod.getMethods().get(0));
    }
    
    public void testMethodFoundInSuperclassButHelpeMethodsFoundInSubclasses() {
        class Order {}
        class Customer {
            public Collection getOrders() {
                return null;
            }
        }
        
        class CustomerEx extends Customer {
            public void removeFromOrders(final Order o) {};

            public String validateRemoveFromOrders(final Order o) {
                return null;
            };
        }
        
        
        final Method collectionAccessorMethod = findMethod(Customer.class, "getOrders");
        final Method removeFromMethod = findMethod(CustomerEx.class, "removeFromOrders",
                new Class[] { Order.class });
        final Method validateRemoveFromMethod = findMethod(CustomerEx.class, "validateRemoveFromOrders",
                new Class[] { Order.class });

        facetFactory.process(CustomerEx.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(CollectionRemoveFromFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof CollectionRemoveFromFacetViaMethod);
        final CollectionRemoveFromFacetViaMethod collectionRemoveFromFacetViaMethod = (CollectionRemoveFromFacetViaMethod) facet;
        assertEquals(removeFromMethod, collectionRemoveFromFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(removeFromMethod));

        final Facet facet1 = facetHolder.getFacet(CollectionValidateRemoveFromFacet.class);
        assertNotNull(facet1);
        assertTrue(facet1 instanceof CollectionValidateRemoveFromFacetViaMethod);
        final CollectionValidateRemoveFromFacetViaMethod collectionValidateRemoveFromFacetViaMethod = (CollectionValidateRemoveFromFacetViaMethod) facet1;
        assertEquals(validateRemoveFromMethod, collectionValidateRemoveFromFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(validateRemoveFromMethod));
    }

    class Order {}

    public static class CustomerStatic {
        public Collection getOrders() {
            return null;
        }

        public static String nameOrders() {
            return "Most Recent Orders";
        };

        public static String descriptionOrders() {
            return "Some old description";
        }

        public static boolean alwaysHideOrders() {
            return true;
        }

        public static boolean protectOrders() {
            return true;
        }

        public static boolean hideOrders(final UserMemento userMemento) {
            return true;
        }

        public static String disableOrders(final UserMemento userMemento) {
            return "disabled for this user";
        }

        public static void getOtherOrders() {}

        public static boolean alwaysHideOtherOrders() {
            return false;
        }

        public static boolean protectOtherOrders() {
            return false;
        }
    }

    public void testInstallsNamedFacetUsingNameMethodAndRemovesMethod() {
        final Method collectionAccessorMethod = findMethod(CustomerStatic.class, "getOrders");
        final Method nameMethod = findMethod(CustomerStatic.class, "nameOrders");

        facetFactory.process(CustomerStatic.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(NamedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NamedFacetViaMethod);
        final NamedFacetViaMethod namedFacet = (NamedFacetViaMethod) facet;
        assertEquals("Most Recent Orders", namedFacet.value());

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(nameMethod));
    }

    public void testInstallsDescribedAsFacetUsingDescriptionAndRemovesMethod() {
        final Method collectionAccessorMethod = findMethod(CustomerStatic.class, "getOrders");
        final Method descriptionMethod = findMethod(CustomerStatic.class, "descriptionOrders");

        facetFactory.process(CustomerStatic.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DescribedAsFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DescribedAsFacetViaMethod);
        final DescribedAsFacetViaMethod describedAsFacet = (DescribedAsFacetViaMethod) facet;
        assertEquals("Some old description", describedAsFacet.value());

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(descriptionMethod));
    }

    public void testInstallsHiddenFacetUsingAlwaysHideAndRemovesMethod() {
        final Method collectionAccessorMethod = findMethod(CustomerStatic.class, "getOrders");
        final Method alwaysHideMethod = findMethod(CustomerStatic.class, "alwaysHideOrders");

        facetFactory.process(CustomerStatic.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HiddenFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof HiddenFacetAlways);
        final HiddenFacetAbstract hiddenFacetAlways = (HiddenFacetAlways) facet;
        assertEquals(When.ALWAYS, hiddenFacetAlways.value());

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(alwaysHideMethod));
    }

    public void testInstallsHiddenFacetUsingAlwaysHideWhenNotAndRemovesMethod() {
        final Method collectionAccessorMethod = findMethod(CustomerStatic.class, "getOtherOrders");
        final Method alwaysHideMethod = findMethod(CustomerStatic.class, "alwaysHideOtherOrders");

        facetFactory.process(CustomerStatic.class, collectionAccessorMethod, methodRemover, facetHolder);

        assertNull(facetHolder.getFacet(HiddenFacet.class));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(alwaysHideMethod));
    }

    public void testInstallsDisabledFacetUsingProtectAndRemovesMethod() {
        final Method collectionAccessorMethod = findMethod(CustomerStatic.class, "getOrders");
        final Method protectMethod = findMethod(CustomerStatic.class, "protectOrders");
        // reflector.setLoadSpecificationClassReturn(voidNoSpec);

        facetFactory.process(CustomerStatic.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DisabledFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DisabledFacetAlways);
        final DisabledFacetAlways disabledFacetAlways = (DisabledFacetAlways) facet;
        assertEquals(When.ALWAYS, disabledFacetAlways.value());

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(protectMethod));
    }

    public void testDoesNotInstallDisabledFacetUsingProtectWhenNotAndRemovesMethod() {
        final Method collectionAccessorMethod = findMethod(CustomerStatic.class, "getOtherOrders");
        final Method protectMethod = findMethod(CustomerStatic.class, "protectOtherOrders");

        facetFactory.process(CustomerStatic.class, collectionAccessorMethod, methodRemover, facetHolder);

        assertNull(facetHolder.getFacet(DisabledFacet.class));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(protectMethod));
    }

    public void testInstallsHiddenForSessionFacetAndRemovesMethod() {
        final Method collectionAccessorMethod = findMethod(CustomerStatic.class, "getOrders");
        final Method hideMethod = findMethod(CustomerStatic.class, "hideOrders", new Class[] { UserMemento.class });

        facetFactory.process(CustomerStatic.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HideForSessionFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof HideForSessionFacetViaMethod);
        final HideForSessionFacetViaMethod hideForSessionFacetViaMethod = (HideForSessionFacetViaMethod) facet;
        assertEquals(hideMethod, hideForSessionFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(hideMethod));
    }

    public void testInstallsDisabledForSessionFacetAndRemovesMethod() {
        final Method collectionAccessorMethod = findMethod(CustomerStatic.class, "getOrders");
        final Method disableMethod = findMethod(CustomerStatic.class, "disableOrders", new Class[] { UserMemento.class });

        facetFactory.process(CustomerStatic.class, collectionAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DisableForSessionFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DisableForSessionFacetViaMethod);
        final DisableForSessionFacetViaMethod disableForSessionFacetViaMethod = (DisableForSessionFacetViaMethod) facet;
        assertEquals(disableMethod, disableForSessionFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(disableMethod));
    }

}

// Copyright (c) Naked Objects Group Ltd.
