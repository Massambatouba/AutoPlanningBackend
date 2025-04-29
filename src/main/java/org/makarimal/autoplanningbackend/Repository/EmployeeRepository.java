package org.makarimal.autoplanningbackend.Repository;

import org.makarimal.autoplanningbackend.model.Employee;
import org.makarimal.autoplanningbackend.model.AgentType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {
    List<Employee> findByQualificationsContains(String keyword);

    List<Employee> findByCompanyId(Long companyId);

    List<Employee> findByAgentType(String agentType);

    @Query("SELECT e FROM Employee e JOIN e.siteAssignments s WHERE s.id = :siteId")
    List<Employee> findAvailableEmployeesForSite(Long siteId);

    List<Employee> findByAgentTypeAndCompanyId(AgentType agentType, Long companyId);
}
