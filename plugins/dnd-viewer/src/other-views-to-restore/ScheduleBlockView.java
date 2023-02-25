package org.nakedobjects.viewer.dnd.special;

import org.nakedobjects.object.NakedObject;
import org.nakedobjects.object.reflect.NakedObjectField;
import org.nakedobjects.plugins.dndviewer.ColorsAndFonts;
import org.nakedobjects.viewer.dnd.Canvas;
import org.nakedobjects.viewer.dnd.Color;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.InternalDrag;
import org.nakedobjects.viewer.dnd.Location;
import org.nakedobjects.viewer.dnd.ObjectContent;
import org.nakedobjects.viewer.dnd.Size;
import org.nakedobjects.viewer.dnd.Style;
import org.nakedobjects.viewer.dnd.ViewAreaType;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.ViewSpecification;
import org.nakedobjects.viewer.dnd.basic.IconGraphic;
import org.nakedobjects.viewer.dnd.basic.ObjectTitleText;
import org.nakedobjects.viewer.dnd.basic.TitleText;
import org.nakedobjects.viewer.dnd.core.ObjectView;

import org.apache.log4j.Logger;


public class ScheduleBlockView extends ObjectView {
	private static final Logger LOG = Logger.getLogger(ScheduleBlockView.class);
   	private NakedObjectField timePeriodField;
	private NakedObjectField colorField;
	private TitleText text;
	private IconGraphic icon;

	public ScheduleBlockView(Content content, ViewSpecification specification, ViewAxis axis, NakedObjectField timePeriodField, NakedObjectField colorField) {
        super(content, specification, axis);
        this.timePeriodField = timePeriodField;
        this.colorField = colorField;

        icon = new IconGraphic(this, Toolkit.getText(ColorsAndFonts.TEXT_NORMAL));
        text = new ObjectTitleText(this, Toolkit.getText(ColorsAndFonts.TEXT_NORMAL));
 	}

    public void draw(Canvas canvas) {
        super.draw(canvas);
        
   		Color color;
   		if(colorField == null) {
   			color = Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY3);
   		} else {
	   		NakedObject object = ((ObjectContent) getContent()).getObject();
   			org.nakedobjects.application.value.Color fieldColor =  (org.nakedobjects.application.value.Color) object.getField(colorField);
   			color = new Color((fieldColor).intValue());
   		}

		
        Size size = getSize();
        int width = size.getWidth() - 1;
        int height = size.getHeight() - 1;
        canvas.drawSolidRectangle(0, 0, width, height, color);
        canvas.drawRectangle(0, 0, width, height, Toolkit.getColor(ColorsAndFonts.COLOR_PRIMARY2));
        
        int x = 0;
        int baseline = icon.getBaseline();
        icon.draw(canvas, x, baseline);
        x += icon.getSize().getWidth();
        text.draw(canvas, x, baseline);

    }
    
    public ViewAreaType viewAreaType(Location mouseLocation) {
		int  objectBoundary = icon.getSize().getWidth();

    	return mouseLocation.getX() > objectBoundary ? ViewAreaType.INTERNAL : ViewAreaType.CONTENT;
	}
    /*
    public View dragFrom(Location location) {
		int direction;
		
		if(location.getY() <= 8) {
			direction = ViewResizeOutline.TOP;
		} else if(location.getY() >= getSize().getHeight() - 8) {
			direction = ViewResizeOutline.BOTTOM;
		} else {
			direction = ViewResizeOutline.CENTER;
		}
		
		// TODO this should be done via static method that creates and displays overlay
	    ViewResizeOutline outlineView =new ViewResizeOutline(this, direction);
	    
//	    outlineView.setLocation(getView().getLocationWithinViewer());
//	    outlineView.setSize(getView().getSize());
	    
    	getViewManager().setOverlayView(outlineView);
		LOG.debug("drag view start " + location);
		return outlineView;
	}
    */
    
    public void dragTo(InternalDrag drag) {
    	NakedObject object = ((ObjectContent) getContent()).getObject();
        TimePeriod tp = calculate(drag);
        TimePeriod timePeriod = (TimePeriod) getObject().getField(timePeriodField);
        ((TimePeriod) timePeriod.getValue()).copyObject(tp);
//        ((Appointment) object).getTime().copyObject(tp);
        invalidateLayout();
	}
    
    public void drag(InternalDrag drag) {
       	ViewResizeOutline outlineView = (ViewResizeOutline) drag.getOverlay();
		outlineView.setDisplay(calculate(drag).title().toString());
  	}

	private TimePeriod calculate(InternalDrag drag) {
		// TODO this fails when the layout decorator is itself decorated (e.g. by a WindowBorder!
		ScheduleLayout layout = (ScheduleLayout) getParent().getSpecification();
       	
		Location location = drag.getLocation();
		location.move(0, -getView().getLocation().getY());
		int top = drag.getOverlay().getLocation().getY() - location.getY();
       	int bottom = top + drag.getOverlay().getSize().getHeight();
       	
       	LOG.debug(top + " " + bottom);
       	
       	TimePeriod tp = new TimePeriod();
       	tp.setValue(layout.getTime(getParent(), top), layout.getTime(getParent(), bottom));
       	
       	return tp;
	}


}

/*
Naked Objects - a framework that exposes behaviourally complete
business objects directly to the user.
Copyright (C) 2000 - 2005  Naked Objects Group Ltd

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA

The authors can be contacted via www.nakedobjects.org (the
registered address of Naked Objects Group is Kingsway House, 123 Goldworth
Road, Woking GU21 1NR, UK).
*/