package org.makarimal.autoplanningbackend.Repository;

import org.makarimal.autoplanningbackend.model.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    Optional<Company> findByName(String name);
    Optional<Company> findByLicenceCode(String licenceCode);
}
