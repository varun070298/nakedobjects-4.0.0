package org.nakedobjects.application.valueholder;

import org.easymock.MockControl;
import org.nakedobjects.application.BusinessObject;


public class ColorTest extends ValueTestCase {

    public void testEncodedString() {
        MockControl control = MockControl.createControl(BusinessObject.class);
        BusinessObject bo = (BusinessObject) control.getMock();
        bo.objectChanged();
        control.replay(); // finished recording

        Color color = new Color(bo);
        assertEquals("enc", "NULL", color.asEncodedString());

        control.verify();
    }
}
// Copyright (c) Naked Objects Group Ltd.
