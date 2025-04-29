package org.makarimal.autoplanningbackend.controller;

import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.makarimal.autoplanningbackend.dto.SiteRequirementOverrideDTO;
import org.makarimal.autoplanningbackend.model.Site;
import org.makarimal.autoplanningbackend.model.SiteRequirementOverride;
import org.makarimal.autoplanningbackend.service.SiteRequirementOverrideServiceItf;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/companies/{companyId}/requirements")
public class SiteRequirementOverrideController {

    private final SiteRequirementOverrideServiceItf service;

    @Autowired
    public SiteRequirementOverrideController(SiteRequirementOverrideServiceItf service) {
        this.service = service;
    }

    @GetMapping
    public List<SiteRequirementOverride> getAll(@PathVariable Long companyId) {
        return service.getAll(companyId);
    }

    @PostMapping
    public ResponseEntity<?> create(@PathVariable Long companyId, @RequestBody @Valid List<SiteRequirementOverrideDTO> overrideDTOs) {
        List<SiteRequirementOverride> overrides = overrideDTOs.stream().map(dto -> {
            SiteRequirementOverride override = new SiteRequirementOverride();
            override.setAgentType(dto.getAgentType());
            override.setDayOfWeek(dto.getDayOfWeek());
            override.setDate(dto.getDate());
            override.setNumberOfAgentsRequired(dto.getNumberOfAgentsRequired());
            override.setStartTime(dto.getStartTime());
            System.out.println("Trying to find Site with siteId=" + dto.getSiteId() + " and companyId=" + companyId);


            override.setEndTime(dto.getEndTime());
            override.setRequiredSkills(dto.getRequiredSkills());
            override.setReason(dto.getReason());

            Site site = new Site();
            site.setId(dto.getSiteId());
            override.setSite(site);

            return override;
        }).toList();

        return ResponseEntity.ok(service.save(companyId, overrides));
    }

    @PutMapping
    public ResponseEntity<?> update(@PathVariable Long companyId, @RequestBody List<SiteRequirementOverrideDTO> overrideDTOs) {
        try {
            List<SiteRequirementOverride> updatedOverrides = service.update(companyId, overrideDTOs);
            return ResponseEntity.ok(updatedOverrides);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/id/{id}")
    public ResponseEntity<?> getById(@PathVariable Long companyId, @PathVariable Long id) {
        return service.getById(companyId, id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/name/{name}")
    public ResponseEntity<List<SiteRequirementOverride>> getByNom(@PathVariable Long companyId, @PathVariable String name) {
        List<SiteRequirementOverride> overrides = service.getBySite(companyId, name);
        return ResponseEntity.ok(overrides);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long companyId, @PathVariable Long id) {
        service.delete(companyId, id);
        return ResponseEntity.noContent().build();
    }
}