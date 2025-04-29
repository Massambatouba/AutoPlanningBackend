package org.makarimal.autoplanningbackend.Repository;

import org.makarimal.autoplanningbackend.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface SiteRepository extends JpaRepository<Site, Long> {
    Optional<Site> findByIdAndCompanyId(Long siteId, Long companyId);

    @Query("SELECT s FROM Site s WHERE s.company.id = :companyId")
    List<Site> findByCompanyId(@Param("companyId") Long companyId);
}
