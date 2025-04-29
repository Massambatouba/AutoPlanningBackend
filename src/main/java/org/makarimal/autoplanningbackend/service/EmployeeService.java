package org.makarimal.autoplanningbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.makarimal.autoplanningbackend.Repository.EmployeeRepository;
import org.makarimal.autoplanningbackend.Repository.SiteRepository;
import org.makarimal.autoplanningbackend.model.Employee;
import org.makarimal.autoplanningbackend.model.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class EmployeeService implements EmployeeServiceItf{

    private EmployeeRepository employeeRepository;

    @Autowired

    private SiteRepository siteRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public Optional<Employee> getEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }

    @Override
    public List<Employee> findEmployeesByQualification(String keyword) {
        return employeeRepository.findByQualificationsContains(keyword);
    }

    @Transactional
    @Override
    public Employee saveEmployee(Employee employee) {
        if (employee.getPreference() != null) {
            // Lien entre l'employé et sa préférence
            employee.getPreference().setEmployee(employee);

            // Vérification si des sites préférés ont été transmis
            if (employee.getPreference().getPreferredSites() != null && !employee.getPreference().getPreferredSites().isEmpty()) {
                Set<Long> siteIds = employee.getPreference().getPreferredSites()
                        .stream()
                        .map(Site::getId)
                        .collect(Collectors.toSet());

                Set<Site> sites = new HashSet<>(siteRepository.findAllById(siteIds));

                employee.getPreference().setPreferredSites(sites);
            }
        }


        return employeeRepository.save(employee);
    }

    @Transactional
    public Employee updateEmployee(Long employeeId, Employee updatedEmployee, Long companyId) {
        // 1. Vérifier l'existence de l'employé
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new EntityNotFoundException("Employé non trouvé"));

        // 2. Vérifier que l'employé appartient à la bonne entreprise
        if (!existingEmployee.getCompany().getId().equals(companyId)) {
            throw new SecurityException("L'employé n'appartient pas à cette entreprise");
        }

        // 3. Mettre à jour les champs nécessaires
        existingEmployee.setFirstName(updatedEmployee.getFirstName());
        existingEmployee.setLastName(updatedEmployee.getLastName());
        existingEmployee.setEmail(updatedEmployee.getEmail());
        existingEmployee.setPhone(updatedEmployee.getPhone());
        existingEmployee.setContractType(updatedEmployee.getContractType());
        existingEmployee.setContractHours(updatedEmployee.getContractHours());
        existingEmployee.setHireDate(updatedEmployee.getHireDate());
        existingEmployee.setStatus(updatedEmployee.getStatus());
        existingEmployee.setAgentType(updatedEmployee.getAgentType());

        // Mettre à jour la préférence s'il y en a
        if (updatedEmployee.getPreference() != null) {
            if (existingEmployee.getPreference() != null) {
                // Mise à jour manuelle des champs de la préférence existante
                existingEmployee.getPreference().setMinimumMonthlyHours(
                        updatedEmployee.getPreference().getMinimumMonthlyHours());
                existingEmployee.getPreference().setOnlyWeekend(
                        updatedEmployee.getPreference().getOnlyWeekend());
                existingEmployee.getPreference().setPreferredShift(
                        updatedEmployee.getPreference().getPreferredShift());
                existingEmployee.getPreference().setWorksOnWeekend(
                        updatedEmployee.getPreference().getWorksOnWeekend());

                // Gérer les sites préférés si besoin
                if (updatedEmployee.getPreference().getPreferredSites() != null) {
                    existingEmployee.getPreference().setPreferredSites(
                            updatedEmployee.getPreference().getPreferredSites());
                }

            } else {
                // Si pas de préférence existante, on ajoute une nouvelle
                updatedEmployee.getPreference().setEmployee(existingEmployee);
                existingEmployee.setPreference(updatedEmployee.getPreference());
            }
        }


        return employeeRepository.save(existingEmployee);
    }


    @Override
    public void deleteEmployee(Long id) {
        employeeRepository.deleteById(id);
    }

    @Override
    public List<Employee> findAllByCompanyId(Long companyId) {
        return employeeRepository.findByCompanyId(companyId);
    }

    @Override
    public Optional<Employee> findEmployeeById(Long id) {
        return employeeRepository.findById(id);
    }


    @Override
    public List<Employee> findAvailableEmployeesForSite(Long siteId) {
        return employeeRepository.findAvailableEmployeesForSite(siteId);
    }
}
