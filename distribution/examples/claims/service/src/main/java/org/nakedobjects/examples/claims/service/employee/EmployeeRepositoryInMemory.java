package org.nakedobjects.examples.claims.service.employee;

import java.util.List;

import org.nakedobjects.applib.AbstractFactoryAndRepository;

import org.nakedobjects.examples.claims.dom.employee.Employee;
import org.nakedobjects.examples.claims.dom.employee.EmployeeRepository;


public class EmployeeRepositoryInMemory extends AbstractFactoryAndRepository implements EmployeeRepository {

	// {{ Id, iconName
    public String getId() {
        return "claimants";
    }
    public String iconName() {
        return "EmployeeRepository";
    }
    // }}

    
    // {{ action: allEmployees
    public List<Employee> allEmployees() {
        return allInstances(Employee.class);
    }
    // }}

    
    // {{ action: findEmployees
    public List<Employee> findEmployees(String name) {
        return allMatches(Employee.class, name);
    }
    // }}
}
