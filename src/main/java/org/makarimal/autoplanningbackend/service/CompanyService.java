package org.makarimal.autoplanningbackend.service;

import jakarta.persistence.EntityNotFoundException;
import org.makarimal.autoplanningbackend.Repository.CompanyRepository;
import org.makarimal.autoplanningbackend.model.Company;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CompanyService implements CompanyServiceItf{

    private final CompanyRepository companyRepository;

    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    public Company createCompany(Company company) {
        return companyRepository.save(company);
    }

    public Optional<Company> findByName(String name) {
        return companyRepository.findByName(name);
    }

    public Optional<Company> findByLicenceCode(String licenceCode) {
        return companyRepository.findByLicenceCode(licenceCode);
    }

    public Optional<Company> findById(Long id) {
        return companyRepository.findById(id);
    }

    @Override
    public Company getCompanyById(Long id) {
        return companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Company with id " + id + " not found"));
    }

    public void deleteCompany(Long id) {
        companyRepository.deleteById(id);
    }

    public Company updateAccess(Long companyId, boolean hasSection) {
        Company company = companyRepository.findById(companyId).orElseThrow();
        company.setHasSection(hasSection);
        return companyRepository.save(company);
    }
}
