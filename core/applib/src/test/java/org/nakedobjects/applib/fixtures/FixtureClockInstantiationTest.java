package org.nakedobjects.applib.fixtures;

import org.junit.Before;
import org.junit.Test;
import org.nakedobjects.applib.clock.Clock;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class FixtureClockInstantiationTest {

	@Before
	public void setUp() {
		
	}
	
	@Test
	public void shouldSetupClockSingletonWithFixtureClockWhenInitialize() {
		FixtureClock.initialize();
		assertThat(Clock.getInstance(), is(FixtureClock.class));
	}

	@Test
	public void canInitializeFixtureClockMultipleTimesButAlwaysGetTheSameFixtureClock() {
		FixtureClock fixtureClock1 = FixtureClock.initialize();
		FixtureClock fixtureClock2 = FixtureClock.initialize();
		assertThat(fixtureClock1, is(fixtureClock2));
	}

	@Test
	public void canRemoveFixtureClock() {
		FixtureClock.initialize();
		assertThat(FixtureClock.remove(), is(true));
		assertThat(FixtureClock.remove(), is(false)); // already removed.
	}


}
