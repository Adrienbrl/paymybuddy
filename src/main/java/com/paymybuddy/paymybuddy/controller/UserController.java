package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.domain.User;
import com.paymybuddy.paymybuddy.dto.*;
import com.paymybuddy.paymybuddy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API REST de gestion utilisateur (inscription, profil, mot de passe, relations).
 */
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Endpoint d'inscription.
     */
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO req) {
        User u = userService.register(req.username(), req.email(), req.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(u));
    }

    /**
     * Endpoint de connexion (prototype sans JWT).
     */
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody LoginRequestDTO req) {
        User u = userService.authenticateByEmail(req.email(), req.password());
        return ResponseEntity.ok(toDto(u));
    }

    /**
     * Récupère un utilisateur par identifiant.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Integer id) {
        User u = userService.getById(id);
        return ResponseEntity.ok(toDto(u));
    }

    /**
     * Ajoute une relation pour l'utilisateur ciblé.
     */
    @PostMapping("/{id}/connections")
    public ResponseEntity<Void> addConnection(
            @PathVariable Integer id,
            @Valid @RequestBody AddConnectionRequestDTO req
    ) {
        userService.addConnection(id, req.otherUserId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    /**
     * Met à jour le profil utilisateur.
     */
    @PutMapping("/{id}/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateProfileRequestDTO req
    ) {
        User u = userService.updateProfile(id, req.username(), req.email());
        return ResponseEntity.ok(toDto(u));
    }

    /**
     * Met à jour le mot de passe utilisateur.
     */
    @PutMapping("/{id}/password")
    public ResponseEntity<Void> changePassword(
            @PathVariable Integer id,
            @Valid @RequestBody ChangePasswordRequestDTO req
    ) {
        userService.changePassword(id, req.oldPassword(), req.newPassword());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    private static UserResponseDTO toDto(User u) {
        return new UserResponseDTO(u.getId(), u.getUsername(), u.getEmail());
    }
}

