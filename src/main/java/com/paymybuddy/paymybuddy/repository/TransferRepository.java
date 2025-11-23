package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.domain.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransferRepository extends JpaRepository<Transfer, Integer> {

    // Historique des transferts ÉMIS par un utilisateur (paginé)
    Page<Transfer> findBySenderId(Integer senderId, Pageable pageable);

    // Historique des transferts REÇUS par un utilisateur (paginé)
    Page<Transfer> findByReceiverId(Integer receiverId, Pageable pageable);
}

