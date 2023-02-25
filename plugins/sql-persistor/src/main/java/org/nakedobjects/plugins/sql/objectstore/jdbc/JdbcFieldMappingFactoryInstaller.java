package org.nakedobjects.plugins.sql.objectstore.jdbc;

import org.nakedobjects.applib.value.Color;
import org.nakedobjects.applib.value.Date;
import org.nakedobjects.applib.value.DateTime;
import org.nakedobjects.applib.value.Money;
import org.nakedobjects.applib.value.Password;
import org.nakedobjects.applib.value.Percentage;
import org.nakedobjects.applib.value.Time;
import org.nakedobjects.applib.value.TimeStamp;
import org.nakedobjects.plugins.sql.objectstore.FieldMappingFactoryInstaller;
import org.nakedobjects.plugins.sql.objectstore.FieldMappingLookup;


public class JdbcFieldMappingFactoryInstaller implements FieldMappingFactoryInstaller {

    public void load(FieldMappingLookup lookup) {
        lookup.addFieldMappingFactory(boolean.class, new JdbcGeneralValueMapper.Factory("CHAR(1)"));
        lookup.addFieldMappingFactory(short.class, new JdbcGeneralValueMapper.Factory("INT"));
        lookup.addFieldMappingFactory(int.class, new JdbcGeneralValueMapper.Factory("INT"));
        lookup.addFieldMappingFactory(long.class, new JdbcGeneralValueMapper.Factory("INT"));
        lookup.addFieldMappingFactory(float.class, new JdbcGeneralValueMapper.Factory("FLOAT"));
        lookup.addFieldMappingFactory(double.class, new JdbcGeneralValueMapper.Factory("FLOAT"));
        lookup.addFieldMappingFactory(char.class, new JdbcGeneralValueMapper.Factory("CHAR(2)"));

        lookup.addFieldMappingFactory(Money.class, new JdbcGeneralValueMapper.Factory("FLOAT"));
        lookup.addFieldMappingFactory(Percentage.class, new JdbcGeneralValueMapper.Factory("FLOAT"));
        lookup.addFieldMappingFactory(Password.class, new JdbcGeneralValueMapper.Factory("VARCHAR(12)"));
        lookup.addFieldMappingFactory(Color.class, new JdbcGeneralValueMapper.Factory("INT"));
        lookup.addFieldMappingFactory(String.class, new JdbcGeneralValueMapper.Factory("VARCHAR(65)"));

        lookup.addFieldMappingFactory(Date.class, new JdbcDateMapper.Factory());
        lookup.addFieldMappingFactory(Time.class, new JdbcTimeMapper.Factory());
        lookup.addFieldMappingFactory(DateTime.class, new JdbcDateTimeMapper.Factory());
        lookup.addFieldMappingFactory(TimeStamp.class, new JdbcTimestampMapper.Factory());

        lookup.setReferenceFieldMappingFactory(new JdbcObjectReferenceFieldMapping.Factory());
        
        lookup .setObjectReferenceMappingfactory(new JdbcObjectReferenceMappingFactory());

    }

}

// Copyright (c) Naked Objects Group Ltd.
