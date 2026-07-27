package fr.eni.td2j.bookhub_api.seeder;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

@Component
@Profile("dev")
public class GlobalSeeder implements CommandLineRunner {
    private final List<EntitySeeder> seeders;

    public GlobalSeeder(List<EntitySeeder> seeders) {
        this.seeders = seeders;
    }

    @Override
    public void run(String... args) throws Exception {
        seeders.stream()
                .sorted(Comparator.comparingInt(EntitySeeder::order))
                .forEach(EntitySeeder::seed);
    }
}
