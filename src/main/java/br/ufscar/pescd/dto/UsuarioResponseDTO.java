package br.ufscar.pescd.dto;
import br.ufscar.pescd.model.Usuario;

public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String username;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.username = usuario.getUsername();
    }
    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getUsername() { return username; }
}