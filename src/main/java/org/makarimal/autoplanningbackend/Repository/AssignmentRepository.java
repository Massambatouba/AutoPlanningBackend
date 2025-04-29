package org.makarimal.autoplanningbackend.Repository;

import org.makarimal.autoplanningbackend.model.Assignment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AssignmentRepository extends JpaRepository<Assignment, Long> {
    List<Assignment> findByScheduleId(Long scheduleId);

    @Query("SELECT COUNT(a) > 0 FROM Assignment a WHERE a.employee.id = :employeeId AND a.date = :date AND " +
            "(:startTime < a.endTime AND :endTime > a.startTime)")
    boolean existsOverlappingAssignment(@Param("employeeId") Long employeeId,
                                        @Param("date") LocalDate date,
                                        @Param("startTime") LocalTime startTime,
                                        @Param("endTime") LocalTime endTime);



    int countByEmployeeIdAndDateBetween(Long id, LocalDate localDate, LocalDate date);

    List<Assignment> findByScheduleIdAndEmployeeIdOrderByDateAscStartTimeAsc(Long scheduleId, Long employeeId);

    List<Assignment> findByScheduleIdAndSiteIdOrderByDateAscStartTimeAsc(Long scheduleId, Long siteId);

    @Query("""
            SELECT COALESCE(SUM(
                EXTRACT(HOUR FROM a.endTime) - EXTRACT(HOUR FROM a.startTime)
            ), 0)
            FROM Assignment a 
            WHERE a.employee.id = :employeeId 
            AND a.date BETWEEN :startDate AND :endDate
            """)
    int calculateWeeklyHours(
            @Param("employeeId") Long employeeId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );
}
