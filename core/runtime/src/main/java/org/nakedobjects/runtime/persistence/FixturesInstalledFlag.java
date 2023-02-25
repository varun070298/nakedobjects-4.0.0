package org.nakedobjects.runtime.persistence;

/**
 * For {@link PersistenceSessionFactory} implementations that can cache 
 * the {@link PersistenceSession#isFixturesInstalled()} so is only called once per application scope.
 */
public interface FixturesInstalledFlag {

    public Boolean isFixturesInstalled();
	public void setFixturesInstalled(Boolean fixturesInstalled);

}
