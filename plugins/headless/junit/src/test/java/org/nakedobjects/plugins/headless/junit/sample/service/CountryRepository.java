package org.nakedobjects.plugins.headless.junit.sample.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.Filter;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.annotation.Named;
import org.nakedobjects.plugins.headless.junit.sample.domain.Country;


@Named("Countries")
public class CountryRepository extends AbstractFactoryAndRepository {

    // {{ Logger
    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(CountryRepository.class);

    // }}

    /**
     * Lists all countries in the repository.
     */
    public List<Country> showAll() {
        return allInstances(Country.class);
    }

    // {{ findByCode
    /**
     * Returns the Country with given code
     */
    public Country findByCode(@Named("Code")
    final String code) {
        return firstMatch(Country.class, new Filter<Country>() {
            public boolean accept(final Country country) {
                return code.equals(country.getCode());
            }
        });
    }

    // }}

    /**
     * Creates a new countryGBR.
     * 
     * <p>
     * For use by fixtures only.
     * 
     * @return
     */
    @Hidden
    public Country newCountry(final String code, final String name) {
        final Country country = newTransientInstance(Country.class);
        country.setCode(code);
        country.setName(name);
        persist(country);
        return country;
    }

}
