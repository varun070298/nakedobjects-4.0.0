#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.service;

import java.util.List;

import org.nakedobjects.applib.AbstractFactoryAndRepository;

import ${package}.dom.DomainObject;


public class DomainObjectRepository extends AbstractFactoryAndRepository {

    public String title() {
        return "Domain Objects";
    }

    public String iconName() {
        return "DomainObject";
    }

    public List<DomainObject> allDomainObjects() {
        return allInstances(DomainObject.class);
    }

    public DomainObject newDomainObject() {
        DomainObject object = newTransientInstance(DomainObject.class);
        return object;
    }

}
