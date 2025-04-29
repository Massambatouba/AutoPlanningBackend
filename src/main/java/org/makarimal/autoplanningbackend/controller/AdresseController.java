package org.makarimal.autoplanningbackend.controller;

import org.makarimal.autoplanningbackend.dto.AdresseDto;
import org.makarimal.autoplanningbackend.model.Adresse;
import org.makarimal.autoplanningbackend.model.Site;
import org.makarimal.autoplanningbackend.service.AdresseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/adresses")
@CrossOrigin(origins = "*")
public class AdresseController {

    @Autowired
    private AdresseService adresseService;

    @PostMapping
    public ResponseEntity<Adresse> createAdresse(@RequestBody Adresse adresse) {
        Adresse savedAdresse = adresseService.saveAdresse(adresse);
        return ResponseEntity.ok(savedAdresse);
    }

    @GetMapping
    public ResponseEntity<List<Adresse>> getAllAdresses() {
        return ResponseEntity.ok(adresseService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Adresse> getById(@PathVariable Long id) {
        return adresseService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }




    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdresse(@PathVariable Long id) {
        adresseService.deleteAdresse(id);
        return ResponseEntity.noContent().build();
    }
}
