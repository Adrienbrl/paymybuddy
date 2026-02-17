package com.paymybuddy.paymybuddy;

import com.paymybuddy.paymybuddy.domain.User;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.service.UserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @Test
    void register_shouldEncodePasswordAndSaveUser() {
        when(userRepository.existsByUsername("gaston")).thenReturn(false);
        when(userRepository.existsByEmail("gaston@paymybuddy.com")).thenReturn(false);
        when(passwordEncoder.encode("secret123")).thenReturn("hashed-secret");

        User saved = User.builder()
                .id(7)
                .username("gaston")
                .email("gaston@paymybuddy.com")
                .password("hashed-secret")
                .build();

        when(userRepository.save(any(User.class))).thenReturn(saved);

        User result = userService.register("gaston", "gaston@paymybuddy.com", "secret123");

        assertEquals(7, result.getId());
        assertEquals("gaston", result.getUsername());
        assertEquals("gaston@paymybuddy.com", result.getEmail());
        assertEquals("hashed-secret", result.getPassword());

        verify(passwordEncoder).encode("secret123");
        verify(userRepository).save(any(User.class));
    }

    @Test
    void register_shouldThrowWhenEmailAlreadyUsed() {
        when(userRepository.existsByUsername("gaston")).thenReturn(false);
        when(userRepository.existsByEmail("gaston@paymybuddy.com")).thenReturn(true);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.register("gaston", "gaston@paymybuddy.com", "secret123")
        );

        assertEquals("Email déjà utilisé", ex.getMessage());
        verify(userRepository, never()).save(any(User.class));
    }

    @Test
    void authenticateByEmail_shouldThrowWhenPasswordDoesNotMatch() {
        User existing = User.builder()
                .id(1)
                .username("gaston")
                .email("gaston@paymybuddy.com")
                .password("hashed-pass")
                .build();

        when(userRepository.findByEmail("gaston@paymybuddy.com")).thenReturn(Optional.of(existing));
        when(passwordEncoder.matches("bad-pass", "hashed-pass")).thenReturn(false);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.authenticateByEmail("gaston@paymybuddy.com", "bad-pass")
        );

        assertEquals("Email ou mot de passe invalide", ex.getMessage());
    }

    @Test
    void addConnection_shouldThrowWhenAddingSelf() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> userService.addConnection(1, 1)
        );

        assertEquals("Impossible de s’ajouter soi-même", ex.getMessage());
        verify(userRepository, never()).findById(any());
    }
}
