package br.ufscar.pescd.model;

import jakarta.persistence.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private String nome;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> cargos;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String senha;

    public Usuario(){}

    public Usuario(Long id, String nome, List<String> cargos, String username, String senha){
        this.id = id;
        this.cargos = cargos;
        this.nome = nome;
        this.username = username;
        this.senha = senha;
    }

    public Long getId(){
        return id;
    }

    public String getNome(){
        return nome;
    }

    public String getUsername(){
        return username;
    }

    public String getSenha(){
        return senha;
    }
    public List<String> getCargos(){
        return cargos;
    }

    public void setId(Long id) { this.id = id; }

    public void setSenha(String senha){
        this.senha = senha;
    }

    public void setNome(String nome){
        this.nome = nome;
    }

    public void setUsername(String username){
        this.username = username;
    }

    public void setCargos(List<String> cargos){
        this.cargos = cargos;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // O Spring pergunta: "Quais os cargos dessa pessoa?"
        // Nós pegamos a String do seu banco (ex: ROLE_SECRETARIO) e convertemos pro formato do Spring
        return this.cargos.stream()
                .map(cargoString -> new SimpleGrantedAuthority(cargoString))
                .toList();
    }

    @Override
    public String getPassword() {
        return this.senha;
    }


    // Os métodos abaixo perguntam se a conta está bloqueada, expirada, etc.
    // Como você provavelmente não tem isso no banco, retorne TRUE para todos, liberando o acesso.
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}




