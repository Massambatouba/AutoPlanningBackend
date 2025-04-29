package org.makarimal.autoplanningbackend.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "site_requirement_overrides")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SiteRequirementOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    private LocalDate date; // Exception spécifique (ex: 01/05)

    @Enumerated(EnumType.STRING)
    private AgentType agentType;

    private int requiredCount;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek;


    private Integer numberOfAgentsRequired; // Peut être 0 si pas d'agents ce jour

    private LocalTime startTime;
    private LocalTime endTime;

    private Long companyId;

    @ElementCollection
    @CollectionTable(name = "required_skills", joinColumns = @JoinColumn(name = "site_requirement_override_id"))
    @Column(name = "skill")
    private List<String> requiredSkills = new ArrayList<>();

    private String reason;


}
