package br.ufscar.pescd.dto;

import br.ufscar.pescd.model.Inscricao;

public class InscricaoDetalhesDTO {

    private Long id;

    private String nomeAluno;
    private String statusAtual;

    // Plano de Trabalho
    private String disciplina;
    private String codigo;
    private String curso;
    private String supervisor;
    private String parecerSupervisor;

    // Documentação
    private String instituicao;
    private String disciplinaMinistrada;
    private Integer cargaHoraria;

    public InscricaoDetalhesDTO() {
    }

    public InscricaoDetalhesDTO(Inscricao inscricao) {

        this.id = inscricao.getId();

        this.nomeAluno = inscricao.getAluno().getNome();
        this.statusAtual = inscricao.getStatusPlano().name();

        // Plano de Trabalho
        this.disciplina = inscricao.getNomeDisciplina();
        this.codigo = inscricao.getCodigoDisciplina();
        this.curso = inscricao.getCursoDisciplina();

        if (inscricao.getSupervisor() != null) {
            this.supervisor = inscricao.getSupervisor().getNome();
        }

        this.parecerSupervisor = inscricao.getParecerPlano();

        // Documentação
        this.instituicao = inscricao.getInstituicaoMinistrou();
        this.disciplinaMinistrada = inscricao.getNomeDisciplinaMinistrada();
        this.cargaHoraria = inscricao.getCargaHorariaDisciplina();
    }

    public Long getId() {
        return id;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public String getStatusAtual() {
        return statusAtual;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getCurso() {
        return curso;
    }

    public String getSupervisor() {
        return supervisor;
    }

    public String getParecerSupervisor() {
        return parecerSupervisor;
    }

    public String getInstituicao() {
        return instituicao;
    }

    public String getDisciplinaMinistrada() {
        return disciplinaMinistrada;
    }

    public Integer getCargaHoraria() {
        return cargaHoraria;
    }
}