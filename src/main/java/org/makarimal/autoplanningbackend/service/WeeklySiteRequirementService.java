package org.makarimal.autoplanningbackend.service;

import lombok.RequiredArgsConstructor;
import org.makarimal.autoplanningbackend.Repository.WeeklySiteRequirementRepository;
import org.makarimal.autoplanningbackend.model.WeeklySiteRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class WeeklySiteRequirementService {

    @Autowired
    private final WeeklySiteRequirementRepository repository;

    public List<WeeklySiteRequirement> getAll() {
        return repository.findAll();
    }

    public WeeklySiteRequirement save(WeeklySiteRequirement req) {
        return repository.save(req);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public Optional<WeeklySiteRequirement> getBySite(Long siteId) {
        return repository.findById(siteId);
    }
}

