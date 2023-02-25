package org.nakedobjects.plugins.headless.junit;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

import org.junit.Test;
import org.nakedobjects.applib.events.InteractionEvent;
import org.nakedobjects.applib.events.PropertyAccessEvent;
import org.nakedobjects.plugins.headless.applib.listeners.InteractionAdapter;
import org.nakedobjects.plugins.headless.applib.listeners.InteractionListener;
import org.nakedobjects.plugins.headless.junit.sample.domain.Customer;


public class InteractionListenerTest extends AbstractTest {

    @Test
    public void shouldBeAbleToAddListener() {
        final Customer proxiedCustRP = getHeadlessViewer().view(custJsDO);
        final InteractionEvent[] events = { null };
        final InteractionListener l = new InteractionAdapter() {
            @Override
            public void propertyAccessed(PropertyAccessEvent ev) {
                events[0] = ev;
            }
        };
        getHeadlessViewer().addInteractionListener(l);

        proxiedCustRP.getFirstName();
        assertThat(events[0], notNullValue());
        final PropertyAccessEvent ev = (PropertyAccessEvent) events[0];
        assertThat(ev.getMemberNaturalName(), is("First Name"));
    }

}
