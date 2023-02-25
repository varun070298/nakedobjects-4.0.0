package org.nakedobjects.viewer.dnd.special;

import org.nakedobjects.object.NakedObjectRuntimeException;
import org.nakedobjects.object.NakedObjectSpecification;
import org.nakedobjects.object.NakedObjectSpecificationLoader;
import org.nakedobjects.object.reflect.NakedObjectField;
import org.nakedobjects.viewer.dnd.Content;
import org.nakedobjects.viewer.dnd.ObjectContent;
import org.nakedobjects.viewer.dnd.View;
import org.nakedobjects.viewer.dnd.ViewAxis;
import org.nakedobjects.viewer.dnd.basic.SimpleIdentifier;
import org.nakedobjects.viewer.dnd.core.AbstractCompositeViewSpecification;

import org.apache.log4j.Logger;

public class ScheduleBlockSpecification extends AbstractCompositeViewSpecification{
	private static final Logger LOG = Logger.getLogger(ScheduleBlockView.class);

	public View createView(Content content, ViewAxis axis) {
    	NakedObjectSpecification nc = ((ObjectContent) content).getObject().getSpecification();
    	NakedObjectField[] flds = nc.getFields();
    	NakedObjectField timePeriodField = null;
    	NakedObjectField colorField = null;
    	for (int i = 0; i < flds.length; i++) {
			NakedObjectField field = flds[i];
			if(field.getType().isOfType(NakedObjects.getSpecificationLoader().loadSpecification(TimePeriod.class))) {
				LOG.debug("found TimePeriod field " + field);
				timePeriodField = field;
			}
			if(field.getType().isOfType(NakedObjects.getSpecificationLoader().loadSpecification(org.nakedobjects.application.value.Color.class))) {
				LOG.debug("found Color field " + field);
				colorField = field;
			}
		}
    	if(timePeriodField == null) {
        	throw new NakedObjectRuntimeException("Can't create Shedule view without a TimePeriod");
    	} else {
    		return new SimpleIdentifier(new ScheduleBlockView(content, this, axis, timePeriodField, colorField));
    	}
	}
	
	public String getName() {
		return "Schedule Block";
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