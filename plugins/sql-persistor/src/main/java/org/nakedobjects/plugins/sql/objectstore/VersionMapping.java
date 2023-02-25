package org.nakedobjects.plugins.sql.objectstore;

import java.util.Date;

import org.nakedobjects.metamodel.adapter.version.SerialNumberVersion;
import org.nakedobjects.metamodel.adapter.version.Version;


public class VersionMapping {
    private String lastActivityDateColumn;
    private String lastActivityUserColumn;
    private String versionColumn;

    public void init() {
        lastActivityDateColumn = "MODIFIED_ON";
        lastActivityUserColumn = "MODIFIED_BY";
        versionColumn = "VERSION";
    }

    public String insertColumns() {
        return versionColumn + ", " + lastActivityUserColumn + ", " + lastActivityDateColumn;
    }

    public String insertValues(SerialNumberVersion version) {
        return version.sequence() + ", '" + version.getUser() + "',  " + Sql.timestamp;
    }

    public String whereClause(SerialNumberVersion version) {
        return versionColumn + " = " + version.sequence();
    }

    public String updateAssigment(long nextSequence) {
        return versionColumn + " = " + nextSequence;
    }

    public String appendSelectColumns() {
        StringBuffer sql = new StringBuffer();
        sql.append(versionColumn);
        sql.append(",");
        sql.append(lastActivityUserColumn);
        sql.append(",");
        sql.append(lastActivityDateColumn);
        return sql.toString();
    }

    public String appendColumnDefinitions() {
        StringBuffer sql = new StringBuffer();

        sql.append(versionColumn);
        sql.append(" bigint");

        sql.append(",");
        sql.append(lastActivityUserColumn);
        sql.append(" varchar(32)");

        sql.append(",");
        sql.append(lastActivityDateColumn);
        sql.append(" timestamp");

        return sql.toString();
    }

    public Object appendUpdateValues(long versionSequence) {
        return versionColumn + "=" + versionSequence;
    }

    public Version getLock(Results rs) {
        long number = rs.getLong(versionColumn);
        String user = rs.getString(lastActivityUserColumn);
        Date time = rs.getDate(lastActivityDateColumn);
        Version version = new SerialNumberVersion(number, user, time);
        return version;
    }

}

// Copyright (c) Naked Objects Group Ltd.
