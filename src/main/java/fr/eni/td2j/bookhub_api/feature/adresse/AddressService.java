package fr.eni.td2j.bookhub_api.feature.adresse;

<<<<<<< Updated upstream:src/main/java/fr/eni/td2j/bookhub_api/adresse/AddressService.java
import fr.eni.td2j.bookhub_api.adresse.dto.request.AddressDTO;
=======
import fr.eni.td2j.bookhub_api.feature.adresse.dto.request.AddressCreateDTO;
>>>>>>> Stashed changes:src/main/java/fr/eni/td2j/bookhub_api/feature/adresse/AddressService.java
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address getAddressById(Long id) {
        return addressRepository.findById(id).orElse(null);
    }

    public List<Address> getAllAddresses() {
        return addressRepository.findAll();
    }

    public Address saveAddress (AddressDTO addressDTO){
        Optional<Address> addressExisting = addressRepository.findByStreetAndCityAndPostalCodeAndCountry(
                addressDTO.getStreet(),
                addressDTO.getCity(),
                addressDTO.getPostalCode(),
                addressDTO.getCountry()
        );
        if(addressExisting.isPresent()){
            return addressExisting.get();
        }

        Address saved = addressRepository.save(
                Address.builder()
                        .street(addressDTO.getStreet())
                        .city(addressDTO.getCity())
                        .postalCode(addressDTO.getPostalCode())
                        .country(addressDTO.getCountry())
                        .build()
        );
        return saved;
    }

}
