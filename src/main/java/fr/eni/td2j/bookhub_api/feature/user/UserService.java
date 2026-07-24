package fr.eni.td2j.bookhub_api.feature.user;

import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.feature.adresse.Address;
import fr.eni.td2j.bookhub_api.feature.adresse.AddressService;
import fr.eni.td2j.bookhub_api.feature.user.dto.request.RegisterDTO;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
public class UserService {
    private final AddressService addressService;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public UserService(AddressService addressService, PasswordEncoder passwordEncoder, UserRepository userRepository) {
        this.addressService = addressService;
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public User register(RegisterDTO dto) {
        Address address = addressService.saveAddress(dto.getAddress());

        User user = User.builder()
                .role(dto.getRole())
                .name(dto.getName())
                .firstName(dto.getFirstName())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .phone(dto.getPhone())
                .address(address)
                .build();

        return userRepository.save(user);
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

    public User getConnectedUser(UserDetails userDetails) {
        if (userDetails == null) {
            return null;
        }

        return userRepository.findByEmail(userDetails.getUsername())
                .orElse(null);
    }
}
