package org.nakedobjects.example.expenses.fixtures;

import org.nakedobjects.applib.fixtures.AbstractFixture;
import org.nakedobjects.example.expenses.claims.ProjectCode;
import org.nakedobjects.example.expenses.employee.EmployeeRepository;


public class ProjectCodeFixture extends AbstractFixture {

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

    public static ProjectCode CODE1;
    public static ProjectCode CODE2;
    public static ProjectCode CODE3;

    @Override
    public void install() {
        CODE1 = createProjectCode("001", "Marketing");
        CODE2 = createProjectCode("002", "Sales");
        CODE3 = createProjectCode("003", "Training");
        createProjectCode("004", "Consulting");
        createProjectCode("005", "Product Development");
        createProjectCode("006", "Recruitment");
        createProjectCode("007", "Overhead");
    }

    private ProjectCode createProjectCode(final String code, final String description) {
        final ProjectCode pCode = newTransientInstance(ProjectCode.class);
        pCode.setCode(code);
        pCode.setDescription(description);
        persist(pCode);
        return pCode;
    }

}
