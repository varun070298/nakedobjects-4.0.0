package org.nakedobjects.runtime.util;

import java.util.Enumeration;
import java.util.Vector;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.authentication.AuthenticationSession;
import org.nakedobjects.metamodel.commons.debug.Debug;
import org.nakedobjects.metamodel.commons.debug.DebugInfo;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.SpecificationFacets;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAction;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionConstants;
import org.nakedobjects.metamodel.spec.feature.NakedObjectActionParameter;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFacets;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.metamodel.util.SpecUtils;


public final class Dump {

	private Dump() {}
	
    private static void collectionGraph(
            final NakedObject collection,
            final int level,
            final Vector<NakedObject> ignoreObjects,
            final DebugString s, AuthenticationSession authenticationSession) {

        if (ignoreObjects.contains(collection)) {
            s.append("*\n");
        } else {
            ignoreObjects.addElement(collection);
            final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
            final Enumeration e = facet.elements(collection);
            while (e.hasMoreElements()) {
                graphIndent(s, level);
                final NakedObject element = ((NakedObject) e.nextElement());
                s.append(element);
                if (ignoreObjects.contains(element)) {
                    s.append("*\n");
                } else {
                    s.indent();
                    graph(element, level + 1, ignoreObjects, s, authenticationSession);
                    s.unindent();
                }
            }
        }
    }

    /**
     * Creates an ascii object graph diagram for the specified object, up to three levels deep.
     * @param authenticationSession TODO
     */
    public static String graph(final NakedObject object, AuthenticationSession authenticationSession) {
        final DebugString s = new DebugString();
        graph(object, s, authenticationSession);
        return s.toString();
    }

    public static void graph(final NakedObject object, final DebugString s, AuthenticationSession authenticationSession) {
        simpleObject(object, s);
        s.appendln();
        s.append(object);
        graph(object, 0, new Vector<NakedObject>(25, 10), s, authenticationSession);
    }

    private static void simpleObject(final NakedObject object, final DebugString s) {
        s.appendln(object.titleString());
        s.indent();
        final NakedObjectSpecification objectSpec = object.getSpecification();
        if (objectSpec.isCollection()) {
            final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(object);
            final Enumeration e = facet.elements(object);
            int i = 1;
            while (e.hasMoreElements()) {
                final NakedObject element = ((NakedObject) e.nextElement());
                s.appendln(i++ + " " + element.titleString());
            }
        } else if (object instanceof NakedObject) {
            try {
                NakedObjectAssociation[] fields;
                fields = objectSpec.getAssociations();
                for (int i = 0; i < fields.length; i++) {
                    final NakedObjectAssociation field = fields[i];
                    final NakedObject obj = field.get((NakedObject) object);

                    final String name = field.getId();
                    if (obj == null) {
                        s.appendln(name, "null");
                    } else {
                        s.appendln(name, obj.titleString());
                    }
                }
            } catch (final RuntimeException e) {
                s.appendException(e);
            }
        }
        s.unindent();
    }

    private static void graph(
    		final NakedObject object, 
    		final int level, 
    		final Vector<NakedObject> ignoreObjects, 
    		final DebugString info, 
    		final AuthenticationSession authenticationSession) {
        if (level > 3) {
            info.appendln("..."); // only go 3 levels?
        } else {
            info.append("\n");
            if (object.getSpecification().isCollection()) {
                collectionGraph((NakedObject) object, level, ignoreObjects, info, authenticationSession);
            } else if (object.getSpecification().isObject()) {
                objectGraph((NakedObject) object, level, ignoreObjects, info, authenticationSession);
            } else {
                info.append("??? " + object);
            }
        }
    }

    /**
     * Creates an ascii object graph diagram for the specified object, up to three levels deep, and not
     * expanding any of the objects in the excludedObjects vector.
     * @param authenticationSession TODO
     */
    public static String graph(final NakedObject object, final Vector<NakedObject> excludedObjects, AuthenticationSession authenticationSession) {
        final DebugString s = new DebugString();
        s.append(object);
        graph(object, 0, excludedObjects, s, authenticationSession);
        return s.toString();
    }

    private static void graphIndent(final DebugString s, final int level) {
        for (int indent = 0; indent < level; indent++) {
            s.append(Debug.indentString(4) + "|");
        }
        s.append(Debug.indentString(4) + "+--");
    }

    public static String adapter(final NakedObject object) {
        final DebugString s = new DebugString();
        adapter(object, s);
        return s.toString();
    }

    public static void adapter(final NakedObject adapter, final DebugString string) {
        try {
            string.appendln("Adapter", adapter.getClass().getName());
            string.appendln("Class", adapter.getObject() == null ? "none" : adapter.getObject().getClass().getName());
            string.appendAsHexln("Hash", adapter.hashCode());
            string.appendln("Object", adapter.getObject());
            string.appendln("Title", adapter.titleString());
            string.appendln("Specification", adapter.getSpecification().getFullName());

            string.appendln();

            string.appendln("Icon", adapter.getIconName());
            string.appendln("OID", adapter.getOid());
            string.appendln("State", adapter.getResolveState());
            string.appendln("Version", adapter.getVersion());
            
        } catch (final RuntimeException e) {
            string.appendException(e);
        }

    }

    private static void objectGraph(
    		final NakedObject object, 
    		final int level, 
    		final Vector<NakedObject> ignoreObjects, 
    		final DebugString s, 
    		final AuthenticationSession authenticationSession) {
    	ignoreObjects.addElement(object);

        try {
            // work through all its fields
            NakedObjectAssociation[] fields;
            fields = object.getSpecification().getAssociations();
            for (int i = 0; i < fields.length; i++) {
                final NakedObjectAssociation field = fields[i];
                final NakedObject obj = field.get(object);
                final String name = field.getId();
                graphIndent(s, level);

				if (field.isVisible(authenticationSession, object).isVetoed()) {
                    s.append(name + ": (not visible)");
                    s.append("\n");
                } else {
                    if (obj == null) {
                        s.append(name + ": null\n");
                        /*
                         * } else if (obj.getSpecification().isParseable()) { s.append(name + ": " +
                         * obj.titleString()); s.append("\n");
                         */} else {
                        if (ignoreObjects.contains(obj)) {
                            s.append(name + ": " + obj + "*\n");
                        } else {
                            s.append(name + ": " + obj);
                            graph(obj, level + 1, ignoreObjects, s, authenticationSession);

                        }
                    }
                }
            }
        } catch (final RuntimeException e) {
            s.appendException(e);
        }

    }

    public static String specification(final NakedObject object) {
        final DebugString s = new DebugString();
        specification(object, s);
        return s.toString();
    }

    public static void specification(final NakedObject nakedObject, final DebugString debug) {
        final NakedObjectSpecification specification = nakedObject.getSpecification();
        specification(specification, debug);
    }

    public static void specification(final NakedObjectSpecification specification, final DebugString debug) {
        try {
            debug.appendTitle(specification.getClass().getName());
            debug.appendAsHexln("Hash code", specification.hashCode());
            debug.appendln("ID", specification.getIdentifier());
            debug.appendln("Full Name", specification.getFullName());
            debug.appendln("Short Name", specification.getShortName());
            debug.appendln("Singular Name", specification.getSingularName());
            debug.appendln("Plural Name", specification.getPluralName());
            debug.appendln("Description", specification.getDescription());
            debug.blankLine();
            debug.appendln("Features", featureList(specification));
            debug.appendln("Type", SpecUtils.typeNameFor(specification));
            if (specification.superclass() != null) {
                debug.appendln("Superclass", specification.superclass().getFullName());
            }
            debug.appendln("Interfaces", specificationNames(specification.interfaces()));
            debug.appendln("Subclasses", specificationNames(specification.subclasses()));
            debug.blankLine();
            debug.appendln("Service", specification.isService());
            debug.appendln("Encodable", specification.isEncodeable());
            debug.appendln("Parseable", specification.isParseable());
            debug.appendln("Aggregated", specification.isValueOrIsAggregated());

        } catch (final RuntimeException e) {
            debug.appendException(e);
        }

        if (specification instanceof DebugInfo) {
            ((DebugInfo) specification).debugData(debug);
        }

        debug.blankLine();

        debug.appendln("Facets");
        final Class<? extends Facet>[] facetTypes = specification.getFacetTypes();
        debug.indent();
        if (facetTypes.length == 0) {
            debug.appendln("none");
        } else {
            for (int i = 0; i < facetTypes.length; i++) {
                final Class<? extends Facet> type = facetTypes[i];
                final Facet facet = specification.getFacet(type);
                debug.appendln(facet.toString());
            }
        }
        debug.unindent();
        debug.blankLine();

        debug.appendln("Fields");
        debug.indent();
        specificationFields(specification, debug);
        debug.unindent();

        debug.appendln("Object Actions");
        debug.indent();
        specificationActionMethods(specification, debug);
        debug.unindent();

        debug.appendln("Related Service Actions");
        debug.indent();
        specificationServiceMethods(specification, debug);
        debug.unindent();
    }

    public static String featureList(final NakedObjectSpecification specification) {
        final StringBuffer str = new StringBuffer();
        if (specification.isAbstract()) {
            str.append("Abstract ");
        }
        if (SpecificationFacets.isBoundedSet(specification)) {
            str.append("Bounded ");
        }
        if (SpecificationFacets.isCached(specification)) {
            str.append("Cached ");
        }
        if (SpecificationFacets.isAlwaysImmutable(specification)) {
            str.append("Immutable (always) ");
        }
        if (SpecificationFacets.isImmutableOncePersisted(specification)) {
            str.append("Immutable (once persisted) ");
        }
        if (specification.isService()) {
            str.append("Service ");
        }
        return str.toString();
    }

    private static void specificationActionMethods(final NakedObjectSpecification specification, final DebugString debug) {
        try {
            final NakedObjectAction[] userActions = specification.getObjectActions(NakedObjectActionConstants.USER);
            final NakedObjectAction[] explActions = specification.getObjectActions(NakedObjectActionConstants.EXPLORATION);
            final NakedObjectAction[] debActions = specification.getObjectActions(NakedObjectActionConstants.DEBUG);
            specificationMethods(userActions, explActions, debActions, debug);
        } catch (final RuntimeException e) {
            debug.appendException(e);
        }
    }

    private static void specificationServiceMethods(final NakedObjectSpecification specification, final DebugString debug) {
        try {
            final NakedObjectAction[] userActions = specification.getServiceActionsFor(NakedObjectActionConstants.USER);
            final NakedObjectAction[] explActions = specification.getServiceActionsFor(NakedObjectActionConstants.EXPLORATION);
            final NakedObjectAction[] debActions = specification.getServiceActionsFor(NakedObjectActionConstants.DEBUG);
            specificationMethods(userActions, explActions, debActions, debug);
        } catch (final RuntimeException e) {
            debug.appendException(e);
        }
    }

    private static void specificationFields(final NakedObjectSpecification specification, final DebugString debug) {
        final NakedObjectAssociation[] fields = specification.getAssociations();
        debug.appendln("All");
        debug.indent();
        for (int i = 0; i < fields.length; i++) {
            debug.appendln((i + 1) + "." + fields[i].getId());
        }
        debug.unindent();

        final NakedObjectAssociation[] fields2 = specification
                .getAssociations(NakedObjectAssociationFilters.STATICALLY_VISIBLE_ASSOCIATIONS);
        debug.appendln("Static");
        debug.indent();
        for (int i = 0; i < fields2.length; i++) {
            debug.appendln((i + 1) + "." + fields2[i].getId());
        }
        debug.unindent();
        debug.appendln();

        try {
            if (fields.length == 0) {
                debug.appendln("none");
            } else {
                for (int i = 0; i < fields.length; i++) {

                    final NakedObjectAssociation field = (NakedObjectAssociation) fields[i];
                    debug.appendln((i + 1) + "." + field.getId() + "  (" + field.getClass().getName() + ")");

                    debug.indent();
                    final String description = field.getDescription();
                    if (description != null && !description.equals("")) {
                        debug.appendln("Description", description);
                    }
                    final String help = field.getHelp();
                    if (help != null && !help.equals("")) {
                        debug
                                .appendln("Help", help.substring(0, Math.min(30, help.length()))
                                        + (help.length() > 30 ? "..." : ""));
                    }

                    debug.appendln("ID", field.getIdentifier());
                    debug.appendln("Short ID", field.getId());
                    debug.appendln("Name", field.getName());
                    final String type = field.isOneToManyAssociation() ? "Collection" : field.isOneToOneAssociation() ? "Object" : "Unknown";
                    debug.appendln("Type", type);
                    debug.appendln("Has identity", !field.getSpecification().isCollectionOrIsAggregated());
                    debug.appendln("Spec", field.getSpecification().getFullName());

                    debug.appendln("Flags", (NakedObjectAssociationFacets.isHidden(field) ? "" : "Visible ")
                            + (NakedObjectAssociationFacets.isNotPersisted(field) ? "Not Persisted" : " ")
                            + (field.isMandatory() ? "Mandatory " : ""));

                    final Class<? extends Facet>[] facets = field.getFacetTypes();
                    if (facets.length > 0) {
                        debug.appendln("Facets");
                        debug.indent();
                        boolean none = true;
                        for (int j = 0; j < facets.length; j++) {
                            debug.appendln(field.getFacet(facets[j]).toString());
                            none = false;
                        }
                        if (none) {
                            debug.appendln("none");
                        }
                        debug.unindent();
                    }

                    debug.appendln(field.debugData());

                    debug.unindent();
                    debug.unindent();
                    debug.indent();

                }

            }
        } catch (final RuntimeException e) {
            debug.appendException(e);
        }

    }

    private static void specificationMethods(
            final NakedObjectAction[] userActions,
            final NakedObjectAction[] explActions,
            final NakedObjectAction[] debActions,
            final DebugString debug) {
        if (userActions.length == 0 && explActions.length == 0 && debActions.length == 0) {
            debug.appendln("no actions...");
        } else {
            debug.appendln("User actions");
            debug.indent();
            for (int i = 0; i < userActions.length; i++) {
                actionDetails(debug, userActions[i], 8, i);
            }
            debug.unindent();

            debug.appendln("Exploration actions");
            debug.indent();
            for (int i = 0; i < explActions.length; i++) {
                actionDetails(debug, explActions[i], 8, i);
            }
            debug.unindent();

            debug.appendln("Debug actions");
            debug.indent();
            for (int i = 0; i < debActions.length; i++) {
                actionDetails(debug, debActions[i], 8, i);
            }
            debug.unindent();
        }
    }

    private static void actionDetails(final DebugString debug, final NakedObjectAction a, final int indent, final int count) {
        debug.appendln((count + 1) + "." + a.getId() + " (" + a.getClass().getName() + ")");
        debug.indent();
        final int newIndent = indent + 4;
        try {
            final NakedObjectAction[] debActions = a.getActions();
            if (debActions.length > 0) {
                for (int i = 0; i < debActions.length; i++) {
                    actionDetails(debug, debActions[i], newIndent, i);
                }

            } else {
                if (a.getDescription() != null && !a.getDescription().equals("")) {
                    debug.appendln("Description", a.getDescription());
                }
                debug.appendln("ID", a.getId());
                // debug.appendln(12, "Returns", f.getReturnType() == null ? "Nothing" :
                // f.getReturnType().getFullName());

                debug.appendln(a.debugData());
                debug.appendln("Target", a.getTarget());
                debug.appendln("On type", a.getOnType());

                final Class<? extends Facet>[] facets = a.getFacetTypes();
                if (facets.length > 0) {
                    debug.appendln("Facets");
                    debug.indent();
                    for (int j = 0; j < facets.length; j++) {
                        debug.appendln(a.getFacet(facets[j]).toString());
                    }
                    debug.unindent();
                }

                final NakedObjectSpecification returnType = a.getReturnType();
                debug.appendln("Returns", returnType == null ? "VOID" : returnType.toString());

                final NakedObjectActionParameter[] parameters = a.getParameters();
                if (parameters.length == 0) {
                    debug.appendln("Parameters", "none");
                } else {
                    debug.appendln("Parameters");
                    debug.indent();
                    final NakedObjectActionParameter[] p = a.getParameters();
                    for (int j = 0; j < parameters.length; j++) {
                        debug.append(p[j].getName());
                        debug.append(" (");
                        debug.append(parameters[j].getSpecification().getFullName());
                        debug.appendln(")");
                        debug.indent();
                        final Class<? extends Facet>[] parameterFacets = p[j].getFacetTypes();
                        for (int i = 0; i < parameterFacets.length; i++) {
                            debug.appendln(p[j].getFacet(parameterFacets[i]).toString());
                        }
                        debug.unindent();
                    }
                    debug.unindent();
                }
            }
        } catch (final RuntimeException e) {
            debug.appendException(e);
        }

        debug.unindent();
    }

    private static String[] specificationNames(final NakedObjectSpecification[] specifications) {
        final String[] names = new String[specifications.length];
        for (int i = 0; i < names.length; i++) {
            names[i] = specifications[i].getFullName();
        }
        return names;
    }

}
// Copyright (c) Naked Objects Group Ltd.
