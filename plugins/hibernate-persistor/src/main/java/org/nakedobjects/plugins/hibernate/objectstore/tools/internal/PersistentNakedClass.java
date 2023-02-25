package org.nakedobjects.plugins.hibernate.objectstore.tools.internal;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;


/**
 * Details of a persistent class within Naked Objects to be updated by Hibernate. Holds details of the
 * hierarchy, so it can be mapped to class/subclass within Hibernate.
 */
public class PersistentNakedClass {
    private PersistentNakedClass parent = null;
    private final List<PersistentNakedClass> subClasses = new ArrayList<PersistentNakedClass>();
    private final NakedObjectSpecification spec;
    private final String name;
    private final boolean root;
    private int referenceCount = 0;
    private NakedObjectAssociation[] uniqueFields = null;
    private String tableName;
    private boolean duplicateUnqualifiedClassName = false;
    private boolean requireVersion = false;
    private final HashMap<String, Association> associations = new HashMap<String, Association>();

    public PersistentNakedClass() {
        this.name = "root";
        this.spec = null;
        root = true;
    }

    public PersistentNakedClass(final NakedObjectSpecification spec, final PersistentNakedClass parent) {
        this.spec = spec;
        this.name = spec.getFullName();
        root = false;
        if (parent != null) {
            this.parent = parent;
            parent.subClasses.add(this);
        }
    }

    public void addReference() {
        referenceCount++;
    }

    protected void debugString(final StringBuffer sb, final String prefix) {
        sb.append("\n" + prefix + name);
        if (referenceCount > 0) {
            sb.append(" (ref=").append(referenceCount).append(")");
        }
        for (final Iterator<PersistentNakedClass> iter = subClasses.iterator(); iter.hasNext();) {
            iter.next().debugString(sb, "  " + prefix);
        }
    }

    private void ensureUniqueFieldsResolved() {
        if (uniqueFields != null) {
            return;
        }
        if (parent.isRoot()) {
            uniqueFields = spec.getAssociations();
            return;
        }
        final NakedObjectAssociation[] parentFields = parent.getSpecification().getAssociations();
        final HashMap<String, String> parentIds = new HashMap<String, String>();
        for (int i = 0; i < parentFields.length; i++) {
            if (!parentFields[i].isDerived()) {
                parentIds.put(parentFields[i].getId(), "");
            }
        }

        final List<NakedObjectAssociation> uniqueList = new ArrayList<NakedObjectAssociation>();
        final NakedObjectAssociation[] fields = spec.getAssociations();
        for (int i = 0; i < fields.length; i++) {
            if (!parentIds.containsKey(fields[i].getId())) {
                uniqueList.add(fields[i]);
            }
        }
        uniqueFields = uniqueList.toArray(new NakedObjectAssociation[0]);
    }

    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PersistentNakedClass other = (PersistentNakedClass) obj;
        if (!name.equals(other.name)) {
            return false;
        }
        return true;
    }

    public String getName() {
        return name;
    }

    public PersistentNakedClass getParent() {
        return parent;
    }

    public int getReferenceCount() {
        return referenceCount;
    }

    public NakedObjectSpecification getSpecification() {
        return spec;
    }

    public Iterator<PersistentNakedClass> getSubClasses() {
        return subClasses.iterator();
    }

    public PersistentNakedClass[] getSubClassesArray() {
        return subClasses.toArray(new PersistentNakedClass[0]);
    }

    public String getTableName() {
        return tableName;
    }

    /**
     * Return the one, and only one association from this persistent class to the associated class.
     */
    public NakedObjectAssociation getUniqueAssociation(final String associatedClassName) {
        ensureUniqueFieldsResolved();
        NakedObjectAssociation association = null;
        for (int i = 0; i < uniqueFields.length; i++) {
            if (uniqueFields[i].isOneToOneAssociation() || uniqueFields[i].isOneToManyAssociation()) {
                if (associatedClassName.equals(uniqueFields[i].getSpecification().getFullName())) {
                    if (association != null) {
                        return null;
                    }
                    association = uniqueFields[i];
                }
            }
        }
        return association;
    }

    /**
     * Fields unique to this class, i.e. not further up the hierarchy. Note: when this method is called the
     * hierarchy may have been stripped down to remove abstact classes, so won't necessarily be the classes
     * declared fields.
     */
    public NakedObjectAssociation[] getUniqueFields() {
        ensureUniqueFieldsResolved();
        return uniqueFields;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    public boolean hasSubClasses() {
        return subClasses.size() > 0;
    }

    public boolean isAbstract() {
        // return supportsFeature(specification, NakedObjectSpecification.ABSTRACT);
        return spec.isAbstract();
    }

    public boolean isInterface() {
        try {
            return Class.forName(name).isInterface();
        } catch (final ClassNotFoundException e) {
            throw new NakedObjectException(e);
        }
    }

    public boolean isReferenced() {
        return referenceCount > 0;
    }

    public boolean isRoot() {
        return root;
    }

    /**
     * Return true if there is one, and only one association from this persistent class to the associated
     * class.
     */
    public boolean isUniqueAssociation(final String associatedClassName) {
        return getUniqueAssociation(associatedClassName) != null;
    }

    /**
     * This class is to be removed, so change the hierarchy so the superclass has all the subclasses
     */
    public void removeFromHierarchy() {
        if (hasSubClasses()) {
            for (final Iterator<PersistentNakedClass> iter = subClasses.iterator(); iter.hasNext();) {
                iter.next().parent = parent;
            }
            parent.subClasses.addAll(subClasses);
        }
        if (parent != null) {
            parent.subClasses.remove(this);
            parent = null;
        }
    }

    public void setParent(final PersistentNakedClass newParent) {
        if (parent != null) {
            parent.subClasses.remove(this);
        }
        this.parent = newParent;
        newParent.subClasses.add(this);
    }

    public void setTableName(final String tableName) {
        this.tableName = tableName;
    }

    @Override
    public String toString() {
        return "PersistentNakedClass[name=" + name + (root ? " (root)" : "")
                + (referenceCount > 0 ? "(ref=" + referenceCount + ")" : "") + "]";
    }

    public boolean isDuplicateUnqualifiedClassName() {
        return duplicateUnqualifiedClassName;
    }

    public void setDuplicateUnqualifiedClassName(final boolean duplicateUnqualifiedClassName) {
        this.duplicateUnqualifiedClassName = duplicateUnqualifiedClassName;
    }

    public boolean isRequireVersion() {
        return requireVersion;
    }

    public void setRequireVersion(final boolean requireVersion) {
        this.requireVersion = requireVersion;
    }

    public void addAssociation(final String name, final Association association) {
        associations.put(name, association);
    }

    public Association getAssociation(final String name) {
        return associations.get(name);
    }

    public boolean hasAssociation(final String name) {
        return associations.containsKey(name);
    }
}
// Copyright (c) Naked Objects Group Ltd.
