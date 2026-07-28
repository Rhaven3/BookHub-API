package fr.eni.td2j.bookhub_api.feature.user;


import fr.eni.td2j.bookhub_api.exception.BadRequestException;
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
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Cet email est déjà utilisé par un autre compte.");
        }
        Address address = addressService.saveAddress(dto.getAddress());

        User user = User.builder()
                .role("USER")
                .lastName(dto.getLastName())
                .firstName(dto.getFirstName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .address(address)
                .build();
        userRepository.save(user);
    }

    public UserResponseDTO updateProfile(String currentEmail, UpdateUserDTO dto) {
        User user = userRepository.findByEmail(currentEmail)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));

        // Si l'email change, vérifier qu'il n'est pas déjà pris par un autre compte
        if (!user.getEmail().equals(dto.getEmail())
                && userRepository.existsByEmail(dto.getEmail())) {
            throw new EmailAlreadyExistsException("Cet email est déjà utilisé par un autre compte.");
        }
        user.setLastName(dto.getLastName());
        user.setFirstName(dto.getFirstName());
        user.setPhone(dto.getPhone());
        user.setEmail(dto.getEmail());

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

    public void updatePassword(String email, String ancienMotDePasse, String nouveauMotDePasse) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("Utilisateur introuvable"));

        if (!passwordEncoder.matches(ancienMotDePasse, user.getPassword())) {
            throw new BadRequestException("Ancien mot de passe incorrect");
        }

        user.setPassword(passwordEncoder.encode(nouveauMotDePasse));
        userRepository.save(user);
    }
}