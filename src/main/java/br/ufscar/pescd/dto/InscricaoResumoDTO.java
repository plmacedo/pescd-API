package br.ufscar.pescd.dto;

import br.ufscar.pescd.model.Inscricao;

public class InscricaoResumoDTO {

    private Long id;
    private String nomeAluno;
    private String statusPlano;

    public InscricaoResumoDTO() {
    }

    public InscricaoResumoDTO(Inscricao inscricao) {
        this.id = inscricao.getId();
        this.nomeAluno = inscricao.getAluno().getNome();
        this.statusPlano = inscricao.getStatusPlano().name();
    }

    public Long getId() {
        return id;
    }

    public String getNomeAluno() {
        return nomeAluno;
    }

    public String getStatusPlano() {
        return statusPlano;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNomeAluno(String nomeAluno) {
        this.nomeAluno = nomeAluno;
    }

    public void setStatusPlano(String statusPlano) {
        this.statusPlano = statusPlano;
    }
}