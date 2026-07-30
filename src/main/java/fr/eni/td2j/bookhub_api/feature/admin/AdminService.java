package fr.eni.td2j.bookhub_api.feature.admin;

import fr.eni.td2j.bookhub_api.exception.EmailAlreadyExistsException;
import fr.eni.td2j.bookhub_api.exception.NotFoundException;
import fr.eni.td2j.bookhub_api.feature.address.Address;
import fr.eni.td2j.bookhub_api.feature.address.AddressService;
import fr.eni.td2j.bookhub_api.feature.user.User;
import fr.eni.td2j.bookhub_api.feature.user.UserMapper;
import fr.eni.td2j.bookhub_api.feature.user.UserRepository;
import fr.eni.td2j.bookhub_api.feature.user.dto.request.UpdateUserDTO;
import fr.eni.td2j.bookhub_api.feature.user.dto.response.UserResponseDTO;
import org.springframework.stereotype.Service;

@Service
public class AdminService {

    private final UserRepository userRepository;
    private final AddressService addressService;
    private final UserMapper userMapper;

    public AdminService(UserRepository userRepository, AddressService addressService, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.addressService = addressService;
        this.userMapper = userMapper;
    }

    public UserResponseDTO updateUser(Long id, UpdateUserDTO dto) {
        User user = userRepository.findById(id)
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
}
