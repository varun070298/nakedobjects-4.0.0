package org.nakedobjects.plugins.headless.junit.sample.fixtures;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.fixtures.AbstractFixture;
import org.nakedobjects.plugins.headless.junit.sample.service.CountryRepository;


public class CountriesFixture extends AbstractFixture {

    // {{ Logger
    private final static Logger LOGGER = Logger.getLogger(CountriesFixture.class);

    public Logger getLOGGER() {
        return LOGGER;
    }

    // }}

    @Override
    public void install() {
        getLOGGER().debug("installing");
        getCountryRepository().newCountry("AUS", "Australia");
        getCountryRepository().newCountry("GBR", "United Kingdom of Great Britain & N. Ireland");
        getCountryRepository().newCountry("USA", "United States of America");
    }

    // {{ Injected: CountryRepository
    private CountryRepository countryRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected CountryRepository getCountryRepository() {
        return this.countryRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setCountryRepository(final CountryRepository countryRepository) {
        this.countryRepository = countryRepository;
    }
    // }}

}
