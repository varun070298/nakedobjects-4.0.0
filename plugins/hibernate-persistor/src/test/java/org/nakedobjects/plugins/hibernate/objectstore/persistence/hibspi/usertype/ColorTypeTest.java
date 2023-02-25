package org.nakedobjects.plugins.hibernate.objectstore.persistence.hibspi.usertype;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.easymock.MockControl;
import org.hibernate.Hibernate;
import org.nakedobjects.applib.value.Color;


public class ColorTypeTest extends TypesTestCase {

    public void testNullSafeGetNotNull() throws Exception {
        final int value = 4;

        final MockControl<ResultSet> control = MockControl.createControl(ResultSet.class);
        final ResultSet rs = (ResultSet) control.getMock();
        control.expectAndReturn(rs.getInt(names[0]), value);
        control.expectAndReturn(rs.wasNull(), false);
        control.replay(); // finished recording

        final ColorType type = new ColorType();
        final Color returned = (Color) type.nullSafeGet(rs, names, null);
        assertEquals("color", value, returned.intValue());

        control.verify();
    }

    public void testNullSafeGetIsNull() throws Exception {
        final MockControl<ResultSet> control = MockControl.createControl(ResultSet.class);
        final ResultSet rs = (ResultSet) control.getMock();
        control.expectAndReturn(rs.getInt(names[0]), 0);
        control.expectAndReturn(rs.wasNull(), true);
        control.replay(); // finished recording

        final ColorType type = new ColorType();
        assertNull(type.nullSafeGet(rs, names, null));

        control.verify();
    }

    public void testNullSafeSetNotNull() throws Exception {
        final int value = 5;
        final Color color = new Color(value);

        final MockControl<PreparedStatement> control = MockControl.createControl(PreparedStatement.class);
        final PreparedStatement ps = (PreparedStatement) control.getMock();
        ps.setInt(1, value);
        control.replay(); // finished recording

        final ColorType type = new ColorType();
        type.nullSafeSet(ps, color, 1);

        control.verify();
    }

    public void testNullSafeSetIsNull() throws Exception {
        final MockControl<PreparedStatement> control = MockControl.createControl(PreparedStatement.class);
        final PreparedStatement ps = (PreparedStatement) control.getMock();
        ps.setNull(1, Hibernate.INTEGER.sqlType());
        control.replay(); // finished recording

        final ColorType type = new ColorType();
        type.nullSafeSet(ps, null, 1);

        control.verify();
    }

    public void testBasics() {
        final ColorType type = new ColorType();
        super.basicTest(type);
        assertEquals("returned class", org.nakedobjects.applib.value.Color.class, type.returnedClass());
    }

}
// Copyright (c) Naked Objects Group Ltd.
