package fr.eni.td2j.bookhub_api.service;

import fr.eni.td2j.bookhub_api.adresse.Address;
import fr.eni.td2j.bookhub_api.adresse.AddressRepository;
import fr.eni.td2j.bookhub_api.adresse.AddressService;
import fr.eni.td2j.bookhub_api.adresse.dto.request.AddressCreateDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AdressServiceTest {

    @Mock
    private AddressRepository addressRepository;

    @InjectMocks
    private AddressService addressService;

    private Address address;

    private AddressCreateDTO addressCreateDTO;

    @BeforeEach
    void setUp() {
        address = Address.builder()
                .id(1L)
                .street("1 rue de la Paix")
                .city("Paris")
                .postalCode("75002")
                .country("France")
                .build();
        addressCreateDTO = new AddressCreateDTO();
        addressCreateDTO.setCity("Paris");
        addressCreateDTO.setCountry("France");
        addressCreateDTO.setStreet("1 rue de la Paix");
        addressCreateDTO.setPostalCode("75002");
    }

    @Test
    void saveAddress_shouldReturnSavedAddress() {
        when(addressRepository.findByStreetAndCityAndPostalCodeAndCountry(
                addressCreateDTO.getStreet(),
                addressCreateDTO.getCity(),
                addressCreateDTO.getPostalCode(),
                addressCreateDTO.getCountry()
        )).thenReturn(address);

        when(addressRepository.save(address)).thenReturn(address);

        Address result = addressService.saveAddress(addressCreateDTO);

        assertThat(result).isEqualTo(address);
        verify(addressRepository).save(address);
    }

    @Test
    void getAddressById_shouldReturnAddress_whenFound() {
    }

    @Test
    void getAddressById_shouldReturnNull_whenNotFound() {
    }

    @Test
    void resolveAddress_shouldReturnExistingAddress_whenAlreadyExists() {
    }

    @Test
    void resolveAddress_shouldCreateNewAddress_whenNotExists() {
    }
}
