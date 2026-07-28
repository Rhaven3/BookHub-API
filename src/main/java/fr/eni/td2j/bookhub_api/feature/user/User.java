package fr.eni.td2j.bookhub_api.feature.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.eni.td2j.bookhub_api.feature.address.Address;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String role;

    @Column(nullable = true)
    private String lastName;

    @Column(nullable = true)
    private String firstName;

    @Column(nullable = false, unique = true)
    private String email;

    // JsonIgnore pour ne pas exposer le mot de passe dans les réponses JSON
    @JsonIgnore
    @Column(name = "password", nullable = false)
    private String password;

    private String phone;

    @ManyToOne
    @JoinColumn(name = "address_id", nullable = true)
    @JsonIgnore
    private Address address;
}
