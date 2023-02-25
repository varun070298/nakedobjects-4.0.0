package org.nakedobjects.metamodel.facets.ordering;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;
import java.util.StringTokenizer;
import java.util.TreeSet;
import java.util.Vector;

import org.nakedobjects.metamodel.facets.ordering.memberorder.DeweyOrderSet;


public class OrderSet implements Comparable {

    private final Vector<Object> elements = new Vector<Object>();
    private final String groupFullName;
    private final String groupName;
    private final String groupPath;

    public OrderSet(final String groupFullName) {
        this.groupFullName = groupFullName;

        groupName = deriveGroupName(groupFullName);
        groupPath = deriveGroupPath(groupFullName);
    }

    // ///////////////// Group Name etc ////////////////////

    /**
     * Last component of the comma-separated group name supplied in the constructor (analogous to the file
     * name extracted from a fully qualified file name).
     * 
     * <p>
     * For example, if supplied <tt>abc,def,ghi</tt> in the constructor, then this will return <tt>ghi</tt>.
     * 
     * @return
     */
    public String getGroupName() {
        return groupName;
    }

    /**
     * The group name exactly as it was supplied in the constructor (analogous to a fully qualified file
     * name).
     * 
     * <p>
     * For example, if supplied <tt>abc,def,ghi</tt> in the constructor, then this will return the same string
     * <tt>abc,def,ghi</tt>.
     * 
     * @return
     */
    public String getGroupFullName() {
        return groupFullName;
    }

    /**
     * Represents the parent groups, derived from the group name supplied in the constructor (analogous to the
     * directory portion of a fully qualified file name).
     * 
     * <p>
     * For example, if supplied <tt>abc,def,ghi</tt> in the constructor, then this will return
     * <tt>abc,def</tt>.
     * 
     * @return
     */
    public String getGroupPath() {
        return groupPath;
    }

    /**
     * Splits name by comma, then title case the last component.
     * 
     * @param groupFullName
     * @return
     */
    private static String deriveGroupName(final String groupFullName) {
        final StringTokenizer tokens = new StringTokenizer(groupFullName, ",", false);
        final String[] groupNameComponents = new String[tokens.countTokens()];
        for (int i = 0; tokens.hasMoreTokens(); i++) {
            groupNameComponents[i] = tokens.nextToken();
        }
        final String groupSimpleName = groupNameComponents.length > 0 ? groupNameComponents[groupNameComponents.length - 1] : "";
        if (groupSimpleName.length() > 1) {
            return groupSimpleName.substring(0, 1).toUpperCase() + groupSimpleName.substring(1);
        } else {
            return groupSimpleName.toUpperCase();
        }

    }

    /**
     * Everything upto the last comma, else empty string if none.
     * 
     * @param groupFullName
     * @return
     */
    private static String deriveGroupPath(final String groupFullName) {
        final int lastComma = groupFullName.lastIndexOf(",");
        if (lastComma == -1) {
            return "";
        }
        return groupFullName.substring(0, lastComma);
    }

    // ///////////////////// Parent & Children ///////////////////

    private OrderSet parent;

    protected void setParent(final DeweyOrderSet parent) {
        this.parent = parent;
    }

    public OrderSet getParent() {
        return parent;
    }

    /**
     * A staging area until we are ready to add the child sets to the collection of elements owned by the
     * superclass.
     */
    protected SortedSet<DeweyOrderSet> childOrderSets = new TreeSet<DeweyOrderSet>();

    protected void addChild(final DeweyOrderSet childOrderSet) {
        childOrderSets.add(childOrderSet);
    }

    public List<DeweyOrderSet> children() {
        final ArrayList<DeweyOrderSet> list = new ArrayList<DeweyOrderSet>();
        list.addAll(childOrderSets);
        return list;
    }

    protected void copyOverChildren() {
        addAll(childOrderSets);
    }

    // ///////////////////// Elements (includes children) ///////////////////

    /**
     * Returns a copy of the elements, in sequence.
     * 
     * @return
     */
    public List<Object> elementList() {
        final ArrayList<Object> list = new ArrayList<Object>();
        list.addAll(elements);
        return list;
    }

    public int size() {
        return elements.size();
    }

    protected void addElement(final Object element) {
        elements.add(element);
    }

    public Enumeration<Object> elements() {
        return elements.elements();
    }

    protected void addAll(final SortedSet<DeweyOrderSet> sortedMembers) {
        for (final Iterator<DeweyOrderSet> iter = sortedMembers.iterator(); iter.hasNext();) {
            this.addElement(iter.next());
        }
    }

    // ///////////////////////// compareTo //////////////////////

    /**
     * Natural ordering is to compare by {@link #getGroupFullName()}.
     */
    public int compareTo(final Object o) {
        return compareTo((OrderSet) o);
    }

    public int compareTo(final OrderSet o) {
        return getGroupFullName().compareTo(o.getGroupFullName());
    }

}

// Copyright (c) Naked Objects Group Ltd.
