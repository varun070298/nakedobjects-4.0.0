package org.nakedobjects.plugins.dnd.viewer.view.calendar;

import java.util.Date;

import org.nakedobjects.metamodel.adapter.NakedObject;
import org.nakedobjects.metamodel.facets.Facet;
import org.nakedobjects.metamodel.facets.value.DateValueFacet;
import org.nakedobjects.metamodel.spec.NakedObjectSpecification;
import org.nakedobjects.metamodel.spec.feature.NakedObjectAssociation;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewBuilder;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractBuilderDecorator;
import org.nakedobjects.plugins.dnd.viewer.drawing.Size;


public class CalendarLayout extends AbstractBuilderDecorator {

    public CalendarLayout(final ViewBuilder design) {
        super(design);
    }

    @Override
    public Size getRequiredSize(final View view) {
        final CalendarAxis calendarAxis = (CalendarAxis) view.getViewAxis();
        final CalendarDisplay calendarDisplay = calendarAxis.getCalendarDisplay();
        return calendarDisplay.getRequiredSize();
    }

    @Override
    public void layout(final View view, final Size maximumSize) {
        final CalendarAxis calendarAxis = ((CalendarAxis) view.getViewAxis());
        final CalendarDisplay calendarDisplay = calendarAxis.getCalendarDisplay();
        final Size size = view.getSize();
        size.contract(view.getPadding());

        int width;
        int height;
        final Size blockSize = calendarDisplay.getBlockSize(size);
        width = blockSize.getWidth();
        height = blockSize.getHeight();

        final View subviews[] = view.getSubviews();
        for (int i = 0; i < subviews.length; i++) {
            final View v = subviews[i];

            final NakedObjectAssociation dateField = findDate(v);
            if (dateField == null) {
                continue;
            }
            final DateValueFacet facet = dateField.getFacet(DateValueFacet.class);
            final NakedObject field = dateField.get(v.getContent().getNaked());
            final Date date = facet.dateValue(field);
            calendarDisplay.layoutDate(v, date, width, height);
        }

    }

    private NakedObjectAssociation findDate(final View view) {
        final Content c = view.getContent();
        final NakedObject adapter = c.getNaked();
        final NakedObjectSpecification spec = adapter.getSpecification();
        final NakedObjectAssociation[] fields = spec.getAssociations();
        for (int i = 0; i < fields.length; i++) {
            final Facet facet = fields[i].getFacet(DateValueFacet.class);
            if (facet != null) {
                return fields[i];
            }
            /*
             * if(fields[j].getSpecification() ==
             * NakedObjectsContext.getReflector().loadSpecification(DateValue.class)) { date = (DateValue)
             * fields[j].get((NakedObject) adapter).getObject(); break; }
             */
        }
        return null;
    }
}

// Copyright (c) Naked Objects Group Ltd.
