package org.makarimal.autoplanningbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.makarimal.autoplanningbackend.Repository.CompanyRepository;
import org.makarimal.autoplanningbackend.Repository.SiteRepository;
import org.makarimal.autoplanningbackend.model.Company;
import org.makarimal.autoplanningbackend.model.Site;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SiteService implements SiteServiceItf{

    @Autowired
    private SiteRepository siteRepository;

    @Autowired
    private CompanyRepository companyRepository;

    @Override
    public List<Site> getAll() {
        return siteRepository.findAll();
    }

    @Override
    public Site getById(Long id) {
        return siteRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Site not found with id: " + id));
    }

    @Override
    public Site createSite(Site site) {
        // Enregistre l’adresse en cascade (si elle existe)
        return siteRepository.save(site);
    }

    @Override
    public Optional<Site> getSiteById(Long siteId) {
        return siteRepository.findById(siteId);
    }

    @Override
    public Site updateSite(Long id, Site siteDetails) {
        return siteRepository.findById(id).map(site -> {
            site.setName(siteDetails.getName());
            site.setContactPerson(siteDetails.getContactPerson());
            site.setContactPhone(siteDetails.getContactPhone());
            site.setOpenOnWeekends(siteDetails.isOpenOnWeekends());
            site.setOpenOnHolidays(siteDetails.isOpenOnHolidays());
            site.setOpeningHours(siteDetails.getOpeningHours());
            site.setOpeningTime(siteDetails.getOpeningTime());
            site.setClosingTime(siteDetails.getClosingTime());
            return siteRepository.save(site);
        }).orElseThrow(() -> new EntityNotFoundException("Site not found with id: " + id));
    }

    @Override
    public void deleteSite(Long id) {
        siteRepository.deleteById(id);
    }
}
