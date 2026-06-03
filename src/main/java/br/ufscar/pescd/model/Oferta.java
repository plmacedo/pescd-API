package br.ufscar.pescd.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "oferta")
public class Oferta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime criacao;

    @Column(nullable = false)
    private LocalDate inicio;

    @Column(nullable = false)
    private LocalDate fim;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String semestre;


    //AJEITAR O NULLABLE
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "criador_id", nullable = true)
    private Usuario criador;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "professor_id", nullable = false)
    private Usuario prof;

    @Column(nullable = false)
    private int matriculados;

    @Column(nullable = false)
    private String status;

    @Column(nullable = true)
    private LocalDateTime encerramento;

    @Column(nullable = true)
    private String encerradoPor;

    public Oferta() {}

    public Oferta(Long id,
                  LocalDate inicio,
                  LocalDate fim,
                  String nome,
                  String semestre,
                  Usuario prof,
                  int matriculados) {

        this.id = id;
        this.inicio = inicio;
        this.fim = fim;
        this.nome = nome;
        this.semestre = semestre;
        this.prof = prof;
        this.matriculados = matriculados;

        // quem criou e quando criou
        this.criacao = LocalDateTime.now();

        // status inicial
        if (LocalDate.now().isAfter(fim)) {
            this.status = "Aguardando encerramento do secretario";
        } else {
            this.status = "Em andamento";
        }
    }

    // GETTERS
    public Long getId() {
        return id;
    }

    public LocalDateTime getCriacao() {
        return criacao;
    }

    public LocalDate getInicio() {
        return inicio;
    }

    public LocalDate getFim() {
        return fim;
    }

    public String getNome() {
        return nome;
    }

    public String getSemestre() {
        return semestre;
    }

    public Usuario getCriador() {
        return criador;
    }

    public Usuario getProf() {
        return prof;
    }

    public int getMatriculados() {
        return matriculados;
    }

    public String getStatus() {
        return status;
    }

    public LocalDateTime getEncerramento() {
        return encerramento;
    }

    public String getEncerradoPor() {
        return encerradoPor;
    }

    // SETTERS
    public void setStatus(String status) {
        this.status = status;
    }

    public void setEncerramento(LocalDateTime encerramento) {
        this.encerramento = encerramento;
    }

    public void setEncerradoPor(String encerradoPor) {
        this.encerradoPor = encerradoPor;
    }
}