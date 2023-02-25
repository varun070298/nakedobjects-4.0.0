#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.fixture;

import org.nakedobjects.applib.fixtures.AbstractFixture;

import ${package}.dom.DomainObject;


public class DomainObjectFixture extends AbstractFixture {

    @Override
    public void install() {
        DomainObject object = newTransientInstance(DomainObject.class);
        object.setName("My Object");
        persist(object);
    }
}
