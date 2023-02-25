package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.accessor;

import org.junit.Before;
import org.nakedobjects.plugins.hibernate.objectstore.HibernateConstants;
import org.nakedobjects.plugins.hibernate.objectstore.testdomain.TestObject;
import org.nakedobjects.plugins.hibernate.objectstore.util.HibernateUtil;
import org.nakedobjects.runtime.testsystem.ProxyJunit4TestCase;

public abstract class UserAccessorAbstractTestCase extends ProxyJunit4TestCase {

    @Before
    public void setUpHibernateConfig() throws Exception {
        system.addConfiguration(HibernateConstants.PROPERTY_PREFIX + "auto", "true");
        system.addConfiguration(HibernateConstants.PROPERTY_PREFIX + "classes", TestObject.class.getName());

        HibernateUtil.initialiseSessionFactory();
    }


}
// Copyright (c) Naked Objects Group Ltd.
