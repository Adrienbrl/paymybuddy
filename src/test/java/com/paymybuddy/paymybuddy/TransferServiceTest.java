package com.paymybuddy.paymybuddy;

import com.paymybuddy.paymybuddy.domain.Transfer;
import com.paymybuddy.paymybuddy.domain.User;
import com.paymybuddy.paymybuddy.dto.TransferResponseDTO;
import com.paymybuddy.paymybuddy.repository.TransferRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import com.paymybuddy.paymybuddy.service.TransferService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TransferServiceTest {

    @Mock
    private TransferRepository transferRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private TransferService transferService;

    @Test
    void createTransfer_shouldSaveAndReturnDto_whenReceiverIsConnection() {
        User sender = User.builder().id(1).username("gaston").connections(new HashSet<>()).build();
        User receiver = User.builder().id(2).username("anatole").build();
        sender.getConnections().add(receiver);

        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2)).thenReturn(Optional.of(receiver));

        Transfer persisted = Transfer.builder()
                .id(10)
                .sender(sender)
                .receiver(receiver)
                .description("courses")
                .amount(new BigDecimal("15.50"))
                .build();

        when(transferRepository.save(any(Transfer.class))).thenReturn(persisted);

        TransferResponseDTO dto = transferService.createTransfer(
                1,
                2,
                new BigDecimal("15.50"),
                "  courses  "
        );

        assertEquals(10, dto.id());
        assertEquals("anatole", dto.relationName());
        assertEquals("courses", dto.description());
        assertEquals(new BigDecimal("15.50"), dto.amount());
    }

    @Test
    void createTransfer_shouldThrowWhenReceiverIsNotConnection() {
        User sender = User.builder().id(1).username("gaston").connections(new HashSet<>()).build();
        User receiver = User.builder().id(2).username("anatole").build();

        when(userRepository.findById(1)).thenReturn(Optional.of(sender));
        when(userRepository.findById(2)).thenReturn(Optional.of(receiver));

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> transferService.createTransfer(1, 2, new BigDecimal("20.00"), "test")
        );

        assertEquals("Le destinataire doit être une relation", ex.getMessage());
    }

    @Test
    void createTransfer_shouldThrowWhenAmountHasMoreThanTwoDecimals() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> transferService.createTransfer(1, 2, new BigDecimal("10.999"), "test")
        );

        assertEquals("Le montant ne peut pas dépasser 2 décimales", ex.getMessage());
    }
}
