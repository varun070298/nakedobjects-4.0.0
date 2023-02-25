package org.nakedobjects.plugins.dnd.viewer.content;

import java.util.Enumeration;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.adapter.ResolveState;
import org.nakedobjects.metamodel.commons.debug.DebugString;
import org.nakedobjects.metamodel.commons.exceptions.UnexpectedCallException;
import org.nakedobjects.metamodel.consent.Consent;
import org.nakedobjects.metamodel.consent.ConsentAbstract;
import org.nakedobjects.metamodel.facets.actcoll.typeof.TypeOfFacet;
import org.nakedobjects.metamodel.facets.collections.modify.CollectionFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.util.CollectionFacetUtils;
import org.nakedobjects.plugins.dnd.CollectionContent;
import org.nakedobjects.plugins.dnd.CollectionSorter;
import org.nakedobjects.plugins.dnd.Comparator;
import org.nakedobjects.plugins.dnd.UserAction;
import org.nakedobjects.plugins.dnd.UserActionSet;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.Workspace;
import org.nakedobjects.plugins.dnd.viewer.action.AbstractUserAction;
import org.nakedobjects.plugins.dnd.viewer.drawing.Image;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.image.ImageFactory;


public abstract class AbstractCollectionContent extends AbstractContent implements CollectionContent {
    private static final TypeComparator TYPE_COMPARATOR = new TypeComparator();
    private static final TitleComparator TITLE_COMPARATOR = new TitleComparator();
    private final static CollectionSorter sorter = new SimpleCollectionSorter();
    private Comparator order;
    private boolean reverse;

    public final Enumeration allElements() {
        final NakedObject[] elements = elements();

        sorter.sort(elements, order, reverse);

        return new Enumeration() {
            int i = 0;
            int size = elements.length;

            public boolean hasMoreElements() {
                return i < size;
            }

            public Object nextElement() {
                return elements[i++];
            }
        };
    }

    public void debugDetails(final DebugString debug) {
        debug.appendln("order", order);
        debug.appendln("reverse order", reverse);
    }

    public NakedObject[] elements() {
        final NakedObject collection = getCollection();
        final CollectionFacet facet = CollectionFacetUtils.getCollectionFacetFromSpec(collection);
        final NakedObject[] elementsArray = new NakedObject[facet.size(collection)];
        int i = 0;
        final Enumeration elements = facet.elements(collection);
        while (elements.hasMoreElements()) {
            elementsArray[i++] = (NakedObject) elements.nextElement();
        }
        return elementsArray;
    }

    public abstract NakedObject getCollection();

    public NakedObjectSpecification getElementSpecification() {
        final NakedObject collection = getCollection();
        final TypeOfFacet facet = collection.getTypeOfFacet();
        return facet.valueSpec();
    }

    public String getDescription() {
        return "Collection";
    }

    @Override
    public void contentMenuOptions(final UserActionSet options) {
        final NakedObject collection = getCollection();

        OptionFactory.addObjectMenuOptions(collection, options);

        // TODO find all collection actions, and make them available
        // not valid ObjectOption.menuOptions((NakedObject) object, options);
        /*
         * Action[] actions = collection.getSpecification().getObjectActions(Action.USER);
         * 
         * for (int i = 0; i < actions.length; i++) { final Action action = actions[i]; AbstractUserAction
         * option; option = new AbstractUserAction(actions[i].getId()) { public void execute(final Workspace
         * workspace, final View view, final Location at) { Naked result = collection.execute(action, new
         * Naked[0]); at.add(20, 20); workspace.addOpenViewFor(result, at); } };
         * 
         * if (option != null) { options.add(option); } }
         */
        options.add(new AbstractUserAction("Clear resolved", UserAction.DEBUG) {
            @Override
            public Consent disabled(final View component) {
                return ConsentAbstract.allowIf(collection == null || collection.getResolveState() != ResolveState.TRANSIENT
                        || collection.getResolveState() == ResolveState.GHOST);
            }

            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                collection.changeState(ResolveState.GHOST);
            }
        });

    }

    @Override
    public void viewMenuOptions(final UserActionSet options) {
        final UserActionSet sortOptions = new UserActionSet("Sort", options);
        options.add(sortOptions);

        sortOptions.add(new AbstractUserAction("Clear") {
            @Override
            public Consent disabled(final View component) {
                return ConsentAbstract.allowIf(order != null);
            }

            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                order = null;
                view.invalidateContent();
            }
        });

        if (reverse) {
            sortOptions.add(new AbstractUserAction("Normal sort order") {
                @Override
                public Consent disabled(final View component) {
                    return ConsentAbstract.allowIf(order != null);
                }

                @Override
                public void execute(final Workspace workspace, final View view, final Location at) {
                    reverse = false;
                    view.invalidateContent();
                }
            });
        } else {
            sortOptions.add(new AbstractUserAction("Reverse sort order") {
                @Override
                public Consent disabled(final View component) {
                    return ConsentAbstract.allowIf(order != null);
                }

                @Override
                public void execute(final Workspace workspace, final View view, final Location at) {
                    reverse = true;
                    view.invalidateContent();
                }
            });
        }

        sortOptions.add(new AbstractUserAction("Sort by title") {
            @Override
            public Consent disabled(final View component) {
                return ConsentAbstract.allowIf(order != TITLE_COMPARATOR);
            }

            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                order = TITLE_COMPARATOR;
                view.invalidateContent();
            }
        });

        sortOptions.add(new AbstractUserAction("Sort by type") {
            @Override
            public Consent disabled(final View component) {
                return ConsentAbstract.allowIf(order != TYPE_COMPARATOR);
            }

            @Override
            public void execute(final Workspace workspace, final View view, final Location at) {
                order = TYPE_COMPARATOR;
                view.invalidateContent();
            }
        });

        final NakedObjectAssociation[] fields = getElementSpecification().getAssociations();
        for (int i = 0; i < fields.length; i++) {
            final NakedObjectAssociation field = fields[i];

            sortOptions.add(new AbstractUserAction("Sort by " + field.getName()) {
                @Override
                public void execute(final Workspace workspace, final View view, final Location at) {
                    order = new FieldComparator(field);
                    view.invalidateContent();
                }
            });
        }
    }

    public void parseTextEntry(final String entryText) {
        throw new UnexpectedCallException();
    }

    public void setOrder(final Comparator order) {
        this.order = order;
    }

    public void setOrderByField(final NakedObjectAssociation field) {
        if (order instanceof FieldComparator && ((FieldComparator) order).getField() == field) {
            reverse = !reverse;
        } else {
            order = new FieldComparator(field);
            reverse = false;
        }
    }

    public void setOrderByElement() {
        if (order == TITLE_COMPARATOR) {
            reverse = !reverse;
        } else {
            order = TITLE_COMPARATOR;
            reverse = false;
        }
    }

    public NakedObjectAssociation getFieldSortOrder() {
        if (order instanceof FieldComparator) {
            return ((FieldComparator) order).getField();
        } else {
            return null;
        }
    }

    public Image getIconPicture(final int iconHeight) {
        final NakedObject nakedObject = getCollection();
        if (nakedObject == null) {
            return ImageFactory.getInstance().loadIcon("emptyField", iconHeight, null);
        }
        final NakedObjectSpecification specification = nakedObject.getSpecification();
        Image icon = ImageFactory.getInstance().loadIcon(specification, iconHeight, null);
        if (icon == null) {
            icon = ImageFactory.getInstance().loadDefaultIcon(iconHeight, null);
        }
        return icon;
    }

    public boolean getOrderByElement() {
        return order == TITLE_COMPARATOR;
    }

    public boolean getReverseSortOrder() {
        return reverse;
    }

    public boolean isOptionEnabled() {
        return false;
    }

    public NakedObject[] getOptions() {
        return null;
    }
}
// Copyright (c) Naked Objects Group Ltd.
