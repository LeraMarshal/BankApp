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
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    @NonNull
    private ClientStatus status;

    @Column(name = "first_name")
    @NonNull
    private String firstName;

    @Column(name = "last_name")
    @NonNull
    private String lastName;

    @Column(name = "email")
    @NonNull
    private String email;

    @Column(name = "password")
    @NonNull
    private String password;

    @Column(name = "address")
    @NonNull
    private String address;

    @Column(name = "phone")
    @NonNull
    private String phone;

    @GeneratedColumn("created_at")
    private Timestamp createdAt;

    @GeneratedColumn("updated_at")
    private Timestamp updatedAt;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "client")
    private List<Account> accounts;
}
