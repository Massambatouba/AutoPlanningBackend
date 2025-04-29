package org.makarimal.autoplanningbackend.dto;

import lombok.Data;
import org.makarimal.autoplanningbackend.model.AgentType;
import org.makarimal.autoplanningbackend.model.Employee;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
@Data
public class EmployeeDto {

    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private LocalDate hireDate;
    private AgentType agentType;
    private Employee.ContractType contractType;
    private Integer contractHours;
    private Set<String> qualifications = new HashSet<>();
    private List<EmployeeSiteAssignmentDto> siteAssignments; // Liste des IDs de sites associés à l'employé


}

