package org.nakedobjects.plugins.headless.junit.internal;

import java.util.ArrayList;
import java.util.List;

import org.nakedobjects.plugins.headless.junit.Fixture;
import org.nakedobjects.plugins.headless.junit.Fixtures;
import org.nakedobjects.runtime.fixturesinstaller.FixturesInstallerAbstract;
import org.nakedobjects.runtime.fixturesinstaller.FixturesInstallerDelegate;


public class FixtureInstallerAnnotatedClass extends FixturesInstallerAbstract  {

    private List<Object> fixtures = new ArrayList<Object>();


    /**
     * @see #addFixturesAnnotatedOn(Class)
     */
	public FixtureInstallerAnnotatedClass() {
		super("annotated");
    }

	
    /////////////////////////////////////////////
    // Hook method
    /////////////////////////////////////////////

	/**
	 * Just copies the fixtures added using {@link #addFixturesAnnotatedOn(Class)}
	 * into the delegate.
	 */
	@Override
	protected void addFixturesTo(FixturesInstallerDelegate delegate) {
		for(Object fixture: fixtures) {
			delegate.addFixture(fixture);
		}
	}

    /////////////////////////////////////////////
    // addFixturesAnnotatedOn (not API)
    /////////////////////////////////////////////

	/**
	 * Should be called prior to installing; typically called immediately after instantiation.
	 * 
	 * <p>
	 * Note: an alternative design would be to have a 1-arg constructor, but the convention for
	 * installers is to make them no-arg.
	 */
    public void addFixturesAnnotatedOn(Class<?> javaClass) throws InstantiationException, IllegalAccessException {
        final Fixtures fixturesAnnotation = javaClass.getAnnotation(Fixtures.class);
        if (fixturesAnnotation != null) {
            final Fixture[] fixtureAnnotations = fixturesAnnotation.value();
            for (final Fixture fixtureAnnotation : fixtureAnnotations) {
                addFixtureRepresentedBy(fixtureAnnotation, fixtures);
            }
        }

        final Fixture fixtureAnnotation = javaClass.getAnnotation(Fixture.class);
        if (fixtureAnnotation != null) {
            addFixtureRepresentedBy(fixtureAnnotation, fixtures);
        }
    }

    private void addFixtureRepresentedBy(final Fixture fixtureAnnotation, final List<Object> fixtures) throws InstantiationException, IllegalAccessException {
        final Class<?> fixtureClass = fixtureAnnotation.value();
        fixtures.add(fixtureClass.newInstance());
    }

}
