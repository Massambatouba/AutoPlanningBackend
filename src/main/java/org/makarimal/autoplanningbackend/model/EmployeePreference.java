package org.makarimal.autoplanningbackend.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeePreference {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    @JsonBackReference
    private Employee employee;

    private Boolean worksOnWeekend;
    private Boolean onlyWeekend;

    // JOUR, NUIT, INDIFFERENT
    private String preferredShift;

    private Integer minimumMonthlyHours;

    @ManyToMany
    @JoinTable(
            name = "employee_preferred_sites",
            joinColumns = @JoinColumn(name = "preference_id"),
            inverseJoinColumns = @JoinColumn(name = "site_id")
    )
    private Set<Site> preferredSites = new HashSet<>();
}
