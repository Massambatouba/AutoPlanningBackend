package org.makarimal.autoplanningbackend.controller;

import org.makarimal.autoplanningbackend.model.Company;
import org.makarimal.autoplanningbackend.model.Site;
import org.makarimal.autoplanningbackend.service.CompanyService;
import org.makarimal.autoplanningbackend.service.SiteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies/{companyId}/sites")
@CrossOrigin("*")
public class SiteController {

    @Autowired
    private SiteService siteService;

    @Autowired
    private CompanyService companyService;

    // Liste des sites d'une entreprise
    @GetMapping
    public ResponseEntity<List<Site>> getAllSites(@PathVariable Long companyId) {
        Company company = companyService.getCompanyById(companyId);
        List<Site> allSites = siteService.getAll();
        List<Site> companySites = allSites.stream()
                .filter(site -> site.getCompany() != null && site.getCompany().getId().equals(companyId))
                .toList();
        return ResponseEntity.ok(companySites);
    }

    // Détail d’un site spécifique
    @GetMapping("/{siteId}")
    public ResponseEntity<Site> getSite(@PathVariable Long companyId, @PathVariable Long siteId) {
        Site site = siteService.getById(siteId);
        if (!site.getCompany().getId().equals(companyId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(site);
    }

    // Création d’un nouveau site pour une entreprise
    @PostMapping
    public ResponseEntity<Site> createSite(
            @PathVariable Long companyId,
            @RequestBody Site site
    ) {
        Company company = companyService.getCompanyById(companyId);

        // Lier l'adresse à l'entreprise
        if (site.getAddress() != null) {
            site.getAddress().setCompany(company);
        }

        site.setCompany(company);

        Site createdSite = siteService.createSite(site);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdSite);
    }


    // Mise à jour d’un site
    @PutMapping("/{siteId}")
    public ResponseEntity<Site> updateSite(@PathVariable Long companyId, @PathVariable Long siteId, @RequestBody Site siteDetails) {
        Site existingSite = siteService.getById(siteId);
        if (!existingSite.getCompany().getId().equals(companyId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        Site updatedSite = siteService.updateSite(siteId, siteDetails);
        return ResponseEntity.ok(updatedSite);
    }

    // Suppression d’un site
    @DeleteMapping("/{siteId}")
    public ResponseEntity<Void> deleteSite(@PathVariable Long companyId, @PathVariable Long siteId) {
        Site site = siteService.getById(siteId);
        if (!site.getCompany().getId().equals(companyId)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        siteService.deleteSite(siteId);
        return ResponseEntity.noContent().build();
    }
}
