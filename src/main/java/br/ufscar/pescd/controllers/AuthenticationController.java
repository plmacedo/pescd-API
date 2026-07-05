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
        // 1. Empacota as credenciais que vieram no JSON
        var usernamePassword = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());

        // 2. O Spring vai usar o seu AutenticacaoService para ir no banco e checar a senha
        var auth = this.authenticationManager.authenticate(usernamePassword);

        // 3. Se a senha bater, geramos o token!
        var token = tokenService.gerarToken(((Usuario) auth.getPrincipal()).getUsername());

        // 4. Devolve o token na tela
        return ResponseEntity.ok(new LoginResponseDTO(token));
    }
}