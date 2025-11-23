package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.domain.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;
import java.util.Optional;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /* --- FONCTIONS POUR LA PAGE D'INSCRIPTION --- */

    @Transactional
    public User register(String username, String email, String rawPassword){
        if (username == null || username.isBlank() || email == null || email.isBlank()
                || rawPassword == null || rawPassword.isBlank()){
            throw new IllegalArgumentException("Les champs username, mail et mot de passe sont requis");
        }
        if (userRepository.existsByUsername(username)) {
            throw new IllegalArgumentException("Username déjà utilisé");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email déjà utilisé");
        }
        User user = User.builder()
                .username(username)
                .email(email)
                .password(passwordEncoder.encode(rawPassword))
                .build();
        return userRepository.save(user);
    }

    /* --- FONCTIONS POUR LA PAGE DE CONNEXION --- */

    public User authenticateByEmail(String email, String rawPassword) {
        Optional<User> optional = userRepository.findByEmail(email);
        User user = optional.orElseThrow(() -> new IllegalArgumentException("Email ou mot de passe invalide"));

        if (!passwordEncoder.matches(rawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Email ou mot de passe invalide");
        }
        return user;
    }

    public User getById(Integer id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur introuvable"));
    }

    /* --- FONCTIONS POUR LA PAGE D'AJOUT D'UNE RELATION --- */

    @Transactional
    public void addConnection(Integer userId, Integer otherUserId) {
        if (userId == null || otherUserId == null) {
            throw new IllegalArgumentException("Identifiants requis");
        }
        if (userId.equals(otherUserId)) {
            throw new IllegalArgumentException("Impossible de s’ajouter soi-même");
        }

        User u1 = userRepository.findById(userId)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur introuvable"));
        User u2 = userRepository.findById(otherUserId)
                .orElseThrow(() -> new NoSuchElementException("Relation introuvable"));

        if (u1.getConnections().contains(u2)) {
            return;
        }
        u1.addConnection(u2);
    }

    /* --- FONCTIONS POUR LA PAGE DE PROFIl --- */

    @Transactional
    public User updateProfile(Integer id, String newUsername, String newEmail) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur introuvable"));

        if (newUsername != null && !newUsername.isBlank() && !newUsername.equals(user.getUsername())) {
            if (userRepository.existsByUsername(newUsername)) {
                throw new IllegalArgumentException("Username déjà utilisé");
            }
            user.setUsername(newUsername);
        }
        if (newEmail != null && !newEmail.isBlank() && !newEmail.equals(user.getEmail())) {
            if (userRepository.existsByEmail(newEmail)) {
                throw new IllegalArgumentException("Email déjà utilisé");
            }
            user.setEmail(newEmail);
        }
        return user;
    }

    @Transactional
    public void changePassword(Integer id, String oldRawPassword, String newRawPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(oldRawPassword, user.getPassword())) {
            throw new IllegalArgumentException("Ancien mot de passe invalide");
        }
        if (newRawPassword == null || newRawPassword.isBlank()) {
            throw new IllegalArgumentException("Nouveau mot de passe requis");
        }
        user.setPassword(passwordEncoder.encode(newRawPassword));
    }
}
