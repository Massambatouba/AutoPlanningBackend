package org.makarimal.autoplanningbackend.service;

import org.makarimal.autoplanningbackend.model.Assignment;
import java.util.List;

public interface AssignmentServiceItf {
    List<Assignment> getAllByAssignment(Long idAssignment);
    Assignment createAssignment(Assignment assignment);
    Assignment update(Long id, Assignment assignment);
    void delete(Long id);
    List<Assignment> getByScheduleId(Long scheduleId);
    List<Assignment> getByScheduleAndEmployeeId(Long scheduleId, Long employeeId);
    List<Assignment> getByScheduleAndSiteId(Long scheduleId, Long siteId);
    int calculateWeeklyHours(Long employeeId, java.time.LocalDate startDate, java.time.LocalDate endDate);
}
