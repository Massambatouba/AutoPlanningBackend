package org.makarimal.autoplanningbackend.Repository;

import org.makarimal.autoplanningbackend.model.WeeklySiteRequirement;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WeeklySiteRequirementRepository extends JpaRepository<WeeklySiteRequirement, Long> {}
