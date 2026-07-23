package fr.eni.td2j.bookhub_api.user;

import fr.eni.td2j.bookhub_api.adresse.Address;
import fr.eni.td2j.bookhub_api.adresse.AddressService;
import fr.eni.td2j.bookhub_api.user.dto.request.RegisterDTO;
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
}
