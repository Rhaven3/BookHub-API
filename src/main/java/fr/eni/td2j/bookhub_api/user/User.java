package fr.eni.td2j.bookhub_api.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fr.eni.td2j.bookhub_api.adresse.Address;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String role;

    @Column(nullable = true)
    private String name;

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
    @JoinColumn(name = "address_id")
    private Address address;
}
