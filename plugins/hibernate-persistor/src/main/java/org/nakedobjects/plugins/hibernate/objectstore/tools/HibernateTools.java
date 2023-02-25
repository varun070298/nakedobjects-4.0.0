package org.nakedobjects.plugins.hibernate.objectstore.tools;

import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.nakedobjects.plugins.hibernate.objectstore.tools.internal.Nof2HbmXml;
import org.nakedobjects.plugins.hibernate.objectstore.util.HibernateUtil;


public class HibernateTools {

    /**
     * Export schema to database - will also drop tables first
     * 
     * @param script
     *            to write DDL script to System.out
     * @param export
     *            to export updates to the database
     */
    public static void exportSchema(final boolean script, final boolean export) {
        final SchemaExport schemaExport = new SchemaExport(HibernateUtil.getConfiguration());
        // schemaExport.edrop(script, export);
        schemaExport.create(script, export);
    }

    /**
     * Drop schema from database
     * 
     * @param script
     *            to write DDL script to System.out
     * @param export
     *            to export updates to the database
     */
    public static void dropSchema(final boolean script, final boolean export) {
        final SchemaExport schemaExport = new SchemaExport(HibernateUtil.getConfiguration());
        schemaExport.drop(script, export);
    }

    /**
     * Update Schema in the database
     * 
     * @param script
     *            to write DDL script to System.out
     * @param export
     *            to export updates to the database
     */
    public static void updateSchema(final boolean script, final boolean export) {
        updateSchema(HibernateUtil.getConfiguration(), script, export);
    }

    /**
     * Update Schema in the database
     * 
     * @param script
     *            to write DDL script to System.out
     * @param export
     *            to export updates to the database
     */
    public static void updateSchema(final Configuration cfg, final boolean script, final boolean export) {
        new SchemaUpdate(cfg).execute(script, export);
    }

    /**
     * Export Hibernate mapping files for all Naked Objects currently in NakedObjects.
     * 
     * @param outDir
     */
    public static void exportHbmXml(final String basedir) {
        new Nof2HbmXml().exportHbmXml(basedir);
    }

}
// Copyright (c) Naked Objects Group Ltd.
