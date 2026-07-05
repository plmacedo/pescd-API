package br.ufscar.pescd.security;

import br.ufscar.pescd.repositories.UsuarioRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component // chamado em toda nova request
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UsuarioRepository usuarioRepository;

    public SecurityFilter(TokenService tokenService, UsuarioRepository usuarioRepository) {
        this.tokenService = tokenService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String token = recuperarToken(request);

        if (token != null) {
            String username = tokenService.validarToken(token);

            if (username != null) {// se validacao teve exito
                // Busca o usuário do banco
                UserDetails usuario = usuarioRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("Usuário não encontrado no banco de dados."));;

                // Libera o acesso
                var authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }

        // Passa a requisição para o próximo filtro da esteira
        filterChain.doFilter(request, response);
    }

    private String recuperarToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return null;
        }
        return authHeader.replace("Bearer ", ""); // Remove a palavra "Bearer " e deixa só o hash
    }
}