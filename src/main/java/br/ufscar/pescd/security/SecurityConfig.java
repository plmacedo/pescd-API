package br.ufscar.pescd.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    // 1. Declaramos a variável aqui
    private final SecurityFilter securityFilter;

    // 2. O Spring usa esse construtor para injetar o filtro automaticamente
    public SecurityConfig(SecurityFilter securityFilter) {
        this.securityFilter = securityFilter;
    }


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable()).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        //TODOS (Já com as aspas arrumadas para o Swagger funcionar!)
                        .requestMatchers("/login", "/api/auth/login" , "/css/**", "/visitante/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html","/error").permitAll()
                        //ALUNO
                        .requestMatchers("/aluno/**").hasRole("ALUNO")
                        //ADMINISTRADOR
                        .requestMatchers("/administrador/**" ).hasRole("ADMINISTRADOR")
                        //RESPONSAVEL
                        .requestMatchers("/responsavel/**" ).hasRole("RESPONSAVEL")
                        //SUPERVISOR
                        .requestMatchers("/supervisor/**" ).hasRole("SUPERVISOR")
                        //SECRETARIO
                        .requestMatchers("/secretario/**" ).hasRole("SECRETARIO")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);



        return http.build();
    }

    @Bean
    public AuthenticationSuccessHandler customSuccessHandler() {
        return (request, response, authentication) -> {
            response.sendRedirect("/home");
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(br.ufscar.pescd.services.AutenticacaoService autenticacaoService, PasswordEncoder passwordEncoder) {

        // Agora o AutenticacaoService entra direto no momento da criação (dentro do 'new')
        org.springframework.security.authentication.dao.DaoAuthenticationProvider provider = new org.springframework.security.authentication.dao.DaoAuthenticationProvider(autenticacaoService);

        provider.setPasswordEncoder(passwordEncoder);

        return new org.springframework.security.authentication.ProviderManager(provider);
    }

}