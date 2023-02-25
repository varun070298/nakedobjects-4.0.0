package org.nakedobjects.metamodel.facets.properties;

import java.lang.reflect.Method;

import org.nakedobjects.applib.security.UserMemento;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.actions.DescribedAsFacetViaMethod;
import org.nakedobjects.metamodel.facets.actions.NamedFacetViaMethod;
import org.nakedobjects.metamodel.facets.disable.DisableForContextFacet;
import org.nakedobjects.metamodel.facets.disable.DisableForContextFacetViaMethod;
import org.nakedobjects.metamodel.facets.disable.DisableForSessionFacet;
import org.nakedobjects.metamodel.facets.disable.DisableForSessionFacetViaMethod;
import org.nakedobjects.metamodel.facets.disable.DisabledFacet;
import org.nakedobjects.metamodel.facets.disable.DisabledFacetAlways;
import org.nakedobjects.metamodel.facets.hide.HiddenFacet;
import org.nakedobjects.metamodel.facets.hide.HiddenFacetAlways;
import org.nakedobjects.metamodel.facets.hide.HideForContextFacet;
import org.nakedobjects.metamodel.facets.hide.HideForContextFacetViaMethod;
import org.nakedobjects.metamodel.facets.hide.HideForSessionFacet;
import org.nakedobjects.metamodel.facets.hide.HideForSessionFacetViaMethod;
import org.nakedobjects.metamodel.facets.naming.describedas.DescribedAsFacet;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacet;
import org.nakedobjects.metamodel.facets.propcoll.access.PropertyAccessorFacet;
import org.nakedobjects.metamodel.facets.propcoll.access.PropertyAccessorFacetViaAccessor;
import org.nakedobjects.metamodel.facets.propcoll.derived.DerivedFacet;
import org.nakedobjects.metamodel.facets.propcoll.derived.DerivedFacetInferred;
import org.nakedobjects.metamodel.facets.properties.choices.PropertyChoicesFacet;
import org.nakedobjects.metamodel.facets.properties.choices.PropertyChoicesFacetViaMethod;
import org.nakedobjects.metamodel.facets.properties.defaults.PropertyDefaultFacet;
import org.nakedobjects.metamodel.facets.properties.defaults.PropertyDefaultFacetViaMethod;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyClearFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyClearFacetViaClearMethod;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyClearFacetViaSetterMethod;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyInitializationFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertyInitializationFacetViaSetterMethod;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacet;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacetViaModifyMethod;
import org.nakedobjects.metamodel.facets.properties.modify.PropertySetterFacetViaSetterMethod;
import org.nakedobjects.metamodel.facets.properties.validate.PropertyValidateFacet;
import org.nakedobjects.metamodel.facets.properties.validate.PropertyValidateFacetViaMethod;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;


public class PropertyFieldMethodsFacetFactoryTest extends AbstractFacetFactoryTest {

    private PropertyMethodsFacetFactory facetFactory;

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new PropertyMethodsFacetFactory();
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
        assertTrue(contains(featureTypes, NakedObjectFeatureType.PROPERTY));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testPropertyAccessorFacetIsInstalledAndMethodRemoved() {
        class Customer {
            public String getFirstName() {
                return null;
            }
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");

        facetFactory.process(CustomerStatic.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertyAccessorFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertyAccessorFacetViaAccessor);
        final PropertyAccessorFacetViaAccessor propertyAccessorFacetViaAccessor = (PropertyAccessorFacetViaAccessor) facet;
        assertEquals(propertyAccessorMethod, propertyAccessorFacetViaAccessor.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertyAccessorMethod));
    }

    public void testSetterFacetIsInstalledForSetterMethodAndMethodRemoved() {
        class Customer {
            public String getFirstName() {
                return null;
            }

            public void setFirstName(final String firstName) {}
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");
        final Method propertySetterMethod = findMethod(Customer.class, "setFirstName", new Class[] { String.class });

        facetFactory.process(Customer.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertySetterFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertySetterFacetViaSetterMethod);
        final PropertySetterFacetViaSetterMethod propertySetterFacet = (PropertySetterFacetViaSetterMethod) facet;
        assertEquals(propertySetterMethod, propertySetterFacet.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertySetterMethod));
    }

    public void testInitializationFacetIsInstalledForSetterMethodAndMethodRemoved() {
        class Customer {
            public String getFirstName() {
                return null;
            }

            public void setFirstName(final String firstName) {}
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");
        final Method propertySetterMethod = findMethod(Customer.class, "setFirstName", new Class[] { String.class });

        facetFactory.process(Customer.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertyInitializationFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertyInitializationFacet);
        final PropertyInitializationFacetViaSetterMethod propertySetterFacet = (PropertyInitializationFacetViaSetterMethod) facet;
        assertEquals(propertySetterMethod, propertySetterFacet.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertySetterMethod));
    }

    public void testSetterFacetIsInstalledMeansNoDisabledOrDerivedFacetsInstalled() {
        class Customer {
            public String getFirstName() {
                return null;
            }

            public void setFirstName(final String firstName) {}
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");

        facetFactory.process(Customer.class, propertyAccessorMethod, methodRemover, facetHolder);

        assertNull(facetHolder.getFacet(DerivedFacet.class));
        assertNull(facetHolder.getFacet(DisabledFacet.class));
    }

    public void testSetterFacetIsInstalledForModifyMethodAndMethodRemoved() {
        class Customer {
            public String getFirstName() {
                return null;
            }

            public void modifyFirstName(final String firstName) {}
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");
        final Method propertyModifyMethod = findMethod(Customer.class, "modifyFirstName", new Class[] { String.class });

        facetFactory.process(Customer.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertySetterFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertySetterFacetViaModifyMethod);
        final PropertySetterFacetViaModifyMethod propertySetterFacet = (PropertySetterFacetViaModifyMethod) facet;
        assertEquals(propertyModifyMethod, propertySetterFacet.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertyModifyMethod));
    }

    public void testModifyMethodWithNoSetterStillInstallsDisabledAndDerivedFacets() {
        class Customer {
            public String getFirstName() {
                return null;
            }

            public void modifyFirstName(final String firstName) {}
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");
        final Method propertyModifyMethod = findMethod(Customer.class, "modifyFirstName", new Class[] { String.class });

        facetFactory.process(Customer.class, propertyAccessorMethod, methodRemover, facetHolder);

        Facet facet = facetHolder.getFacet(DerivedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DerivedFacetInferred);

        facet = facetHolder.getFacet(DisabledFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DisabledFacetAlways);
    }

    public void testIfHaveSetterAndModifyFacetThenTheModifyFacetWinsOut() {
        class Customer {
            public String getFirstName() {
                return null;
            }

            public void setFirstName(final String firstName) {}

            public void modifyFirstName(final String firstName) {}
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");
        final Method propertySetterMethod = findMethod(Customer.class, "setFirstName", new Class[] { String.class });
        final Method propertyModifyMethod = findMethod(Customer.class, "modifyFirstName", new Class[] { String.class });

        facetFactory.process(Customer.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertySetterFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertySetterFacetViaModifyMethod);
        final PropertySetterFacetViaModifyMethod propertySetterFacet = (PropertySetterFacetViaModifyMethod) facet;
        assertEquals(propertyModifyMethod, propertySetterFacet.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertySetterMethod));
        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertyModifyMethod));
    }

    public void testClearFacet() {
        class Customer {
            public String getFirstName() {
                return null;
            }

            public void clearFirstName() {}
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");
        final Method propertyClearMethod = findMethod(Customer.class, "clearFirstName");

        facetFactory.process(Customer.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertyClearFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertyClearFacetViaClearMethod);
        final PropertyClearFacetViaClearMethod propertyClearFacet = (PropertyClearFacetViaClearMethod) facet;
        assertEquals(propertyClearMethod, propertyClearFacet.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertyClearMethod));
    }

    public void testClearFacetViaSetterIfNoExplicitClearMethod() {
        class Customer {
            public String getFirstName() {
                return null;
            }

            public void setFirstName(final String firstName) {}
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");
        final Method propertySetterMethod = findMethod(Customer.class, "setFirstName", new Class[] { String.class });

        facetFactory.process(Customer.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertyClearFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertyClearFacetViaSetterMethod);
        final PropertyClearFacetViaSetterMethod propertyClearFacet = (PropertyClearFacetViaSetterMethod) facet;
        assertEquals(propertySetterMethod, propertyClearFacet.getMethods().get(0));
    }

    public void testChoicesFacetFoundAndMethodRemoved() {
        class Customer {
            public String getFirstName() {
                return null;
            }

            public Object[] choicesFirstName() {
                return null;
            }
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");
        final Method propertyChoicesMethod = findMethod(Customer.class, "choicesFirstName");

        facetFactory.process(Customer.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertyChoicesFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertyChoicesFacetViaMethod);
        final PropertyChoicesFacetViaMethod propertyChoicesFacet = (PropertyChoicesFacetViaMethod) facet;
        assertEquals(propertyChoicesMethod, propertyChoicesFacet.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertyChoicesMethod));
    }

    public void testDefaultFacetFoundAndMethodRemoved() {
        class Customer {
            public String getFirstName() {
                return null;
            }

            public String defaultFirstName() {
                return null;
            }
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");
        final Method propertyDefaultMethod = findMethod(Customer.class, "defaultFirstName");

        facetFactory.process(Customer.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertyDefaultFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertyDefaultFacetViaMethod);
        final PropertyDefaultFacetViaMethod propertyDefaultFacet = (PropertyDefaultFacetViaMethod) facet;
        assertEquals(propertyDefaultMethod, propertyDefaultFacet.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertyDefaultMethod));
    }

    public void testValidateFacetFoundAndMethodRemoved() {
        class Customer {
            public String getFirstName() {
                return null;
            }

            public String validateFirstName(final String firstName) {
                return null;
            }
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");
        final Method propertyValidateMethod = findMethod(Customer.class, "validateFirstName", new Class[] { String.class });

        facetFactory.process(Customer.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertyValidateFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertyValidateFacetViaMethod);
        final PropertyValidateFacetViaMethod propertyValidateFacet = (PropertyValidateFacetViaMethod) facet;
        assertEquals(propertyValidateMethod, propertyValidateFacet.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertyValidateMethod));
    }

    public void testDisableFacetFoundAndMethodRemoved() {
        class Customer {
            public String getFirstName() {
                return null;
            }

            public String disableFirstName(final String fn) {
                return "disabled";
            }
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");
        final Method propertyDisableMethod = findMethod(Customer.class, "disableFirstName", new Class[] { String.class });

        facetFactory.process(Customer.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DisableForContextFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DisableForContextFacetViaMethod);
        final DisableForContextFacetViaMethod disableForContextFacet = (DisableForContextFacetViaMethod) facet;
        assertEquals(propertyDisableMethod, disableForContextFacet.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertyDisableMethod));
    }

    public void testDisableFacetNoArgsFoundAndMethodRemoved() {
        class Customer {
            public String getFirstName() {
                return null;
            }

            public String disableFirstName() {
                return "disabled";
            }
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");
        final Method propertyDisableMethod = findMethod(Customer.class, "disableFirstName");

        facetFactory.process(Customer.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DisableForContextFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DisableForContextFacetViaMethod);
        final DisableForContextFacetViaMethod disableForContextFacet = (DisableForContextFacetViaMethod) facet;
        assertEquals(propertyDisableMethod, disableForContextFacet.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertyDisableMethod));
    }

    public void testHiddenFacetFoundAndMethodRemoved() {
        class Customer {
            public String getFirstName() {
                return null;
            }

            public boolean hideFirstName(final String fn) {
                return true;
            }
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");
        final Method propertyHideMethod = findMethod(Customer.class, "hideFirstName", new Class[] { String.class });

        facetFactory.process(Customer.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HideForContextFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof HideForContextFacetViaMethod);
        final HideForContextFacetViaMethod hideForContextFacet = (HideForContextFacetViaMethod) facet;
        assertEquals(propertyHideMethod, hideForContextFacet.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertyHideMethod));
    }

    public void testHiddenFacetWithNoArgFoundAndMethodRemoved() {
        class Customer {
            public String getFirstName() {
                return null;
            }

            public boolean hideFirstName() {
                return true;
            }
        }
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");
        final Method propertyHideMethod = findMethod(Customer.class, "hideFirstName");

        facetFactory.process(Customer.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HideForContextFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof HideForContextFacetViaMethod);
        final HideForContextFacetViaMethod hideForContextFacet = (HideForContextFacetViaMethod) facet;
        assertEquals(propertyHideMethod, hideForContextFacet.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertyHideMethod));
    }

    public void testPropertyFoundOnSuperclass() {
        class Customer {
            public String getFirstName() {
                return null;
            }
        }
        
        class CustomerEx extends Customer {
        }
        
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");

        facetFactory.process(CustomerEx.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(PropertyAccessorFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof PropertyAccessorFacetViaAccessor);
        final PropertyAccessorFacetViaAccessor accessorFacet = (PropertyAccessorFacetViaAccessor) facet;
        assertEquals(propertyAccessorMethod, accessorFacet.getMethods().get(0));
    }

    public void testPropertyFoundOnSuperclassButHelperMethodFoundOnSubclass() {
        class Customer {
            public String getFirstName() {
                return null;
            }
        }
        
        class CustomerEx extends Customer {
            public boolean hideFirstName() {
                return true;
            }
            

            public String disableFirstName() {
                return "disabled";
            }
        }
        
        final Method propertyAccessorMethod = findMethod(Customer.class, "getFirstName");
        final Method propertyHideMethod = findMethod(CustomerEx.class, "hideFirstName");
        final Method propertyDisableMethod = findMethod(CustomerEx.class, "disableFirstName");

        facetFactory.process(CustomerEx.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HideForContextFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof HideForContextFacetViaMethod);
        final HideForContextFacetViaMethod hideForContextFacet = (HideForContextFacetViaMethod) facet;
        assertEquals(propertyHideMethod, hideForContextFacet.getMethods().get(0));
        
        final Facet facet2 = facetHolder.getFacet(DisableForContextFacet.class);
        assertNotNull(facet2);
        assertTrue(facet2 instanceof DisableForContextFacetViaMethod);
        final DisableForContextFacetViaMethod disableForContextFacet = (DisableForContextFacetViaMethod) facet2;
        assertEquals(propertyDisableMethod, disableForContextFacet.getMethods().get(0));
    }

    public static class CustomerStatic {
        public String getFirstName() {
            return null;
        }

        // required otherwise marked as DisabledFacetAlways
        public void setFirstName(final String firstName) {}

        public static String nameFirstName() {
            return "Given name";
        };

        public static String descriptionFirstName() {
            return "Some old description";
        }

        public static boolean alwaysHideFirstName() {
            return true;
        }

        public static boolean protectFirstName() {
            return true;
        }

        public static boolean hideFirstName(final UserMemento userMemento) {
            return true;
        }

        public static String disableFirstName(final UserMemento userMemento) {
            return "disabled for this user";
        }

        public String getLastName() {
            return null;
        }

        // required otherwise marked as DisabledFacetAlways
        public void setLastName(final String firstName) {}

        public static boolean alwaysHideLastName() {
            return false;
        }

        public static boolean protectLastName() {
            return false;
        }
    }

    public void testInstallsNamedFacetUsingNameMethodAndRemovesMethod() {
        final Method propertyAccessorMethod = findMethod(CustomerStatic.class, "getFirstName");
        final Method nameMethod = findMethod(CustomerStatic.class, "nameFirstName");

        facetFactory.process(CustomerStatic.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(NamedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NamedFacetViaMethod);
        final NamedFacetViaMethod namedFacet = (NamedFacetViaMethod) facet;
        assertEquals("Given name", namedFacet.value());

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(nameMethod));
    }

    public void testInstallsDescribedAsFacetUsingDescriptionAndRemovesMethod() {
        final Method propertyAccessorMethod = findMethod(CustomerStatic.class, "getFirstName");
        final Method descriptionMethod = findMethod(CustomerStatic.class, "descriptionFirstName");

        facetFactory.process(CustomerStatic.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DescribedAsFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DescribedAsFacetViaMethod);
        final DescribedAsFacetViaMethod describedAsFacet = (DescribedAsFacetViaMethod) facet;
        assertEquals("Some old description", describedAsFacet.value());

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(descriptionMethod));
    }

    public void testInstallsHiddenFacetUsingAlwaysHideAndRemovesMethod() {
        final Method propertyAccessorMethod = findMethod(CustomerStatic.class, "getFirstName");
        final Method propertyAlwaysHideMethod = findMethod(CustomerStatic.class, "alwaysHideFirstName");

        facetFactory.process(CustomerStatic.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HiddenFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof HiddenFacetAlways);

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertyAlwaysHideMethod));
    }

    public void testInstallsHiddenFacetUsingAlwaysHideWhenNotAndRemovesMethod() {
        final Method propertyAccessorMethod = findMethod(CustomerStatic.class, "getLastName");
        final Method propertyAlwaysHideMethod = findMethod(CustomerStatic.class, "alwaysHideLastName");

        facetFactory.process(CustomerStatic.class, propertyAccessorMethod, methodRemover, facetHolder);

        assertNull(facetHolder.getFacet(HiddenFacet.class));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertyAlwaysHideMethod));
    }

    public void testInstallsDisabledFacetUsingProtectAndRemovesMethod() {
        final Method propertyAccessorMethod = findMethod(CustomerStatic.class, "getFirstName");
        final Method propertyProtectMethod = findMethod(CustomerStatic.class, "protectFirstName");

        facetFactory.process(CustomerStatic.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DisabledFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DisabledFacetAlways);

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertyProtectMethod));
    }

    public void testDoesNotInstallDisabledFacetUsingProtectWhenNotAndRemovesMethod() {
        final Method propertyAccessorMethod = findMethod(CustomerStatic.class, "getLastName");
        final Method propertyProtectMethod = findMethod(CustomerStatic.class, "protectLastName");

        facetFactory.process(CustomerStatic.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DisabledFacet.class);
        assertNull(facet);

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(propertyProtectMethod));
    }

    public void testInstallsHiddenForSessionFacetAndRemovesMethod() {
        final Method propertyAccessorMethod = findMethod(CustomerStatic.class, "getFirstName");
        final Method hideMethod = findMethod(CustomerStatic.class, "hideFirstName", new Class[] { UserMemento.class });

        facetFactory.process(CustomerStatic.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HideForSessionFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof HideForSessionFacetViaMethod);
        final HideForSessionFacetViaMethod hideForSessionFacetViaMethod = (HideForSessionFacetViaMethod) facet;
        assertEquals(hideMethod, hideForSessionFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(hideMethod));

    }

    public void testInstallsDisabledForSessionFacetAndRemovesMethod() {
        final Method propertyAccessorMethod = findMethod(CustomerStatic.class, "getFirstName");
        final Method disableMethod = findMethod(CustomerStatic.class, "disableFirstName", new Class[] { UserMemento.class });

        facetFactory.process(CustomerStatic.class, propertyAccessorMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DisableForSessionFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DisableForSessionFacetViaMethod);
        final DisableForSessionFacetViaMethod disableForSessionFacetViaMethod = (DisableForSessionFacetViaMethod) facet;
        assertEquals(disableMethod, disableForSessionFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(disableMethod));
    }

}

// Copyright (c) Naked Objects Group Ltd.
