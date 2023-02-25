package org.nakedobjects.plugins.sql.objectstore.jdbc;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMapping;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMappingFactory;


public class JdbcDateMapper extends AbstractJdbcFieldMapping {
    
    public static class Factory implements FieldMappingFactory {
        public FieldMapping createFieldMapping(final NakedObjectAssociation field) {
            return new JdbcDateMapper(field);
        }
    }

    protected JdbcDateMapper(NakedObjectAssociation field) {
        super(field);
    }

    public String valueAsDBString(final NakedObject value) {
        EncodableFacet encodeableFacet = value.getSpecification().getFacet(EncodableFacet.class);
        String encodedString = encodeableFacet.toEncodedString(value);
        String year = encodedString.substring(0, 4);
        String month = encodedString.substring(4, 6);
        String day = encodedString.substring(6, 8);
        String encodedWithAdaptions = year + "-" + month + "-" + day;
        return "'" + encodedWithAdaptions + "'";
    }

    public NakedObject setFromDBColumn(final String encodedValue, final NakedObjectAssociation field) {
        // convert date to yyyymmdd
        String year = encodedValue.substring(0, 4);
        String month = encodedValue.substring(5, 7);
        String day = encodedValue.substring(8, 10);
        String valueString = year + month + day;
        return field.getSpecification().getFacet(EncodableFacet.class).fromEncodedString(valueString);
    }

    public String columnType() {
        return "DATE";
    }

}
// Copyright (c) Naked Objects Group Ltd.
