package org.makarimal.autoplanningbackend.controller;

import org.makarimal.autoplanningbackend.model.Company;
import org.makarimal.autoplanningbackend.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/companies")
@CrossOrigin(origins = "*")
public class CompanyController {

    @Autowired
    private CompanyService companyService;

    @PostMapping
    public ResponseEntity<Company> createCompany(@RequestBody Company company) {
        Company savedCompany = companyService.createCompany(company);
        return ResponseEntity.ok(savedCompany);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Company> getCompanyById(@PathVariable Long id) {
        Company company = companyService.getCompanyById(id);
        return ResponseEntity.ok(company);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCompany(@PathVariable Long id) {
        companyService.deleteCompany(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/access")
    public ResponseEntity<Company> updateCompanyAccess(@PathVariable Long id, @RequestParam boolean hasSection) {
        Company updatedCompany = companyService.updateAccess(id, hasSection);
        return ResponseEntity.ok(updatedCompany);
    }

    @GetMapping("/by-name")
    public ResponseEntity<Company> findByName(@RequestParam String name) {
        Optional<Company> company = companyService.findByName(name);
        return company.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-licence")
    public ResponseEntity<Company> findByLicenceCode(@RequestParam String licenceCode) {
        Optional<Company> company = companyService.findByLicenceCode(licenceCode);
        return company.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
