package org.nakedobjects.plugins.headless.junit.internal;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.plugins.headless.junit.Service;
import org.nakedobjects.plugins.headless.junit.Services;
import org.nakedobjects.runtime.persistence.services.ServicesInstallerAbstract;


public class ServicesInstallerAnnotatedClass extends ServicesInstallerAbstract {

    public ServicesInstallerAnnotatedClass() {
		super("annotated");
	}

    public void addServicesAnnotatedOn(Class<?> javaClass) throws InstantiationException, IllegalAccessException {
        List<Object> services = new ArrayList<Object>();
        addServicesAnnotatedOn(javaClass, services);
        addServices(services);
    }

    private void addServicesAnnotatedOn(final Class<?> testClass, final List<Object> services) throws InstantiationException, IllegalAccessException {
        final Services servicesAnnotation = testClass.getAnnotation(Services.class);
        if (servicesAnnotation != null) {
            final Service[] serviceAnnotations = servicesAnnotation.value();
            for (final Service serviceAnnotation : serviceAnnotations) {
                addServiceRepresentedBy(serviceAnnotation, services);
            }
        }

        final Service serviceAnnotation = testClass.getAnnotation(Service.class);
        if (serviceAnnotation != null) {
            addServiceRepresentedBy(serviceAnnotation, services);
        }
    }

    private void addServiceRepresentedBy(final Service serviceAnnotation, final List<Object> services) throws InstantiationException, IllegalAccessException {
        final Class<?> serviceClass = serviceAnnotation.value();
        // there's no need to unravel any Collections of services,
        // because the ServiceLoader will do it for us later.
        services.add(serviceClass.newInstance());
    }


}


// Copyright (c) Naked Objects Group Ltd.
