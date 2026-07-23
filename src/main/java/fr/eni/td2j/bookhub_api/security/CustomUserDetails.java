package fr.eni.td2j.bookhub_api.security;

import fr.eni.td2j.bookhub_api.user.User;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;


/**
 * Adaptateur entre l'entité métier {@link User} et l'interface {@link UserDetails}
 * attendue par Spring Security.
 * <p>
 * Spring Security ne connaît rien de notre entité JPA : il travaille uniquement
 * avec des objets {@link UserDetails}. Cette classe fait donc le pont en
 * "enveloppant" un {@link User} pour exposer les informations dont
 * Spring Security a besoin (identifiant de connexion, mot de passe, rôles...).
 */
public class CustomUserDetails implements UserDetails {

    /**
     * L'utilisateur métier issu de la base de données.
     */
    private final User user;

    /**
     * Crée un CustomUserDetails à partir d'un utilisateur récupéré en base.
     *
     * @param user l'entité utilisateur chargée depuis le repository
     */
    public CustomUserDetails(User user) {
        this.user = user;
    }

    /**
     * Permet de récupérer l'entité {@link User} complète (id, nom, prénom...)
     * à partir de l'utilisateur authentifié, sans refaire de requête en base.
     *
     * @return l'entité utilisateur wrappée
     */
    public User getUser() {
        return user;
    }

    /**
     * Raccourci pour accéder directement à l'id de l'utilisateur.
     *
     * @return l'id de l'utilisateur
     */
    public Long getId() {
        return user.getId();
    }

    /**
     * Retourne les rôles/autorisations de l'utilisateur.
     * <p>
     * Spring Security attend que chaque rôle soit préfixé par "ROLE_"
     * (convention utilisée par des méthodes comme {@code hasRole("ADMIN")}).
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole()));
    }

    /**
     * @return le mot de passe encodé (BCrypt) de l'utilisateur
     */
    @Override
    public String getPassword() {
        return user.getPassword();
    }

    /**
     * @return l'identifiant utilisé pour la connexion (ici, l'email)
     */
    @Override
    public String getUsername() {
        return user.getEmail();
    }

    /**
     * @return toujours true : la gestion d'expiration de compte n'est pas implémentée
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * @return toujours true : la gestion de verrouillage de compte n'est pas implémentée
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * @return toujours true : la gestion d'expiration des identifiants n'est pas implémentée
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * @return toujours true : tous les comptes sont considérés comme actifs pour l'instant
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}