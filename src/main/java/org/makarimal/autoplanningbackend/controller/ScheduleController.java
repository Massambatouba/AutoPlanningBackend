package org.makarimal.autoplanningbackend.controller;

import org.makarimal.autoplanningbackend.model.Assignment;
import org.makarimal.autoplanningbackend.model.Schedule;
import org.makarimal.autoplanningbackend.service.ScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/schedules")
public class ScheduleController {

    private final ScheduleService scheduleService;

    @Autowired
    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * Générer un planning entre deux dates
     */
    @PostMapping("/generate")
    public ResponseEntity<Schedule> generateSchedule(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("createdBy") String createdBy,
            @RequestParam("companyId") Long companyId
    ) {
        Schedule schedule = scheduleService.generateSchedule(startDate, endDate, createdBy, companyId);
        return ResponseEntity.ok(schedule);
    }

    /**
     * Générer tous les plannings des employees d'une entreprise
     */
    @PostMapping("/generate/all-employees")
    public ResponseEntity<List<Assignment>> generateSchedulesForAllEmployees(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("createdBy") String createdBy,
            @RequestParam("companyId") Long companyId
    ) {
        List<Assignment> assignments = scheduleService.generateSchedulesForAllEmployees(startDate, endDate, createdBy, companyId);
        return ResponseEntity.ok(assignments);
    }


    @PostMapping("/generate/individual")
    public ResponseEntity<List<Schedule>> generateSchedulesForAllEmployeesIndividually(
            @RequestParam("startDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam("endDate") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate,
            @RequestParam("createdBy") String createdBy,
            @RequestParam("companyId") Long companyId
    ) {
        List<Schedule> schedules = scheduleService.generateIndividualSchedulesForAllEmployees(startDate, endDate, createdBy, companyId);
        return ResponseEntity.ok(schedules);
    }




    @GetMapping("/{scheduleId}/employee/{employeeId}")
    public ResponseEntity<List<Assignment>> getEmployeeSchedule(
            @PathVariable Long scheduleId,
            @PathVariable Long employeeId,
            @RequestParam Long companyId) {
        List<Assignment> assignments = scheduleService.getEmployeeSchedule(scheduleId, employeeId, companyId);
        return ResponseEntity.ok(assignments);
    }

    @GetMapping("/{scheduleId}/site/{siteId}")
    public ResponseEntity<List<Assignment>> getSiteSchedule(
            @PathVariable Long scheduleId,
            @PathVariable Long siteId,
            @RequestParam Long companyId) {
        List<Assignment> assignments = scheduleService.getSiteSchedule(scheduleId, siteId, companyId);
        return ResponseEntity.ok(assignments);
    }



    @PostMapping("/generate/monthly")
    public ResponseEntity<Schedule> generateMonthlySchedule(
            @RequestParam("month") int month,
            @RequestParam("year") int year,
            @RequestParam("createdBy") String createdBy,
            @RequestParam("companyId") Long companyId
    ) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Schedule schedule = scheduleService.generateSchedule(startDate, endDate, createdBy, companyId);
        return ResponseEntity.ok(schedule);
    }


    /**
     * Récupérer un planning par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Schedule> getScheduleById(@PathVariable Long id) {
        return scheduleService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Liste de tous les plannings
     */
    @GetMapping
    public ResponseEntity<List<Schedule>> getAllSchedules() {
        List<Schedule> schedules = scheduleService.findAll();
        return ResponseEntity.ok(schedules);
    }

    /**
     * Valider et envoyer un planning à l'employé
     */
    @PutMapping("/{id}/validate")
    public ResponseEntity<Schedule> validateAndSendSchedule(@PathVariable Long id) {
        Schedule updated = scheduleService.validateAndSendSchedule(id);
        return ResponseEntity.ok(updated);
    }

}
