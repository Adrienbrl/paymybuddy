package com.paymybuddy.paymybuddy.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;

/**
 * Entité représentant un transfert d'argent entre deux utilisateurs.
 */
@Entity
@Table(
        name = "transfer",
        indexes = {
                @Index(name = "idx_transfer_sender",   columnList = "sender_id"),
                @Index(name = "idx_transfer_receiver", columnList = "receiver_id")
        }
)
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"sender", "receiver"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class Transfer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "sender_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_transfer_sender")
    )
    private User sender;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "receiver_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_transfer_receiver")
    )
    private User receiver;

    @Size(max = 255)
    @Column(name = "description", length = 255)
    private String description;

    @NotNull
    @Positive
    @Digits(integer = 10, fraction = 2)
    @Column(name = "amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal amount;

    /**
     * Contrainte de validation: un utilisateur ne peut pas s'envoyer un transfert.
     *
     * @return {@code true} si l'émetteur et le destinataire sont différents.
     */
    @AssertTrue(message = "sender and receiver must be different")
    private boolean isSenderDifferentFromReceiver() {
        return sender == null || !sender.equals(receiver);
    }

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

}
