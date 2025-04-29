package org.makarimal.autoplanningbackend.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class EmployeeSiteAssignmentDto {
    @NotNull(message = "Site ID cannot be null")
    private Long siteId;
    private boolean estSitePrincipal;
    private String notes;
}
