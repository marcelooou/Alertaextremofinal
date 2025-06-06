package com.fiap.alertachuva.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.ExpiredJwtException; // Import para JWT expirado
import io.jsonwebtoken.SignatureException; // Import para assinatura inválida
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Base64; // Import para Base64.getDecoder()

@Component // Marca esta classe como um componente Spring para ser auto-detectada
public class JwtFilter extends OncePerRequestFilter {

    @Autowired
    private UserDetailsService userDetailsService; // Seu AuthService (que implementa UserDetailsService)

    @Value("${app.jwt.secret}") // Injeta a mesma chave secreta usada para gerar o token
    private String secret;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 1. Tenta obter o cabeçalho de autorização da requisição
        final String authorizationHeader = request.getHeader("Authorization");

        String username = null;
        String jwt = null;

        // 2. Se o cabeçalho existe e começa com "Bearer ", extrai o token
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7); // Remove "Bearer "
            try {
                // Extrai o username do token usando a chave secreta
                username = Jwts.parserBuilder()
                        .setSigningKey(Base64.getDecoder().decode(secret)) // Decodifica a chave antes de usar
                        .build()
                        .parseClaimsJws(jwt)
                        .getBody()
                        .getSubject();
            } catch (ExpiredJwtException e) {
                logger.warn("JWT token has expired for user: " + e.getClaims().getSubject());
                // Permite a requisição prosseguir para que o Spring Security possa lidar com o erro 401
                // Se você quiser um 403 aqui, pode lançar uma AuthenticationException
            } catch (SignatureException e) {
                logger.error("Invalid JWT signature: " + e.getMessage());
                // Permite a requisição prosseguir para que o Spring Security possa lidar com o erro 401
            } catch (Exception e) {
                logger.error("Error parsing JWT token: " + e.getMessage());
                // Permite a requisição prosseguir
            }
        }

        // 3. Se o username foi extraído e o usuário não está autenticado no contexto atual do Spring Security
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Carrega os detalhes do usuário usando o UserDetailsService (seu AuthService)
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            // 4. Se o token é válido (a validação é feita implicitamente pelo Jwts.parserBuilder().parseClaimsJws)
            // e os userDetails são carregados
            if (userDetails != null) {
                // Cria um objeto de autenticação para o Spring Security
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // Define a autenticação no SecurityContext do Spring Security
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }

        // 5. Continua a cadeia de filtros
        filterChain.doFilter(request, response);
    }
}