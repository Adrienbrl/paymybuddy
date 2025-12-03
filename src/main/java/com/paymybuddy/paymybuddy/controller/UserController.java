package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.domain.User;
import com.paymybuddy.paymybuddy.dto.*;
import com.paymybuddy.paymybuddy.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> register(@Valid @RequestBody RegisterRequestDTO req) {
        User u = userService.register(req.username(), req.email(), req.password());
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(u));
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> login(@Valid @RequestBody LoginRequestDTO req) {
        User u = userService.authenticateByEmail(req.email(), req.password());
        // (Plus tard : émettre un JWT ou créer une session. Ici, on renvoie juste l'utilisateur.)
        return ResponseEntity.ok(toDto(u));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponseDTO> getById(@PathVariable Integer id) {
        User u = userService.getById(id);
        return ResponseEntity.ok(toDto(u));
    }

    @PostMapping("/{id}/connections")
    public ResponseEntity<Void> addConnection(
            @PathVariable Integer id,
            @Valid @RequestBody AddConnectionRequestDTO req
    ) {
        userService.addConnection(id, req.otherUserId());
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("/{id}/profile")
    public ResponseEntity<UserResponseDTO> updateProfile(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateProfileRequestDTO req
    ) {
        User u = userService.updateProfile(id, req.username(), req.email());
        return ResponseEntity.ok(toDto(u));
    }

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

