package fr.eni.td2j.bookhub_api.service;

import fr.eni.td2j.bookhub_api.adresse.Address;
import fr.eni.td2j.bookhub_api.adresse.AddressRepository;
import fr.eni.td2j.bookhub_api.adresse.AddressService;
import fr.eni.td2j.bookhub_api.adresse.dto.request.AddressDTO;
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

    private AddressDTO addressCreateDTO;

    @BeforeEach
    void setUp() {
        address = Address.builder()
                .id(1L)
                .street("1 rue de la Paix")
                .city("Paris")
                .postalCode("75002")
                .country("France")
                .build();

        addressCreateDTO = new AddressDTO();
        addressCreateDTO.setCity("Paris");
        addressCreateDTO.setCountry("France");
        addressCreateDTO.setStreet("1 rue de la Paix");
        addressCreateDTO.setPostalCode("75002");
    }

    // On test l'utilisation d'une adresse existante via les infos du DTO
    // En vérifiant qu'il n'y a pas d'ajout en BDD via le never()
    @Test
    void saveAddress_shouldReturnExistingAddress_whenAlreadyExists() {
        when(addressRepository.findByStreetAndCityAndPostalCodeAndCountry(
                addressCreateDTO.getStreet(),
                addressCreateDTO.getCity(),
                addressCreateDTO.getPostalCode(),
                addressCreateDTO.getCountry()
        )).thenReturn(Optional.of(address));

        Address result = addressService.saveAddress(addressCreateDTO);

        assertThat(result).isEqualTo(address);
        verify(addressRepository, never()).save(any(Address.class));
    }

    // On test la création d'un adresse en BDD
    // Donc adrese inexistante en BDD
    @Test
    void saveAddress_shouldCreateAndReturnNewAddress_whenNotExists() {
        when(addressRepository.findByStreetAndCityAndPostalCodeAndCountry(
                addressCreateDTO.getStreet(),
                addressCreateDTO.getCity(),
                addressCreateDTO.getPostalCode(),
                addressCreateDTO.getCountry()
        )).thenReturn(Optional.empty());

        when(addressRepository.save(any(Address.class))).thenReturn(address);

        Address result = addressService.saveAddress(addressCreateDTO);

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