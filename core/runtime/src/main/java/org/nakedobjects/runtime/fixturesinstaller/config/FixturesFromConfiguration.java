package org.nakedobjects.runtime.fixturesinstaller.config;

import org.apache.log4j.Logger;
import org.nakedobjects.metamodel.commons.exceptions.NakedObjectException;
import org.nakedobjects.metamodel.commons.factory.InstanceFactory;
import org.nakedobjects.metamodel.config.ConfigurationConstants;
import org.nakedobjects.runtime.fixturesinstaller.FixturesInstallerAbstract;
import org.nakedobjects.runtime.fixturesinstaller.FixturesInstallerDelegate;


public class FixturesFromConfiguration extends FixturesInstallerAbstract {
	
	private static final Logger LOG = Logger.getLogger(FixturesFromConfiguration.class);
    private static final String NAKEDOBJECTS_FIXTURES = ConfigurationConstants.ROOT + "fixtures";
    private static final String NAKEDOBJECTS_FIXTURES_PREFIX = ConfigurationConstants.ROOT + "fixtures.prefix";
    
    public FixturesFromConfiguration() {
    	super("configuration");
    }
    
    protected void addFixturesTo(FixturesInstallerDelegate delegate) {
        String fixturePrefix = getConfiguration().getString(NAKEDOBJECTS_FIXTURES_PREFIX);
        fixturePrefix = fixturePrefix == null ? "" : fixturePrefix.trim();
        if (fixturePrefix.length() > 0 && !fixturePrefix.endsWith(ConfigurationConstants.DELIMITER)) {
            fixturePrefix = fixturePrefix + ConfigurationConstants.DELIMITER;
        }

		try {
	        final String[] fixtureList = getConfiguration().getList(NAKEDOBJECTS_FIXTURES);
	        boolean fixtureLoaded = false;
            for (int i = 0; i < fixtureList.length; i++) {
                String fixtureFullyQualifiedName = fixturePrefix + fixtureList[i];
                LOG.info("  adding fixture " + fixtureFullyQualifiedName);
				final Object fixture = InstanceFactory.createInstance(fixtureFullyQualifiedName);
				fixtureLoaded = true;
				delegate.addFixture(fixture);
            }
	        if (!fixtureLoaded) {
	        	LOG.warn("No fixtures loaded from configuration");
	        }
		} catch (IllegalArgumentException e) {
            throw new NakedObjectException(e);
		} catch (SecurityException e) {
			throw new NakedObjectException(e);
		}
    }

}
// Copyright (c) Naked Objects Group Ltd.
