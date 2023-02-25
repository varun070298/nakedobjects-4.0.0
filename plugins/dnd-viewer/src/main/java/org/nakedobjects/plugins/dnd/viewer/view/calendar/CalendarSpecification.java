package org.nakedobjects.plugins.dnd.viewer.view.calendar;

import java.util.List;

import org.nakedobjects.metamodel.facets.hide.HiddenFacet;
import org.nakedobjects.metamodel.facets.value.DateValueFacet;
import org.nakedobjects.metamodel.spec.feature.OneToOneAssociation;
import org.nakedobjects.plugins.dnd.CollectionContent;
import org.nakedobjects.plugins.dnd.ColorsAndFonts;
import org.nakedobjects.plugins.dnd.Content;
import org.nakedobjects.plugins.dnd.SubviewSpec;
import org.nakedobjects.plugins.dnd.Toolkit;
import org.nakedobjects.plugins.dnd.View;
import org.nakedobjects.plugins.dnd.ViewAxis;
import org.nakedobjects.plugins.dnd.ViewRequirement;
import org.nakedobjects.plugins.dnd.viewer.border.ObjectBorder;
import org.nakedobjects.plugins.dnd.viewer.builder.AbstractCompositeViewSpecification;
import org.nakedobjects.plugins.dnd.viewer.builder.CollectionElementBuilder;
import org.nakedobjects.plugins.dnd.viewer.view.graphic.IconGraphic;
import org.nakedobjects.plugins.dnd.viewer.view.simple.Icon;
import org.nakedobjects.plugins.dnd.viewer.view.text.ObjectTitleText;


public class CalendarSpecification extends AbstractCompositeViewSpecification implements SubviewSpec {

    public CalendarSpecification() {
        builder = new CalendarLayout(new CollectionElementBuilder(this));
    }

    public boolean canDisplay(final Content content, ViewRequirement requirement) {
        boolean openCollection = content instanceof CollectionContent && requirement.is(ViewRequirement.OPEN);
        if (openCollection) {
            List<OneToOneAssociation> propertyList = ((CollectionContent) content).getElementSpecification().getPropertyList();
            for (OneToOneAssociation association : propertyList) {
                if (!association.containsFacet(HiddenFacet.class) && association.getSpecification().containsFacet(DateValueFacet.class)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected ViewAxis axis(Content content) {
        return new CalendarAxis();
    }
    
    @Override
    protected View decorateView(View view) {
        final CalendarDisplay monthDisplay = new MonthDisplay();
        final CalendarDisplay weekDisplay = new WeekDisplay();
        final CalendarTemplate calendarTemplate = new CalendarTemplate(view, monthDisplay);
        final CalendarBorderTab[] tabs = new CalendarBorderTab[] { new NamedCalendarBorderTab("Month", monthDisplay),
                new NamedCalendarBorderTab("Week", weekDisplay), new NamedCalendarBorderTab("Day", null) };
        final CalendarBorder calendarBorder = new CalendarBorder(calendarTemplate, tabs);
        return calendarBorder;
    }

    public String getName() {
        return "Calendar";
    }

    @Override
    public boolean isOpen() {
        return true;
    }

    @Override
    public boolean isReplaceable() {
        return true;
    }

    @Override
    public boolean isSubView() {
        return false;
    }

    public View createSubview(final Content content, final ViewAxis axis, int fieldNumber) {
        // Specification spec = new FallbackView.Specification();
        // return new PanelBorder(spec.createView(content, axis));

        final Icon icon = new Icon(content, this, axis);

        icon.setTitle(new ObjectTitleText(icon, Toolkit.getText(ColorsAndFonts.TEXT_NORMAL)));
        icon.setSelectedIcon(new IconGraphic(icon, 24));
        icon.setVertical(true);

        return new ObjectBorder(icon);

    }
}

// Copyright (c) Naked Objects Group Ltd.
