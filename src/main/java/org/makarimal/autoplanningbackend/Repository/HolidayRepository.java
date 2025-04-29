package org.makarimal.autoplanningbackend.Repository;

import org.makarimal.autoplanningbackend.model.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    boolean existsByDateAndCompanyId(LocalDate date, Long companyId);
}