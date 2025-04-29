package org.makarimal.autoplanningbackend.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.makarimal.autoplanningbackend.Repository.SiteRepository;
import org.makarimal.autoplanningbackend.Repository.SiteRequirementOverrideRepository;
import org.makarimal.autoplanningbackend.dto.SiteRequirementOverrideDTO;
import org.makarimal.autoplanningbackend.model.Site;
import org.makarimal.autoplanningbackend.model.SiteRequirementOverride;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SiteRequirementOverrideService implements SiteRequirementOverrideServiceItf {

    @Autowired
    private final SiteRequirementOverrideRepository repository;

    private static final Logger log = LoggerFactory.getLogger(SiteRequirementOverrideService.class);

    @Autowired
    private final SiteRepository siteRepository;

    @Override
    public List<SiteRequirementOverride> getAll(Long companyId) {
        return repository.findBySiteCompanyId(companyId);
    }

    @Override
    public List<SiteRequirementOverride> save(Long companyId, List<SiteRequirementOverride> overrides) {
        return overrides.stream().map(override -> {
            if (override.getSite() == null || override.getSite().getId() == null) {
                throw new IllegalArgumentException("Site or Site ID cannot be null.");
            }

            Long siteId = override.getSite().getId();

            Optional<Site> optionalSite = siteRepository.findByIdAndCompanyId(siteId, companyId);
            if (optionalSite.isEmpty()) {
                log.warn("No site found for siteId={} and companyId={}", siteId, companyId);
                throw new EntityNotFoundException("Site not found for this company. siteId=" + siteId + ", companyId=" + companyId);
            }
            Site site = optionalSite.get();

            override.setSite(site);

            return repository.save(override);
        }).toList();
    }


    @Override
    public List<SiteRequirementOverride> update(Long companyId, List<SiteRequirementOverrideDTO> overrideDTOs) {
        return overrideDTOs.stream().map(dto -> {
            if (dto.getSiteId() == null) {
                throw new IllegalArgumentException("Site ID is required for update.");
            }

            SiteRequirementOverride existingOverride = repository.findById(dto.getSiteId())
                    .orElseThrow(() -> new EntityNotFoundException("Override not found with ID: " + dto.getSiteId()));

            if (!existingOverride.getSite().getCompany().getId().equals(companyId)) {
                throw new IllegalArgumentException("Access Denied: Wrong Company ID.");
            }

            existingOverride.setAgentType(dto.getAgentType());
            existingOverride.setDayOfWeek(dto.getDayOfWeek());
            existingOverride.setDate(dto.getDate());
            existingOverride.setNumberOfAgentsRequired(dto.getNumberOfAgentsRequired());
            existingOverride.setStartTime(dto.getStartTime());
            existingOverride.setEndTime(dto.getEndTime());
            existingOverride.setRequiredSkills(dto.getRequiredSkills());
            existingOverride.setReason(dto.getReason());

            Site site = siteRepository.findByIdAndCompanyId(dto.getSiteId(), companyId)
                    .orElseThrow(() -> new EntityNotFoundException("Site not found for this company."));
            existingOverride.setSite(site);

            return repository.save(existingOverride);
        }).toList();
    }

    @Override
    public Optional<SiteRequirementOverride> getById(Long companyId, Long id) {
        return repository.findById(id)
                .filter(o -> o.getSite().getCompany().getId().equals(companyId));
    }

    @Override
    public List<SiteRequirementOverride> getBySite(Long companyId, String siteName) {
        return repository.findBySiteNameAndSiteCompanyId(siteName, companyId);
    }

    @Override
    public void delete(Long companyId, Long id) {
        SiteRequirementOverride override = repository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Override not found with ID: " + id));
        if (!override.getSite().getCompany().getId().equals(companyId)) {
            throw new IllegalArgumentException("Access Denied: Wrong Company ID.");
        }
        repository.deleteById(id);
    }
}
