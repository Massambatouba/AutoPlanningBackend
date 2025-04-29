package org.makarimal.autoplanningbackend.service;

import org.makarimal.autoplanningbackend.model.Assignment;
import org.makarimal.autoplanningbackend.model.Schedule;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface ScheduleServiceItf {
    Schedule generateSchedule(LocalDate startDate, LocalDate endDate, String createdBy, Long companyId);

    Schedule generateMonthlySchedule(int month, int year, String createdBy, Long companyId);

    List<Assignment> generateSchedulesForAllEmployees(LocalDate startDate, LocalDate endDate, String createdBy, Long companyId);

    List<Schedule> generateIndividualSchedulesForAllEmployees(LocalDate startDate, LocalDate endDate, String createdBy, Long companyId);

    List<Schedule> findAll();

    Optional<Schedule> findById(Long id);

    Schedule validateAndSendSchedule(Long scheduleId);

    List<Assignment> getEmployeeSchedule(Long scheduleId, Long employeeId, Long companyId);

    List<Assignment> getSiteSchedule(Long scheduleId, Long siteId, Long companyId);
}
