package br.ufscar.pescd.dto;

import jakarta.validation.constraints.NotBlank;

public record AuthenticationDTO(
        @NotBlank(message = "O username não pode estar vazio") String username,
        @NotBlank(message = "A senha não pode estar vazia") String password
) {}