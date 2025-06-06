package com.fiap.alertachuva.auth;

import com.fiap.alertachuva.entity.AppUser;
import com.fiap.alertachuva.repository.AppUserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; // Import para @Value
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder; // Import para PasswordEncoder
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Base64; // Import para Base64.getDecoder()

@RestController
@RequestMapping("/auth")
public class AuthController {

    // A CHAVE SECRETA SERÁ INJETADA DA VARIÁVEL DE AMBIENTE
    @Value("${app.jwt.secret}") // Esta anotação pega o valor de 'app.jwt.secret' do application.properties
    private String secretKey;   // Esta variável vai guardar a chave

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; // Injeção do PasswordEncoder

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AppUser user) {
        // Usa o AuthenticationManager do Spring Security para autenticar o usuário
        // Ele vai usar seu AuthService (UserDetailsService) e PasswordEncoder para verificar a senha
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

        // Se a autenticação falhar, uma exceção será lançada pelo AuthenticationManager,
        // então não precisamos de um if de verificação de senha aqui.
        // Se chegou até aqui, a autenticação foi bem-sucedida.

        // Gerar o token JWT
        String token = Jwts.builder()
                .setSubject(user.getUsername()) // O subject do token é o username
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora de validade
                // DECODIFICA A CHAVE BASE64 AQUI antes de usar para assinar o token
                .signWith(SignatureAlgorithm.HS256, Base64.getDecoder().decode(secretKey))
                .compact();

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }

    // ENDPOINT DE REGISTRO
    @PostMapping("/register")
    public Map<String, String> register(@RequestBody AppUser appUser) {
        // Verifica se o usuário já existe
        if (appUserRepository.findByUsername(appUser.getUsername()).isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Usuário já existe!");
            // Em um ambiente real, você pode preferir lançar uma exceção ou retornar um ResponseEntity com status 409 CONFLICT
            return errorResponse;
        }
        // Criptografa a senha antes de salvar no banco de dados
        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(appUser); // Salva o novo usuário no banco
        return Map.of("message", "Usuário registrado com sucesso!");
    }
}