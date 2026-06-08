package br.ufscar.pescd.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;

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

    @Column(name = "instituicao_ministrou")
    private String instituicaoMinistrou;

    @Column(name = "nome_disciplina_ministrada")
    private String nomeDisciplinaMinistrada;

    @Column(name = "curso_disciplina_ministrada")
    private String cursoDisciplinaMinistrada;

    @Column(name = "carga_horaria_disciplina")
    private Integer cargaHorariaDisciplina;

    @Lob // armazenar arquivos binários grandes no banco de dados
    @Column(name = "arquivo_documentacao", length = 5242880) // máx de 5MB
    private byte[] arquivoDocumentacao;


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

    public void setOferta(Oferta oferta) {
        this.oferta = oferta;
    }

    public void setPlanoDeTrabalho(String PlanoDeTrabalho) {this.PlanoDeTrabalho = PlanoDeTrabalho; }

    public void setStatusPlano(StatusPlano statusPlano) {this.statusPlano = statusPlano; }


    public String getInstituicaoMinistrou() {
        return instituicaoMinistrou;
    }

    public String getNomeDisciplinaMinistrada() {
        return nomeDisciplinaMinistrada;
    }

    public String getCursoDisciplinaMinistrada() {
        return cursoDisciplinaMinistrada;
    }

    public Integer getCargaHorariaDisciplina() {
        return cargaHorariaDisciplina;
    }

    public byte[] getArquivoDocumentacao() {
        return arquivoDocumentacao;
    }

    public void setInstituicaoMinistrou(String instituicaoMinistrou) {
        this.instituicaoMinistrou = instituicaoMinistrou;
    }

    public void setNomeDisciplinaMinistrada(String nomeDisciplinaMinistrada) {
        this.nomeDisciplinaMinistrada = nomeDisciplinaMinistrada;
    }

    public void setCursoDisciplinaMinistrada(String cursoDisciplinaMinistrada) {
        this.cursoDisciplinaMinistrada = cursoDisciplinaMinistrada;
    }

    public void setCargaHorariaDisciplina(Integer cargaHorariaDisciplina) {
        this.cargaHorariaDisciplina = cargaHorariaDisciplina;
    }

    public void setArquivoDocumentacao(byte[] arquivoDocumentacao) {
        this.arquivoDocumentacao = arquivoDocumentacao;
    }
}