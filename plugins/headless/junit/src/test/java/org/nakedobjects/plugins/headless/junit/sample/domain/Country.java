package org.nakedobjects.plugins.headless.junit.sample.domain;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.Bounded;
import org.nakedobjects.applib.annotation.MaxLength;
import org.nakedobjects.applib.annotation.Optional;
import org.nakedobjects.applib.annotation.TypicalLength;
import org.nakedobjects.applib.util.TitleBuffer;


@Bounded
public class Country extends AbstractDomainObject {

    // {{ Logger
    @SuppressWarnings("unused")
    private final static Logger LOGGER = Logger.getLogger(Country.class);

    // }}

    // {{ Identification Methods
    /**
     * Defines the title that will be displayed on the user interface in order to identity this object.
     */
    public String title() {
        final TitleBuffer t = new TitleBuffer();
        t.append(getName());
        return t.toString();
    }
    // }}

    // {{ Code
    private String code;

    @TypicalLength(3)
    @MaxLength(3)
    public String getCode() {
        return this.code;
    }

    public void setCode(final String code) {
        this.code = code;
    }
    // }}

    // {{ Name
    private String name;

    @TypicalLength(50)
    @MaxLength(255)
    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }
    // }}

    // {{ FavouriteHolidayDestination
    private Country favouriteHolidayDestination;

    @Optional
    public Country getFavouriteHolidayDestination() {
        return favouriteHolidayDestination;
    }

    public void setFavouriteHolidayDestination(final Country favouriteHolidayDestination) {
        this.favouriteHolidayDestination = favouriteHolidayDestination;
    }
    // }}

    // {{ Colonies
    private List<Country> colonies = new ArrayList<Country>();

    public List<Country> getColonies() {
        return this.colonies;
    }

    @SuppressWarnings("unused")
    private void setColonies(final List<Country> colonies) {
        this.colonies = colonies;
    }

    public void addToColonies(final Country country) {
        getColonies().add(country);
    }

    public void removeFromColonies(final Country country) {
        getColonies().remove(country);
    }
    public String validateAddToColonies;

    public String validateAddToColonies(final Country country) {
        return validateAddToColonies;
    }
    public String validateRemoveFromColonies;

    public String validateRemoveFromColonies(final Country country) {
        return validateRemoveFromColonies;
    }

    public String disableColonies;

    public String disableColonies() {
        return this.disableColonies;
    }

    public boolean hideColonies;

    public boolean hideColonies() {
        return this.hideColonies;
    }

    // }}

    // {{
    /**
     * An action to invoke
     */
    public void foobar() {}
    // }}

}
