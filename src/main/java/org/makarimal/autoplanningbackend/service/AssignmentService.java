package org.makarimal.autoplanningbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.makarimal.autoplanningbackend.Repository.AssignmentRepository;
import org.makarimal.autoplanningbackend.model.Assignment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
public class AssignmentService implements AssignmentServiceItf {

    @Autowired
    private AssignmentRepository assignmentRepository;

    @Override
    public List<Assignment> getAllByAssignment(Long idAssignment) {
        return assignmentRepository.findAll();
    }

    @Override
    public Assignment createAssignment(Assignment assignment) {
        return assignmentRepository.save(assignment);
    }

    @Override
    public Assignment update(Long id, Assignment assignment) {
        Assignment existing = assignmentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Assignment not found with id: " + id));

        existing.setEmployee(assignment.getEmployee());
        existing.setSite(assignment.getSite());
        existing.setDate(assignment.getDate());
        existing.setStartTime(assignment.getStartTime());
        existing.setEndTime(assignment.getEndTime());
        existing.setStatus(assignment.getStatus());

        return assignmentRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!assignmentRepository.existsById(id)) {
            throw new EntityNotFoundException("Assignment not found with id: " + id);
        }
        assignmentRepository.deleteById(id);
    }

    @Override
    public List<Assignment> getByScheduleId(Long scheduleId) {
        return assignmentRepository.findByScheduleId(scheduleId);
    }

    @Override
    public List<Assignment> getByScheduleAndEmployeeId(Long scheduleId, Long employeeId) {
        return assignmentRepository.findByScheduleIdAndEmployeeIdOrderByDateAscStartTimeAsc(scheduleId, employeeId);
    }

    @Override
    public List<Assignment> getByScheduleAndSiteId(Long scheduleId, Long siteId) {
        return assignmentRepository.findByScheduleIdAndSiteIdOrderByDateAscStartTimeAsc(scheduleId, siteId);
    }

    @Override
    public int calculateWeeklyHours(Long employeeId, LocalDate startDate, LocalDate endDate) {
        return assignmentRepository.calculateWeeklyHours(employeeId, startDate, endDate);
    }
}
