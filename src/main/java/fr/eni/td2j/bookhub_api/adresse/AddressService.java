package fr.eni.td2j.bookhub_api.adresse;

import fr.eni.td2j.bookhub_api.adresse.dto.request.AddressCreateDTO;
import org.springframework.stereotype.Service;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    public Address getAddressById(Long id) {
        return addressRepository.findById(id).orElse(null);
    }

    public Address saveAddress (AddressCreateDTO addressDTO){
        Address addressExisting = addressRepository.findByStreetAndCityAndPostalCodeAndCountry(
                addressDTO.getStreet(),
                addressDTO.getCity(),
                addressDTO.getPostalCode(),
                addressDTO.getCountry()
        );
        if(addressExisting != null){
            return addressExisting;
        }
        return addressRepository.save(
                Address.builder()
                        .street(addressDTO.getStreet())
                        .city(addressDTO.getCity())
                        .postalCode(addressDTO.getPostalCode())
                        .country(addressDTO.getCountry())
                        .build()
        );
    }
}
