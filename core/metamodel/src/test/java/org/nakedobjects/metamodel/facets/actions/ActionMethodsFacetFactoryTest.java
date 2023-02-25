package org.nakedobjects.metamodel.facets.actions;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.List;

import org.nakedobjects.applib.Identifier;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.applib.security.UserMemento;
import org.nakedobjects.metamodel.facets.AbstractFacetFactoryTest;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.When;
import org.nakedobjects.metamodel.facets.actions.choices.ActionChoicesFacet;
import org.nakedobjects.metamodel.facets.actions.choices.ActionChoicesFacetViaMethod;
import org.nakedobjects.metamodel.facets.actions.choices.ActionParameterChoicesFacet;
import org.nakedobjects.metamodel.facets.actions.choices.ActionParameterChoicesFacetViaMethod;
import org.nakedobjects.metamodel.facets.actions.debug.DebugFacet;
import org.nakedobjects.metamodel.facets.actions.defaults.ActionDefaultsFacet;
import org.nakedobjects.metamodel.facets.actions.defaults.ActionDefaultsFacetViaMethod;
import org.nakedobjects.metamodel.facets.actions.defaults.ActionParameterDefaultsFacet;
import org.nakedobjects.metamodel.facets.actions.defaults.ActionParameterDefaultsFacetViaMethod;
import org.nakedobjects.metamodel.facets.actions.executed.ExecutedFacet;
import org.nakedobjects.metamodel.facets.actions.exploration.ExplorationFacet;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacet;
import org.nakedobjects.metamodel.facets.actions.invoke.ActionInvocationFacetViaMethod;
import org.nakedobjects.metamodel.facets.actions.validate.ActionValidationFacet;
import org.nakedobjects.metamodel.facets.actions.validate.ActionValidationFacetViaMethod;
import org.nakedobjects.metamodel.facets.disable.DisableForContextFacet;
import org.nakedobjects.metamodel.facets.disable.DisableForContextFacetViaMethod;
import org.nakedobjects.metamodel.facets.disable.DisableForSessionFacet;
import org.nakedobjects.metamodel.facets.disable.DisableForSessionFacetViaMethod;
import org.nakedobjects.metamodel.facets.disable.DisabledFacet;
import org.nakedobjects.metamodel.facets.disable.DisabledFacetAbstract;
import org.nakedobjects.metamodel.facets.hide.HiddenFacet;
import org.nakedobjects.metamodel.facets.hide.HiddenFacetAbstract;
import org.nakedobjects.metamodel.facets.hide.HideForSessionFacet;
import org.nakedobjects.metamodel.facets.hide.HideForSessionFacetViaMethod;
import org.nakedobjects.metamodel.facets.naming.describedas.DescribedAsFacet;
import org.nakedobjects.metamodel.facets.naming.describedas.DescribedAsFacetAbstract;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacet;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacetAbstract;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectFeatureType;
import org.nakedobjects.metamodel.specloader.internal.peer.JavaNakedObjectActionParamPeer;
import org.nakedobjects.metamodel.specloader.internal.peer.JavaNakedObjectActionPeer;
import org.nakedobjects.metamodel.specloader.internal.peer.NakedObjectActionPeer;
import org.nakedobjects.metamodel.testspec.TestProxySpecification;


public class ActionMethodsFacetFactoryTest extends AbstractFacetFactoryTest {

    private ActionMethodsFacetFactory facetFactory;
    private final NakedObjectSpecification voidNoSpec = new TestProxySpecification("VOID");
    private final NakedObjectSpecification stringNoSpec = new TestProxySpecification("java.lang.String");
    private final NakedObjectSpecification customerNoSpec = new TestProxySpecification("Customer");

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        facetFactory = new ActionMethodsFacetFactory();
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
        assertFalse(contains(featureTypes, NakedObjectFeatureType.COLLECTION));
        assertTrue(contains(featureTypes, NakedObjectFeatureType.ACTION));
        assertFalse(contains(featureTypes, NakedObjectFeatureType.ACTION_PARAMETER));
    }

    public void testActionInvocationFacetIsInstalledAndMethodRemoved() {
        class Customer {
            public void someAction() {}
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ActionInvocationFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ActionInvocationFacetViaMethod);
        final ActionInvocationFacetViaMethod actionInvocationFacetViaMethod = (ActionInvocationFacetViaMethod) facet;
        assertEquals(actionMethod, actionInvocationFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(actionMethod));
    }

    public void testProvidesDefaultNameForActionButIgnoresAnyNamedAnnotation() {
        class Customer {
            @Named("Renamed an action with a named annotation")
            public void anActionWithNamedAnnotation() {}
        }
        final Method method = findMethod(Customer.class, "anActionWithNamedAnnotation");

        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(NamedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NamedFacet);
        final NamedFacet namedFacet = (NamedFacet) facet;
        assertEquals("An Action With Named Annotation", namedFacet.value());
    }

    public void testPicksUpDebugPrefixAndSetsNameAppropriatelyAlso() {
        class Customer {
            public void debugAnActionWithDebugPrefix() {}
        }
        final Method method = findMethod(Customer.class, "debugAnActionWithDebugPrefix");
        reflector.setLoadSpecificationStringReturn(voidNoSpec);
        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        Facet facet = facetHolder.getFacet(DebugFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DebugFacet);

        facet = facetHolder.getFacet(NamedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NamedFacet);
        final NamedFacet namedFacet = (NamedFacet) facet;
        assertEquals("An Action With Debug Prefix", namedFacet.value());
    }

    public void testPicksUpExplorationPrefixAndSetsNameAppropriatelyAlso() {
        class Customer {
            public void explorationAnActionWithExplorationPrefix() {}
        }
        final Method method = findMethod(Customer.class, "explorationAnActionWithExplorationPrefix");
        reflector.setLoadSpecificationStringReturn(voidNoSpec);
        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        Facet facet = facetHolder.getFacet(ExplorationFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ExplorationFacet);

        facet = facetHolder.getFacet(NamedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NamedFacet);
        final NamedFacet namedFacet = (NamedFacet) facet;
        assertEquals("An Action With Exploration Prefix", namedFacet.value());
    }

    public void testCannotHaveBothDebugAndThenExplorationPrefix() {
        class Customer {
            public void debugExplorationAnActionWithDebugAndExplorationPrefix() {}
        }
        final Method method = findMethod(Customer.class, "debugExplorationAnActionWithDebugAndExplorationPrefix");
        reflector.setLoadSpecificationStringReturn(voidNoSpec);
        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        Facet facet = facetHolder.getFacet(DebugFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DebugFacet);

        facet = facetHolder.getFacet(ExplorationFacet.class);
        assertNull(facet);
    }

    public void testCannotHaveBothExplorationAndThenDebugPrefix() {
        class Customer {
            public void explorationDebugAnActionWithExplorationAndDebugPrefix() {}
        }
        final Method method = findMethod(Customer.class, "explorationDebugAnActionWithExplorationAndDebugPrefix");
        reflector.setLoadSpecificationStringReturn(voidNoSpec);
        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        Facet facet = facetHolder.getFacet(ExplorationFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ExplorationFacet);

        facet = facetHolder.getFacet(DebugFacet.class);
        assertNull(facet);
    }

    public void testPicksUpLocalPrefixAndSetsNameAppropriatelyAlso() {
        class Customer {
            public void localAnActionWithLocalPrefix() {}
        }
        final Method method = findMethod(Customer.class, "localAnActionWithLocalPrefix");
        reflector.setLoadSpecificationStringReturn(voidNoSpec);
        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        Facet facet = facetHolder.getFacet(ExecutedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ExecutedFacet);
        final ExecutedFacet executedFacet = (ExecutedFacet) facet;
        assertEquals(ExecutedFacet.Where.LOCALLY, executedFacet.value());

        facet = facetHolder.getFacet(NamedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NamedFacet);
        final NamedFacet namedFacet = (NamedFacet) facet;
        assertEquals("An Action With Local Prefix", namedFacet.value());
    }

    public void testPicksUpRemotePrefixAndSetsNameAppropriatelyAlso() {
        class Customer {
            public void remoteAnActionWithRemotePrefix() {}
        }
        final Method method = findMethod(Customer.class, "remoteAnActionWithRemotePrefix");
        reflector.setLoadSpecificationStringReturn(voidNoSpec);
        facetFactory.process(Customer.class, method, methodRemover, facetHolder);

        Facet facet = facetHolder.getFacet(ExecutedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ExecutedFacet);
        final ExecutedFacet executedFacet = (ExecutedFacet) facet;
        assertEquals(ExecutedFacet.Where.REMOTELY, executedFacet.value());

        facet = facetHolder.getFacet(NamedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NamedFacet);
        final NamedFacet namedFacet = (NamedFacet) facet;
        assertEquals("An Action With Remote Prefix", namedFacet.value());
    }

    public void testInstallsValidateMethodNoArgsFacetAndRemovesMethod() {
        class Customer {
            public void someAction() {}

            public String validateSomeAction() {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");
        final Method validateMethod = findMethod(Customer.class, "validateSomeAction");
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ActionValidationFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ActionValidationFacetViaMethod);
        final ActionValidationFacetViaMethod actionValidationFacetViaMethod = (ActionValidationFacetViaMethod) facet;
        assertEquals(validateMethod, actionValidationFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(validateMethod));
    }

    public void testInstallsValidateMethodSomeArgsFacetAndRemovesMethod() {
        class Customer {
            public void someAction(final int x, final int y) {}

            public String validateSomeAction(final int x, final int y) {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction", new Class[] { int.class, int.class });
        final Method validateMethod = findMethod(Customer.class, "validateSomeAction", new Class[] { int.class, int.class });
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ActionValidationFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ActionValidationFacetViaMethod);
        final ActionValidationFacetViaMethod actionValidationFacetViaMethod = (ActionValidationFacetViaMethod) facet;
        assertEquals(validateMethod, actionValidationFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(validateMethod));
    }

    public void testInstallsParameterDefaultsMethodSomeArgsFacetAndRemovesMethod() {
        class Customer {
            public void someAction(final int x, final Long y) {}

            public Object[] defaultSomeAction(final int x, final Long y) {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction", new Class[] { int.class, Long.class });
        final Method defaultMethod = findMethod(Customer.class, "defaultSomeAction", new Class[] { int.class, Long.class });
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ActionDefaultsFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ActionDefaultsFacetViaMethod);
        final ActionDefaultsFacetViaMethod actionDefaultFacetViaMethod = (ActionDefaultsFacetViaMethod) facet;
        assertEquals(defaultMethod, actionDefaultFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(defaultMethod));
    }

    public void testInstallsParameterDefaultsMethodNoArgsFacetAndRemovesMethod() {
        class Customer {
            public void someAction(final int x, final Long y) {}

            public Object[] defaultSomeAction() {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction", new Class[] { int.class, Long.class });
        final Method defaultMethod = findMethod(Customer.class, "defaultSomeAction", new Class[] {});
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ActionDefaultsFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ActionDefaultsFacetViaMethod);
        final ActionDefaultsFacetViaMethod actionDefaultFacetViaMethod = (ActionDefaultsFacetViaMethod) facet;
        assertEquals(defaultMethod, actionDefaultFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(defaultMethod));
    }

    public void testInstallsParameterChoicesMethodSomeArgsFacetAndRemovesMethod() {
        class Customer {
            public void someAction(final int x, final Long y) {}

            public Object[] choicesSomeAction(final int x, final Long y) {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction", new Class[] { int.class, Long.class });
        final Method choicesMethod = findMethod(Customer.class, "choicesSomeAction", new Class[] { int.class, Long.class });
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ActionChoicesFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ActionChoicesFacetViaMethod);
        final ActionChoicesFacetViaMethod actionChoicesFacetViaMethod = (ActionChoicesFacetViaMethod) facet;
        assertEquals(choicesMethod, actionChoicesFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(choicesMethod));
    }

    public void testInstallsParameterChoicesMethodNoArgsFacetAndRemovesMethod() {
        class Customer {
            public void someAction(final int x, final Long y) {}

            public Object[] choicesSomeAction() {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction", new Class[] { int.class, Long.class });
        final Method choicesMethod = findMethod(Customer.class, "choicesSomeAction", new Class[] {});
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ActionChoicesFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof ActionChoicesFacetViaMethod);
        final ActionChoicesFacetViaMethod actionChoicesFacetViaMethod = (ActionChoicesFacetViaMethod) facet;
        assertEquals(choicesMethod, actionChoicesFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(choicesMethod));
    }

    public static class CustomerStatic {
        public void someAction(final int x, final Long y) {}

        public static String nameSomeAction() {
            return "Another Name";
        }

        public static String descriptionSomeAction() {
            return "Some old description";
        }

        public static boolean alwaysHideSomeAction(final int x, final Long y) {
            return true;
        }

        public static boolean protectSomeAction(final int x, final Long y) {
            return true;
        }

        public static boolean hideSomeAction(final UserMemento userMemento) {
            return true;
        }

        public static String disableSomeAction(final UserMemento userMemento) {
            return "disabled for this user";
        }

        public static void otherAction(final int x, final Long y) {}

        public static boolean alwaysHideOtherAction(final int x, final Long y) {
            return false;
        }

        public static boolean protectOtherAction(final int x, final Long y) {
            return false;
        }
    }

    public void testInstallsNamedFacetUsingNameMethodAndRemovesMethod() {
        final Method actionMethod = findMethod(CustomerStatic.class, "someAction", new Class[] { int.class, Long.class });
        final Method nameMethod = findMethod(CustomerStatic.class, "nameSomeAction");
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(CustomerStatic.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(NamedFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof NamedFacetAbstract);
        final NamedFacetAbstract namedFacetAbstract = (NamedFacetAbstract) facet;
        assertEquals("Another Name", namedFacetAbstract.value());

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(nameMethod));
    }

    public void testInstallsDescribedAsFacetUsingDescriptionAndRemovesMethod() {
        final Method actionMethod = findMethod(CustomerStatic.class, "someAction", new Class[] { int.class, Long.class });
        final Method descriptionMethod = findMethod(CustomerStatic.class, "descriptionSomeAction");
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(CustomerStatic.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DescribedAsFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DescribedAsFacetAbstract);
        final DescribedAsFacetAbstract describedAsFacetAbstract = (DescribedAsFacetAbstract) facet;
        assertEquals("Some old description", describedAsFacetAbstract.value());

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(descriptionMethod));
    }

    public void testInstallsHiddenFacetUsingAlwaysHideAndRemovesMethod() {
        final Method actionMethod = findMethod(CustomerStatic.class, "someAction", new Class[] { int.class, Long.class });
        final Method alwaysHideMethod = findMethod(CustomerStatic.class, "alwaysHideSomeAction", new Class[] { int.class,
                Long.class });
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(CustomerStatic.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HiddenFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof HiddenFacetAbstract);
        final HiddenFacetAbstract hiddenFacetAbstract = (HiddenFacetAbstract) facet;
        assertEquals(When.ALWAYS, hiddenFacetAbstract.value());

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(alwaysHideMethod));
    }

    public void testInstallsHiddenFacetUsingAlwaysHideWhenNotAndRemovesMethod() {
        final Method actionMethod = findMethod(CustomerStatic.class, "otherAction", new Class[] { int.class, Long.class });
        final Method alwaysHideMethod = findMethod(CustomerStatic.class, "alwaysHideOtherAction", new Class[] { int.class,
                Long.class });
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(CustomerStatic.class, actionMethod, methodRemover, facetHolder);

        assertNull(facetHolder.getFacet(HiddenFacet.class));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(alwaysHideMethod));
    }

    public void testInstallsDisabledFacetUsingProtectAndRemovesMethod() {
        final Method actionMethod = findMethod(CustomerStatic.class, "someAction", new Class[] { int.class, Long.class });
        final Method protectMethod = findMethod(CustomerStatic.class, "protectSomeAction", new Class[] { int.class, Long.class });
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(CustomerStatic.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DisabledFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DisabledFacetAbstract);
        final DisabledFacetAbstract disabledFacetAbstract = (DisabledFacetAbstract) facet;
        assertEquals(When.ALWAYS, disabledFacetAbstract.value());

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(protectMethod));
    }

    public void testDoesNotInstallDisabledFacetUsingProtectWhenNotAndRemovesMethod() {
        final Method actionMethod = findMethod(CustomerStatic.class, "otherAction", new Class[] { int.class, Long.class });
        final Method protectMethod = findMethod(CustomerStatic.class, "protectOtherAction", new Class[] { int.class, Long.class });
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(CustomerStatic.class, actionMethod, methodRemover, facetHolder);

        assertNull(facetHolder.getFacet(DisabledFacet.class));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(protectMethod));
    }

    public void testInstallsHiddenForSessionFacetAndRemovesMethod() {
        final Method actionMethod = findMethod(CustomerStatic.class, "someAction", new Class[] { int.class, Long.class });
        final Method hideMethod = findMethod(CustomerStatic.class, "hideSomeAction", new Class[] { UserMemento.class });
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(CustomerStatic.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(HideForSessionFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof HideForSessionFacetViaMethod);
        final HideForSessionFacetViaMethod hideForSessionFacetViaMethod = (HideForSessionFacetViaMethod) facet;
        assertEquals(hideMethod, hideForSessionFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(hideMethod));
    }

    public void testInstallsDisabledForSessionFacetAndRemovesMethod() {
        final Method actionMethod = findMethod(CustomerStatic.class, "someAction", new Class[] { int.class, Long.class });
        final Method disableMethod = findMethod(CustomerStatic.class, "disableSomeAction", new Class[] { UserMemento.class });
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(CustomerStatic.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(DisableForSessionFacet.class);
        assertNotNull(facet);
        assertTrue(facet instanceof DisableForSessionFacetViaMethod);
        final DisableForSessionFacetViaMethod disableForSessionFacetViaMethod = (DisableForSessionFacetViaMethod) facet;
        assertEquals(disableMethod, disableForSessionFacetViaMethod.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(disableMethod));
    }

    public void testActionReturnTypeWhenVoid() {
        class Customer {
            public void someAction() {}
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ActionInvocationFacet.class);
        final ActionInvocationFacetViaMethod actionInvocationFacetViaMethod = (ActionInvocationFacetViaMethod) facet;
        assertEquals(voidNoSpec, actionInvocationFacetViaMethod.getReturnType());
    }

    public void testActionReturnTypeWhenNotVoid() {
        class Customer {
            public String someAction() {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");
        reflector.setLoadSpecificationStringReturn(stringNoSpec);

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ActionInvocationFacet.class);
        final ActionInvocationFacetViaMethod actionInvocationFacetViaMethod = (ActionInvocationFacetViaMethod) facet;
        assertEquals(stringNoSpec, actionInvocationFacetViaMethod.getReturnType());
    }

    public void testActionOnType() {
        class Customer {
            public String someAction() {
                return null;
            }
        }
        final Method actionMethod = findMethod(Customer.class, "someAction");
        reflector.setLoadSpecificationStringReturn(customerNoSpec);

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolder);

        final Facet facet = facetHolder.getFacet(ActionInvocationFacet.class);
        final ActionInvocationFacetViaMethod actionInvocationFacetViaMethod = (ActionInvocationFacetViaMethod) facet;
        assertEquals(customerNoSpec, actionInvocationFacetViaMethod.getOnType());
    }

    private static NakedObjectActionPeer createHolderWithParms() {
        JavaNakedObjectActionParamPeer param1 = new JavaNakedObjectActionParamPeer(
                new TestProxySpecification("java.lang.Integer"));
        JavaNakedObjectActionParamPeer param2 = new JavaNakedObjectActionParamPeer(new TestProxySpecification("java.lang.Long"));
        JavaNakedObjectActionParamPeer[] parms = new JavaNakedObjectActionParamPeer[] { param1, param2 };
        Identifier id = Identifier.classIdentifier("action");
        return new JavaNakedObjectActionPeer(id, parms);
    }

    public void testInstallsParameterDefaultsMethodAndRemovesMethod() {
        class Customer {
            public void someAction(final int x, final long y) {}

            public int default0SomeAction() {
                return 0;
            }

            public long default1SomeAction() {
                return 0;
            }
        }

        Method actionMethod = findMethod(Customer.class, "someAction", new Class[] { int.class, long.class });
        Method default0Method = findMethod(Customer.class, "default0SomeAction", new Class[] {});
        Method default1Method = findMethod(Customer.class, "default1SomeAction", new Class[] {});

        NakedObjectActionPeer facetHolderWithParms = createHolderWithParms();

        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolderWithParms);

        final Facet facet0 = facetHolderWithParms.getParameters()[0].getFacet(ActionParameterDefaultsFacet.class);
        assertNotNull(facet0);
        assertTrue(facet0 instanceof ActionParameterDefaultsFacetViaMethod);
        final ActionParameterDefaultsFacetViaMethod actionDefaultFacetViaMethod0 = (ActionParameterDefaultsFacetViaMethod) facet0;
        assertEquals(default0Method, actionDefaultFacetViaMethod0.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(default0Method));

        final Facet facet1 = facetHolderWithParms.getParameters()[1].getFacet(ActionParameterDefaultsFacet.class);
        assertNotNull(facet1);
        assertTrue(facet1 instanceof ActionParameterDefaultsFacetViaMethod);
        final ActionParameterDefaultsFacetViaMethod actionDefaultFacetViaMethod1 = (ActionParameterDefaultsFacetViaMethod) facet1;
        assertEquals(default1Method, actionDefaultFacetViaMethod1.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(default1Method));

    }

    public void testInstallsParameterChoicesMethodAndRemovesMethod() {
        class Customer {
            @SuppressWarnings("unused")
			public void someAction(final int x, final long y) {}

            @SuppressWarnings("unused")
			public List<Integer> choices0SomeAction() {
                return Collections.emptyList();
            }

            @SuppressWarnings("unused")
			public List<Long> choices1SomeAction() {
                return Collections.emptyList();
            }
        }

        Method actionMethod = findMethod(Customer.class, "someAction", new Class[] { int.class, long.class });
        Method choices0Method = findMethod(Customer.class, "choices0SomeAction", new Class[] {});
        Method choices1Method = findMethod(Customer.class, "choices1SomeAction", new Class[] {});

        NakedObjectActionPeer facetHolderWithParms = createHolderWithParms();

        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolderWithParms);

        final Facet facet0 = facetHolderWithParms.getParameters()[0].getFacet(ActionParameterChoicesFacet.class);
        assertNotNull(facet0);
        assertTrue(facet0 instanceof ActionParameterChoicesFacetViaMethod);
        final ActionParameterChoicesFacetViaMethod actionChoicesFacetViaMethod0 = (ActionParameterChoicesFacetViaMethod) facet0;
        assertEquals(choices0Method, actionChoicesFacetViaMethod0.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(choices0Method));

        final Facet facet1 = facetHolderWithParms.getParameters()[1].getFacet(ActionParameterChoicesFacet.class);
        assertNotNull(facet1);
        assertTrue(facet1 instanceof ActionParameterChoicesFacetViaMethod);
        final ActionParameterChoicesFacetViaMethod actionChoicesFacetViaMethod1 = (ActionParameterChoicesFacetViaMethod) facet1;
        assertEquals(choices1Method, actionChoicesFacetViaMethod1.getMethods().get(0));

        assertTrue(methodRemover.getRemoveMethodMethodCalls().contains(choices1Method));

    }
    

    public void testActionsPickedUpFromSuperclass() {
        class Customer {
            public void someAction(final int x, final long y) {}
        }
        
        class CustomerEx extends Customer {
        }

        Method actionMethod = findMethod(CustomerEx.class, "someAction", new Class[] { int.class, long.class });
 

        NakedObjectActionPeer facetHolderWithParms = createHolderWithParms();

        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(CustomerEx.class, actionMethod, methodRemover, facetHolderWithParms);

        final Facet facet0 = facetHolderWithParms.getFacet(ActionInvocationFacet.class);
        assertNotNull(facet0);
    }

    public void testActionsPickedUpFromSuperclassButHelpersFromSubClass() {
        class Customer {
            public void someAction(final int x, final long y) {}

            public int[] choices0SomeAction() {
                return new int[0];
            }
        }
        
        class CustomerEx extends Customer {
            @Override
            public int[] choices0SomeAction() {
                return new int[0];
            }

            public long[] choices1SomeAction() {
                return new long[0];
            }        
            
            public String disableSomeAction() {
                return null;
            }
        }

        Method actionMethod = findMethod(CustomerEx.class, "someAction", new Class[] { int.class, long.class });
        Method choices0Method = findMethod(CustomerEx.class, "choices0SomeAction", new Class[] {});
        Method choices1Method = findMethod(CustomerEx.class, "choices1SomeAction", new Class[] {});
        Method disableMethod = findMethod(CustomerEx.class, "disableSomeAction", new Class[] {});


        NakedObjectActionPeer facetHolderWithParms = createHolderWithParms();

        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        facetFactory.process(CustomerEx.class, actionMethod, methodRemover, facetHolderWithParms);

        final Facet facet0 = facetHolderWithParms.getFacet(ActionInvocationFacet.class);
        assertNotNull(facet0);
        
        final Facet facet1 = facetHolderWithParms.getParameters()[0].getFacet(ActionParameterChoicesFacet.class);
        assertNotNull(facet1);
        assertTrue(facet1 instanceof ActionParameterChoicesFacetViaMethod);
        final ActionParameterChoicesFacetViaMethod actionChoicesFacetViaMethod0 = (ActionParameterChoicesFacetViaMethod) facet1;
        assertEquals(choices0Method, actionChoicesFacetViaMethod0.getMethods().get(0));

        final Facet facet2 = facetHolderWithParms.getParameters()[1].getFacet(ActionParameterChoicesFacet.class);
        assertNotNull(facet2);
        assertTrue(facet2 instanceof ActionParameterChoicesFacetViaMethod);
        final ActionParameterChoicesFacetViaMethod actionChoicesFacetViaMethod1 = (ActionParameterChoicesFacetViaMethod) facet2;
        assertEquals(choices1Method, actionChoicesFacetViaMethod1.getMethods().get(0));
        
        //  facetHolder.getFacet(DisableForSessionFacet.class);
        
        final Facet facet3 = facetHolderWithParms.getFacet(DisableForContextFacet.class);
        assertNotNull(facet3);
        assertTrue(facet3 instanceof DisableForContextFacetViaMethod);
        final DisableForContextFacetViaMethod disableFacetViaMethod = (DisableForContextFacetViaMethod) facet3;
        assertEquals(disableMethod, disableFacetViaMethod.getMethods().get(0));

    }


    public void testBothChoicesMethodCausesException() {
        class Customer {
            public void someAction(final int x, final long y) {}

            public int[] choices0SomeAction() {
                return new int[0];
            }

            public long[] choices1SomeAction() {
                return new long[0];
            }

            public Object[] choicesSomeAction(final int x, final long y) {
                return null;
            }
        }

        Method actionMethod = findMethod(Customer.class, "someAction", new Class[] { int.class, long.class });
        NakedObjectActionPeer facetHolderWithParms = createHolderWithParms();
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        try {
            facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolderWithParms);
            fail("exception expected");
        } catch (org.nakedobjects.metamodel.exceptions.ReflectionException expected) {

        }
    }

    public void testBothDefaultMethodCausesException() {
        class Customer {
            public void someAction(final int x, final long y) {}

            public int default0SomeAction() {
                return 0;
            }

            public long default1SomeAction() {
                return 0;
            }

            public Object[] defaultSomeAction(final int x, final long y) {
                return null;
            }
        }

        Method actionMethod = findMethod(Customer.class, "someAction", new Class[] { int.class, long.class });
        NakedObjectActionPeer facetHolderWithParms = createHolderWithParms();
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        try {
            facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolderWithParms);
            fail("exception expected");
        } catch (org.nakedobjects.metamodel.exceptions.ReflectionException expected) {

        }
    }

    public void testBothDefaultChoicesMethodCausesException() {
        class Customer {
            public void someAction(final int x, final long y) {}

            public int default0SomeAction() {
                return 0;
            }

            public long default1SomeAction() {
                return 0;
            }

            public Object[] choicesSomeAction(final int x, final long y) {
                return null;
            }
        }

        Method actionMethod = findMethod(Customer.class, "someAction", new Class[] { int.class, long.class });
        NakedObjectActionPeer facetHolderWithParms = createHolderWithParms();
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        try {
            facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolderWithParms);
            fail("exception expected");
        } catch (org.nakedobjects.metamodel.exceptions.ReflectionException expected) {

        }
    }

    public void testBothChoicesDefaultMethodCausesException() {
        class Customer {
            public void someAction(final int x, final long y) {}

            public int[] choices0SomeAction() {
                return new int[0];
            }

            public long[] choices1SomeAction() {
                return new long[0];
            }

            public Object[] defaultSomeAction(final int x, final long y) {
                return null;
            }
        }

        Method actionMethod = findMethod(Customer.class, "someAction", new Class[] { int.class, long.class });
        NakedObjectActionPeer facetHolderWithParms = createHolderWithParms();
        reflector.setLoadSpecificationStringReturn(voidNoSpec);

        try {
            facetFactory.process(Customer.class, actionMethod, methodRemover, facetHolderWithParms);
            fail("exception expected");
        } catch (org.nakedobjects.metamodel.exceptions.ReflectionException expected) {

        }
    }

}

// Copyright (c) Naked Objects Group Ltd.
