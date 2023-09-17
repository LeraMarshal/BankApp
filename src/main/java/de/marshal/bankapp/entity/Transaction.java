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
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "debit_account_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    private Account debitAccount;

    @JoinColumn(name = "credit_account_id")
    @ManyToOne(fetch = FetchType.EAGER)
    @NonNull
    private Account creditAccount;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @NonNull
    private TransactionStatus status;

    @Column(name = "amount")
    @NonNull
    private Long amount;

    @Column(name = "description")
    @NonNull
    private String description;

    @GeneratedColumn("created_at")
    private Timestamp createdAt;

    @GeneratedColumn("updated_at")
    private Timestamp updatedAt;
}
