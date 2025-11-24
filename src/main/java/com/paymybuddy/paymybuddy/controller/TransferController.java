package com.paymybuddy.paymybuddy.controller;

import com.paymybuddy.paymybuddy.domain.Transfer;
import com.paymybuddy.paymybuddy.dto.CreateTransferRequestDTO;
import com.paymybuddy.paymybuddy.dto.TransferResponseDTO;
import com.paymybuddy.paymybuddy.service.TransferService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users/{id}/transfers")
public class TransferController {

    private final TransferService transferService;

    public TransferController(TransferService transferService) {
        this.transferService = transferService;
    }

    // Créer un transfert (page "Transférer")
    // POST /api/users/{id}/transfers
    @PostMapping
    public ResponseEntity<TransferResponseDTO> createTransfer(
            @PathVariable Integer id,
            @Valid @RequestBody CreateTransferRequestDTO req
    ) {
        Transfer transfer = transferService.createTransfer(
                id,                      // senderId
                req.toUserId(),          // receiverId
                req.amount(),
                req.description()
        );

        // On considère la perspective de l’émetteur : relation = destinataire
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toSentDto(transfer));
    }

    // Historique des transferts envoyés (pour "Mes Transactions")
    // GET /api/users/{id}/transfers/sent?page=0&size=10...
    @GetMapping("/sent")
    public ResponseEntity<Page<TransferResponseDTO>> listSent(
            @PathVariable Integer id,
            Pageable pageable
    ) {
        Page<Transfer> page = transferService.listSent(id, pageable);
        Page<TransferResponseDTO> dtoPage = page.map(TransferController::toSentDto);
        return ResponseEntity.ok(dtoPage);
    }

    // (optionnel mais cohérent avec ton service)
    // Historique des transferts reçus
    // GET /api/users/{id}/transfers/received
    @GetMapping("/received")
    public ResponseEntity<Page<TransferResponseDTO>> listReceived(
            @PathVariable Integer id,
            Pageable pageable
    ) {
        Page<Transfer> page = transferService.listReceived(id, pageable);
        Page<TransferResponseDTO> dtoPage = page.map(TransferController::toReceivedDto);
        return ResponseEntity.ok(dtoPage);
    }

    // ----- mapping Entity -> DTO -----

    private static TransferResponseDTO toSentDto(Transfer t) {
        // Pour un transfert "envoyé", la relation affichée est le RECEIVER
        return new TransferResponseDTO(
                t.getId(),
                t.getReceiver().getUsername(),
                t.getDescription(),
                t.getAmount()
        );
    }

    private static TransferResponseDTO toReceivedDto(Transfer t) {
        // Pour un transfert "reçu", la relation affichée est le SENDER
        return new TransferResponseDTO(
                t.getId(),
                t.getSender().getUsername(),
                t.getDescription(),
                t.getAmount()
        );
    }
}

