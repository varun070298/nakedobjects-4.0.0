package org.nakedobjects.plugins.hibernate.objectstore.tools.internal;

import java.util.StringTokenizer;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.runtimecontext.spec.NakedObjectSpecificationNoMember;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.specloader.SpecificationLoader;
import org.nakedobjects.plugins.hibernate.objectstore.HibernateConstants;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class MappingHelper {
    private static final String LIST_SEPARATOR = ", ";
    private static final String NAKEDOBJECTS_CLASSES_LIST = HibernateConstants.PROPERTY_PREFIX + "classes";
    private static final String NAKEDOBJECTS_CLASSES_PREFIX = HibernateConstants.PROPERTY_PREFIX + "classes.prefix";

    public static void loadRequiredClasses() {
        final NakedObjectConfiguration configuration = NakedObjectsContext.getConfiguration();
        final SpecificationLoader loader = NakedObjectsContext.getSpecificationLoader();
        String classPrefix = configuration.getString(NAKEDOBJECTS_CLASSES_PREFIX);
        classPrefix = classPrefix == null ? "" : classPrefix.trim();
        if (classPrefix.length() > 0 && !classPrefix.endsWith(".")) {
            classPrefix = classPrefix + ".";
        }
        final String classList = configuration.getString(NAKEDOBJECTS_CLASSES_LIST);
        if (classList != null) {
            final StringTokenizer classes = new StringTokenizer(classList, LIST_SEPARATOR);
            while (classes.hasMoreTokens()) {
                final NakedObjectSpecification specification = loader.loadSpecification(classPrefix + classes.nextToken().trim());
                if (specification instanceof NakedObjectSpecificationNoMember) {
                    throw new NakedObjectException("No such class " + specification.getFullName());
                }
            }
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
