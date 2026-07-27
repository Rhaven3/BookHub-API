package fr.eni.td2j.bookhub_api.service;


import fr.eni.td2j.bookhub_api.exception.EmailAlreadyExistsException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.feature.adresse.Address;
import fr.eni.td2j.bookhub_api.feature.adresse.AddressService;
import fr.eni.td2j.bookhub_api.feature.adresse.dto.request.AddressDTO;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.feature.user.UserMapper;
import fr.eni.td2j.bookhub_api.feature.user.UserRepository;
import fr.eni.td2j.bookhub_api.feature.user.UserService;
import fr.eni.td2j.bookhub_api.feature.user.dto.request.RegisterDTO;
import fr.eni.td2j.bookhub_api.feature.user.dto.request.UpdateUserDTO;
import fr.eni.td2j.bookhub_api.feature.user.dto.response.UserResponseDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private AddressService addressService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    private RegisterDTO registerDTO;
    private Address address;
    private AddressDTO addressDTO;

    @BeforeEach
    void setUp() {
        addressDTO = AddressDTO.builder()
                .street("1 rue de la Paix")
                .city("Paris")
                .postalCode("75002")
                .country("France")
                .build();

        registerDTO = new RegisterDTO();
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

        userService.register(registerDTO);

        verify(addressService).saveAddress(registerDTO.getAddress());
        verify(passwordEncoder).encode("plainPassword");
        verify(userRepository).save(any(User.class));
    }
    @Test
    void register_shouldThrowEmailAlreadyExistsException_whenEmailAlreadyUsed() {
        when(userRepository.findByEmail(registerDTO.getEmail())).thenReturn(Optional.of(new User()));

        assertThrows(EmailAlreadyExistsException.class, () -> userService.register(registerDTO));

        // Vérifie qu'on ne va jamais jusqu'à la sauvegarde
        verify(userRepository, never()).save(any(User.class));
        verify(addressService, never()).saveAddress(any());
    }

    @Test
    void updateProfile_shouldUpdateUserAndReturnDto_whenUserExists() {

        // Given : un user existant en base, et un DTO de mise à jour avec de nouvelles infos + une nouvelle adresse
        String email = "jean.dupont@test.com";

        User existingUser = User.builder()
                .id(1L)
                .lastName("Ancien nom")
                .firstName("Ancien prénom")
                .email(email)
                .phone("0600000000")
                .build();

        UpdateUserDTO dto = new UpdateUserDTO();
        dto.setName("Dupont");
        dto.setFirstName("Jean");
        dto.setPhone("0612345678");
        dto.setAddress(addressDTO);

        Address updatedAddress = new Address();
        UserResponseDTO expectedResponse = UserResponseDTO.builder()
                .id(1L)
                .name("Dupont")
                .firstName("Jean")
                .email(email)
                .phone("0612345678")
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(addressService.saveAddress(dto.getAddress())).thenReturn(updatedAddress);
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(userMapper.toDto(any(User.class))).thenReturn(expectedResponse);

        // When : on met à jour le profil de ce user
        UserResponseDTO result = userService.updateProfile(email, dto);

        // Then : le user est bien retrouvé, son adresse sauvegardée, l'entité mise à jour persistée,
        // et le mapper appelé pour retourner un DTO de réponse cohérent
        assertThat(result).isEqualTo(expectedResponse);
        verify(userRepository).findByEmail(email);
        verify(addressService).saveAddress(dto.getAddress());
        verify(userRepository).save(any(User.class));
        verify(userMapper).toDto(any(User.class));
    }

    @Test
    void deleteAccount_shouldAnonymizeUserData_whenUserExists() {
        // Given : un user existant avec des données personnelles complètes
        String email = "jean.dupont@test.com";

        User existingUser = User.builder()
                .id(1L)
                .lastName("Dupont")
                .firstName("Jean")
                .email(email)
                .phone("0612345678")
                .address(address)
                .build();

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // When : on supprime le compte
        userService.deleteAccount(email);

        // Then : les données personnelles sont bien anonymisées
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());

        User savedUser = userCaptor.getValue();
        assertThat(savedUser.getLastName()).isEqualTo("Anonyme");
        assertThat(savedUser.getFirstName()).isNull();
        assertThat(savedUser.getPhone()).isNull();
        assertThat(savedUser.getAddress()).isNull();
        assertThat(savedUser.getEmail()).isEqualTo("deleted_user_1@bookhub.local");
    }

    @Test
    void deleteAccount_shouldThrowNotFoundException_whenUserDoesNotExist() {
        // Given
        String email = "inconnu@test.com";
        when(userRepository.findByEmail(email)).thenReturn(Optional.empty());

        // When / Then
        assertThrows(NotFoundException.class, () -> userService.deleteAccount(email));

        verify(userRepository, never()).save(any(User.class));
    }
}