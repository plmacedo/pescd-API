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

    @Column(name = "instituicao_ministrou")
    private String instituicaoMinistrou;

    @Column(name = "nome_disciplina_ministrada")
    private String nomeDisciplinaMinistrada;

    @Column(name = "curso_disciplina_ministrada")
    private String cursoDisciplinaMinistrada;

    @Column(name = "carga_horaria_disciplina")
    private Integer cargaHorariaDisciplina;

    @Column(columnDefinition = "TEXT", name = "parecer_plano")
    private String parecerPlano;

    @Column(name = "data_aprovacao_plano")
    private LocalDateTime dataAprovacaoPlano;

    @Column(name = "frequencia")
    private Integer frequencia;

    @Column(columnDefinition = "TEXT", name = "parecer_relatorio")
    private String parecerRelatorio;

    @Column(name = "sugestao_nota", length = 1)
    private String sugestaoNota;

    @Column(name = "data_aprovacao_relatorio")
    private LocalDateTime dataAprovacaoRelatorio;

    @Lob // armazenar arquivos binários grandes no banco de dados
    @Column(name = "arquivo_documentacao", length = 5242880) // máx de 5MB
    private byte[] arquivoDocumentacao;
    @Column(name = "codigo_disciplina")
    private String codigoDisciplina;

    @Column(name = "nome_disciplina")
    private String nomeDisciplina;

    @Column(name = "curso_disciplina")
    private String cursoDisciplina;

    @Column(columnDefinition = "TEXT")
    private String parecerResponsavel;

    private Integer frequenciaFinal;

    private String notaFinal;

    private LocalDateTime dataConclusaoResponsavel;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supervisor_id")
    private Usuario supervisor;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] arquivoPlano;

    @Lob
    @Column(name = "arquivo_relatorio_final", length = 5242880) // Limite de 5MB
    private byte[] arquivoRelatorioFinal;


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


    public String getParecerPlano() { return parecerPlano; }
    public void setParecerPlano(String parecerPlano) { this.parecerPlano = parecerPlano; }

    public LocalDateTime getDataAprovacaoPlano() { return dataAprovacaoPlano; }
    public void setDataAprovacaoPlano(LocalDateTime dataAprovacaoPlano) { this.dataAprovacaoPlano = dataAprovacaoPlano; }
    public byte[] getArquivoRelatorioFinal() { return arquivoRelatorioFinal; }
    public void setArquivoRelatorioFinal(byte[] arquivoRelatorioFinal) { this.arquivoRelatorioFinal = arquivoRelatorioFinal; }

    public Integer getFrequencia() { return frequencia; }
    public void setFrequencia(Integer frequencia) { this.frequencia = frequencia; }

    public String getParecerRelatorio() { return parecerRelatorio; }
    public void setParecerRelatorio(String parecerRelatorio) { this.parecerRelatorio = parecerRelatorio; }

    public String getSugestaoNota() { return sugestaoNota; }
    public void setSugestaoNota(String sugestaoNota) { this.sugestaoNota = sugestaoNota; }

    public LocalDateTime getDataAprovacaoRelatorio() { return dataAprovacaoRelatorio; }
    public void setDataAprovacaoRelatorio(LocalDateTime dataAprovacaoRelatorio) { this.dataAprovacaoRelatorio = dataAprovacaoRelatorio; }

    public String getParecerResponsavel() {
        return parecerResponsavel;
    }

    public void setParecerResponsavel(String parecerResponsavel) {
        this.parecerResponsavel = parecerResponsavel;
    }

    public Integer getFrequenciaFinal() {
        return frequenciaFinal;
    }

    public void setFrequenciaFinal(Integer frequenciaFinal) {
        this.frequenciaFinal = frequenciaFinal;
    }

    public String getNotaFinal() {
        return notaFinal;
    }

    public void setNotaFinal(String notaFinal) {
        this.notaFinal = notaFinal;
    }

    public LocalDateTime getDataConclusaoResponsavel() {
        return dataConclusaoResponsavel;
    }

    public void setDataConclusaoResponsavel(LocalDateTime dataConclusaoResponsavel) {
        this.dataConclusaoResponsavel = dataConclusaoResponsavel;
    }
}