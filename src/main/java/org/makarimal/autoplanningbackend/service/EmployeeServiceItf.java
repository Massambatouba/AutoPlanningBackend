package org.makarimal.autoplanningbackend.service;


import org.makarimal.autoplanningbackend.model.Employee;

import java.util.List;
import java.util.Optional;

public interface EmployeeServiceItf {

    public List<Employee> getAllEmployees();

    public Optional<Employee> getEmployeeById(Long id);

    public List<Employee> findEmployeesByQualification(String qualification);

    public Employee saveEmployee(Employee employee);

    public void deleteEmployee(Long id);

    List<Employee> findAllByCompanyId(Long companyId);



    public Optional<Employee> findEmployeeById(Long id);

    public List<Employee> findAvailableEmployeesForSite(Long siteId);


}
