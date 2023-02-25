package org.nakedobjects.runtime.persistence;

import org.nakedobjects.applib.DomainObjectContainer;
import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.services.ServicesInjector;
import org.nakedobjects.metamodel.services.ServicesInjectorDefault;
import org.nakedobjects.metamodel.services.container.DomainObjectContainerDefault;
import org.nakedobjects.runtime.persistence.adapterfactory.AdapterFactory;
import org.nakedobjects.runtime.persistence.adapterfactory.pojo.PojoAdapterFactory;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactory;
import org.nakedobjects.runtime.persistence.objectfactory.ObjectFactoryBasic;
import org.nakedobjects.runtime.persistence.oidgenerator.OidGenerator;
import org.nakedobjects.runtime.persistence.oidgenerator.simple.SimpleOidGenerator;

public final class PersistenceConstants {
    
    /**
     * Key used to lookup implementation of {@link AdapterFactory} in {@link NakedObjectConfiguration}.
     */
    public static final String ADAPTER_FACTORY_CLASS_NAME = ConfigurationConstants.ROOT + "persistor.adapter-factory";
    public static final String ADAPTER_FACTORY_CLASS_NAME_DEFAULT = PojoAdapterFactory.class.getName();
    
    /**
     * Key used to lookup implementation of {@link OidGenerator} in {@link NakedObjectConfiguration}.
     */
    public static final String OID_GENERATOR_CLASS_NAME = ConfigurationConstants.ROOT + "persistor.oid-generator";
    public static final String OID_GENERATOR_CLASS_NAME_DEFAULT = SimpleOidGenerator.class.getName();

    /**
     * Key used to lookup implementation of {@link ObjectFactory} in {@link NakedObjectConfiguration}.
     */
    public static final String OBJECT_FACTORY_CLASS_NAME = ConfigurationConstants.ROOT + "persistor.object-factory";
    public static final String OBJECT_FACTORY_CLASS_NAME_DEFAULT = ObjectFactoryBasic.class.getName();

    /**
     * Key used to lookup implementation of {@link ServicesInjector} in {@link NakedObjectConfiguration}.
     */
    public static final String SERVICES_INJECTOR_CLASS_NAME = ConfigurationConstants.ROOT + "persistor.services-injector";
    public static final String SERVICES_INJECTOR_CLASS_NAME_DEFAULT = ServicesInjectorDefault.class.getName();

    /**
     * Key used to lookup implementation of {@link DomainObjectContainer} in {@link NakedObjectConfiguration}.
     */
    public static final String DOMAIN_OBJECT_CONTAINER_CLASS_NAME = ConfigurationConstants.ROOT + "persistor.domain-object-container";
    public static final String DOMAIN_OBJECT_CONTAINER_NAME_DEFAULT = DomainObjectContainerDefault.class.getName();


    
    private PersistenceConstants() {
    }

}
