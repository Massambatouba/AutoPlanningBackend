package org.makarimal.autoplanningbackend.model;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import lombok.*;

import java.time.Duration;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "sites")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

@JsonIdentityInfo(
        generator = ObjectIdGenerators.PropertyGenerator.class,
        property = "id"
)
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Site {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String contactPerson;
    private String contactPhone;

    private boolean openOnWeekends;
    private boolean openOnHolidays;

    private LocalTime openingTime;
    private LocalTime closingTime;

    // Lien vers l’entreprise (company)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id")
    @JsonIgnore
    private Company company;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "addresse_id")
    private Adresse address;

    @Column(name = "heure_ouverture")
    private LocalTime openingHours; // JSON string with opening hours by day

    private Duration defaultShiftDuration;

    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<SiteRequirementOverride> requirements = new HashSet<>();

    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private Set<EmployeeSiteAssignment> employeeAssignments = new HashSet<>();

    @OneToMany(mappedBy = "site", cascade = CascadeType.ALL)
    private Set<Assignment> shifts = new HashSet<>();
}
