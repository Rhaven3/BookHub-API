package fr.eni.td2j.bookhub_api.feature.user;

import fr.eni.td2j.bookhub_api.feature.address.Address;
import fr.eni.td2j.bookhub_api.feature.address.AddressRepository;
import fr.eni.td2j.bookhub_api.common.seeder.EntitySeeder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class UserSeeder implements EntitySeeder {

    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    public UserSeeder(UserRepository userRepository, AddressRepository addressRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.addressRepository = addressRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void seed() {
        if (userRepository.count() > 0) return;

        Address rennes = addressRepository.findAll().stream()
                .filter(a -> a.getCity().equals("Rennes"))
                .findFirst()
                .orElseThrow();

        Address nantes = addressRepository.findAll().stream()
                .filter(a -> a.getCity().equals("Nantes"))
                .findFirst()
                .orElseThrow();

        Address paris = addressRepository.findAll().stream()
                .filter(a -> a.getCity().equals("Paris"))
                .findFirst()
                .orElseThrow();

        userRepository.save(User.builder()
                .role(Role.ADMIN.name())
                .firstName("Admin")
                .lastName("Système")
                .email("admin@bookhub.fr")
                .password(passwordEncoder.encode("Admin123!"))
                .address(rennes)
                .phone("0600000001")
                .build());

        userRepository.save(User.builder()
                .role(Role.LIBRARIAN.name())
                .firstName("Claire")
                .lastName("Martin")
                .email("claire.martin@bookhub.fr")
                .password(passwordEncoder.encode("Libraire123!"))
                .address(nantes)
                .phone("0600000002")
                .build());

        userRepository.save(User.builder()
                .role(Role.LIBRARIAN.name())
                .firstName("Marc")
                .lastName("Dubois")
                .email("marc.dubois@bookhub.fr")
                .password(passwordEncoder.encode("Libraire123!"))
                .address(nantes)
                .phone("0600000003")
                .build());

        userRepository.save(User.builder()
                .role(Role.USER.name())
                .firstName("Alice")
                .lastName("Bernard")
                .email("alice.bernard@mail.com")
                .address(paris)
                .password(passwordEncoder.encode("User123!"))
                .phone("0600000004")
                .build());

        userRepository.save(User.builder()
                .role(Role.USER.name())
                .firstName("Bob")
                .lastName("Petit")
                .email("bob.petit@mail.com")
                .address(paris)
                .password(passwordEncoder.encode("User123!"))
                .phone("0600000005")
                .build());
        userRepository.save(User.builder()
                .role(Role.USER.name())
                .firstName("Camille")
                .lastName("Roux")
                .email("camille.roux@mail.com")
                .address(rennes)
                .password(passwordEncoder.encode("User123!"))
                .phone("0600000006")
                .build());

        userRepository.save(User.builder()
                .role(Role.USER.name())
                .firstName("Hugo")
                .lastName("Fontaine")
                .email("hugo.fontaine@mail.com")
                .address(nantes)
                .password(passwordEncoder.encode("User123!"))
                .phone("0600000007")
                .build());

        userRepository.save(User.builder()
                .role(Role.USER.name())
                .firstName("Léa")
                .lastName("Girard")
                .email("lea.girard@mail.com")
                .address(paris)
                .password(passwordEncoder.encode("User123!"))
                .phone("0600000008")
                .build());

        userRepository.save(User.builder()
                .role(Role.USER.name())
                .firstName("Thomas")
                .lastName("Morel")
                .email("thomas.morel@mail.com")
                .address(rennes)
                .password(passwordEncoder.encode("User123!"))
                .phone("0600000009")
                .build());

        userRepository.save(User.builder()
                .role(Role.USER.name())
                .firstName("Manon")
                .lastName("Lefevre")
                .email("manon.lefevre@mail.com")
                .address(nantes)
                .password(passwordEncoder.encode("User123!"))
                .phone("0600000010")
                .build());

        userRepository.save(User.builder()
                .role(Role.USER.name())
                .firstName("Nathan")
                .lastName("Simon")
                .email("nathan.simon@mail.com")
                .address(paris)
                .password(passwordEncoder.encode("User123!"))
                .phone("0600000011")
                .build());

        userRepository.save(User.builder()
                .role(Role.USER.name())
                .firstName("Chloé")
                .lastName("Michel")
                .email("chloe.michel@mail.com")
                .address(rennes)
                .password(passwordEncoder.encode("User123!"))
                .phone("0600000012")
                .build());

        userRepository.save(User.builder()
                .role(Role.USER.name())
                .firstName("Enzo")
                .lastName("Garnier")
                .email("enzo.garnier@mail.com")
                .address(nantes)
                .password(passwordEncoder.encode("User123!"))
                .phone("0600000013")
                .build());

        userRepository.save(User.builder()
                .role(Role.USER.name())
                .firstName("Sarah")
                .lastName("Faure")
                .email("sarah.faure@mail.com")
                .address(paris)
                .password(passwordEncoder.encode("User123!"))
                .phone("0600000014")
                .build());

        userRepository.save(User.builder()
                .role(Role.LIBRARIAN.name())
                .firstName("Julie")
                .lastName("Blanc")
                .email("julie.blanc@bookhub.fr")
                .address(rennes)
                .password(passwordEncoder.encode("Libraire123!"))
                .phone("0600000015")
                .build());
    }

    @Override
    public int order() {
        return 2;
    }
}
