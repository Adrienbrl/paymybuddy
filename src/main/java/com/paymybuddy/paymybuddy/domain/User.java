package com.paymybuddy.paymybuddy.domain;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(
        name = "user",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_user_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_user_username", columnNames = "username")
        }
)
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(exclude = {"password", "connections", "connectedToMe"})
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Integer id;

    @NotBlank
    @Size(max = 100)
    @Column(name = "username", nullable = false, length = 100)
    private String username;

    @NotBlank
    @Email
    @Size(max = 255)
    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @NotBlank
    @Size(min =8, max = 100)
    @Column(name = "password", nullable = false, length = 100)
    @JsonIgnore
    private String password;

    @Builder.Default
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_connection",
            joinColumns = @JoinColumn(name = "user_id", foreignKey = @ForeignKey(name = "fk_uc_user")),
            inverseJoinColumns = @JoinColumn(name = "connection_id", foreignKey = @ForeignKey(name = "fk_uc_connection"))
    )
    private Set<User> connections = new HashSet<>();

    @Builder.Default
    @ManyToMany(mappedBy = "connections", fetch = FetchType.LAZY)
    @JsonIgnore
    private Set<User> connectedToMe = new HashSet<>();

    public void addConnection(User other) {
        if (other == null || this.equals(other)) return;
        this.connections.add(other);
        other.connectedToMe.add(this);
    }

    public void removeConnection(User other) {
        if (other == null) return;
        this.connections.remove(other);
        other.connectedToMe.remove(this);
    }

}
