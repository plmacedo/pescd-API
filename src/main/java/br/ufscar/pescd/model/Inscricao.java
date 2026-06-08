package br.ufscar.pescd.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import org.springframework.web.multipart.MultipartFile;

@Entity
@Table(name = "inscricao")
public class Inscricao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "aluno_id", nullable = false)
    private Usuario aluno;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "oferta_id", nullable = false)
    private Oferta oferta;

    @Column(columnDefinition = "TEXT")
    private String PlanoDeTrabalho;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusPlano statusPlano = StatusPlano.PENDENTE;

    @Column(name = "codigo_disciplina")
    private String codigoDisciplina;

    @Column(name = "nome_disciplina")
    private String nomeDisciplina;

    @Column(name = "curso_disciplina")
    private String cursoDisciplina;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private Usuario supervisor;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] arquivoPlano;


    // Construtor vazio (obrigatório para o Spring/Hibernate funcionar)
    public Inscricao() {}


    public Inscricao(Usuario aluno, Oferta oferta) {
        this.aluno = aluno;
        this.oferta = oferta;
    }


    public Long getId() {
        return id;
    }

    public Usuario getAluno() {
        return aluno;
    }

    public Oferta getOferta() {
        return oferta;
    }

    public String getPlanoDeTrabalho() { return PlanoDeTrabalho; }

    public StatusPlano getStatusPlano() {return statusPlano; }

    public void setAluno(Usuario aluno) {
        this.aluno = aluno;
    }

    public void setOferta(Oferta oferta) {this.oferta = oferta; }

    public void setPlanoDeTrabalho(String PlanoDeTrabalho) {this.PlanoDeTrabalho = PlanoDeTrabalho; }

    public void setStatusPlano(StatusPlano statusPlano) {this.statusPlano = statusPlano; }

    public String getCodigoDisciplina() { return codigoDisciplina; }
    public void setCodigoDisciplina(String codigoDisciplina) { this.codigoDisciplina = codigoDisciplina; }

    public String getNomeDisciplina() { return nomeDisciplina; }
    public void setNomeDisciplina(String nomeDisciplina) { this.nomeDisciplina = nomeDisciplina; }

    public String getCursoDisciplina() { return cursoDisciplina; }
    public void setCursoDisciplina(String cursoDisciplina) { this.cursoDisciplina = cursoDisciplina; }

    public Usuario getSupervisor() { return supervisor; }
    public void setSupervisor(Usuario supervisor) { this.supervisor = supervisor; }

    public byte[] getArquivoPlano() { return arquivoPlano; }
    public void setArquivoPlano(byte[] arquivoPlano) { this.arquivoPlano = arquivoPlano; }
}