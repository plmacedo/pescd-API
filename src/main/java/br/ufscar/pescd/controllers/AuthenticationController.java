package br.ufscar.pescd.controllers;

import br.ufscar.pescd.dto.AuthenticationDTO;
import br.ufscar.pescd.dto.LoginResponseDTO;
import br.ufscar.pescd.security.TokenService;
import br.ufscar.pescd.model.Usuario;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthenticationController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;

    public AuthenticationController(AuthenticationManager authenticationManager, TokenService tokenService) {
        this.authenticationManager = authenticationManager;
        this.tokenService = tokenService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@RequestBody @Valid AuthenticationDTO dto) {
        // coleta dados do dto
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());

        //  autenticacao do usuario
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // Se a senha bater, gera o token
        var token = tokenService.gerarToken(((Usuario) auth.getPrincipal()).getUsername());

        // devolve o token na tela
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}