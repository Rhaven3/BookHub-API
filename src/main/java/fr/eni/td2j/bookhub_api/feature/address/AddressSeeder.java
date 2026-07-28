package fr.eni.td2j.bookhub_api.feature.address;

import fr.eni.td2j.bookhub_api.common.seeder.EntitySeeder;
import org.springframework.stereotype.Component;

@Component
public class AddressSeeder implements EntitySeeder {

    private final AddressRepository addressRepository;

    public AddressSeeder(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    @Override
    public void seed() {
        if (addressRepository.count() > 0) return;

        addressRepository.save(Address.builder()
                .street("12 rue de la Paix")
                .city("Rennes")
                .postalCode("35000")
                .country("France")
                .build());

        addressRepository.save(Address.builder()
                .street("45 avenue Victor Hugo")
                .city("Nantes")
                .postalCode("44000")
                .country("France")
                .build());

        addressRepository.save(Address.builder()
                .street("8 rue des Lilas")
                .city("Paris")
                .postalCode("75011")
                .country("France")
                .build());
    }

    @Override
    public int order() {
        return 1; // aucune dépendance, peut s'exécuter tôt
    }
}