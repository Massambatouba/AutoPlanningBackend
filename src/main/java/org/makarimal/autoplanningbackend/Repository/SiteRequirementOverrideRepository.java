package org.makarimal.autoplanningbackend.Repository;

import org.makarimal.autoplanningbackend.model.SiteRequirementOverride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
@Repository
public interface SiteRequirementOverrideRepository extends JpaRepository<SiteRequirementOverride, Long> {

    List<SiteRequirementOverride> findBySiteIdAndDayOfWeek(Long siteId, DayOfWeek dayOfWeek);

    List<SiteRequirementOverride> findBySiteId(Long siteId);

    List<SiteRequirementOverride> findBySiteCompanyId(Long companyId);

    List<SiteRequirementOverride> findBySiteNameAndSiteCompanyId(String name, Long companyId);

    // Méthode pour trouver les exigences d'un site en fonction du jour de la semaine et de la date
    List<SiteRequirementOverride> findBySiteIdAndDate(Long siteId, LocalDate date);

    // Ou si tu veux filtrer aussi par jour de la semaine, tu peux ajouter ça
    List<SiteRequirementOverride> findBySiteIdAndDayOfWeekAndDate(Long siteId, DayOfWeek dayOfWeek, LocalDate date);


}