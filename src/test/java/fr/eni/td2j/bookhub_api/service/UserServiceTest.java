package fr.eni.td2j.bookhub_api.service;

import fr.eni.td2j.bookhub_api.adresse.Address;
import fr.eni.td2j.bookhub_api.adresse.AddressService;
import fr.eni.td2j.bookhub_api.adresse.dto.request.AddressDTO;
import fr.eni.td2j.bookhub_api.user.User;
import fr.eni.td2j.bookhub_api.user.UserRepository;
import fr.eni.td2j.bookhub_api.user.UserService;
import fr.eni.td2j.bookhub_api.user.dto.request.RegisterDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private RegisterDTO registerDTO;
    private Address address;

    @BeforeEach
    void setUp() {
        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setStreet("1 rue de la Paix");
        addressDTO.setCity("Paris");
        addressDTO.setPostalCode("75002");
        addressDTO.setCountry("France");

        registerDTO = new RegisterDTO();
        registerDTO.setRole("USER");
        registerDTO.setName("Dupont");
        registerDTO.setFirstName("Jean");
        registerDTO.setEmail("jean.dupont@test.com");
        registerDTO.setPassword("plainPassword");
        registerDTO.setPhone("0600000000");
        registerDTO.setAddress(addressDTO);

        address = Address.builder()
                .id(1L)
                .street("1 rue de la Paix")
                .city("Paris")
                .postalCode("75002")
                .country("France")
                .build();
    }

    @Test
    void register_shouldSaveUser_withEncodedPasswordAndResolvedAddress() {
        when(addressService.saveAddress(registerDTO.getAddress())).thenReturn(address);
        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        User result = userService.register(registerDTO);

        assertThat(result.getEmail()).isEqualTo("jean.dupont@test.com");
        assertThat(result.getPassword()).isEqualTo("encodedPassword");
        assertThat(result.getAddress()).isEqualTo(address);
        verify(addressService).saveAddress(registerDTO.getAddress());
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(any(User.class));
    }
}