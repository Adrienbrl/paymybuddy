package com.paymybuddy.paymybuddy.repository;

import com.paymybuddy.paymybuddy.domain.Transfer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * Repository JPA des transferts.
 */
public interface TransferRepository extends JpaRepository<Transfer, Integer> {

    /**
     * Historique paginé des transferts émis.
     */
    Page<Transfer> findBySenderId(Integer senderId, Pageable pageable);

    /**
     * Historique paginé des transferts reçus.
     */
    Page<Transfer> findByReceiverId(Integer receiverId, Pageable pageable);

    /**
     * Historique complet des transferts émis triés du plus récent au plus ancien.
     */
    @Query("SELECT t FROM Transfer t " +
            "JOIN FETCH t.sender " +
            "JOIN FETCH t.receiver " +
            "WHERE t.sender.id = :senderId " +
            "ORDER BY t.createdAt DESC")
    List<Transfer> findBySenderIdOrderByCreatedAtDesc(Integer senderId);

}

