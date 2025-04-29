package org.makarimal.autoplanningbackend.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.makarimal.autoplanningbackend.Repository.CompanyRepository;
import org.makarimal.autoplanningbackend.Repository.SiteRepository;
import org.makarimal.autoplanningbackend.dto.EmployeeDto;
import org.makarimal.autoplanningbackend.dto.EmployeeSiteAssignmentDto;
import org.makarimal.autoplanningbackend.model.Company;
import org.makarimal.autoplanningbackend.model.Employee;
import org.makarimal.autoplanningbackend.model.EmployeeSiteAssignment;
import org.makarimal.autoplanningbackend.model.Site;
import org.makarimal.autoplanningbackend.service.EmployeeService;
import org.makarimal.autoplanningbackend.service.EmployeeSiteAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/companies/{companyId}/employees")
public class EmployeeController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private EmployeeSiteAssignmentService employeeSiteAssignmentService;

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private CompanyRepository companyRepository;


    @GetMapping
    public ResponseEntity<List<Employee>> getAllEmployees(@PathVariable Long companyId) {
        List<Employee> employees = employeeService.findAllByCompanyId(companyId);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(
            @PathVariable Long companyId,
            @PathVariable Long employeeId) {

        Optional<Employee> employee = employeeService.findEmployeeById(employeeId);
        if (employee.isPresent() && employee.get().getCompany().getId().equals(companyId)) {
            return ResponseEntity.ok(employee.get());
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/available/{siteId}")
    public ResponseEntity<List<Employee>> getAvailableEmployeesForSite(
            @PathVariable Long companyId,
            @PathVariable Long siteId) {

        List<Employee> availableEmployees = employeeService.findAvailableEmployeesForSite(siteId);

        // Filtrer les employés de la même société que celle spécifiée dans l'URL
        List<Employee> employeesInCompany = availableEmployees.stream()
                .filter(e -> e.getCompany().getId().equals(companyId))
                .collect(Collectors.toList());

        return ResponseEntity.ok(employeesInCompany);
    }



    @PostMapping
    public ResponseEntity<Employee> createEmployeeWithSites(
            @Valid
            @PathVariable Long companyId,
            @RequestBody EmployeeDto employeeDto) {

        // Créer l'employé à partir du DTO
        Employee employee = new Employee();
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmail(employeeDto.getEmail());
        employee.setPhone(employeeDto.getPhone());
        employee.setHireDate(employeeDto.getHireDate());
        employee.setAgentType(employeeDto.getAgentType());
        employee.setContractType(employeeDto.getContractType());
        employee.setContractHours(employeeDto.getContractHours());
        employee.setQualifications(employeeDto.getQualifications());
        employee.setStatus(Employee.EmployeeStatus.ACTIVE);

        // Associer l'entreprise
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException("Company not found"));
        employee.setCompany(company);

        // Sauvegarder l'employé
        Employee savedEmployee = employeeService.saveEmployee(employee);

        // Créer les affectations de site
        if (employeeDto.getSiteAssignments() != null) {
            for (EmployeeSiteAssignmentDto assignmentDTO : employeeDto.getSiteAssignments()) {
                Long siteId = assignmentDTO.getSiteId();
                if (siteId == null) {
                    throw new IllegalArgumentException("Site ID must not be null");
                }

                Site site = siteRepository.findById(siteId)
                        .orElseThrow(() -> new EntityNotFoundException("Site not found with ID: " + siteId));

                // Vérification de l'isolement des entreprises
                if (!site.getCompany().getId().equals(companyId)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(null); // Refuser l'affectation si le site n'appartient pas à la même société
                }

                // Créer l'affectation
                EmployeeSiteAssignment assignment = new EmployeeSiteAssignment();
                assignment.setEmployee(savedEmployee);
                assignment.setSite(site);
                assignment.setEstSitePrincipal(assignmentDTO.isEstSitePrincipal());
                assignment.setNotes(assignmentDTO.getNotes());

                employeeSiteAssignmentService.create(assignment);
            }
        }

        return ResponseEntity.ok(savedEmployee);
    }


    // Supprimer un employé
    @DeleteMapping("/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable Long companyId,
            @PathVariable Long employeeId) {

        Optional<Employee> employee = employeeService.findEmployeeById(employeeId);
        if (employee.isPresent() && employee.get().getCompany().getId().equals(companyId)) {
            employeeService.deleteEmployee(employeeId);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }



    @PutMapping("/{employeeId}")
    public ResponseEntity<Object> updateEmployee(
            @PathVariable Long companyId,
            @PathVariable Long employeeId,
            @RequestBody Employee employee) {

        Optional<Employee> existingEmployee = employeeService.findEmployeeById(employeeId);
        if (existingEmployee.isEmpty() || !existingEmployee.get().getCompany().getId().equals(companyId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Employee does not belong to the company.");
        }

        try {
            Employee updatedEmployee = employeeService.updateEmployee(employeeId, employee, companyId);
            return ResponseEntity.ok(updatedEmployee);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Employee not found.");
        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access forbidden: the employee does not belong to the company.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error during update.");
        }
    }





    //Récupérer les employés par qualification
    @GetMapping("/qualification/{keyword}")
    public ResponseEntity<List<Employee>> getEmployeesByQualification(
            @PathVariable Long companyId,
            @PathVariable String keyword) {

        List<Employee> employees = employeeService.findEmployeesByQualification(keyword);
        List<Employee> employeesInCompany = employees.stream()
                .filter(e -> e.getCompany().getId().equals(companyId))
                .collect(Collectors.toList());

        return ResponseEntity.ok(employeesInCompany);
    }
}
