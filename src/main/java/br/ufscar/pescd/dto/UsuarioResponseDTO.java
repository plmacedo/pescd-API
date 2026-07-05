package br.ufscar.pescd.dto;
import br.ufscar.pescd.model.Usuario;
import java.util.List;

public class UsuarioResponseDTO {
    private Long id;
    private String nome;
    private String username;
    private List<String> cargos;

    public UsuarioResponseDTO(Usuario usuario) {
        this.id = usuario.getId();
        this.nome = usuario.getNome();
        this.username = usuario.getUsername();
        this.cargos = usuario.getCargos();
    }
    // Getters
    public Long getId() { return id; }
    public String getNome() { return nome; }
    public String getUsername() { return username; }
    public List<String> getCargos() { return cargos; }
}