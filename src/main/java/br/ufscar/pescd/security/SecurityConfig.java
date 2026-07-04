package br.ufscar.pescd.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth
                        //TODOS (Já com as aspas arrumadas para o Swagger funcionar!)
                        .requestMatchers("/login" , "/css/**", "/visitante/**", "/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui.html").permitAll()
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
                .formLogin(form -> form
                        .loginPage("/login")
                        .successHandler(customSuccessHandler())
                        .permitAll()
                )

                .logout(Customizer.withDefaults());

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
}