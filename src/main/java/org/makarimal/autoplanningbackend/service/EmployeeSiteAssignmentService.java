package org.makarimal.autoplanningbackend.service;

import org.makarimal.autoplanningbackend.Repository.EmployeeSiteAssignmentRepository;
import org.makarimal.autoplanningbackend.model.EmployeeSiteAssignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeSiteAssignmentService implements EmployeeSiteAssignmentServiceItf {

    @Autowired
    private EmployeeSiteAssignmentRepository employeeSiteAssignmentRepository;
    @Override
    public List<EmployeeSiteAssignment> findAll() {
        return employeeSiteAssignmentRepository.findAll();
    }

    @Override
    public EmployeeSiteAssignment create(EmployeeSiteAssignment employeeSiteAssignment) {
        return employeeSiteAssignmentRepository.save(employeeSiteAssignment);
    }

    @Override
    public Optional<EmployeeSiteAssignment> findById(Long id) {
        return employeeSiteAssignmentRepository.findById(id);
    }

    @Override
    public void deleteById(Long id) {
        employeeSiteAssignmentRepository.deleteById(id);
    }



    @Override
    public List<EmployeeSiteAssignment> getEmployeeSiteAssignmentByIdEmployee(Long employeeId) {
        return employeeSiteAssignmentRepository.findByEmployeeId(employeeId);
    }
}
