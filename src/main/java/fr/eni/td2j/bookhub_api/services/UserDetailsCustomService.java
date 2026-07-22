package fr.eni.td2j.bookhub_api.services;

import fr.eni.td2j.bookhub_api.entity.User;
import fr.eni.td2j.bookhub_api.repository.UserRepository;
import fr.eni.td2j.bookhub_api.security.CustomUserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

@Service
public class UserDetailsCustomService implements UserDetailsService {

    private final UserRepository userRepository;

    public UserDetailsCustomService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User utilisateur = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Utilisateur non trouvé"));

        return new CustomUserDetails(utilisateur);
    }
}
