package fr.eni.td2j.bookhub_api.feature.user;


import fr.eni.td2j.bookhub_api.exception.EmailAlreadyExistsException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;

import fr.eni.td2j.bookhub_api.exception.EmailAlreadyExistsException;
import fr.eni.td2j.bookhub_api.feature.address.Address;
import fr.eni.td2j.bookhub_api.feature.address.AddressService;
import fr.eni.td2j.bookhub_api.feature.user.dto.request.RegisterDTO;
import fr.eni.td2j.bookhub_api.feature.user.dto.request.UpdateUserDTO;
import fr.eni.td2j.bookhub_api.feature.user.dto.response.UserResponseDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    public UserService(AddressService addressService, PasswordEncoder passwordEncoder, UserRepository userRepository, UserMapper userMapper) {
        this.addressService = addressService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public void register(RegisterDTO dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new EmailAlreadyExistsException("Cet email est déjà utilisé");
        }
        Address address = addressService.saveAddress(dto.getAddress());

        User user = User.builder()
                .role("USER")
                .lastName(dto.getName())
                .firstName(dto.getFirstName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .address(address)
                .build();
        userRepository.save(user);
    }

    public UserResponseDTO updateProfile(String email, UpdateUserDTO dto) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));

        user.setLastName(dto.getName());
        user.setFirstName(dto.getFirstName());
        user.setPhone(dto.getPhone());

        if (dto.getAddress() != null) {
            Address address = addressService.saveAddress(dto.getAddress());
            user.setAddress(address);
        }

        User savedUser = userRepository.save(user);

        return userMapper.toDto(savedUser);
    }

    public void deleteAccount(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));
        user.setAddress(null);
        user.setLastName("Anonyme");
        user.setFirstName(null);
        user.setPhone(null);
        user.setEmail("deleted_user_" + user.getId() + "@bookhub.local");
        userRepository.save(user);
    }

    public UserResponseDTO getCurrentUser(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));

        return userMapper.toDto(user);
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));
    }
}