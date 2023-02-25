package org.nakedobjects.plugins.dnd.viewer.builder;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.metamodel.commons.filters.Filter;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociationFilters;
import org.nakedobjects.metamodel.util.NakedObjectUtils;
import org.nakedobjects.plugins.dnd.CompositeViewSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.FieldContent;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.SubviewSpec;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.viewer.view.simple.CompositeViewUsingBuilder;
import org.nakedobjects.plugins.dnd.viewer.view.simple.FieldErrorView;
import org.nakedobjects.runtime.context.NakedObjectsContext;


public class ObjectFieldBuilder extends AbstractViewBuilder {
    private static final Logger LOG = Logger.getLogger(ObjectFieldBuilder.class);
    private final SubviewSpec subviewDesign;

    public ObjectFieldBuilder(final SubviewSpec subviewDesign) {
        this.subviewDesign = subviewDesign;
    }

    @Override
    public void build(final View view) {
        Assert.assertEquals("ensure the view is the complete decorated view", view.getView(), view);

        final Content content = view.getContent();
        final NakedObject object = ((ObjectContent) content).getObject();

        LOG.debug("build view " + view + " for " + object);

        NakedObjectSpecification spec = object.getSpecification();
        Filter<NakedObjectAssociation> filter = NakedObjectAssociationFilters.dynamicallyVisible(
                NakedObjectsContext.getAuthenticationSession(), object);
        final NakedObjectAssociation[] flds = spec.getAssociations(filter);

        if (view.getSubviews().length == 0) {
            newBuild(view, object, flds);
        } else {
            updateBuild(view, object, flds);
        }
    }

    public View createCompositeView(final Content content, final CompositeViewSpecification specification, final ViewAxis axis) {
        return new CompositeViewUsingBuilder(content, specification, axis);
    }

    @Override
    public View decorateSubview(final View subview) {
        return subviewDesign.decorateSubview(subview);
    }

    private void newBuild(final View view, final NakedObject object, final NakedObjectAssociation[] flds) {
        LOG.debug("  as new build");
        for (int f = 0; f < flds.length; f++) {
            final NakedObjectAssociation field = flds[f];
            addField(view, object, field, f);
        }
    }

    private void addField(final View view, final NakedObject object, final NakedObjectAssociation field, int fieldNumber) {
        final View fieldView = createFieldView(view, object, fieldNumber, field);
        if (fieldView != null) {
            view.addView(decorateSubview(fieldView));
        }
    }

    private void updateBuild(final View view, final NakedObject object, final NakedObjectAssociation[] flds) {
        LOG.debug("  as update build");
        /*
         * 1/ To remove fields: look through views and remove any that don't exists in visible fields
         * 
         * 2/ From remaining views, check for changes as already being done, and replace if needed
         * 
         * 3/ Finally look through fields to see if there is no existing subview; and add one
         */

        View[] subviews = view.getSubviews();

        // remove views for fields that no longer exist
        outer: for (int i = 0; i < subviews.length; i++) {
            final FieldContent fieldContent = ((FieldContent) subviews[i].getContent());

            for (int j = 0; j < flds.length; j++) {
                final NakedObjectAssociation field = flds[j];
                if (fieldContent.getField() == field) {
                    continue outer;
                }
            }
            view.removeView(subviews[i]);
        }

        // update existing fields if needed
        subviews = view.getSubviews();
        for (int i = 0; i < subviews.length; i++) {
            final View subview = subviews[i];
            final NakedObjectAssociation field = ((FieldContent) subview.getContent()).getField();
            final NakedObject value = field.get(object);

            if (field.isOneToManyAssociation()) {
                subview.update(value);
            } else if (field.isOneToOneAssociation()) {
                final NakedObject existing = subview.getContent().getNaked();

                // if the field is parseable then it may have been modified; we need to replace what was
                // typed in with the actual title.
                if (!field.getSpecification().isParseable()) {
                    final boolean changedValue = value != existing;
                    final boolean isDestroyed = existing != null && existing.getResolveState().isDestroyed();
                    if (changedValue || isDestroyed) {
                        View fieldView;
                        fieldView = createFieldView(view, object, i, field);
                        if (fieldView != null) {
                            view.replaceView(subview, decorateSubview(fieldView));
                        } else {
                            view.addView(new FieldErrorView("No field for " + value));
                        }
                    }
                } else {
                    if (NakedObjectUtils.exists(value) && !NakedObjectUtils.wrappedEqual(value, existing)) {
                        final View fieldView = createFieldView(view, object, i, field);
                        view.replaceView(subview, decorateSubview(fieldView));
                    } else {
                        subview.refresh();
                    }
                }
            } else {
                throw new UnknownTypeException(field.getName());
            }
        }

        // add new fields
        outer2: for (int j = 0; j < flds.length; j++) {
            final NakedObjectAssociation field = flds[j];
            for (int i = 0; i < subviews.length; i++) {
                final FieldContent fieldContent = ((FieldContent) subviews[i].getContent());
                if (fieldContent.getField() == field) {
                    continue outer2;
                }
            }
            addField(view, object, field, j);
        }
    }

    private View createFieldView(
            final View view,
            final NakedObject object,
            final int fieldNumber,
            final NakedObjectAssociation field) {
        if (field == null) {
            throw new NullPointerException();
        }

        if (field.isOneToOneAssociation()) {
            NakedObjectsContext.getPersistenceSession().resolveField(object, field);
        }

        final Content content1 = Toolkit.getContentFactory().createFieldContent(field, object);
        final View fieldView = subviewDesign.createSubview(content1, view.getViewAxis(), fieldNumber);
        return fieldView;
    }

}
// Copyright (c) Naked Objects Group Ltd.
