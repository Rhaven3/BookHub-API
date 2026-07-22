package fr.eni.td2j.bookhub_api.repository;

import fr.eni.td2j.bookhub_api.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AuthorRepository extends JpaRepository<Author,Long> {
}
