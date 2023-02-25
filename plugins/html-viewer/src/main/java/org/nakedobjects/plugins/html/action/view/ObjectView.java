package org.nakedobjects.plugins.html.action.view;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.SpecificationFacets;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters;
import org.nakedobjects.plugins.html.component.Block;
import org.nakedobjects.plugins.html.component.Component;
import org.nakedobjects.plugins.html.component.ComponentFactory;
import org.nakedobjects.plugins.html.component.ViewPane;
import org.nakedobjects.plugins.html.context.Context;
import org.nakedobjects.plugins.html.request.Request;
import org.nakedobjects.runtime.context.NakedObjectsContext;



public class ObjectView extends ObjectViewAbstract {
    @Override
    protected boolean addObjectToHistory() {
        return true;
    }

    @Override
    protected void doExecute(final Context context, final ViewPane content, final NakedObject adapter, final String field) {
        final String id = context.mapObject(adapter);
        createObjectView(context, adapter, content, id);
        final NakedObjectSpecification specification = adapter.getSpecification();

        // TODO: this test should be done by the ImmutableFacetFactory installing an immutableFacet on every
        // member
        boolean immutable = SpecificationFacets.isAlwaysImmutable(specification)
                || (adapter.isPersistent() && SpecificationFacets.isImmutableOncePersisted(specification));

        boolean allFieldUneditable = true;
        final NakedObjectAssociation[] flds = specification.getAssociations();
        for (int i = 0; i < flds.length; i++) {
            if (flds[i].isUsable(NakedObjectsContext.getAuthenticationSession(), adapter).isAllowed()) {
                allFieldUneditable = false;
                break;
            }
        }
        if (!immutable && !allFieldUneditable) {
            content.add(context.getComponentFactory().createEditOption(id));
        }
        context.setObjectCrumb(adapter);
    }

    private void createObjectView(final Context context, final NakedObject object, final ViewPane pane, final String id) {
        final NakedObjectSpecification specification = object.getSpecification();
        final NakedObjectAssociation[] visibleFields = specification.getAssociations(NakedObjectAssociationFilters
                .dynamicallyVisible(NakedObjectsContext.getAuthenticationSession(), object));
        for (int i = 0; i < visibleFields.length; i++) {
            final NakedObjectAssociation field = visibleFields[i];

            final ComponentFactory factory = context.getComponentFactory();
            final Block fieldBlock = factory.createBlock("field", field.getDescription());
            fieldBlock.add(factory.createInlineBlock("label", field.getName(), null));
            fieldBlock.add(factory.createInlineBlock("separator", ":  ", null));

            NakedObjectsContext.getPersistenceSession().resolveField(object, field);

            // ordering is important here;
            // look at parseable fields before objects
            final NakedObject associatedObject = field.get(object);
            Component component = null;
            if (field.getSpecification().isParseable()) {
                component = factory.createParseableField(field, associatedObject, false);
            } else if (field.isOneToOneAssociation()) {
                if (associatedObject == null) {
                    component = factory.createInlineBlock("value", "", null);
                    fieldBlock.add(component);
                } else {
                    // previously there was a called to resolveImmediately here on the
                    // associated object, but it has been removed (presumably we don't
                    // want to force eager loading).
                    final String elementId = context.mapObject(associatedObject);
                    component = factory.createObjectIcon(field, associatedObject, elementId, "value");
                }
            } else if (field.isOneToManyAssociation()) {
                component = factory.createCollectionIcon(field, associatedObject, id);
            } else {
                throw new UnknownTypeException(field);
            }
            fieldBlock.add(component);

            pane.add(fieldBlock);
        }
    }

    public String name() {
        return Request.OBJECT_COMMAND;
    }

}

// Copyright (c) Naked Objects Group Ltd.
