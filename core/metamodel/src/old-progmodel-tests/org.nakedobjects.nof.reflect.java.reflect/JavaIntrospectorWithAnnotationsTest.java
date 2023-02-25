package org.nakedobjects.progmodel.java5.reflect;

import java.util.Enumeration;

import junit.framework.TestSuite;

import org.nakedobjects.noa.adapter.NakedObject;
import org.nakedobjects.noa.adapter.ResolveState;
import org.nakedobjects.noa.facets.Facet;
import org.nakedobjects.metamodel.facets.When;
import org.nakedobjects.metamodel.facets.actions.debug.DebugFacet;
import org.nakedobjects.metamodel.facets.actions.executed.ExecutedFacet;
import org.nakedobjects.metamodel.facets.actions.executed.ExecutedFacet.Where;
import org.nakedobjects.metamodel.facets.actions.exploration.ExplorationFacet;
import org.nakedobjects.metamodel.facets.collections.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.facets.disable.DisabledFacet;
import org.nakedobjects.metamodel.facets.hide.HiddenFacet;
import org.nakedobjects.metamodel.facets.naming.describedas.DescribedAsFacet;
import org.nakedobjects.metamodel.facets.naming.named.NamedFacet;
import org.nakedobjects.metamodel.facets.object.bounded.BoundedFacet;
import org.nakedobjects.metamodel.facets.object.immutable.ImmutableFacet;
import org.nakedobjects.metamodel.facets.object.plural.PluralFacet;
import org.nakedobjects.metamodel.facets.ordering.actionorder.ActionOrderFacet;
import org.nakedobjects.metamodel.facets.ordering.fieldorder.FieldOrderFacet;
import org.nakedobjects.metamodel.facets.propcoll.notpersisted.NotPersistedFacet;
import org.nakedobjects.metamodel.facets.propparam.multiline.MultiLineFacet;
import org.nakedobjects.metamodel.facets.propparam.validate.mandatory.OptionalFacet;
import org.nakedobjects.progmodel.java5.annotations.JavaObjectWithAnnotations;
import org.nakedobjects.progmodel.java5.facets.ordering.OrderSet;
import org.nakedobjects.progmodel.java5.reflect.actions.JavaAction;
import org.nakedobjects.progmodel.java5.reflect.propcoll.JavaField;
import org.nakedobjects.nof.reflect.peer.ActionParamPeer;
import org.nakedobjects.nof.testsystem.ProxyTestCase;
import org.nakedobjects.testing.DummyNakedObject;
import org.nakedobjects.testing.TestSpecification;


public class JavaIntrospectorWithAnnotationsTest extends ProxyTestCase {

    public static void main(final String[] args) {
        junit.textui.TestRunner.run(new TestSuite(JavaIntrospectorWithAnnotationsTest.class));
    }

    private JavaIntrospector introspector;
    private NakedObject adapter;
    private JavaObjectWithBasicProgramConventions target;
    private TestFacetHolder facetHolder;

    private JavaAction findAction(String name) {
        OrderSet objectActions = introspector.getObjectActions();
        return findAction(name, objectActions);
    }

    private JavaAction findClassAction(String name) {
        OrderSet objectActions = introspector.getClassActions();
        return findAction(name, objectActions);
    }

    private JavaAction findAction(String name, OrderSet objectActions) {
        Enumeration elements = objectActions.elements();
        while (elements.hasMoreElements()) {
            JavaAction action = (JavaAction) elements.nextElement();
            if (action.getIdentifier().getName().equals(name)) {
                return action;
            }
        }
        fail("action not found: " + name);
        return null;
    }
    
    private JavaField findField(String name) {
        OrderSet fields = introspector.getFields();
        Enumeration elements = fields.elements();
        while (elements.hasMoreElements()) {
            JavaField field = (JavaField) elements.nextElement();
            if (field.getIdentifier().getName().equals(name)) {
                return field;
            }
        }
        fail("action not found: " + name);
        return null;
    }

    protected void setUp() throws Exception {
        super.setUp();

        facetHolder = new TestFacetHolder();
        introspector = new JavaIntrospector(JavaObjectWithAnnotations.class, null, new DummyBuilder(), new JavaReflector(), facetHolder);
        introspector.introspect();
        
        target = new JavaObjectWithBasicProgramConventions();
        adapter = new DummyNakedObject(target, ResolveState.RESOLVED);
        ((DummyNakedObject) adapter).setupSpecification(new TestSpecification());
    }

    
    public void testBounded() throws Exception {
        Facet facet = facetHolder.getFacet(BoundedFacet.class);
        assertNotNull(facet);
    }

    

    public void testMethodNotDebugByDefault() throws Exception {
        JavaAction action = findAction("stop");
        Facet facet = action.getFacet(DebugFacet.class);
        assertNull(facet);
    }
    
    public void testDebugType() throws Exception {
        JavaAction action = findAction("start");
        Facet facet = action.getFacet(DebugFacet.class);
        assertNotNull(facet);
    }

    public void testMethodNotExplorationByDefault() throws Exception {
        JavaAction action = findAction("stop");
        Facet facet = action.getFacet(ExplorationFacet.class);
        assertNull(facet);
    }
    
    public void testExplorationType() throws Exception {
        JavaAction action = findAction("top");
        Facet facet = action.getFacet(ExplorationFacet.class);
        assertNotNull(facet);
    }
    
    public void testFieldNotPersisted() throws Exception {
        JavaField action = findField("one");
        Facet facet = action.getFacet(NotPersistedFacet.class);
        assertNotNull(facet);
    } 
    
    public void testMethodHidden() throws Exception {
        JavaAction action = findAction("stop");
        Facet facet = action.getFacet(HiddenFacet.class);
        assertNotNull(facet);
    }
    
    public void testImmutableOncePersisted() throws Exception {
        ImmutableFacet facet = (ImmutableFacet)facetHolder.getFacet(ImmutableFacet.class);
        assertEquals(When.ONCE_PERSISTED, facet.value());
    }

    public void testExecutionDefault() throws Exception {
        JavaAction action = findAction("start");
        ExecutedFacet facet = (ExecutedFacet) action.getFacet(ExecutedFacet.class);
        assertEquals(Where.DEFAULT, facet.value());
    }

    public void testLocal() throws Exception {
        JavaAction action = findAction("left");
        ExecutedFacet facet = (ExecutedFacet) action.getFacet(ExecutedFacet.class);
        assertEquals(Where.LOCALLY, facet.value());
    }

    public void testRemote() throws Exception {
        JavaAction action = findAction("right");
        ExecutedFacet facet = (ExecutedFacet) action.getFacet(ExecutedFacet.class);
        assertEquals(Where.REMOTELY, facet.value());
    }
    
    public void testActionNotDisableByDefault() throws Exception {
        JavaAction action = findAction("start");
        DisabledFacet facet = (DisabledFacet) action.getFacet(DisabledFacet.class);
        assertEquals(When.NEVER, facet.value());
    }
        
    public void testActionDisabled() throws Exception {
        JavaAction action = findAction("bottom");
        DisabledFacet facet = (DisabledFacet) action.getFacet(DisabledFacet.class);
        assertEquals(When.ALWAYS, facet.value());
    }

    public void testClassActionOrder() throws Exception {
        ActionOrderFacet facet = (ActionOrderFacet)facetHolder.getFacet(ActionOrderFacet.class);
        assertEquals("1, 2, 3", facet.value());
    }
    
    public void testCollectionType() throws Exception {
        JavaField field = findField("collection");
        TypeOfFacet facet =  (TypeOfFacet) field.getFacet(TypeOfFacet.class);
        assertEquals(Long.class, facet.value());
    }

    public void testFieldOrder() throws Exception {
        FieldOrderFacet facet = (FieldOrderFacet)facetHolder.getFacet(FieldOrderFacet.class);
        assertEquals("4, 5, 6", facet.value());
    }

    public void testFieldDescription() throws Exception {
        JavaField field = findField("collection");
        DescribedAsFacet facet =  (DescribedAsFacet) field.getFacet(DescribedAsFacet.class);
        assertEquals("description text", facet.value());
    }

    public void testMemberName() throws Exception {
        JavaField field = findField("collection");
        NamedFacet facet =  (NamedFacet) field.getFacet(NamedFacet.class);
        assertEquals("name text", facet.value());
    }

    public void testParameterNamesDefault() throws Exception {
        JavaAction action = findAction("complete");
        ActionParamPeer actionParamPeer = action.getParameters()[0];
        NamedFacet facet = (NamedFacet) actionParamPeer.getFacet(NamedFacet.class);
        assertEquals(null, facet.value());
    }
    
    public void testParameterNames() throws Exception {
        JavaAction action = findAction("side");
        ActionParamPeer actionParamPeer = action.getParameters()[0];
        NamedFacet facet = (NamedFacet) actionParamPeer.getFacet(NamedFacet.class);
        assertEquals("one", facet.value());
   }
    
    public void testParameterMandatoryByDefault() throws Exception {
        JavaAction action = findAction("complete");
        ActionParamPeer actionParamPeer = action.getParameters()[0];
        OptionalFacet facet = (OptionalFacet) actionParamPeer.getFacet(OptionalFacet.class);
        assertNull(facet);
    }

    public void testOptionalParameter() throws Exception {
        JavaAction action = findAction("side");
        ActionParamPeer actionParamPeer = action.getParameters()[0];
        OptionalFacet facet = (OptionalFacet) actionParamPeer.getFacet(OptionalFacet.class);
        assertNotNull(facet);
    }

    public void testSingularName() throws Exception {
        NamedFacet facet = (NamedFacet)facetHolder.getFacet(NamedFacet.class);
        assertEquals("singular name", facet.value());
    }

    public void testPluralName() throws Exception {
        PluralFacet facet = (PluralFacet)facetHolder.getFacet(PluralFacet.class);
        assertEquals("plural name", facet.value());
    }
    
    public void testDelfault1LineForParameter() throws Exception {
        JavaAction action = findAction("complete");
        ActionParamPeer actionParamPeer = action.getParameters()[0];
        MultiLineFacet facet = (MultiLineFacet) actionParamPeer.getFacet(MultiLineFacet.class);
        assertEquals(1, facet.numberOfLines());
    }
    
    public void testMultiLineForActionParamIfNot() throws Exception {
        JavaAction action = findAction("complete");
        ActionParamPeer actionParamPeer = action.getParameters()[1];
        MultiLineFacet facet = (MultiLineFacet) actionParamPeer.getFacet(MultiLineFacet.class);
        assertEquals(10, facet.numberOfLines());
    }

    
}
// Copyright (c) Naked Objects Group Ltd.
