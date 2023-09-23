package de.marshal.bankapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;

@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode

@Entity
@Table(name = "agreements")
public class Agreement {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "account_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    private Account account;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    private Product product;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @NonNull
    private AgreementStatus status;

    @Column(name = "interest_rate")
    @NonNull
    private Integer interestRate;

    @Column(name = "debt")
    @NonNull
    private Long debt;

    @Column(name = "created_at")
    private Timestamp createdAt;

    @Column(name = "updated_at")
    private Timestamp updatedAt;

    @PrePersist
    private void prePersist() {
        createdAt = updatedAt = Timestamp.from(Instant.now());
    }

    @PreUpdate
    private void preUpdate() {
        updatedAt = Timestamp.from(Instant.now());
    }
}
