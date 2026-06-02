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

        http.authorizeHttpRequests(auth -> auth
                //TODOS
                        .requestMatchers("/login" , "/css", "/visitante").permitAll()
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

            var authorities =
                    authentication.getAuthorities();

            String redirectUrl = "/";

            for (var auth : authorities) {

                String role = auth.getAuthority();

                if (role.equals("ROLE_ADMINISTRADOR")) {
                    redirectUrl = "/administrador/main";
                    break;
                }

                if (role.equals("ROLE_SUPERVISOR")) {
                    redirectUrl = "/supervisor/main";
                    break;
                }

                if (role.equals("ROLE_SECRETARIO")) {
                    redirectUrl = "/secretario/main";
                    break;
                }

                if (role.equals("ROLE_RESPONSAVEL")) {
                    redirectUrl = "/responsavel/main";
                    break;
                }

                if (role.equals("ROLE_ALUNO")) {
                    redirectUrl = "/aluno/main";
                    break;
                }
            }

            response.sendRedirect(redirectUrl);
        };
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}