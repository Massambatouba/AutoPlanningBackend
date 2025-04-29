package org.makarimal.autoplanningbackend.service;

import org.makarimal.autoplanningbackend.model.Company;

import java.util.Optional;

public interface CompanyServiceItf {

    public Company createCompany(Company company);

    public Optional<Company> findByName(String name);

    public Optional<Company> findByLicenceCode(String licenceCode);

    public Optional<Company> findById(Long id);

    Company getCompanyById(Long id);
}
