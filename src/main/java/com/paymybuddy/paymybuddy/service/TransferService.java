package com.paymybuddy.paymybuddy.service;

import com.paymybuddy.paymybuddy.domain.Transfer;
import com.paymybuddy.paymybuddy.domain.User;
import com.paymybuddy.paymybuddy.repository.TransferRepository;
import com.paymybuddy.paymybuddy.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.NoSuchElementException;

@Service
public class TransferService {
    private final TransferRepository transferRepository;
    private final UserRepository userRepository;

    public TransferService(TransferRepository transferRepository, UserRepository userRepository) {
        this.transferRepository = transferRepository;
        this.userRepository = userRepository;
    }

    /* --- FONCTION PRINCIPALE POUR LA PAGE DE TRANSFERT --- */

    @Transactional
    public Transfer createTransfer(Integer senderId, Integer receiverId, BigDecimal amount, String description) {
        if (senderId == null || receiverId == null) {
            throw new IllegalArgumentException("Les identifiants émetteur et destinataire sont requis");
        }
        if (senderId.equals(receiverId)) {
            throw new IllegalArgumentException("Impossible de se rembourser soi-même");
        }
        validateAmount(amount);

        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new NoSuchElementException("Émetteur introuvable"));
        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new NoSuchElementException("Destinataire introuvable"));

        if (!sender.getConnections().contains(receiver)) {
            throw new IllegalArgumentException("Le destinataire doit être une relation");
        }

        BigDecimal normalizedAmount = amount.setScale(2, RoundingMode.HALF_UP);
        String normalizedDescription = normalizeDescription(description);

        Transfer transfer = Transfer.builder()
                .sender(sender)
                .receiver(receiver)
                .amount(normalizedAmount)
                .description(normalizedDescription)
                .build();

        return transferRepository.save(transfer);
    }

    /* --- HISTORIQUE : UTILISÉ POUR REMPLIR "MES TRANSACTIONS" --- */

    public Page<Transfer> listSent(Integer senderId, Pageable pageable) {
        if (senderId == null) {
            throw new IllegalArgumentException("Identifiant utilisateur requis");
        }
        return transferRepository.findBySenderId(senderId, pageable);
    }

    public Page<Transfer> listReceived(Integer receiverId, Pageable pageable) {
        if (receiverId == null) {
            throw new IllegalArgumentException("Identifiant utilisateur requis");
        }
        return transferRepository.findByReceiverId(receiverId, pageable);
    }

    /* --- MÉTHODES PRIVÉES --- */

    private static void validateAmount(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Montant requis");
        }
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Le montant doit être supérieur à 0");
        }
        if (amount.scale() > 2) {
            throw new IllegalArgumentException("Le montant ne peut pas dépasser 2 décimales");
        }
    }

    private static String normalizeDescription(String description) {
        if (description == null) return null;
        String trimmed = description.trim();
        if (trimmed.isEmpty()) return null;
        return trimmed.length() > 255 ? trimmed.substring(0, 255) : trimmed;
    }
}

