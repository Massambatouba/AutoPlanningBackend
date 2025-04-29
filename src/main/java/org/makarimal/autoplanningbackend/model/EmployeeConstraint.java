package org.makarimal.autoplanningbackend.model;

import jakarta.persistence.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeConstraint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relation avec l'employé concerné
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    // Ex: "indisponibilité", "préférence"
    @Column(name = "type_contrainte", nullable = false)
    private String typeContrainte;

    private LocalDate dateDebut;

    private LocalDate dateFin;

    // Ex: "quotidien", "hebdomadaire", "mensuel", "aucune"
    private String recurrence;

    // Priorité de 1 (basse) à 10 (haute)
    private int priorite;

    private String raison;
}

