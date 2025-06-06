package com.fiap.alertachuva.security;

import com.fiap.alertachuva.entity.AppUser; // Importa sua entidade AppUser
import com.fiap.alertachuva.repository.AppUserRepository; // Importa seu AppUserRepository
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service; // Importa a anotação Service

import java.util.ArrayList; // Para criar uma lista vazia de authorities (se você não tiver roles)

@Service // Marca esta classe como um serviço Spring
public class AuthService implements UserDetailsService {

    @Autowired
    private AppUserRepository appUserRepository; // Injeta seu AppUserRepository

    /**
     * Este método é chamado pelo Spring Security quando um usuário tenta fazer login.
     * Ele deve carregar os detalhes do usuário pelo username.
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Busca o AppUser no banco de dados usando o AppUserRepository
        AppUser appUser = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));

        // Constrói e retorna um objeto UserDetails.
        // O UserDetails é uma interface do Spring Security que representa os detalhes do usuário autenticado.
        // Neste exemplo, estamos usando a implementação padrão User do Spring Security.
        // Se você tiver roles (papéis) na sua entidade AppUser, você precisaria mapeá-las aqui.
        // Por enquanto, usamos uma lista vazia de authorities.
        return new org.springframework.security.core.userdetails.User(
            appUser.getUsername(),
            appUser.getPassword(),
            new ArrayList<>() // Lista vazia de Authorities (se não houver roles por enquanto)
        );
    }
}