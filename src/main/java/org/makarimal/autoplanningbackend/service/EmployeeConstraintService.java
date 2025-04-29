package org.makarimal.autoplanningbackend.service;

import lombok.RequiredArgsConstructor;
import org.makarimal.autoplanningbackend.Repository.EmployeeConstraintRepository;
import org.makarimal.autoplanningbackend.model.EmployeeConstraint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeConstraintService {

    @Autowired
    private final EmployeeConstraintRepository repository;

    public List<EmployeeConstraint> getAll() {
        return repository.findAll();
    }

    public List<EmployeeConstraint> getByEmployee(Long employeeId) {
        return repository.findByEmployeeId(employeeId);
    }

    public EmployeeConstraint save(EmployeeConstraint constraint) {
        return repository.save(constraint);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}

