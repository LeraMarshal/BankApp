package de.marshal.bankapp.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GeneratedColumn;

import java.sql.Timestamp;
import java.util.List;

@AllArgsConstructor
@RequiredArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode

@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    @NonNull
    private String name;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @NonNull
    private ProductStatus status;

    @Column(name = "currency_code")
    @NonNull
    private Integer currencyCode;

    @Column(name = "min_interest_rate")
    @NonNull
    private Integer minInterestRate;

    @Column(name = "max_offer_limit")
    @NonNull
    private Long maxOfferLimit;

    @GeneratedColumn("created_at")
    private Timestamp createdAt;

    @GeneratedColumn("updated_at")
    private Timestamp updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "product")
    private List<Agreement> agreements;
}
