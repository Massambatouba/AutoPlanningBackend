package org.makarimal.autoplanningbackend.service;

import org.makarimal.autoplanningbackend.Repository.UserRepository;
import org.makarimal.autoplanningbackend.model.User;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> getUsersByCompanyId(Long companyId) {
        return userRepository.findAllByCompanyId(companyId);
    }

    public Optional<User> findByNomUtilisateur(String username) {
        return userRepository.findByNomUtilisateur(username);
    }

    public void deleteUser(Long id) {
        userRepository.deleteById(id);
    }
}
