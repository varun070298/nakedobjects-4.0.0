package org.nakedobjects.examples.orders.domain;

import org.nakedobjects.applib.AbstractDomainObject;
import org.nakedobjects.applib.annotation.Disabled;
import org.nakedobjects.applib.annotation.MaxLength;
import org.nakedobjects.applib.annotation.TypicalLength;
import org.nakedobjects.applib.annotation.When;
import org.nakedobjects.applib.util.TitleBuffer;

public class Product extends AbstractDomainObject {

    // use ctrl+space to bring up the NO templates.
    // if you do not wish to subclass AbstractDomainObject,
    // then use the "injc - Inject Container" template.
    
    // also, use CoffeeBytes code folding with
    // user-defined regions of {{ and }}


    // {{ Identification Methods
    /**
     * Defines the title that will be displayed on the user
     * interface in order to identity this object.
     */
    public String title() {
        TitleBuffer t = new TitleBuffer();
        if (getCode() != null){
           t.append(getCode());
           t.append(":", getDescription());
        }
        return t.toString();
    }
    // }}
    

    // {{ Code
    private String code;
    @TypicalLength(9)
    @MaxLength(9)
    @Disabled(When.ONCE_PERSISTED)
    public String getCode() {
        return this.code;
    }
    public void setCode(String code) {
        this.code = code;
    }
    // }}
    
    
    // {{ Description
    private String description;
    @TypicalLength(50)
    @MaxLength(255)
    public String getDescription() {
        return this.description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    // }}

    
    // {{ Price
    private Double price;
    public Double getPrice() {
        return this.price;
    }
    public void setPrice(Double price) {
        this.price = price;
    }
    public String validatePrice(Double price) {
        if (price.doubleValue() <= 0) {
            return "Price must be positive";
        }
        return null;
    }
    // }}

}
