package org.makarimal.autoplanningbackend.Repository;

import org.makarimal.autoplanningbackend.model.Schedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScheduleRepository extends JpaRepository<Schedule, Long> {
}
