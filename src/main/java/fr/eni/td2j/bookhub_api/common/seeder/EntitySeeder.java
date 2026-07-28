package fr.eni.td2j.bookhub_api.common.seeder;

public interface EntitySeeder {
    void seed();

    default int order() {
        return 0; // permet de définir un ordre d'exécution
    }
}
