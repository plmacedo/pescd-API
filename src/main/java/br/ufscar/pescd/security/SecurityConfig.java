package br.ufscar.pescd.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Informa ao Security para respeitar as regras do CorsConfig
                .cors(Customizer.withDefaults())

                // 2. Mantém o CSRF desativado (necessário para APIs Stateless)
                .csrf(csrf -> csrf.disable())

                // 3. Define a política de sessão como STATELESS (API REST pura)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                .authorizeHttpRequests(auth -> auth
                        // Removi o "/css/**" pois a API não servirá mais arquivos estáticos CSS
                        .requestMatchers("/login", "/visitante/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
                        .requestMatchers("/aluno/**").hasRole("ALUNO")
                        .requestMatchers("/administrador/**" ).hasRole("ADMINISTRADOR")
                        .requestMatchers("/responsavel/**" ).hasRole("RESPONSAVEL")
                        .requestMatchers("/supervisor/**" ).hasRole("SUPERVISOR")
                        .requestMatchers("/secretario/**" ).hasRole("SECRETARIO")
                        .anyRequest().authenticated()
                );

        // NOTA: O .formLogin() e o customSuccessHandler() foram completamente removidos.
        // O logout() também foi removido, pois em JWT o logout é feito no frontend (apagando o token).

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}