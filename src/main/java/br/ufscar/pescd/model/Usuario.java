package br.ufscar.pescd.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "usuario")
public class Usuario{
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

}

