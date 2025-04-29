package org.makarimal.autoplanningbackend.service;

import org.makarimal.autoplanningbackend.model.User;

import java.util.List;
import java.util.Optional;

public interface UserServiceItf {

    public User createUser(User user);

    public List<User> getUsersByCompanyId(Long companyId);

    public Optional<User> findByNomUtilisateur(String username);

    public void deleteUser(Long id);
}
