package org.makarimal.autoplanningbackend.service;

import org.makarimal.autoplanningbackend.dto.SiteRequirementOverrideDTO;
import org.makarimal.autoplanningbackend.model.SiteRequirementOverride;

import java.util.List;
import java.util.Optional;

public interface SiteRequirementOverrideServiceItf {

    List<SiteRequirementOverride> getAll(Long companyId);

    List<SiteRequirementOverride> save(Long companyId, List<SiteRequirementOverride> overrides);

    List<SiteRequirementOverride> update(Long companyId, List<SiteRequirementOverrideDTO> overrides);

    void delete(Long companyId, Long id);

    Optional<SiteRequirementOverride> getById(Long companyId, Long id);

    List<SiteRequirementOverride> getBySite(Long companyId, String siteName);
}
