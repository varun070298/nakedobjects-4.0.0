package org.nakedobjects.plugins.sql.objectstore.auto;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.sql.objectstore.AbstractMapper;
import org.nakedobjects.plugins.sql.objectstore.CollectionMapper;
import org.nakedobjects.plugins.sql.objectstore.DatabaseConnector;
import org.nakedobjects.plugins.sql.objectstore.FieldMappingLookup;
import org.nakedobjects.plugins.sql.objectstore.IdMapping;
import org.nakedobjects.plugins.sql.objectstore.Results;
import org.nakedobjects.plugins.sql.objectstore.jdbc.JdbcObjectReferenceMapping;
import org.nakedobjects.plugins.sql.objectstore.mapping.ObjectReferenceMapping;
import org.nakedobjects.runtime.persistence.PersistorUtil;


public class AutoCollectionMapper extends AbstractMapper implements CollectionMapper {
    private static final Logger LOG = Logger.getLogger(AutoCollectionMapper.class);
    private String tableName;
    private NakedObjectAssociation field;
    private ObjectReferenceMapping elementMapping;
    private IdMapping idMapping;

    public AutoCollectionMapper(
            final NakedObjectSpecification specification,
            final NakedObjectAssociation field,
            final FieldMappingLookup lookup) {
        this.field = field;

        NakedObjectSpecification spec = field.getFacet(TypeOfFacet.class).valueSpec();
        idMapping = lookup.createIdMapping();
        elementMapping = lookup.createMapping(spec);

        String className = specification.getShortName();
        String columnName = field.getId();
        tableName = asSqlName(className) + "_" + asSqlName(columnName);
    }

    public void createTables(final DatabaseConnector connector) {
        if (!connector.hasTable(tableName)) {
            StringBuffer sql = new StringBuffer();
            sql.append("create table ");
            sql.append(tableName);
            sql.append(" (");

            idMapping.appendColumnDefinitions(sql);
            sql.append(", ");
            elementMapping.appendColumnDefinitions(sql);

            sql.append(")");

            connector.update(sql.toString());
        }
    }

    public void loadInternalCollection(final DatabaseConnector connector, final NakedObject parent) {
        NakedObject collection = (NakedObject) field.get(parent);
        if (collection.getResolveState().canChangeTo(ResolveState.RESOLVING)) {
            LOG.debug("loading internal collection " + field);
            collection.changeState(ResolveState.RESOLVING);
            
            StringBuffer sql = new StringBuffer();
            sql.append("select ");
            idMapping.appendColumnNames(sql);
            sql.append(", ");
            elementMapping.appendColumnNames(sql);
            sql.append(" from ");
            sql.append(tableName);

            Results rs = connector.select(sql.toString());
            List<NakedObject> list = new ArrayList<NakedObject>();
            while (rs.next()) {
                NakedObject element = ((JdbcObjectReferenceMapping) elementMapping).initializeField(rs);
                LOG.debug("  element  " + element.getOid());
                list.add(element);
            }
            CollectionFacet collectionFacet = collection.getSpecification().getFacet(CollectionFacet.class);
            collectionFacet.init(collection, list.toArray(new NakedObject[list.size()]));
            rs.close();
            PersistorUtil.end(collection);
        }
    }

    public boolean needsTables(final DatabaseConnector connector) {
        return !connector.hasTable(tableName);
    }

    public void saveInternalCollection(final DatabaseConnector connector, final NakedObject parent) {
        NakedObject collection = (NakedObject) field.get(parent);
        LOG.debug("saving internal collection " + collection);

        StringBuffer sql = new StringBuffer();
        sql.append("delete from ");
        sql.append(tableName);
        sql.append(" where ");
        idMapping.appendWhereClause(sql, parent);
        connector.update(sql.toString());

        sql = new StringBuffer();
        sql.append("insert into ");
        sql.append(tableName);
        sql.append(" (");
        idMapping.appendColumnNames(sql);
        sql.append(", ");
        elementMapping.appendColumnNames(sql);
        sql.append(" ) values (");
        idMapping.appendInsertValues(sql, parent);
        sql.append(", ");

        CollectionFacet collectionFacet = collection.getSpecification().getFacet(CollectionFacet.class);
        for (NakedObject element : collectionFacet.iterable(collection)) {
            StringBuffer values = new StringBuffer();
            elementMapping.appendInsertValues(values, element);
            connector.update(sql.toString() + values + ")");
        }
    }

}
// Copyright (c) Naked Objects Group Ltd.
