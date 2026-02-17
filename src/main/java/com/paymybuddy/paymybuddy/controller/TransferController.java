package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.dto.CreateTransferRequestDTO;
import com.paymybuddy.paymybuddy.dto.TransferResponseDTO;
import com.paymybuddy.paymybuddy.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * API REST dédiée aux transferts d'un utilisateur.
 */
@RestController
@RequestMapping("/api/users/{id}/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    /**
     * Crée un transfert sortant pour l'utilisateur courant.
     */
    @PostMapping
    public ResponseEntity<TransferResponseDTO> createTransfer(
            @PathVariable Integer id,
            @Valid @RequestBody CreateTransferRequestDTO req
    ) {
        TransferResponseDTO transfer = transferService.createTransfer(
                id,
                req.toUserId(),
                req.amount(),
                req.description()
        );

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(transfer);
    }

    /**
     * Liste paginée des transferts envoyés.
     */
    @GetMapping("/sent")
    public ResponseEntity<Page<TransferResponseDTO>> listSent(
            @PathVariable Integer id,
            Pageable pageable
    ) {
        Page<TransferResponseDTO> page = transferService.listSent(id, pageable);
        return ResponseEntity.ok(page);
    }

    /**
     * Liste paginée des transferts reçus.
     */
    @GetMapping("/received")
    public ResponseEntity<Page<TransferResponseDTO>> listReceived(
            @PathVariable Integer id,
            Pageable pageable
    ) {
        Page<TransferResponseDTO> page = transferService.listReceived(id, pageable);
        return ResponseEntity.ok(page);
    }
}
