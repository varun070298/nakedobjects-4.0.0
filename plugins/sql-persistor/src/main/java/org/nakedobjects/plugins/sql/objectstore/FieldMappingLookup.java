package org.nakedobjects.plugins.sql.objectstore;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMapping;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMappingFactory;
import org.nakedobjects.plugins.sql.objectstore.mapping.ObjectReferenceMapping;
import org.nakedobjects.plugins.sql.objectstore.mapping.ObjectReferenceMappingFactory;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class FieldMappingLookup {
    private static final Logger LOG = Logger.getLogger(FieldMappingLookup.class);
    private final Map<NakedObjectSpecification, FieldMappingFactory> fieldMappings = new HashMap<NakedObjectSpecification, FieldMappingFactory>();
    private final Map<NakedObjectSpecification, ObjectReferenceMappingFactory> referenceMappings = new HashMap<NakedObjectSpecification, ObjectReferenceMappingFactory>();
    private FieldMappingFactory referenceFieldMappingfactory;
    private ObjectReferenceMappingFactory objectReferenceMappingfactory;

    public FieldMapping createMapping(NakedObjectAssociation field) {
        NakedObjectSpecification spec = field.getSpecification();
        FieldMappingFactory factory = fieldMappings.get(spec);
        if (factory != null) {
            return factory.createFieldMapping(field);
        } else if (spec.isEncodeable()) {
            // TODO add generic encodeable mapping
            throw new NotYetImplementedException();
        } else if (true /* TODO test for reference */) {
            factory = referenceFieldMappingfactory;
            addFieldMappingFactory(spec, factory);
            return factory.createFieldMapping(field);
        } else {
            throw new NakedObjectException("No mapper for " + spec + " (no default mapper)");
        }
    }

    public ObjectReferenceMapping createMapping(NakedObjectSpecification spec) {
        ObjectReferenceMappingFactory factory = referenceMappings.get(spec);
        if (factory != null) {
            return factory.createReferenceMapping(spec);
        } else if (spec.isEncodeable()) {
            // TODO add generic encodeable mapping
            throw new NotYetImplementedException();
        } else if (true /* TODO test for reference */) {
            factory = objectReferenceMappingfactory;
         //   add(spec, factory);
            return factory.createReferenceMapping(spec);
        } else {
            throw new NakedObjectException("No mapper for " + spec + " (no default mapper)");
        }
        
    }
    
    public void addFieldMappingFactory(final Class valueType, final FieldMappingFactory mapper) {
        NakedObjectSpecification spec = NakedObjectsContext.getSpecificationLoader().loadSpecification(valueType);
        addFieldMappingFactory(spec, mapper);
    }

    private void addFieldMappingFactory(final NakedObjectSpecification specification, final FieldMappingFactory mapper) {
        LOG.debug("add mapper " + mapper + " for " + specification);
        fieldMappings.put(specification, mapper);
    }

    public void addReferenceMappingFactory(final NakedObjectSpecification specification, final ObjectReferenceMappingFactory mapper) {
       LOG.debug("add mapper " + mapper + " for " + specification);
       referenceMappings.put(specification, mapper);
    }

    public void init() {
    // fieldMappingFactory.load(this);
    }

    public IdMapping createIdMapping() {
        // TODO inject and use external factory
        IdMapping idMapping = new IdMapping();
        idMapping.init();
        return idMapping;
    }

    public VersionMapping createVersionMapping() {
        // TODO inject and use external factory
        VersionMapping versionMapping = new VersionMapping();
        versionMapping.init();
        return versionMapping;
    }

    public void setReferenceFieldMappingFactory(FieldMappingFactory referenceMappingfactory) {
        this.referenceFieldMappingfactory = referenceMappingfactory;
    }

    public void setObjectReferenceMappingfactory(ObjectReferenceMappingFactory objectReferenceMappingfactory) {
        this.objectReferenceMappingfactory = objectReferenceMappingfactory;
    }
}
// Copyright (c) Naked Objects Group Ltd.
