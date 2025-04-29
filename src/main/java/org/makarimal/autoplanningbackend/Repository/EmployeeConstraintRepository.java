package org.makarimal.autoplanningbackend.Repository;

import org.makarimal.autoplanningbackend.model.EmployeeConstraint;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeConstraintRepository extends JpaRepository<EmployeeConstraint, Long> {
    List<EmployeeConstraint> findByEmployeeId(Long employeeId);
}
