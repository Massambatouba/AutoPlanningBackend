package org.makarimal.autoplanningbackend.controller;

import lombok.RequiredArgsConstructor;
import org.makarimal.autoplanningbackend.model.WeeklySiteRequirement;
import org.makarimal.autoplanningbackend.service.WeeklySiteRequirementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/weekly-site-requirements")
@RequiredArgsConstructor
public class WeeklySiteRequirementController {

    @Autowired
    private final WeeklySiteRequirementService weeklySiteRequirementService;

    @PostMapping
    public ResponseEntity<WeeklySiteRequirement> create(@RequestBody WeeklySiteRequirement req) {
        return ResponseEntity.ok(weeklySiteRequirementService.save(req));
    }

    @GetMapping("/site/{siteId}")
    public ResponseEntity<Optional<WeeklySiteRequirement>> getBySite(@PathVariable Long siteId) {
        return ResponseEntity.ok(weeklySiteRequirementService.getBySite(siteId));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        weeklySiteRequirementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
