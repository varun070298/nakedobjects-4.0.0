package org.nakedobjects.plugins.hibernate.objectstore.testdomain;

import java.util.Date;


public class SimpleObject {
    public static String fieldOrder() {
        return "string, someDate, someTime, longField";
    }

    // Property: Hibernate id/fields
    private Long id;
    protected Long getId() {
        return id;
    }
    protected void setId(final Long id) {
        this.id = id;
    }

    
    // {{ Property: String
    private String myString = new String();
    public String getString() {
        return myString;
    }
    public void setString(final String string) {
        myString = string;
    }
    // }}

    

    // {{ Property: LongField
    private long longField;
    public long getLongField() {
        return longField;
    }
    public void setLongField(final long value) {
        longField = value;
    }
    // }}
    

    // {{ Property: SomeDate
    private org.nakedobjects.applib.value.Date someDate;
    public org.nakedobjects.applib.value.Date getSomeDate() {
        return someDate;
    }
    public void setSomeDate(final org.nakedobjects.applib.value.Date date) {
        this.someDate = date;
    }
    // }}

    
    // {{ Property: SomeTime
    private org.nakedobjects.applib.value.Time someTime;
    public org.nakedobjects.applib.value.Time getSomeTime() {
        return someTime;
    }
    public void setSomeTime(final org.nakedobjects.applib.value.Time time) {
        this.someTime = time;
    }
    // }}
    


    
    // {{ special property: LastUpdateUser
    private String lastUpdateUser;
    protected String getLastUpdateUser() {
        return lastUpdateUser;
    }
    protected void setLastUpdateUser(final String lastUpdateUser) {
        this.lastUpdateUser = lastUpdateUser;
    }
    // }}
    


    // {{ special property: LastUpdated
    private Date lastUpdated;
    protected Date getLastUpdated() {
        return lastUpdated;
    }
    protected void setLastUpdated(final Date lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    // }}
    





}
// Copyright (c) Naked Objects Group Ltd.
