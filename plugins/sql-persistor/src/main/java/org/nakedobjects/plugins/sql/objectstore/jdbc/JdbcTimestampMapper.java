package org.nakedobjects.plugins.sql.objectstore.jdbc;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMapping;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMappingFactory;


public class JdbcTimestampMapper extends AbstractJdbcFieldMapping {

    public static class Factory implements FieldMappingFactory {
        public FieldMapping createFieldMapping(final NakedObjectAssociation field) {
            return new JdbcTimestampMapper(field);
        }
    }

    protected JdbcTimestampMapper(NakedObjectAssociation field) {
        super(field);
    }

    public String valueAsDBString(final NakedObject value) {
        EncodableFacet encodeableFacet = value.getSpecification().getFacet(EncodableFacet.class);
        String encodedString = encodeableFacet.toEncodedString(value);
        String year = encodedString.substring(0, 4);
        String month = encodedString.substring(4, 6);
        String day = encodedString.substring(6, 8);
        String hour = encodedString.substring(8, 10);
        String minute = encodedString.substring(10, 12);
        String second = encodedString.substring(12, 14);
        String millisecond = encodedString.substring(14, 17);
        String encodedWithAdaptions = year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second + "."
                + millisecond;
        return "'" + encodedWithAdaptions + "'";
    }

    public NakedObject setFromDBColumn(final String encodedValue, final NakedObjectAssociation field) {
        // convert date to yyyymmddhhmm
        String year = encodedValue.substring(0, 4);
        String month = encodedValue.substring(5, 7);
        String day = encodedValue.substring(8, 10);
        String hour = encodedValue.substring(11, 13);
        String minute = encodedValue.substring(14, 16);
        String second = encodedValue.substring(17, 19);
        int length = encodedValue.length();
        String millisecond = encodedValue.substring(20, length);
        if (length < 21) {
            millisecond = millisecond + "000";
        } else if (length < 22) {
            millisecond = millisecond + "00";
        } else if (length < 23) {
            millisecond = millisecond + "0";
        }
        String valueString = year + month + day + hour + minute + second + millisecond;
        return field.getSpecification().getFacet(EncodableFacet.class).fromEncodedString(valueString);
    }

    public String columnType() {
        return "DATETIME";
    }

}
// Copyright (c) Naked Objects Group Ltd.
