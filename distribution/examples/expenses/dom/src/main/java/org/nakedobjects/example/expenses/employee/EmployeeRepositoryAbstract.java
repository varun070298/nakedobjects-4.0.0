package org.nakedobjects.example.expenses.employee;

import org.nakedobjects.applib.AbstractFactoryAndRepository;
import org.nakedobjects.applib.annotation.Debug;
import org.nakedobjects.applib.annotation.Executed;
import org.nakedobjects.applib.annotation.Hidden;
import org.nakedobjects.applib.security.UserMemento;
import org.nakedobjects.example.expenses.services.UserFinder;

import java.util.List;


public abstract class EmployeeRepositoryAbstract extends AbstractFactoryAndRepository implements EmployeeRepository, UserFinder {
    public String iconName() {
        return "Employee";
    }

    @Debug
    public List<Employee> allEmployees() {
        return allInstances(Employee.class);
    }

    private Employee findEmployeeForUserName(final String userName) {
        final Employee pattern = newTransientInstance(Employee.class);
        pattern.setUserName(userName);
        return firstMatch(Employee.class, pattern);
    }

    @Hidden
    public List<Employee> findEmployeeByName(final String name) {
        return allMatches(Employee.class, name);
    }

    // {{ User Finder
    // private Employee currentUser;

    @Executed(Executed.Where.LOCALLY)
    public Object currentUserAsObject() {
        // if (currentUser == null) {
        // String userName = getUser().getName();
        // currentUser = findEmployeeForUserName(userName);
        // }
	UserMemento user = getUser();
	String userName = user.getName();
        return findEmployeeForUserName(userName);
    }

    // }}

    @Executed(Executed.Where.LOCALLY)
    @Hidden
    public Employee me() {
        return (Employee) currentUserAsObject();
    }

}
