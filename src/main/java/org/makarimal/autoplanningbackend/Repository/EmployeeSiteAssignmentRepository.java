package org.makarimal.autoplanningbackend.Repository;

import org.makarimal.autoplanningbackend.model.EmployeeSiteAssignment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeeSiteAssignmentRepository extends JpaRepository<EmployeeSiteAssignment, Long> {
    List<EmployeeSiteAssignment> findByEmployeeId(Long employeeId);
    List<EmployeeSiteAssignment> findBySiteId(Long siteId);
}
