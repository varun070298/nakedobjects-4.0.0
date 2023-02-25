package org.nakedobjects.plugins.hibernate.objectstore.tools.internal;

/**
 * TODO combine with other versions of TestSystem
 */
public class TestSystem {

    // private NakedObjectConfiguration configuration;
    // private NakedObjectsContext nakedObjects;
    // private TestObjectLoader objectLoader;
    // private NakedObjectPersistor objectPersistor;
    // private NakedObjectReflector specificationLoader;
    //
    // public TestSystem() {
    // }
    //
    // public void addAdapter(final Object object, final DummyNakedObject adapter) {
    // objectLoader.addAdapter(object, adapter);
    // }
    //
    // public void addConfiguration(String name, String value) {
    // configuration.add(name, value);
    // }
    //
    // public void addLoadedIdentity(final DummyOid oid, final NakedObject adapter) {
    // objectLoader.addIdentity(oid, adapter);
    // }
    //
    // // public void addNakedCollectionAdapter(final NakedCollection collection) {
    // // objectLoader.addAdapter(collection.getObject(), collection);
    // // }
    //
    // public void addRecreated(final DummyOid oid, final DummyNakedObject adapter) {
    // objectLoader.addRecreated(oid, adapter);
    //
    // }
    //
    // public void addRecreatedTransient(final DummyNakedObject adapter) {
    // objectLoader.addRecreatedTransient(adapter);
    //
    // }
    //
    // public void addSpecification(final String name) {
    // specificationLoader.loadSpecification(name);
    // }
    //
    // public void addValue(final Object object, final EntryTextParser adapter) {
    // objectLoader.addAdapter(object, adapter);
    //
    // }
    //
    // public NakedObject createAdapterForTransient(final Object associate) {
    // NakedObject createAdapterForTransient = PersistorUtil.createAdapterForTransient(associate, false);
    // objectLoader.addAdapter(associate, createAdapterForTransient);
    // return createAdapterForTransient;
    // }
    //
    // public void init() {
    // nakedObjects = StaticContext.createInstance();
    // configuration = new PropertiesConfiguration();
    // NakedObjectsContext.setConfiguration(configuration);
    //        
    // specificationLoader = new JavaReflector();
    // // ((JavaReflector) specificationLoader).setReflectionPeerFactories(new ReflectionPeerFactory[0]);
    // nakedObjects.setReflector(specificationLoader);
    // objectLoader = new TestObjectLoader();
    // nakedObjects.setObjectLoader(objectLoader);
    // objectPersistor = new TestPersistor();
    // nakedObjects.setObjectPersistor(objectPersistor);
    // nakedObjects.setSession(new TestProxySession());
    //
    // specificationLoader.init();
    // objectLoader.init();
    // objectPersistor.init();
    // }
    //
    // public void setObjectPersistor(final NakedObjectPersistor objectManager) {
    // this.objectPersistor = objectManager;
    // }
    //
    // public void setupLoadedObject(final Object forObject, final NakedObject adapter) {
    // ((TestObjectLoader) objectLoader).addAdapter(forObject, adapter);
    // }
    //
    // public void shutdown() {
    // NakedObjectsContext.closeSession();
    // }
}
// Copyright (c) Naked Objects Group Ltd.
