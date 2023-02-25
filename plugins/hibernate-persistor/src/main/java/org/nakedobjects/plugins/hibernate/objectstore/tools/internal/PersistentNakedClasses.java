package org.nakedobjects.plugins.hibernate.objectstore.tools.internal;

import java.beans.Introspector;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.config.NakedObjectConfiguration;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.Persistability;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.hibernate.objectstore.HibernateConstants;
import org.nakedobjects.runtime.context.NakedObjectsContext;


/**
 * Details of all Naked Object classes which are to be persisted with Hibernate. All specifications must be
 * loaded by the NOF specificationLoader before calling mapClasses() to load up the class hierarchy to be used
 * to map to Hibernate.
 */
public class PersistentNakedClasses {

    private final HashMap<String, PersistentNakedClass> classes = new HashMap<String, PersistentNakedClass>();
    private final HashMap<String, PersistentNakedClass> interfaces = new HashMap<String, PersistentNakedClass>();
    private final PersistentNakedClass rootClass = new PersistentNakedClass();
    private boolean assumeBidirectional = true;

    public static PersistentNakedClasses buildPersistentNakedClasses(final Boolean assumeBidirectional) {
        PersistentNakedClasses classes;
        if (assumeBidirectional == null) {
            classes = new PersistentNakedClasses();
        } else {
            classes = new PersistentNakedClasses(assumeBidirectional.booleanValue());
        }
        classes.buildClassMaps();
        return classes;
    }

    protected PersistentNakedClasses() {
        this(NakedObjectsContext.getConfiguration().getBoolean(HibernateConstants.PROPERTY_PREFIX + "assumeBidirectional", true));
    }

    protected PersistentNakedClasses(final boolean assumeBidirectional) {
        this.assumeBidirectional = assumeBidirectional;
    }

    /**
     * Load the list of persistent classes from the current list of specifications loaded by the Naked Objects
     * specification loader.
     */
    protected void buildClassMaps() {
        final NakedObjectSpecification objectSpec = NakedObjectsContext.getSpecificationLoader().loadSpecification(Object.class);
        buildClassHierachy(objectSpec, rootClass);
        findInterfaces(classes.values().toArray(new PersistentNakedClass[0]));
        optimiseInterfaces();
        checkAssociations();
        removeUnusedAbstractClasses();
        assignTableNames();
        checkInverseAssociations();
        if (assumeBidirectional) {
            mapAssociations();
        }
        markVersionInfo();
    }

    public String debugString() {
        final StringBuffer sb = new StringBuffer(2048);
        rootClass.debugString(sb, "    C--");
        for (final Iterator<PersistentNakedClass> iter = interfaces.values().iterator(); iter.hasNext();) {
            iter.next().debugString(sb, "    I--");
        }
        return sb.toString();
    }

    public PersistentNakedClass getPersistentClass(final String name) {
        return classes.get(name);
    }

    public Iterator<PersistentNakedClass> getPersistentClasses() {
        return classes.values().iterator();
    }

    public boolean isPersistentClass(final String name) {
        return classes.containsKey(name);
    }

    public boolean isPersistentInterface(final String name) {
        return interfaces.containsKey(name);
    }

    private void mapAssociations() {
        for (final Iterator<PersistentNakedClass> iter = classes.values().iterator(); iter.hasNext();) {
            final PersistentNakedClass thisPersistentClass = iter.next();
            final NakedObjectAssociation[] uniqueFields = thisPersistentClass.getUniqueFields();
            for (int i = 0; i < uniqueFields.length; i++) {
                final NakedObjectAssociation field = uniqueFields[i];

                if (field.getSpecification().isValueOrIsAggregated() || field.isDerived()
                        || thisPersistentClass.hasAssociation(field.getId())) {
                    continue;
                }
                final String associatedClassName = field.getSpecification().getFullName();
                if (!thisPersistentClass.isUniqueAssociation(associatedClassName)) {
                    continue;
                }
                final PersistentNakedClass associatedClass = getPersistentClass(associatedClassName);
                if (associatedClass == null) {
                    continue;
                }
                final NakedObjectAssociation associatedField = associatedClass
                        .getUniqueAssociation(thisPersistentClass.getName());
                if (associatedField == null || associatedClass.hasAssociation(associatedField.getId())) {
                    continue;
                }
                boolean inverse;
                if (field.isOneToOneAssociation()) {
                    if (associatedField.isOneToOneAssociation()) {
                        // one-to-one - arbitrarily pick a side
                        inverse = associatedClassName.compareTo(thisPersistentClass.getName()) < 0;
                    } else {
                        // one to many - inverse is collection on other
                        inverse = false;
                    }
                } else {
                    if (associatedField.isOneToOneAssociation()) {
                        // many to one - inverse is collection on this
                        inverse = true;
                    } else {
                        // many-to-many - arbitrarily pick a side
                        inverse = associatedClassName.compareTo(thisPersistentClass.getName()) < 0;
                    }
                }
                final Association association = new Association(associatedClass, associatedField, inverse);
                thisPersistentClass.addAssociation(field.getId(), association);
                final Association reverseAssociation = new Association(thisPersistentClass, field, !inverse);
                associatedClass.addAssociation(associatedField.getId(), reverseAssociation);
            }
        }
    }

    private void checkInverseAssociations() {
        for (final Iterator<PersistentNakedClass> iter = classes.values().iterator(); iter.hasNext();) {
            final PersistentNakedClass persistentClass = iter.next();
            final NakedObjectAssociation[] fields = persistentClass.getUniqueFields();
            for (int i = 0; i < fields.length; i++) {
                if (fields[i].getSpecification().isValueOrIsAggregated() || fields[i].isDerived()) {
                    continue;
                }
                final PersistentNakedClass associatedClass = getPersistentClass(fields[i].getSpecification().getFullName());
                if (associatedClass == null) {
                    continue;
                }
                final String inverse = getInverse(persistentClass, fields[i].getId());
                if (inverse != null) {
                    final NakedObjectAssociation associatedField = associatedClass.getSpecification().getAssociation(inverse);
                    final Association association = new Association(associatedClass, associatedField, false);
                    persistentClass.addAssociation(fields[i].getId(), association);
                    final Association reverseAssociation = new Association(persistentClass, fields[i], true);
                    associatedClass.addAssociation(inverse, reverseAssociation);
                }
            }
        }
    }

    private String getInverse(final PersistentNakedClass persistentClass, final String fieldName) {
        Class<?> clazz;
        try {
            clazz = Class.forName(persistentClass.getSpecification().getFullName());
        } catch (final ClassNotFoundException e) {
            throw new NakedObjectException(e);
        }
        final String nameWithNoSpaces = fieldName.replace(" ", "");
        final String capitalizedName = nameWithNoSpaces.substring(0, 1).toUpperCase() + nameWithNoSpaces.substring(1);
        try {
            final java.lang.reflect.Field field = clazz.getField("inverse" + capitalizedName);
            return Introspector.decapitalize((String) field.get(clazz));
        } catch (final NoSuchFieldException e) {} catch (final IllegalAccessException e) {}
        return null;
    }

    /**
     * Decide which classes should have version information as part of the mapping. Don't include
     * interfaces/abstract classes as they may not have modified date/user etc properties defined.
     */
    private void markVersionInfo() {
        markVersionInfo(rootClass.getSubClasses());
    }

    private void markVersionInfo(final Iterator<PersistentNakedClass> subClasses) {
        for (final Iterator<PersistentNakedClass> iter = subClasses; iter.hasNext();) {
            final PersistentNakedClass persistentClass = iter.next();
            if (persistentClass.isAbstract()) {
                markVersionInfo(persistentClass.getSubClasses());
            } else {
                persistentClass.setRequireVersion(true);
            }
        }
    }

    /**
     * Assign table names for each persistent class, making sure they are not duplicated. We try to use the
     * unqualified class name, if that fails add the lowest level package and keep doing that until a unique
     * name is found.
     */
    private void assignTableNames() {
        final NakedObjectConfiguration config = NakedObjectsContext.getConfiguration();
        final HashMap<String, PersistentNakedClass> tableNames = new HashMap<String, PersistentNakedClass>(classes.size() * 2);
        for (final Iterator<PersistentNakedClass> iter = classes.values().iterator(); iter.hasNext();) {
            final PersistentNakedClass persistentClass = iter.next();
            final String fullName = persistentClass.getName();
            String candidate = config.getString(HibernateConstants.PROPERTY_PREFIX + "table." + fullName);
            if (candidate != null) {
                candidate = candidate.trim().toUpperCase();
            } else {
                candidate = fullName.substring(fullName.lastIndexOf('.') + 1).toUpperCase();
            }
            if (tableNames.containsKey(candidate)) {
                duplicateTableName(tableNames, persistentClass, candidate);
                // make the duplicated table name consistent with the new one just added
                final PersistentNakedClass duplicate = tableNames.get(candidate);
                if (duplicate != null) {
                    tableNames.put(candidate, null);
                    duplicateTableName(tableNames, duplicate, candidate);
                }
            } else {
                tableNames.put(candidate, persistentClass);
                persistentClass.setTableName(candidate);
            }
        }
    }

    private void duplicateTableName(
            final HashMap<String, PersistentNakedClass> tableNames,
            final PersistentNakedClass persistentClass,
            final String candidate) {
        persistentClass.setDuplicateUnqualifiedClassName(true);
        final String fullName = persistentClass.getName();
        final String remaining = fullName.substring(0, fullName.length() - candidate.length() - 1);
        assignTableName(tableNames, persistentClass, remaining, candidate);
    }

    private void assignTableName(
            final HashMap<String, PersistentNakedClass> tableNames,
            final PersistentNakedClass persistentClass,
            final String remaining,
            final String lastCandidate) {
        final int lastDot = remaining.lastIndexOf('.');
        final String candidate = remaining.substring(lastDot + 1).toUpperCase() + "_" + lastCandidate;
        if (tableNames.containsKey(candidate)) {
            if (lastDot == -1) {
                // Sanity check - should never get here!
                throw new NakedObjectException("Cannot create unique table name for" + persistentClass.getName());
            }
            assignTableName(tableNames, persistentClass, remaining.substring(0, lastDot), candidate);
        } else {
            tableNames.put(candidate, persistentClass);
            persistentClass.setTableName(candidate);
        }
    }

    /**
     * Check which classes/interfaces are used in associations, and mark them
     */
    private void checkAssociations() {
        // make an array as we may modify the classes map, which would cause an exception in an iterator
        final PersistentNakedClass[] persistentClasses = classes.values().toArray(new PersistentNakedClass[0]);
        for (int i = 0; i < persistentClasses.length; i++) {
            final NakedObjectAssociation[] fields = persistentClasses[i].getSpecification().getAssociations();
            for (int j = 0; j < fields.length; j++) {
                final NakedObjectAssociation field = fields[j];
                if (field.isDerived() || field.getSpecification().isService()
                        || field.getSpecification().getFullName().startsWith("java.")) {
                    continue;
                }

                if (field.getSpecification().isValueOrIsAggregated()) {
                    continue;
                }

                // object or collection - for both getSpecification returns the spec of the associated object
                final String associatedClassName = field.getSpecification().getFullName();
                if (!classes.containsKey(associatedClassName) && !interfaces.containsKey(associatedClassName)) {
                    // enforce class must be in classes or interface maps
                    throw new NakedObjectException("Missing class/interface: " + field.getSpecification().getFullName());
                }
                // if it's a class add a reference
                final PersistentNakedClass associatedClass = classes.get(associatedClassName);
                if (associatedClass != null) {
                    associatedClass.addReference();
                }
            }
        }
    }

    private void findInterfaces(final PersistentNakedClass[] subclasses) {
        final List<PersistentNakedClass> added = new ArrayList<PersistentNakedClass>();
        for (int i = 0; i < subclasses.length; i++) {
            final NakedObjectSpecification[] implementedInterfaces = subclasses[i].getSpecification().interfaces();
            for (int j = 0; j < implementedInterfaces.length; j++) {
                final NakedObjectSpecification implementedInterface = implementedInterfaces[j];
                final String interfaceName = implementedInterface.getFullName();
                if (!interfaces.containsKey(interfaceName)) {
                    final PersistentNakedClass persistentInterface = new PersistentNakedClass(implementedInterface, rootClass);
                    interfaces.put(interfaceName, persistentInterface);
                    added.add(persistentInterface);
                }
            }
        }
        if (added.size() > 0) {
            findInterfaces(added.toArray(new PersistentNakedClass[0]));
        }
    }

    /**
     * Recursively go through all subclasses and create a hierarchy of PersistentNakedClass objects to map them.
     */
    private void buildClassHierachy(final NakedObjectSpecification parentSpec, final PersistentNakedClass parentPersistentClass) {
        final NakedObjectSpecification[] childSpecs = parentSpec.subclasses();
        for (int i = 0; i < childSpecs.length; i++) {
            final NakedObjectSpecification childSpec = childSpecs[i];
            if (childSpec.isEncodeable() || childSpec.persistability() == Persistability.TRANSIENT || childSpec.isService()) {
                continue;
            }
            final String childClassname = childSpec.getFullName();
            PersistentNakedClass childPersistentClass = null;
            if (childSpec.getAssociations().length > 0) {
                try {
                    final Class<?> cls = Class.forName(childClassname);
                    if (cls.isArray()) {
                        continue;
                    }
                } catch (final ClassNotFoundException e) {
                    throw new NakedObjectException(e);
                }
                childPersistentClass = new PersistentNakedClass(childSpec, parentPersistentClass);
                classes.put(childSpec.getFullName(), childPersistentClass);
            }
            buildClassHierachy(childSpec, childPersistentClass == null ? parentPersistentClass : childPersistentClass);
        }
    }

    /**
     * Remove any unreferenced abstract classes as we don't need Hibernate to map those.
     */
    private void removeUnusedAbstractClasses() {
        removeUnusedAbstractClasses(classes.values());
    }

    private void removeUnusedAbstractClasses(final Collection<PersistentNakedClass> col) {
        for (final Iterator<PersistentNakedClass> iter = col.iterator(); iter.hasNext();) {
            final PersistentNakedClass persistentClass = iter.next();
            if (persistentClass.isAbstract() && !persistentClass.isReferenced()) {
                iter.remove();
                persistentClass.removeFromHierarchy();
            }
        }
    }

    private boolean subclassesImplementOnlyThisInterface(
            final NakedObjectSpecification persistentClass,
            final NakedObjectSpecification interfaceToCheck) {

        final NakedObjectSpecification[] childSpecs = persistentClass.subclasses();
        for (int i = 0; i < childSpecs.length; i++) {
            final NakedObjectSpecification childSpec = childSpecs[i];
            if (childSpec.isEncodeable() || childSpec.persistability() == Persistability.TRANSIENT) {
                continue;
            }
            final NakedObjectSpecification[] implementedInterfaces = childSpec.interfaces();
            if (implementedInterfaces.length == 0) {
                continue;
            } else if (implementedInterfaces.length > 1) {
                return false;
            }
            if (!implementedInterfaces[0].getFullName().equals(interfaceToCheck.getFullName())) {
                return false;
            }
            return subclassesImplementOnlyThisInterface(childSpec, interfaceToCheck);
        }
        return true;
    }

    private boolean isSubclassOf(final NakedObjectSpecification persistentClass, final NakedObjectSpecification subclassToCheck) {

        final NakedObjectSpecification[] childSpecs = persistentClass.subclasses();
        for (int i = 0; i < childSpecs.length; i++) {
            final NakedObjectSpecification childSpec = childSpecs[i];
            if (childSpec.getFullName().equals(subclassToCheck.getFullName())) {
                return true;
            }
            if (isSubclassOf(childSpec, subclassToCheck)) {
                return true;
            }
        }
        return false;
    }

    private boolean interfaceImplementedByOtherNonRelatedClass(
            final NakedObjectSpecification implementingClass,
            final NakedObjectSpecification interfaceToCheck) {

        for (final Iterator<PersistentNakedClass> iter = classes.values().iterator(); iter.hasNext();) {
            final PersistentNakedClass otherClass = iter.next();
            if (implementingClass.equals(otherClass.getSpecification())) {
                continue;
            }
            final NakedObjectSpecification[] implementedInterfaces = otherClass.getSpecification().interfaces();
            for (int i = 0; i < implementedInterfaces.length; i++) {
                if (implementedInterfaces[i].getFullName().equals(interfaceToCheck.getFullName())) {
                    if (!isSubclassOf(implementingClass, otherClass.getSpecification())) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    private void optimiseInterfaces() {
        // one safe optimisation
        // 1. class extends a non-persistent class and implements an interface
        // 2. no subclass implements any other interface (may implemnt same one again)
        // 3. no other non-subclass persistent class implements the interface.
        final PersistentNakedClass[] persistentClasses = classes.values().toArray(
                new PersistentNakedClass[classes.values().size()]);
        for (int i = 0; i < persistentClasses.length; i++) {
            final PersistentNakedClass persistentClass = persistentClasses[i];
            if (classes.containsValue(persistentClass.getParent())) {
                continue;
            }
            final NakedObjectSpecification[] implementedInterfaces = persistentClass.getSpecification().interfaces();
            if (implementedInterfaces.length != 1) {
                continue;
            }
            final NakedObjectSpecification classToCheck = persistentClass.getSpecification();
            final NakedObjectSpecification interfaceToCheck = implementedInterfaces[0];
            if (!subclassesImplementOnlyThisInterface(classToCheck, interfaceToCheck)) {
                continue;
            }
            if (interfaceImplementedByOtherNonRelatedClass(classToCheck, interfaceToCheck)) {
                continue;
            }
            final PersistentNakedClass persistentInterface = interfaces.get(interfaceToCheck.getFullName());
            persistentInterface.setParent(rootClass);
            interfaces.remove(persistentInterface.getName());
            classes.put(persistentInterface.getName(), persistentInterface);
            persistentClass.setParent(persistentInterface);
        }
    }
}
// Copyright (c) Naked Objects Group Ltd.
