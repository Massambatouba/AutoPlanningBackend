package org.makarimal.autoplanningbackend.dto;

import jakarta.validation.constraints.*;
import lombok.Data;
import org.makarimal.autoplanningbackend.model.AgentType;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Data
public class SiteRequirementOverrideDTO {

    @NotNull(message = "Le siteId est obligatoire")
    private Long siteId;

    @NotNull(message = "La date est obligatoire")
    private LocalDate date;

    @NotNull(message = "Le type d'agent est obligatoire")
    private AgentType agentType;

    @NotNull(message = "Le jour de la semaine est obligatoire")
    private DayOfWeek dayOfWeek;

    @NotNull(message = "Le nombre d'agents requis est obligatoire")
    @Positive(message = "Le nombre d'agents requis doit être supérieur à 0")
    private Integer numberOfAgentsRequired;

    @NotNull(message = "L'heure de début est obligatoire")
    private LocalTime startTime;

    @NotNull(message = "L'heure de fin est obligatoire")
    private LocalTime endTime;

    private List<String> requiredSkills;

    private String reason;
}
