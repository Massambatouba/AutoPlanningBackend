package org.makarimal.autoplanningbackend.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmployeeConstraintDTO {

    private Long id;

    @NotNull
    private Long employeeId;

    @NotBlank
    private String typeContrainte;

    @NotNull
    private LocalDate dateDebut;

    @NotNull
    private LocalDate dateFin;

    private String recurrence;

    @Min(1)
    @Max(10)
    private int priorite;

    private String raison;
}

