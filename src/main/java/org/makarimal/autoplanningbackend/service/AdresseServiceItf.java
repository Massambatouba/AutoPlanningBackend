package org.makarimal.autoplanningbackend.service;

import org.makarimal.autoplanningbackend.dto.AdresseDto;
import org.makarimal.autoplanningbackend.model.Adresse;
import org.makarimal.autoplanningbackend.model.Site;

import java.util.List;
import java.util.Optional;

public interface AdresseServiceItf {
    Adresse saveAdresse(Adresse adresse);
    List<Adresse> getAll();
    Adresse saveAdresse(AdresseDto dto);

    Optional<Adresse> getById(Long id);
    void deleteAdresse(Long id);


}
