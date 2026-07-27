package fr.eni.td2j.bookhub_api.service;


import fr.eni.td2j.bookhub_api.feature.adresse.Address;
import fr.eni.td2j.bookhub_api.feature.adresse.AddressRepository;
import fr.eni.td2j.bookhub_api.feature.adresse.AddressService;
import fr.eni.td2j.bookhub_api.feature.adresse.dto.request.AddressDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    private Address address;

    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        address = Address.builder()
                .id(1L)
                .street("1 rue de la Paix")
                .city("Paris")
                .postalCode("75002")
                .country("France")
                .build();

        addressDTO = new AddressDTO();
        addressDTO.setCity("Paris");
        addressDTO.setCountry("France");
        addressDTO.setStreet("1 rue de la Paix");
        addressDTO.setPostalCode("75002");
    }

    // On test l'utilisation d'une adresse existante via les infos du DTO
    // En vérifiant qu'il n'y a pas d'ajout en BDD via le never()
    @Test
    void saveAddress_shouldReturnExistingAddress_whenAlreadyExists() {
        when(addressRepository.findByStreetAndCityAndPostalCodeAndCountry(
                addressDTO.getStreet(),
                addressDTO.getCity(),
                addressDTO.getPostalCode(),
                addressDTO.getCountry()
        )).thenReturn(Optional.of(address));

        Address result = addressService.saveAddress(addressDTO);

        assertThat(result).isEqualTo(address);
        verify(addressRepository, never()).save(any(Address.class));
    }

    // On test la création d'un adresse en BDD
    // Donc adrese inexistante en BDD
    @Test
    void saveAddress_shouldCreateAndReturnNewAddress_whenNotExists() {
        when(addressRepository.findByStreetAndCityAndPostalCodeAndCountry(
                addressDTO.getStreet(),
                addressDTO.getCity(),
                addressDTO.getPostalCode(),
                addressDTO.getCountry()
        )).thenReturn(Optional.empty());

        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address result = addressService.saveAddress(addressDTO);

        assertThat(result).isEqualTo(address);
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    void getAddressById_shouldReturnAddress_whenFound() {
    }

    @Test
    void getAddressById_shouldReturnNull_whenNotFound() {
    }
}