package org.makarimal.autoplanningbackend.controller;

import lombok.RequiredArgsConstructor;
import org.makarimal.autoplanningbackend.model.EmployeeSiteAssignment;
import org.makarimal.autoplanningbackend.service.EmployeeSiteAssignmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/assignments")
@RequiredArgsConstructor
public class EmployeeSiteAssignmentController {

    @Autowired
    private final EmployeeSiteAssignmentService employeeSiteAssignmentService;

    @GetMapping
    public ResponseEntity<List<EmployeeSiteAssignment>> getAll() {
        return ResponseEntity.ok(employeeSiteAssignmentService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<EmployeeSiteAssignment> getById(@PathVariable Long id) {
        return employeeSiteAssignmentService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        employeeSiteAssignmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/by-employee/{employeeId}")
    public ResponseEntity<List<EmployeeSiteAssignment>> getByEmployee(@PathVariable Long employeeId) {
        return ResponseEntity.ok(employeeSiteAssignmentService.getEmployeeSiteAssignmentByIdEmployee(employeeId));
    }

    @GetMapping("/by-site/{siteId}")
    public ResponseEntity<List<EmployeeSiteAssignment>> getBySite(@PathVariable Long siteId) {
        return ResponseEntity.ok(employeeSiteAssignmentService.getEmployeeSiteAssignmentByIdEmployee(siteId));
    }
}
