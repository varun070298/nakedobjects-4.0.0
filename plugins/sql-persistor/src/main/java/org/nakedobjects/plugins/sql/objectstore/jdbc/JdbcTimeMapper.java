package org.nakedobjects.plugins.sql.objectstore.jdbc;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMapping;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMappingFactory;


public class JdbcTimeMapper extends AbstractJdbcFieldMapping {

    public static class Factory implements FieldMappingFactory {
        public FieldMapping createFieldMapping(final NakedObjectAssociation field) {
            return new JdbcTimeMapper(field);
        }
    }

    protected JdbcTimeMapper(NakedObjectAssociation field) {
        super(field);
    }

    public String valueAsDBString(final NakedObject value) {
        EncodableFacet encodeableFacet = value.getSpecification().getFacet(EncodableFacet.class);
        String encodedString = encodeableFacet.toEncodedString(value);
        String minute = encodedString.substring(2, 4);
        String hour = encodedString.substring(0, 2);
        String encodedWithAdaptions = hour + ":" + minute + ":00";
        return "'" + encodedWithAdaptions + "'";
    }

    public NakedObject setFromDBColumn(final String encodedValue, final NakedObjectAssociation field) {
        String hour = encodedValue.substring(0, 2);
        String minute = encodedValue.substring(3, 5);
        String valueString = hour + minute;
        return field.getSpecification().getFacet(EncodableFacet.class).fromEncodedString(valueString);
    }

    public String columnType() {
        return "TIME";
    }

}
// Copyright (c) Naked Objects Group Ltd.
