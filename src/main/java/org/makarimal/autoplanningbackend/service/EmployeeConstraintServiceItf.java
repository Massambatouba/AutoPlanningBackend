package org.makarimal.autoplanningbackend.service;

import org.makarimal.autoplanningbackend.model.EmployeeConstraint;

import java.util.List;

public interface EmployeeConstraintServiceItf {

    public List<EmployeeConstraint> getAll();

    public List<EmployeeConstraint> getByEmployee(Long employeeId);

    public EmployeeConstraint save(EmployeeConstraint constraint);

    public void delete(Long id);

}
