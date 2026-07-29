package fr.eni.td2j.bookhub_api.feature.user;

import fr.eni.td2j.bookhub_api.common.EntityMapper;
import fr.eni.td2j.bookhub_api.feature.address.dto.request.AddressDTO;
import fr.eni.td2j.bookhub_api.feature.user.dto.response.UserResponseDTO;
import org.springframework.stereotype.Component;

@Component
public class UserMapper implements EntityMapper<User, UserResponseDTO> {

    public UserResponseDTO toDto(User user) {
        AddressDTO addressDTO = new AddressDTO();
        if(user.getAddress() != null) {
            addressDTO.setStreet(user.getAddress().getStreet());
            addressDTO.setCity(user.getAddress().getCity());
            addressDTO.setPostalCode(user.getAddress().getPostalCode());
            addressDTO.setCountry(user.getAddress().getCountry());
        }
        return UserResponseDTO.builder()
                .id(user.getId())
                .lastName(user.getLastName())
                .firstName(user.getFirstName())
                .role(user.getRole())
                .email(user.getEmail())
                .phone(user.getPhone())
                .addressDTO(user.getAddress() != null ? addressDTO : null)
                .build();
    }
}
