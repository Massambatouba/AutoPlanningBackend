package org.makarimal.autoplanningbackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.DayOfWeek;
import java.time.LocalTime;

@Entity
@Table(name = "weekly_site_requirements")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WeeklySiteRequirement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "site_id", nullable = false)
    private Site site;

    @Enumerated(EnumType.STRING)
    private DayOfWeek dayOfWeek; // Lundi, Mardi...

    private LocalTime startTime;
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    private AgentType agentType; // ADS, SSIAP, etc.

    private Integer numberOfAgentsRequired;

    @Column(columnDefinition = "jsonb")
    private String requiredSkills;

    private Integer priority;
}
