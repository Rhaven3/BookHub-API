package fr.eni.td2j.bookhub_api.adresse;

import fr.eni.td2j.bookhub_api.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AddressRepository extends JpaRepository<Address, Long> {
 public Address findByStreetAndCityAndPostalCodeAndCountry(String street, String city, String postalCode, String country);
 }
