package org.makarimal.autoplanningbackend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdresseDto {
    private String rue;
    private String ville;
    private String codePostal;
    private String pays;
    private Long companyId;

    public Long getCompanyId() {
        return companyId;
    }
}
