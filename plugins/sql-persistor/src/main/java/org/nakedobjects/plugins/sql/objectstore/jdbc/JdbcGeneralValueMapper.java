package org.nakedobjects.plugins.sql.objectstore.jdbc;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.object.encodeable.EncodableFacet;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMapping;
import org.nakedobjects.plugins.sql.objectstore.mapping.FieldMappingFactory;


public class JdbcGeneralValueMapper extends AbstractJdbcFieldMapping {
    
    public static class Factory implements FieldMappingFactory {
        private final String type;

        public Factory(final String type) {
            this.type = type;
        }

        public FieldMapping createFieldMapping(final NakedObjectAssociation field) {
            return new JdbcGeneralValueMapper(field, type);
        }
    }

    private String type;

    public JdbcGeneralValueMapper(final NakedObjectAssociation field, final String type) {
        super(field);
        this.type = type;
    }

    public String valueAsDBString(final NakedObject value) {
        if (value == null) {
            return "NULL";
        } else {
            EncodableFacet facet = value.getSpecification().getFacet(EncodableFacet.class);
            String encodedString = facet.toEncodedString(value);
            if (encodedString == null || encodedString.equals("NULL")) {
                return "NULL";
            }
            StringBuffer buffer = new StringBuffer("'");
            for (int i = 0; i < encodedString.length(); i++) {
                char c = encodedString.charAt(i);
                if (c == '\'') {
                    buffer.append('\'');
                } else if (c == '\\') {
                    buffer.append('\\');
                }
                buffer.append(c);
            }
            buffer.append("'");
            return buffer.toString();
        }

    }

    public NakedObject setFromDBColumn(final String encodeValue, final NakedObjectAssociation field) {
        EncodableFacet facet = field.getSpecification().getFacet(EncodableFacet.class);
        return facet.fromEncodedString(encodeValue);
    }

    public String columnType() {
        return type;
    }

}
// Copyright (c) Naked Objects Group Ltd.
