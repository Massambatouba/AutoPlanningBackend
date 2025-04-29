package org.makarimal.autoplanningbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.optaplanner.core.api.domain.lookup.PlanningId;
import org.optaplanner.core.api.domain.solution.PlanningEntityCollectionProperty;
import org.optaplanner.core.api.domain.solution.PlanningScore;
import org.optaplanner.core.api.domain.solution.PlanningSolution;
import org.optaplanner.core.api.domain.solution.ProblemFactCollectionProperty;
import org.optaplanner.core.api.domain.valuerange.ValueRangeProvider;
import org.optaplanner.core.api.score.buildin.hardsoft.HardSoftScore;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "schedules")
@Data
@AllArgsConstructor
@NoArgsConstructor
@PlanningSolution
public class Schedule {
    @PlanningId
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime creationDate;
    private String createdBy;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;


    // Représente tous les employés possibles pour l'assignation
    @Transient
    @ValueRangeProvider(id = "employeeRange")
    @ProblemFactCollectionProperty
    private List<Employee> employeeList;

    @Transient
    @ProblemFactCollectionProperty
    private List<SiteRequirementOverride> requirements;

    @Enumerated(EnumType.STRING)
    private ScheduleStatus status;

    private LocalDate startDate;
    private LocalDate endDate;

    @PlanningEntityCollectionProperty
    @OneToMany(mappedBy = "schedule", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Assignment> assignments = new HashSet<>();

    @PlanningScore
    private HardSoftScore score;
    public enum ScheduleStatus {
        DRAFT, PUBLISHED, ARCHIVED, SENT_TO_EMPLOYEE
    }
}
