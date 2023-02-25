package org.nakedobjects.plugins.sql.objectstore;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.factory.InstanceCreationException;
import org.nakedobjects.metamodel.commons.factory.InstanceFactory;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.transaction.ObjectPersistenceException;


public class ObjectMappingLookup {
    private static final Logger LOG = Logger.getLogger(ObjectMappingLookup.class);
    private DatabaseConnectorPool connectionPool;
    private final Map<NakedObjectSpecification, ObjectMapping> mappings = new HashMap<NakedObjectSpecification, ObjectMapping>();
    private ObjectMappingFactory objectMappingFactory;
    private FieldMappingLookup fieldMappingLookup;

    public ObjectMapping getMapping(final NakedObjectSpecification spec) {
        ObjectMapping mapping = mappings.get(spec);
        if (mapping == null) {
            String propertiesBase = SqlObjectStore.BASE_NAME + ".automapper.default";
            mapping = (ObjectMapping) objectMappingFactory.createMapper(spec.getFullName(), propertiesBase, fieldMappingLookup);
            add(spec, mapping);
        }
        LOG.debug("  mapper for " + spec.getSingularName() + " -> " + mapping);
        if (mapping == null) {
            throw new NakedObjectException("No mapper for " + spec + " (no default mapper)");
        }
        return mapping;
    }

    public ObjectMapping getMapping(final NakedObject object) {
        return getMapping(object.getSpecification());
    }

    public void setConnectionPool(final DatabaseConnectorPool connectionPool) {
        this.connectionPool = connectionPool;
    }

    // / ???
    public void setObjectMappingFactory(final ObjectMappingFactory mapperFactory) {
        this.objectMappingFactory = mapperFactory;
    }

    public void setValueMappingLookup(FieldMappingLookup fieldMappingLookup) {
        this.fieldMappingLookup = fieldMappingLookup;
    }

    private void add(final String className, final ObjectMapping mapper) {
        NakedObjectSpecification spec = NakedObjectsContext.getSpecificationLoader().loadSpecification(className);
        if (spec.getPropertyList().size() == 0) {
            throw new SqlObjectStoreException(spec.getFullName() + " has no fields to persist: " + spec);
        }
        add(spec, mapper);
    }

    public void add(final NakedObjectSpecification specification, final ObjectMapping mapper) {
        LOG.debug("add mapper " + mapper + " for " + specification);
        DatabaseConnector connection = connectionPool.acquire();
        mapper.startup(connection, this);
        connectionPool.release(connection);
        mappings.put(specification, mapper);
    }

    public void init() {
        fieldMappingLookup.init();
        
        String prefix = SqlObjectStore.BASE_NAME + ".mapper.";
        NakedObjectConfiguration subset = NakedObjectsContext.getConfiguration().createSubset(prefix);
        Enumeration e = subset.propertyNames();
        while (e.hasMoreElements()) {
            String className = (String) e.nextElement();
            String value = subset.getString(className);

            if (value.startsWith("auto.")) {
                String propertiesBase = SqlObjectStore.BASE_NAME + ".automapper." + value.substring(5) + ".";
                add(className, objectMappingFactory.createMapper(className, propertiesBase, fieldMappingLookup));
            } else if (value.trim().equals("auto")) {
                String propertiesBase = SqlObjectStore.BASE_NAME + ".automapper.default";
                add(className, objectMappingFactory.createMapper(className, propertiesBase, fieldMappingLookup));
            } else {
                LOG.debug("mapper " + className + "=" + value);

                try {
                    add(className, InstanceFactory.createInstance(value, ObjectMapping.class));
                } catch (ObjectPersistenceException ex) {
                    throw new InstanceCreationException("Failed to set up mapper for " + className, ex);
                }
            }
        }
    }

    public void shutdown() {
        for (ObjectMapping mapping : mappings.values()) {
            try {
                mapping.shutdown();
            } catch (ObjectPersistenceException ex) {
                LOG.error("Shutdown mapper " + mapping, ex);
            }
        }
    }

}
// Copyright (c) Naked Objects Group Ltd.
