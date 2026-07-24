package fr.eni.td2j.bookhub_api.author;

import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author,Long> {
    boolean existsByFnameIgnoreCaseAndLnameIgnoreCase(String fname, String lname);

}
