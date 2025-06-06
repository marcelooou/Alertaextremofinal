package com.fiap.alertachuva.auth;

import com.fiap.alertachuva.entity.AppUser;
import com.fiap.alertachuva.repository.AppUserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder; 
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;
import java.util.HashMap;
import java.util.Base64; 

@RestController
@RequestMapping("/auth")
public class AuthController {
    
    @Value("${app.jwt.secret}") 
    private String secretKey;   

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private AppUserRepository appUserRepository;

    @Autowired
    private PasswordEncoder passwordEncoder; 

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody AppUser user) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())
        );

   
        String token = Jwts.builder()
                .setSubject(user.getUsername()) 
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60))               
                .signWith(SignatureAlgorithm.HS256, Base64.getDecoder().decode(secretKey))
                .compact();

        Map<String, String> response = new HashMap<>();
        response.put("token", token);
        return response;
    }


    @PostMapping("/register")
    public Map<String, String> register(@RequestBody AppUser appUser) {
        if (appUserRepository.findByUsername(appUser.getUsername()).isPresent()) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "Usuário já existe!");
            return errorResponse;
        }

        appUser.setPassword(passwordEncoder.encode(appUser.getPassword()));
        appUserRepository.save(appUser); 
        return Map.of("message", "Usuário registrado com sucesso!");
    }
}