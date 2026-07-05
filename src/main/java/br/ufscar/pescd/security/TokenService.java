package br.ufscar.pescd.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

@Service
public class TokenService {

    // seed do hash para dados do usuario
    private final String secret = "123";

    // ao acertar login e senha
    public String gerarToken(String username) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret); // criptografia
            return JWT.create() // gera o token
                    .withIssuer("pescd-api") // Identifica quem gerou o token
                    .withSubject(username)    // Guarda o username do usuário
                    .withExpiresAt(gerarDataExpiracao()) // Define tempo de validade (ex: 2 horas)
                    .sign(algorithm);
        } catch (JWTCreationException exception) {
            throw new RuntimeException("Erro ao gerar token JWT", exception);
        }
    }

    public String validarToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            return JWT.require(algorithm)
                    .withIssuer("pescd-api")
                    .build()
                    .verify(token)
                    .getSubject(); // Se o token for válido, extrai o username de volta
        } catch (JWTVerificationException exception) {
            return null; // Se for inválido, adulterado ou expirado, retorna null
        }
    }

    private Instant gerarDataExpiracao() {
        // token expira em 2h
        return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
    }
}