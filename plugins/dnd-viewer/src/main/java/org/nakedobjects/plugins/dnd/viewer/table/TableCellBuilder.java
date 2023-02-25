package org.nakedobjects.plugins.dnd.viewer.table;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.commons.ensure.Assert;
import org.nakedobjects.metamodel.commons.exceptions.UnexpectedCallException;
import org.nakedobjects.metamodel.commons.exceptions.UnknownTypeException;
import org.nakedobjects.metamodel.facets.value.BooleanValueFacet;
import org.nakedobjects.metamodel.facets.value.ImageValueFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToManyAssociation;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.plugins.dnd.CompositeViewSpecification;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.ObjectContent;
import org.nakedobjects.plugins.dnd.TextParseableContent;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewFactory;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.ViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.basic.UnlinedTextFieldSpecification;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractViewBuilder;
import org.nakedobjects.plugins.dnd.viewer.content.OneToOneFieldImpl;
import org.nakedobjects.plugins.dnd.viewer.content.TextParseableFieldImpl;
import org.nakedobjects.plugins.dnd.viewer.drawing.Location;
import org.nakedobjects.plugins.dnd.viewer.drawing.Padding;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;
import org.nakedobjects.plugins.dnd.viewer.view.field.CheckboxField;
import org.nakedobjects.plugins.dnd.viewer.view.simple.BlankView;
import org.nakedobjects.plugins.dnd.viewer.view.simple.CompositeViewUsingBuilder;
import org.nakedobjects.plugins.dnd.viewer.view.simple.FieldErrorView;
import org.nakedobjects.runtime.context.NakedObjectsContext;


class TableCellBuilder extends AbstractViewBuilder {
    private static final Logger LOG = Logger.getLogger(TableCellBuilder.class);

    private void addField(final View view, final NakedObject object, final NakedObjectAssociation field) {
        final NakedObject value = field.get(object);
        View fieldView;
        fieldView = createFieldView(view, object, field, value);
        if (fieldView != null) {
            view.addView(decorateSubview(fieldView));
        } else {
            view.addView(new FieldErrorView("No field for " + value));
        }
    }

    @Override
    public void build(final View view) {
        Assert.assertEquals("ensure the view is complete decorated view", view.getView(), view);

        final Content content = view.getContent();
        final NakedObject object = ((ObjectContent) content).getObject();

        final TableAxis viewAxis = (TableAxis) view.getViewAxis();

        if (view.getSubviews().length == 0) {
            buildNew(object, view, viewAxis);
        } else {
            buildUpdate(object, view, viewAxis);
        }
    }

    private void buildUpdate(final NakedObject object, final View view, final TableAxis viewAxis) {
        LOG.debug("update view " + view + " for " + object);
        final View[] subviews = view.getSubviews();
        final NakedObjectSpecification spec = object.getSpecification();
        for (int i = 0; i < subviews.length; i++) {
            final NakedObjectAssociation field = fieldFromActualSpec(spec, viewAxis.getFieldForColumn(i));
            final View subview = subviews[i];
            final NakedObject value = field.get(object);

            // if the field is parseable then it may have been modified; we need to replace what was
            // typed in with the actual title.
            if (field.getSpecification().isParseable()) {
                final boolean visiblityChange = !field.isVisible(NakedObjectsContext.getAuthenticationSession(), object).isAllowed()
                        ^ (subview instanceof BlankView);
                final NakedObject nakedObject = subview.getContent().getNaked();
                final boolean valueChange = value != null && value.getObject() != null
                        && !value.getObject().equals(nakedObject.getObject());

                if (visiblityChange || valueChange) {
                    final View fieldView = createFieldView(view, object, field, value);
                    view.replaceView(subview, decorateSubview(fieldView));
                }
                subview.refresh();
            } else if (field.isOneToOneAssociation()) {
                final NakedObject existing = ((ObjectContent) subviews[i].getContent()).getObject();
                final boolean changedValue = value != existing;
                if (changedValue) {
                    View fieldView;
                    fieldView = createFieldView(view, object, field, value);
                    if (fieldView != null) {
                        view.replaceView(subview, decorateSubview(fieldView));
                    } else {
                        view.addView(new FieldErrorView("No field for " + value));
                    }
                }
            }
        }
    }

    private NakedObjectAssociation fieldFromActualSpec(final NakedObjectSpecification spec, final NakedObjectAssociation field) {
        final String fieldName = field.getId();
        return spec.getAssociation(fieldName);
    }

    private void buildNew(final NakedObject object, final View view, final TableAxis viewAxis) {
        LOG.debug("build view " + view + " for " + object);
        final int len = viewAxis.getColumnCount();
        final NakedObjectSpecification spec = object.getSpecification();
        for (int f = 0; f < len; f++) {
            final NakedObjectAssociation field = fieldFromActualSpec(spec, viewAxis.getFieldForColumn(f));
            addField(view, object, field);
        }
    }

    public View createCompositeView(final Content content, final CompositeViewSpecification specification, final ViewAxis axis) {
        return new CompositeViewUsingBuilder(content, specification, axis);
    }

    private View createFieldView(
            final View view,
            final NakedObject object,
            final NakedObjectAssociation field,
            final NakedObject value) {
        if (field == null) {
            throw new NullPointerException();
        }
        final ViewFactory factory = Toolkit.getViewFactory();
        ViewSpecification cellSpec;
        Content content;
        if (field instanceof OneToManyAssociation) {
            throw new UnexpectedCallException("no collections allowed");
        } else if (field instanceof OneToOneAssociation) {

            final NakedObjectSpecification fieldSpecification = field.getSpecification();
            if (fieldSpecification.isParseable()) {
                content = new TextParseableFieldImpl(object, value, (OneToOneAssociation) field);
                // REVIEW how do we deal with IMAGES?
                if (content.getNaked() instanceof ImageValueFacet) {
                    return new BlankView(content);
                }

                if (!field.isVisible(NakedObjectsContext.getAuthenticationSession(), object).isAllowed()) {
                    return new BlankView(content);
                }
                if (((TextParseableContent) content).getNoLines() > 0) {
                    /*
                     * TODO remove this after introducing constraints into view specs that allow the parent
                     * view to specify what kind of subviews it can deal
                     */
                    
                    if (fieldSpecification.containsFacet(BooleanValueFacet.class)) {
                        cellSpec = new CheckboxField.Specification();
                    } else {
                        cellSpec = new UnlinedTextFieldSpecification();
                    }
                } else {
                    cellSpec = factory.getValueFieldSpecification((TextParseableContent) content);
                }
            } else {
                content = new OneToOneFieldImpl(object, value, (OneToOneAssociation) field);
                if (!field.isVisible(NakedObjectsContext.getAuthenticationSession(), object).isAllowed()) {
                    return new BlankView(content);
                }
                cellSpec = factory.getIconizedSubViewSpecification(content);
                return factory.createView(new ViewRequirement(content, view.getViewAxis(), ViewRequirement.CLOSED |  ViewRequirement.SUBVIEW));
                
            }

        } else {
            throw new UnknownTypeException(field);
        }

        final ViewAxis axis = view.getViewAxis();
        return cellSpec.createView(content, axis);
    }

    @Override
    public Size getRequiredSize(final View row) {
        int maxHeight = 0;
        int totalWidth = 0;
        final TableAxis axis = ((TableAxis) row.getViewAxis());
        final View[] cells = row.getSubviews();
        final int maxBaseline = maxBaseline(cells);

        for (int i = 0; i < cells.length; i++) {
            totalWidth += axis.getColumnWidth(i);

            final Size s = cells[i].getRequiredSize(new Size());
            final int b = cells[i].getBaseline();
            final int baselineOffset = Math.max(0, maxBaseline - b);
            maxHeight = Math.max(maxHeight, s.getHeight() + baselineOffset);
        }

        return new Size(totalWidth, maxHeight);
    }

    @Override
    public void layout(final View row, final Size maximumSize) {
        int x = 0;

        // int rowHeight = row.getSize().getHeight();
        final TableAxis axis = ((TableAxis) row.getViewAxis());
        final View[] cells = row.getSubviews();
        final int maxBaseline = maxBaseline(cells);

        for (int i = 0; i < cells.length; i++) {
            final View cell = cells[i];
            final Size s = cell.getRequiredSize(new Size(maximumSize));
            s.setWidth(axis.getColumnWidth(i));
            // s.setHeight(rowHeight);
            cell.setSize(s);

            final int b = cell.getBaseline();
            final int baselineOffset = Math.max(0, maxBaseline - b);
            cell.setLocation(new Location(x, baselineOffset));

            x += s.getWidth();
        }

        final Padding padding = row.getPadding();
        final Size size = new Size(padding.getLeftRight(), padding.getTopBottom());
        row.setSize(size);
    }

    private int maxBaseline(final View[] cells) {
        int maxBaseline = 0;
        for (int i = 0; i < cells.length; i++) {
            final View cell = cells[i];
            maxBaseline = Math.max(maxBaseline, cell.getBaseline());
        }
        return maxBaseline;
    }
}
// Copyright (c) Naked Objects Group Ltd.
