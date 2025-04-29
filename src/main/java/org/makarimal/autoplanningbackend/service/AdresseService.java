package org.makarimal.autoplanningbackend.service;


import org.makarimal.autoplanningbackend.Repository.AdresseRepository;
import org.makarimal.autoplanningbackend.Repository.CompanyRepository;
import org.makarimal.autoplanningbackend.dto.AdresseDto;
import org.makarimal.autoplanningbackend.model.Adresse;
import org.makarimal.autoplanningbackend.model.Company;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AdresseService implements AdresseServiceItf {

    private final AdresseRepository adresseRepository;
    private final CompanyRepository companyRepository;


    public AdresseService(AdresseRepository adresseRepository, CompanyRepository companyRepository) {
        this.adresseRepository = adresseRepository;
        this.companyRepository = companyRepository;
    }

    public Adresse saveAdresse(AdresseDto dto) {
        Company company = companyRepository.findById(dto.getCompanyId())
                .orElseThrow(() -> new RuntimeException("Entreprise non trouvée avec l'ID : " + dto.getCompanyId()));

        Adresse adresse = Adresse.builder()
                .rue(dto.getRue())
                .ville(dto.getVille())
                .codePostal(dto.getCodePostal())
                .pays(dto.getPays())
                .build();

        return adresseRepository.save(adresse);
    }

    @Override
    public Adresse saveAdresse(Adresse adresse) {
        return adresseRepository.save(adresse);
    }

    @Override
    public List<Adresse> getAll() {
        return adresseRepository.findAll();
    }

    @Override
    public Optional<Adresse> getById(Long id) {
        return adresseRepository.findById(id);
    }

    @Override
    public void deleteAdresse(Long id) {
        adresseRepository.deleteById(id);
    }






}

