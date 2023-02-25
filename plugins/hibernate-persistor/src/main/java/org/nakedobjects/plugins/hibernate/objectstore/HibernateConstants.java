package org.nakedobjects.plugins.hibernate.objectstore;

import org.nakedobjects.metamodel.config.ConfigurationConstants;

public final class HibernateConstants {
    
    private HibernateConstants() {}

    public static final String PROPERTY_PREFIX = ConfigurationConstants.ROOT + "persistence.hibernate.";
    
    public static final String PERSIST_ALGORITHM_KEY = PROPERTY_PREFIX + "persistAlgorithm";
    
    public static final String SAVE_IMMEDIATE_KEY = PROPERTY_PREFIX + "saveImmediate";
    public static final String REMAPPING_KEY = PROPERTY_PREFIX + "remapping";
    
    public static final String HIB_SCHEMA_UPDATE_KEY = PROPERTY_PREFIX + "schema-update";
    public static final String HIB_SCHEMA_EXPORT_KEY = PROPERTY_PREFIX + "schema-export";
    
    public static final String HIB_REGENERATE_KEY = PROPERTY_PREFIX + "regenerate";
    public static final String HIB_AUTO_KEY = PROPERTY_PREFIX + "auto";
    public static final String HIB_ANNOTATIONS_KEY = PROPERTY_PREFIX + "annotations";
    public static final String HBM_EXPORT_KEY = PROPERTY_PREFIX + "hbm-export";
    public static final String HIB_INITIALIZED_KEY = PROPERTY_PREFIX + "initialized";

}


// Copyright (c) Naked Objects Group Ltd.
