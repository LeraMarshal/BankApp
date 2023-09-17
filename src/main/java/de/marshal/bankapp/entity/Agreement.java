package de.marshal.bankapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GeneratedColumn;

import java.sql.Timestamp;

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

    @GeneratedColumn("created_at")
    private Timestamp createdAt;

    @GeneratedColumn("updated_at")
    private Timestamp updatedAt;
}
