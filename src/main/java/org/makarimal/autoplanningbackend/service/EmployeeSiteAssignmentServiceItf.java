package org.makarimal.autoplanningbackend.service;

import org.makarimal.autoplanningbackend.model.EmployeeSiteAssignment;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

public interface EmployeeSiteAssignmentServiceItf {
    public List<EmployeeSiteAssignment> findAll();

    public EmployeeSiteAssignment create(EmployeeSiteAssignment employeeSiteAssignment);

    public Optional<EmployeeSiteAssignment> findById(Long id);

    public void deleteById(Long id);

    //public List<EmployeeSiteAssignment> getEmployeeSiteAssignmentById(Long siteId);

    public List<EmployeeSiteAssignment> getEmployeeSiteAssignmentByIdEmployee(Long employeeId);
}
