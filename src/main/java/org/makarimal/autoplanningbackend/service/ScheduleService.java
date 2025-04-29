package org.makarimal.autoplanningbackend.service;

import org.makarimal.autoplanningbackend.Repository.*;
import org.makarimal.autoplanningbackend.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ScheduleService implements ScheduleServiceItf {
    private final SiteRepository siteRepository;
    private final HolidayRepository holidayRepository;
    private final EmployeeRepository employeeRepository;
    private final SiteRequirementOverrideRepository requirementRepository;
    private final ScheduleRepository scheduleRepository;
    private final AssignmentRepository assignmentRepository;
    private final CompanyRepository companyRepository;

    private static final Logger logger = LoggerFactory.getLogger(ScheduleService.class);

    @Autowired
    public ScheduleService(SiteRepository siteRepository,
                           HolidayRepository holidayRepository,
                           EmployeeRepository employeeRepository,
                           SiteRequirementOverrideRepository requirementRepository,
                           ScheduleRepository scheduleRepository,
                           AssignmentRepository assignmentRepository,
                           CompanyRepository companyRepository) {
        this.siteRepository = siteRepository;
        this.holidayRepository = holidayRepository;
        this.employeeRepository = employeeRepository;
        this.requirementRepository = requirementRepository;
        this.scheduleRepository = scheduleRepository;
        this.assignmentRepository = assignmentRepository;
        this.companyRepository = companyRepository;
    }

    @Transactional
    @Override
    public Schedule generateSchedule(LocalDate startDate, LocalDate endDate, String createdBy, Long companyId) {
        logger.info("Starting schedule generation for period: {} - {} for company ID {}", startDate, endDate, companyId);

        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new RuntimeException("Company not found with ID: " + companyId));

        Schedule schedule = new Schedule();
        schedule.setCreationDate(LocalDateTime.now());
        schedule.setCreatedBy(createdBy);
        schedule.setStatus(Schedule.ScheduleStatus.DRAFT);
        schedule.setStartDate(startDate);
        schedule.setEndDate(endDate);
        schedule.setCompany(company);

        schedule = scheduleRepository.save(schedule);
        logger.info("Created new schedule with ID: {}", schedule.getId());

        List<Site> sites = siteRepository.findByCompanyId(companyId);
        logger.info("Found {} sites for company ID {}", sites.size(), companyId);

        if (sites.isEmpty()) {
            logger.warn("No sites found for company ID {}", companyId);
            return schedule;
        }

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            LocalDate currentDate = date;
            logger.debug("Processing schedule for date: {}", currentDate);

            for (Site site : sites) {
                if (shouldSkipSiteForDate(site, currentDate, companyId)) {
                    continue;
                }

                List<SiteRequirementOverride> requirements = requirementRepository
                        .findBySiteIdAndDayOfWeek(site.getId(), currentDate.getDayOfWeek());

                logger.debug("Found {} requirements for site {} on {}", requirements.size(), site.getId(), currentDate);

                if (requirements.isEmpty()) {
                    logger.debug("No requirements found for site {} on {}", site.getId(), currentDate);
                    continue;
                }

                processRequirements(requirements, site, currentDate, schedule);
            }
        }

        return schedule;
    }

        @Override
    @Transactional
    public Schedule generateMonthlySchedule(int month, int year, String createdBy, Long companyId) {
        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());
        return generateSchedule(startDate, endDate, createdBy, companyId);
    }

    @Override
    @Transactional
    public List<Assignment> generateSchedulesForAllEmployees(LocalDate startDate, LocalDate endDate, String createdBy, Long companyId) {
        Schedule schedule = generateSchedule(startDate, endDate, createdBy, companyId);
        return assignmentRepository.findByScheduleId(schedule.getId());
    }

    @Override
    @Transactional
    public List<Schedule> generateIndividualSchedulesForAllEmployees(LocalDate startDate, LocalDate endDate, String createdBy, Long companyId) {
        List<Employee> employees = employeeRepository.findByCompanyId(companyId);
        List<Schedule> schedules = new ArrayList<>();

        for (Employee employee : employees) {
            List<Assignment> assignments = generateAssignmentsForEmployee(employee, startDate, endDate);

            if (!assignments.isEmpty()) {
                Schedule schedule = new Schedule();
                schedule.setCreationDate(LocalDateTime.now());
                schedule.setCreatedBy(createdBy);
                schedule.setStatus(Schedule.ScheduleStatus.DRAFT);
                schedule.setStartDate(startDate);
                schedule.setEndDate(endDate);

                Company company = companyRepository.findById(companyId)
                        .orElseThrow(() -> new RuntimeException("Company not found"));
                schedule.setCompany(company);

                schedule = scheduleRepository.save(schedule);

                for (Assignment assignment : assignments) {
                    assignment.setSchedule(schedule);
                }
                assignmentRepository.saveAll(assignments);

                schedules.add(schedule);
            }
        }

        return schedules;
    }

    @Override
    public List<Schedule> findAll() {
        return scheduleRepository.findAll();
    }

    @Override
    public Optional<Schedule> findById(Long id) {
        return scheduleRepository.findById(id);
    }

    @Override
    @Transactional
    public Schedule validateAndSendSchedule(Long scheduleId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));
        schedule.setStatus(Schedule.ScheduleStatus.SENT_TO_EMPLOYEE);
        return scheduleRepository.save(schedule);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Assignment> getEmployeeSchedule(Long scheduleId, Long employeeId, Long companyId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("Access denied: Schedule does not belong to your company");
        }

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        if (!employee.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("Access denied: Employee does not belong to your company");
        }

        return assignmentRepository.findByScheduleIdAndEmployeeIdOrderByDateAscStartTimeAsc(scheduleId, employeeId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Assignment> getSiteSchedule(Long scheduleId, Long siteId, Long companyId) {
        Schedule schedule = scheduleRepository.findById(scheduleId)
                .orElseThrow(() -> new RuntimeException("Schedule not found"));

        if (!schedule.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("Access denied: Schedule does not belong to your company");
        }

        Site site = siteRepository.findById(siteId)
                .orElseThrow(() -> new RuntimeException("Site not found"));

        if (!site.getCompany().getId().equals(companyId)) {
            throw new RuntimeException("Access denied: Site does not belong to your company");
        }

        return assignmentRepository.findByScheduleIdAndSiteIdOrderByDateAscStartTimeAsc(scheduleId, siteId);
    }

    private boolean shouldSkipSiteForDate(Site site, LocalDate date, Long companyId) {
        if (!site.isOpenOnWeekends() && (date.getDayOfWeek().getValue() >= 6)) {
            logger.debug("Skipping weekend for site: {} on date: {}", site.getId(), date);
            return true;
        }

        if (!site.isOpenOnHolidays() && holidayRepository.existsByDateAndCompanyId(date, companyId)) {
            logger.debug("Skipping holiday for site: {} on date: {}", site.getId(), date);
            return true;
        }

        return false;
    }

    private void processRequirements(List<SiteRequirementOverride> requirements, Site site, LocalDate date, Schedule schedule) {
        for (SiteRequirementOverride requirement : requirements) {
            logger.debug("Processing requirement for site {} on {} - Agent type required: {}, Count: {}",
                    site.getId(), date, requirement.getAgentType(), requirement.getNumberOfAgentsRequired());

            List<Employee> suitableEmployees = findSuitableEmployees(requirement, date);
            logger.debug("Found {} suitable employees for requirement", suitableEmployees.size());

            if (suitableEmployees.isEmpty()) {
                logger.warn("No suitable employees found for requirement at site {} on {}", site.getId(), date);
                continue;
            }

            int assignedCount = 0;
            for (Employee employee : suitableEmployees) {
                if (assignedCount >= requirement.getNumberOfAgentsRequired()) {
                    break;
                }

                if (createAssignment(schedule, employee, site, date, requirement)) {
                    assignedCount++;
                }
            }

            if (assignedCount < requirement.getNumberOfAgentsRequired()) {
                logger.warn("Could not fulfill requirement for site {} on {}. Required: {}, Assigned: {}",
                        site.getId(), date, requirement.getNumberOfAgentsRequired(), assignedCount);
            }
        }
    }

    private boolean createAssignment(Schedule schedule, Employee employee, Site site, LocalDate date, SiteRequirementOverride requirement) {
        try {
            Assignment assignment = new Assignment();
            assignment.setSchedule(schedule);
            assignment.setEmployee(employee);
            assignment.setSite(site);
            assignment.setDate(date);
            assignment.setStartTime(requirement.getStartTime());
            assignment.setEndTime(requirement.getEndTime());
            assignment.setShiftType(Assignment.ShiftType.NORMAL);
            assignment.setStatus(Assignment.ShiftStatus.PLANNED);
            assignment.setManuallyModified(false);

            assignmentRepository.save(assignment);
            logger.debug("Created assignment for employee {} at site {} on {}",
                    employee.getId(), site.getId(), date);
            return true;
        } catch (Exception e) {
            logger.error("Failed to create assignment for employee {} at site {} on {}: {}",
                    employee.getId(), site.getId(), date, e.getMessage());
            return false;
        }
    }

    private List<Employee> findSuitableEmployees(SiteRequirementOverride requirement, LocalDate date) {
        logger.debug("Finding suitable employees for requirement on date: {} with agent type: {}",
                date, requirement.getAgentType());

        List<Employee> qualifiedEmployees = employeeRepository.findByAgentTypeAndCompanyId(
                requirement.getAgentType(),
                requirement.getSite().getCompany().getId()
        );

        logger.debug("Found {} qualified employees for agent type {}",
                qualifiedEmployees.size(), requirement.getAgentType());

        List<Employee> availableEmployees = qualifiedEmployees.stream()
                .filter(employee -> isEmployeeAvailable(employee, date, requirement))
                .sorted(Comparator.comparingInt(e ->
                        assignmentRepository.countByEmployeeIdAndDateBetween(
                                e.getId(),
                                date.minusDays(7),
                                date
                        )))
                .collect(Collectors.toList());

        logger.debug("After availability check, found {} available employees", availableEmployees.size());
        return availableEmployees;
    }


    private boolean isEmployeeAvailable(Employee employee, LocalDate date, SiteRequirementOverride requirement) {
        // Check if employee has no overlapping assignments
        boolean noOverlap = !assignmentRepository.existsOverlappingAssignment(
                employee.getId(),
                date,
                requirement.getStartTime(),
                requirement.getEndTime()
        );

        if (!noOverlap) {
            logger.debug("Employee {} has overlapping assignment on {}", employee.getId(), date);
            return false;
        }

        // Check weekly hours limit
        int weeklyHours = assignmentRepository.calculateWeeklyHours(
                employee.getId(),
                date.minusDays(6),
                date
        );

        if (weeklyHours >= 40) {
            logger.debug("Employee {} has reached weekly hours limit ({} hours)", employee.getId(), weeklyHours);
            return false;
        }

        return true;
    }

    private List<Assignment> generateAssignmentsForEmployee(Employee employee, LocalDate startDate, LocalDate endDate) {
        List<Assignment> assignments = new ArrayList<>();
        List<Site> sites = siteRepository.findByCompanyId(employee.getCompany().getId());

        logger.debug("Generating assignments for employee {} ({}) from {} to {}",
                employee.getId(),
                employee.getFirstName(),
                startDate,
                endDate);

        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
            for (Site site : sites) {
                // Skip if site is closed
                if (shouldSkipSiteForDate(site, date, employee.getCompany().getId())) {
                    continue;
                }

                List<SiteRequirementOverride> requirements = requirementRepository
                        .findBySiteIdAndDayOfWeek(site.getId(), date.getDayOfWeek());

                logger.debug("Found {} requirements for site {} on {}",
                        requirements.size(),
                        site.getId(),
                        date);

                for (SiteRequirementOverride requirement : requirements) {
                    if (isSuitableForAssignment(employee, requirement)) {
                        boolean isAvailable = !assignmentRepository.existsOverlappingAssignment(
                                employee.getId(),
                                date,
                                requirement.getStartTime(),
                                requirement.getEndTime()
                        );

                        if (isAvailable) {
                            Assignment assignment = new Assignment();
                            assignment.setEmployee(employee);
                            assignment.setSite(site);
                            assignment.setDate(date);
                            assignment.setStartTime(requirement.getStartTime());
                            assignment.setEndTime(requirement.getEndTime());
                            assignment.setShiftType(Assignment.ShiftType.NORMAL);
                            assignment.setStatus(Assignment.ShiftStatus.PLANNED);
                            assignment.setManuallyModified(false);

                            assignments.add(assignment);
                            logger.debug("Created assignment for employee {} at site {} on {}",
                                    employee.getId(),
                                    site.getId(),
                                    date);
                        }
                    }
                }
            }
        }

        logger.info("Generated {} assignments for employee {} ({})",
                assignments.size(),
                employee.getFirstName(),
                employee.getId());

        return assignments;
    }

    private boolean isSuitableForAssignment(Employee employee, SiteRequirementOverride requirement) {
        if (employee == null || requirement == null || employee.getAgentType() == null || requirement.getAgentType() == null) {
            logger.debug("Null check failed - employee: {}, requirement: {}",
                    employee != null ? employee.getId() : "null",
                    requirement != null ? requirement.getId() : "null");
            return false;
        }

        logger.debug("Comparing agentType for employee {} ({}): employee type={}, required type={}",
                employee.getId(),
                employee.getFirstName(),
                employee.getAgentType(),
                requirement.getAgentType());

        return employee.getAgentType().equals(requirement.getAgentType());
    }
}
