package br.ufscar.pescd.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "oferta")
public class Oferta{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = false)
    private LocalDateTime criacao;

    @Column(nullable = false, unique = false)
    private LocalDate inicio;

    @Column(nullable = false, unique = false)
    private LocalDate fim;

    @Column(nullable = false, unique = false)
    private String nome;

    @Column(nullable = false, unique = false)
    private String semestre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criador_id", nullable = false)
    private Usuario criador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Usuario prof;

    @Column(nullable = false, unique = false)
    private int matriculados;

    public Oferta(){}

    public Oferta(Long id, LocalDate inicio, LocalDate fim, String nome, String semestre, Usuario prof, int matriculados){
        this.id = id;
        this.inicio = inicio;
        this.fim = fim;
        this.nome = nome;
        this.semestre = semestre;
        this.prof = prof;
        this.matriculados = matriculados;
        this.criacao = LocalDateTime.now();
    }

    public Long getId(){
        return id;
    }

    public String getNome(){
        return nome;
    }

    public LocalDateTime getCriacao(){
        return criacao;
    }

    public LocalDate getInicio(){
        return inicio;
    }
    public LocalDate getFim(){
        return fim;
    }
    public String getSemestre(){
        return semestre;
    }
    public Usuario getProf(){
        return prof;
    }
    public Usuario getCriador(){
        return criador;
    }

    public int getMatriculados(){
        return matriculados;
    }

}

