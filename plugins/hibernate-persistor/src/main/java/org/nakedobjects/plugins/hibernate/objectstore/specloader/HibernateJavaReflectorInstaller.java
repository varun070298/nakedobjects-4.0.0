package org.nakedobjects.plugins.hibernate.objectstore.specloader;

import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.specloader.classsubstitutor.ClassSubstitutor;
import org.nakedobjects.plugins.hibernate.objectstore.persistence.container.HibernateContainer;
import org.nakedobjects.plugins.hibernate.objectstore.specloader.classsubstitutor.HibernateClassSubstitutor;
import org.nakedobjects.runtime.system.installers.JavaReflectorInstaller;


/**
 * Sets up a {@link HibernateContainer}. 
 */
public class HibernateJavaReflectorInstaller extends JavaReflectorInstaller {

	
	public HibernateJavaReflectorInstaller() {
		super("hibernate");
	}

    @Override
    protected ClassSubstitutor createClassSubstitutor(
    		NakedObjectConfiguration configuration) {
    	return new HibernateClassSubstitutor();
    }
    
}
// Copyright (c) Naked Objects Group Ltd.
