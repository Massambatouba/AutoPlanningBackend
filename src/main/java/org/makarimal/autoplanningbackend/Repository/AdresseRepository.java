package org.makarimal.autoplanningbackend.Repository;


import org.makarimal.autoplanningbackend.model.Adresse;
import org.makarimal.autoplanningbackend.model.Site;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdresseRepository extends JpaRepository<Adresse, Long> {


}
