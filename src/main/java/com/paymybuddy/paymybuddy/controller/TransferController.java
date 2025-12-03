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

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(toSentDto(transfer));
    }

    @GetMapping("/sent")
    public ResponseEntity<Page<TransferResponseDTO>> listSent(
            @PathVariable Integer id,
            Pageable pageable
    ) {
        Page<Transfer> page = transferService.listSent(id, pageable);
        Page<TransferResponseDTO> dtoPage = page.map(TransferController::toSentDto);
        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/received")
    public ResponseEntity<Page<TransferResponseDTO>> listReceived(
            @PathVariable Integer id,
            Pageable pageable
    ) {
        Page<Transfer> page = transferService.listReceived(id, pageable);
        Page<TransferResponseDTO> dtoPage = page.map(TransferController::toReceivedDto);
        return ResponseEntity.ok(dtoPage);
    }

    private static TransferResponseDTO toSentDto(Transfer t) {
        return new TransferResponseDTO(
                t.getId(),
                t.getReceiver().getUsername(),
                t.getDescription(),
                t.getAmount()
        );
    }

    private static TransferResponseDTO toReceivedDto(Transfer t) {
        return new TransferResponseDTO(
                t.getId(),
                t.getSender().getUsername(),
                t.getDescription(),
                t.getAmount()
        );
    }
}

