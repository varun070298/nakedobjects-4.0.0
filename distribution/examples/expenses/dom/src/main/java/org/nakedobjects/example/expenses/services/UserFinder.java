package org.nakedobjects.example.expenses.services;

/**
 * Defines a service for getting hold of the current user as an object. Will typically be implemented by a
 * service that manages a type of object that represents users (e.g. an EmployeeRepository or
 * OrganisationRepository).
 */
public interface UserFinder {

    Object currentUserAsObject();

}
