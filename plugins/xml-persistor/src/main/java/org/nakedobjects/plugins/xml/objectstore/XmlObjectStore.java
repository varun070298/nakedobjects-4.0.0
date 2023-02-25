package org.nakedobjects.plugins.xml.objectstore;

import java.text.MessageFormat;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.plugins.xml.objectstore.internal.clock.Clock;
import org.nakedobjects.plugins.xml.objectstore.internal.commands.XmlCreateObjectCommand;
import org.nakedobjects.plugins.xml.objectstore.internal.commands.XmlDestroyObjectCommand;
import org.nakedobjects.plugins.xml.objectstore.internal.commands.XmlUpdateObjectCommand;
import org.nakedobjects.plugins.xml.objectstore.internal.data.CollectionData;
import org.nakedobjects.plugins.xml.objectstore.internal.data.Data;
import org.nakedobjects.plugins.xml.objectstore.internal.data.DataManager;
import org.nakedobjects.plugins.xml.objectstore.internal.data.ObjectData;
import org.nakedobjects.plugins.xml.objectstore.internal.data.ObjectDataVector;
import org.nakedobjects.plugins.xml.objectstore.internal.data.ReferenceVector;
import org.nakedobjects.plugins.xml.objectstore.internal.data.xml.XmlDataManager;
import org.nakedobjects.plugins.xml.objectstore.internal.data.xml.XmlFile;
import org.nakedobjects.plugins.xml.objectstore.internal.services.ServiceManager;
import org.nakedobjects.plugins.xml.objectstore.internal.services.xml.XmlServiceManager;
import org.nakedobjects.plugins.xml.objectstore.internal.version.FileVersion;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.ObjectNotFoundException;
import org.nakedobjects.runtime.persistence.PersistenceSession;
import org.nakedobjects.runtime.persistence.PersistorUtil;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;
import org.nakedobjects.runtime.persistence.objectstore.ObjectStore;
import org.nakedobjects.runtime.persistence.objectstore.transaction.CreateObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.DestroyObjectCommand;
import org.nakedobjects.runtime.persistence.objectstore.transaction.SaveObjectCommand;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SerialOid;
import org.nakedobjects.runtime.persistence.query.PersistenceQuery;
import org.nakedobjects.runtime.persistence.query.PersistenceQueryBuiltIn;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;
import org.nakedobjects.runtime.transaction.PersistenceCommand;


public class XmlObjectStore implements ObjectStore {
	
	private static final Logger LOG = Logger.getLogger(XmlObjectStore.class);
    private static final String XMLOS_DIR = ConfigurationConstants.ROOT + "xmlos.dir";
    private final DataManager dataManager;
    private final ServiceManager serviceManager;
    private boolean isFixturesInstalled;

    public XmlObjectStore(NakedObjectConfiguration configuration) {
        String directory = configuration.getString(XMLOS_DIR, "xml/objects");
        final XmlFile xmlFile = new XmlFile(configuration, directory);
        dataManager = new XmlDataManager(xmlFile);
        serviceManager = new XmlServiceManager(xmlFile);
        serviceManager.loadServices();
    }


    public XmlObjectStore(final DataManager dataManager, final ServiceManager serviceManager) {
        this.dataManager = dataManager;
        this.serviceManager = serviceManager;
        serviceManager.loadServices();
    }

    
    ///////////////////////////////////////////////////////////
    // name
    ///////////////////////////////////////////////////////////

    public String name() {
        return "XML";
    }

    ///////////////////////////////////////////////////////////
    // close
    ///////////////////////////////////////////////////////////

    public void close() {
        LOG.info("close " + this);
    }

    ///////////////////////////////////////////////////////////
    // reset
    ///////////////////////////////////////////////////////////

    public void reset() {}

    ///////////////////////////////////////////////////////////
    // init, shutdown, finalize
    ///////////////////////////////////////////////////////////

    
    public boolean hasInstances(final NakedObjectSpecification cls) {
        LOG.debug("checking instance of " + cls);
        final ObjectData data = new ObjectData(cls, null, null);
        return dataManager.numberOfInstances(data) > 0;
    }

    public void open() throws ObjectPersistenceException {
        isFixturesInstalled = dataManager.isFixturesInstalled();
    }

    public boolean isFixturesInstalled() {
        return isFixturesInstalled;
    }


    private void initObject(final NakedObject object, final ObjectData data) {
        if (object.getResolveState().canChangeTo(ResolveState.RESOLVING)) {
            PersistorUtil.start(object, ResolveState.RESOLVING);

            final NakedObjectAssociation[] fields = object.getSpecification().getAssociations();
            for (int i = 0; i < fields.length; i++) {
                final NakedObjectAssociation field = fields[i];
                if (field.isDerived()) {
                    continue;
                }

                final NakedObjectSpecification fieldSpecification = field.getSpecification();
                if (fieldSpecification.isEncodeable()) {
                    final EncodableFacet encoder = fieldSpecification.getFacet(EncodableFacet.class);
                    NakedObject value;
                    final String valueData = data.value(field.getId());
                    if (valueData == null || valueData.equals("NULL")) {
                        value = null;
                    } else {
                        value = encoder.fromEncodedString(valueData);
                    }
                    ((OneToOneAssociation) field).initAssociation(object, value);
                } else if (field.isOneToManyAssociation()) {
                    initObjectSetupCollection(object, data, field);
                } else if (field.isOneToOneAssociation()) {
                    initObjectSetupReference(object, data, field);
                }
            }
            object.setOptimisticLock(data.getVersion());
            PersistorUtil.end(object);
        }
    }

    private void initObjectSetupReference(final NakedObject object, final ObjectData data, final NakedObjectAssociation field) {
        final SerialOid referenceOid = (SerialOid) data.get(field.getId());
        LOG.debug("setting up field " + field + " with " + referenceOid);
        if (referenceOid == null) {
            return;
        }

        final Data fieldData = dataManager.loadData(referenceOid);

        if (fieldData == null) {
            final NakedObject adapter = getPersistenceSession().recreateAdapter(referenceOid, field.getSpecification());
            if (!adapter.getResolveState().isDestroyed()) {
                adapter.changeState(ResolveState.DESTROYED);
            }
            ((OneToOneAssociation) field).initAssociation(object, adapter);

            LOG.warn("No data found for " + referenceOid + " so field '" + field.getName() + "' not set in object '"
                    + object.titleString() + "'");
        } else {
            final NakedObject reference = getPersistenceSession().recreateAdapter(referenceOid, specFor(fieldData));
            ((OneToOneAssociation) field).initAssociation(object, reference);
        }

        /*
         * if (loadedObjects().isLoaded(referenceOid)) { NakedObject loadedObject =
         * loadedObjects().getLoadedObject(referenceOid); LOG.debug("using loaded object " + loadedObject);
         * object.initAssociation((OneToOneAssociation) field, loadedObject); } else { NakedObject
         * fieldObject; Data fieldData = (Data) dataManager.loadData((SerialOid) referenceOid);
         * 
         * if (fieldData != null) { fieldObject = (NakedObject) specFor(fieldData).acquireInstance(); } else {
         * fieldObject = (NakedObject) field.getSpecification().acquireInstance(); }
         * 
         * fieldObject.setOid(referenceOid);
         * 
         * if (fieldObject instanceof NakedCollection) { fieldObject.setResolved(); }
         * 
         * loadedObjects().loaded(fieldObject); object.initAssociation((OneToOneAssociation) field,
         * fieldObject); }
         */
    }

    private void initObjectSetupCollection(final NakedObject object, final ObjectData data, final NakedObjectAssociation field) {
        /*
         * The internal collection is already a part of the object, and therefore cannot be recreated, but its
         * oid must be set
         */
        final ReferenceVector refs = (ReferenceVector) data.get(field.getId());
        final NakedObject collection = field.get(object);
        if (collection.getResolveState().canChangeTo(ResolveState.RESOLVING)) {
            PersistorUtil.start(collection, ResolveState.RESOLVING);
            final int size = refs == null ? 0 : refs.size();
            final NakedObject[] elements = new NakedObject[size];
            for (int j = 0; j < size; j++) {
                final SerialOid elementOid = refs.elementAt(j);
                NakedObject adapter;
                adapter = getAdapterManager().getAdapterFor(elementOid);
                if (adapter == null) {
                    adapter = getObject(elementOid, null);
                }
                elements[j] = adapter;
            }
            final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
            facet.init(collection, elements);
            PersistorUtil.end(collection);
        }
    }



    ///////////////////////////////////////////////////////////
    // Transaction Management
    ///////////////////////////////////////////////////////////

    public void startTransaction() {
        LOG.debug("start transaction");
    }

    public void endTransaction() {
        LOG.debug("end transaction");
    }


    public void abortTransaction() {
        LOG.debug("transaction aborted");
    }


    ///////////////////////////////////////////////////////////
    // createXxxCommands
    ///////////////////////////////////////////////////////////

    public CreateObjectCommand createCreateObjectCommand(final NakedObject object) {
        return new XmlCreateObjectCommand(object, dataManager);
    }

    public SaveObjectCommand createSaveObjectCommand(final NakedObject object) {
        return new XmlUpdateObjectCommand(object, dataManager);
    }

    public DestroyObjectCommand createDestroyObjectCommand(final NakedObject object) {
        return new XmlDestroyObjectCommand(object, dataManager);
    }


    ///////////////////////////////////////////////////////////
    // execute, flush
    ///////////////////////////////////////////////////////////

    public void execute(final List<PersistenceCommand> commands) {
        LOG.debug("start execution of transaction");
        for (PersistenceCommand command: commands) {
            command.execute(null);
        }
        LOG.debug("end execution");
    }

    ///////////////////////////////////////////////////////////
    // getObject, resolveImmediately, resolveField
    ///////////////////////////////////////////////////////////

    public NakedObject getObject(final Oid oid, final NakedObjectSpecification hint) {
        LOG.debug("getObject " + oid);
        final Data data = dataManager.loadData((SerialOid) oid);
        LOG.debug("  data read " + data);

        NakedObject object;

        if (data instanceof ObjectData) {
            object = recreateObject((ObjectData) data);
        } else if (data instanceof CollectionData) {
            throw new NakedObjectException();
        } else {
            throw new ObjectNotFoundException(oid);
        }
        return object;
    }

    public void resolveField(final NakedObject object, final NakedObjectAssociation field) {
        final NakedObject reference = field.get(object);
        resolveImmediately(reference);
    }

    public void resolveImmediately(final NakedObject object) {
        final ObjectData data = (ObjectData) dataManager.loadData((SerialOid) object.getOid());
        Assert.assertNotNull("Not able to read in data during resolve", object, data);
        initObject(object, data);
    }

    /*
     * The ObjectData holds all references for internal collections, so the object should haves its internal
     * collection populated by this method.
     */
    private NakedObject recreateObject(final ObjectData data) {
        final SerialOid oid = data.getOid();
        final NakedObjectSpecification spec = specFor(data);
        final NakedObject object = getPersistenceSession().recreateAdapter(oid, spec);
        initObject(object, data);
        return object;
    }


    ///////////////////////////////////////////////////////////
    // getInstances, allInstances
    ///////////////////////////////////////////////////////////

    public NakedObject[] getInstances(final PersistenceQuery persistenceQuery) {
    	
    	if (!(persistenceQuery instanceof PersistenceQueryBuiltIn)) {
    		throw new IllegalArgumentException(MessageFormat.format(
							"Provided PersistenceQuery not supported; was {0}; " +
							"the XML object store only supports {1}",
							persistenceQuery.getClass().getName(), 
							PersistenceQueryBuiltIn.class.getName()));
    	}
		PersistenceQueryBuiltIn builtIn = (PersistenceQueryBuiltIn) persistenceQuery;
    	
        LOG.debug("getInstances of " + builtIn.getSpecification() + " where " + builtIn);
        final ObjectData patternData = new ObjectData(builtIn.getSpecification(), null, null);
        final NakedObject[] instances = getInstances(patternData, builtIn);
        return instances;
    }

    private NakedObject[] getInstances(
    		final ObjectData patternData, 
    		final PersistenceQueryBuiltIn persistenceQuery) {
        final ObjectDataVector data = dataManager.getInstances(patternData);
        final NakedObject[] instances = new NakedObject[data.size()];
        int count = 0;

        for (int i = 0; i < data.size(); i++) {
            final ObjectData instanceData = data.element(i);
            LOG.debug("instance data " + instanceData);

            final SerialOid oid = instanceData.getOid();

            final NakedObjectSpecification spec = specFor(instanceData);
            final NakedObject instance = getPersistenceSession().recreateAdapter(oid, spec);
            initObject(instance, instanceData);

            if (persistenceQuery == null || 
            	persistenceQuery.matches(instance)) {
                instances[count++] = instance;
            }
        }

        final NakedObject[] array = new NakedObject[count];
        System.arraycopy(instances, 0, array, 0, count);
        return array;
    }


    private NakedObjectSpecification specFor(final Data data) {
        return getSpecificationLoader().loadSpecification(data.getTypeName());
    }


    ///////////////////////////////////////////////////////////
    // services
    ///////////////////////////////////////////////////////////

    public Oid getOidForService(final String name) {
        return serviceManager.getOidForService(name);
    }

    public void registerService(final String name, final Oid oid) {
        serviceManager.registerService(name, oid);
    }

    ///////////////////////////////////////////////////////////
    // debugging
    ///////////////////////////////////////////////////////////

    public void debugData(final DebugString debug) {
        debug.appendTitle("Business Objects");
        debug.appendln(dataManager.getDebugData());
    }

    public String debugTitle() {
        return "XML Object Store";
    }




    ///////////////////////////////////////////////////////////
    // Dependencies (injected)
    ///////////////////////////////////////////////////////////

    /**
     * Set the clock used to generate sequence numbers and last changed dates for version objects.
     */
    public void setClock(final Clock clock) {
        FileVersion.setClock(clock);
    }

    ///////////////////////////////////////////////////////////
    // Dependencies (from singleton)
    ///////////////////////////////////////////////////////////

    protected static SpecificationLoader getSpecificationLoader() {
        return NakedObjectsContext.getSpecificationLoader();
    }

    private static AdapterManager getAdapterManager() {
        return getPersistenceSession().getAdapterManager();
    }

	protected static PersistenceSession getPersistenceSession() {
		return NakedObjectsContext.getPersistenceSession();
	}


}
// Copyright (c) Naked Objects Group Ltd.
