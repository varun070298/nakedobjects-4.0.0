package org.nakedobjects.example.expenses.employee;

import org.nakedobjects.applib.AbstractService;
import org.nakedobjects.applib.annotation.Executed;
import org.nakedobjects.applib.annotation.MemberOrder;
import org.nakedobjects.applib.annotation.Named;

import java.util.List;


/**
 * Defines the user actions available from the 'Employees' desktop icon or tab.
 * 
 * @author Richard
 * 
 */
@Named("Employees")
public class Employees extends AbstractService {

    private static final int MAX_NUM_EMPLOYEES = 10;

    // {{ Title & ID
    @Override
    public String getId() {
        return "Employees";
    }

    // }}
    public String iconName() {
        return Employee.class.getSimpleName();
    }

    // {{ Injected Services
    /*
     * This region contains references to the services (Repositories, Factories or other Services) used by
     * this domain object. The references are injected by the application container.
     */

    // {{ Injected: EmployeeRepository
    private EmployeeRepository employeeRepository;

    /**
     * This field is not persisted, nor displayed to the user.
     */
    protected EmployeeRepository getEmployeeRepository() {
        return this.employeeRepository;
    }

    /**
     * Injected by the application container.
     */
    public void setEmployeeRepository(final EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    // }}

    // }}

    @MemberOrder(sequence = "2")
    public List<Employee> findEmployeeByName(@Named("Name or start of Name")
    final String name) {
        final List<Employee> results = employeeRepository.findEmployeeByName(name);
        if (results.isEmpty()) {
            warnUser("No employees found matching name: " + name);
            return null;
        } else if (results.size() > MAX_NUM_EMPLOYEES) {
            warnUser("Too many employees found matching name: " + name + "\n Please refine search.");
            return null;
        }
        return results;
    }

    @Executed(Executed.Where.LOCALLY)
    public Employee me() {
        final Employee me = employeeRepository.me();
        if (me == null) {
            warnUser("No Employee representing current user");
        }
        return me;
    }

}
