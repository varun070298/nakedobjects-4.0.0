package org.nakedobjects.plugins.sql.objectstore.auto;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.oid.Oid;
import org.nakedobjects.metamodel.adapter.version.SerialNumberVersion;
import org.nakedobjects.metamodel.adapter.version.Version;
import org.nakedobjects.metamodel.commons.exceptions.NotYetImplementedException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.sql.objectstore.AbstractMapper;
import org.nakedobjects.plugins.sql.objectstore.CollectionMapper;
import org.nakedobjects.plugins.sql.objectstore.DatabaseConnector;
import org.nakedobjects.plugins.sql.objectstore.FieldMappingLookup;
import org.nakedobjects.plugins.sql.objectstore.SqlObjectStoreException;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMapping;
import org.nakedobjects.runtime.context.NakedObjectsContext;
import org.nakedobjects.runtime.persistence.adaptermanager.AdapterManager;


public abstract class AbstractAutoMapper extends AbstractMapper {
    private static final Logger LOG = Logger.getLogger(AbstractAutoMapper.class);
    protected CollectionMapper collectionMappers[];
    protected boolean dbCreatesId;

    protected NakedObjectSpecification specification;
    protected String table;
    protected List<FieldMapping> fieldMappings = new ArrayList<FieldMapping>();

    protected AbstractAutoMapper(final String nakedClassName, final String parameterBase, FieldMappingLookup lookup) {
        specification = NakedObjectsContext.getSpecificationLoader().loadSpecification(nakedClassName);
        if (specification.getPropertyList() == null || specification.getPropertyList().size() == 0) {
            throw new SqlObjectStoreException(specification.getFullName() + " has no fields: " + specification);
        }
        setUpFieldMappers(lookup, nakedClassName, parameterBase);
    }

    private void setUpFieldMappers(FieldMappingLookup lookup, final String nakedClassName, final String parameterBase) {
        NakedObjectConfiguration configParameters = NakedObjectsContext.getConfiguration();
        table = configParameters.getString(parameterBase + "table");
        if (table == null) {
            table = asSqlName(nakedClassName.substring(nakedClassName.lastIndexOf('.') + 1).toUpperCase());
        }

        dbCreatesId = configParameters.getBoolean(parameterBase + "db-ids", false);
        if (configParameters.getBoolean(parameterBase + "all-fields", true)) {
            setupFullMapping(lookup, nakedClassName, configParameters, parameterBase);
        } else {
   //         setupSpecifiedMapping(specification, configParameters, parameterBase);
        }

        LOG.info("table mapping: " + table + " (" + columnList() + ")");
    }

    private void setupFullMapping(
            final FieldMappingLookup lookup,
            final String nakedClassName,
            final NakedObjectConfiguration configParameters,
            final String parameterBase) {
        List<? extends NakedObjectAssociation> fields = specification.getAssociationList();

        int simpleFieldCount = 0;
        int collectionFieldCount = 0;
        for (int i = 0; i < fields.size(); i++) {
            if (fields.get(i).isDerived()) {
                continue;
            } else if (fields.get(i).isOneToManyAssociation()) {
                collectionFieldCount++;
            } else {
                simpleFieldCount++;
            }
        }

        NakedObjectAssociation[] oneToOneProperties = new NakedObjectAssociation[simpleFieldCount];
        NakedObjectAssociation[] oneToManyProperties = new NakedObjectAssociation[collectionFieldCount];
        collectionMappers = new CollectionMapper[collectionFieldCount];
        // Properties collectionMappings = configParameters.getPropertiesStrippingPrefix(parameterBase +
        // "collection");
        NakedObjectConfiguration subset = NakedObjectsContext.getConfiguration().createSubset(parameterBase + ".mapper.");

        for (int i = 0, simpleFieldNo = 0, collectionFieldNo = 0; i < fields.size(); i++) {
            NakedObjectAssociation field = fields.get(i);
            if (field.isDerived()) {
                continue;
            } else if (field.isOneToManyAssociation()) {
                oneToManyProperties[collectionFieldNo] = field;

                String type = subset.getString(field.getId());
                if (type == null || type.equals("association-table")) {
                    collectionMappers[collectionFieldNo] = new AutoCollectionMapper(specification,
                            oneToManyProperties[collectionFieldNo], lookup);
                } else if (type.equals("fk-table")) {
                    String property = parameterBase + field.getId() + ".element-type";
                    String elementType = configParameters.getString(property);
                    if (elementType == null) {
                        throw new SqlObjectStoreException("Expected property " + property);
                    }
                    collectionMappers[collectionFieldNo] = new ReversedAutoAssociationMapper(elementType,
                            oneToManyProperties[collectionFieldNo], parameterBase, lookup);

                } else {
                    // TODO use other mappers where necessary
                    throw new NotYetImplementedException("for " + type);
                }

                collectionFieldNo++;
            } else if (field.isOneToOneAssociation()) {
                oneToOneProperties[simpleFieldNo] = field;
                simpleFieldNo++;
            } else {
                oneToOneProperties[simpleFieldNo] = field;
                simpleFieldNo++;
            }
        }

        for (int f = 0; f < oneToOneProperties.length; f++) {
            NakedObjectAssociation field = oneToOneProperties[f];
            FieldMapping mapping = lookup.createMapping(field);
            fieldMappings.add(mapping);
        }

    }
/*
    private void setupSpecifiedMapping(
            final NakedObjectSpecification specification,
            final NakedObjectConfiguration configParameters,
            final String parameterBase) {
        NakedObjectConfiguration columnMappings = NakedObjectsContext.getConfiguration().createSubset(parameterBase + "column");
        int columnsSize = columnMappings.size();
        // columnNames = new String[columnsSize];
        oneToOneProperties = new NakedObjectAssociation[columnsSize];

        int i = 0;
        for (Enumeration names = columnMappings.propertyNames(); names.hasMoreElements(); i++) {
            String columnName = (String) names.nextElement();
            String fieldName = columnMappings.getString(columnName);
            oneToOneProperties[i] = specification.getAssociation(fieldName);
            // columnNames[i] = columnName;
        }

        NakedObjectConfiguration collectionMappings = NakedObjectsContext.getConfiguration().createSubset(
                parameterBase + "collection");
        int collectionsSize = collectionMappings.size();
        collectionMappers = new AutoCollectionMapper[collectionsSize];
        oneToManyProperties = new NakedObjectAssociation[collectionsSize];

        int j = 0;
        for (Enumeration names = collectionMappings.propertyNames(); names.hasMoreElements(); j++) {
            String propertyName = (String) names.nextElement();
            String collectionName = collectionMappings.getString(propertyName);
            String type = collectionMappings.getString(collectionName);

            oneToManyProperties[j] = specification.getAssociation(collectionName);
            if (type.equals("auto")) {
                collectionMappers[j] = new AutoCollectionMapper(this, specification, oneToManyProperties[j], getLookup());
            } else {
                // TODO use other mappers where necessary
                // new ReversedAutoAssociationMapper(nakedClass, collectionName, parameterBase);

                throw new NotYetImplementedException();
            }
        }
    }
*/
    protected String columnList() {
        StringBuffer sql = new StringBuffer();
        for (FieldMapping mapping : fieldMappings) {
            if (sql.length() > 0) {
                sql.append(",");
            }
            mapping.appendColumnNames(sql);
        }
        return sql.toString();
    }

    protected NakedObject getAdapter(final NakedObjectSpecification specification, final Oid oid) {
        AdapterManager objectLoader = NakedObjectsContext.getPersistenceSession().getAdapterManager();
        NakedObject adapter = objectLoader.getAdapterFor(oid);
        if (adapter != null) {
            return adapter;
        } else {
            return NakedObjectsContext.getPersistenceSession().recreateAdapter(oid, specification);
        }
    }

    public boolean needsTables(final DatabaseConnector connection) {
        for (int i = 0; collectionMappers != null && i < collectionMappers.length; i++) {
            if (collectionMappers[i].needsTables(connection)) {
                return true;
            }
        }
        return !connection.hasTable(table);
    }

    public String toString() {
        return "AbstractAutoMapper [table=" + table + ",noColumns=" + fieldMappings.size() + ",nakedClass="
                + specification.getFullName() + "]";
    }

    protected String values(final NakedObject object) {
        StringBuffer sql = new StringBuffer();
        for (FieldMapping mapping : fieldMappings) {
            mapping.appendInsertValues(sql, object);
            sql.append(",");
        }
        return sql.toString();
    }

    protected String updateWhereClause(final NakedObject object, final boolean and) {
        Version version = object.getVersion();
        long versionNumber = ((SerialNumberVersion) version).getSequence();
        return (and ? " and " + versionNumber + " = VERSION" : "");
    }
}
// Copyright (c) Naked Objects Group Ltd.
