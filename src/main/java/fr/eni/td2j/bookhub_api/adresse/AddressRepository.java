package fr.eni.td2j.bookhub_api.adresse;

import fr.eni.td2j.bookhub_api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
 public Optional<Address> findByStreetAndCityAndPostalCodeAndCountry(String street, String city, String postalCode, String country);
 }
