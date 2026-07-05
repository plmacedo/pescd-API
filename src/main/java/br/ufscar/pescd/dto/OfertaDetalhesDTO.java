package br.ufscar.pescd.dto;

import br.ufscar.pescd.model.Oferta;

import java.util.List;

public class OfertaDetalhesDTO {

    private Long id;
    private String nome;
    private String semestre;
    private String nomeProfessor;
    private String status;
    private List<InscricaoResumoDTO> inscricoes;

    public OfertaDetalhesDTO() {
    }

    public OfertaDetalhesDTO(Oferta oferta) {
        this.id = oferta.getId();
        this.nome = oferta.getNome();
        this.semestre = oferta.getSemestre();
        this.status = oferta.getStatus();

        if (oferta.getProf() != null) {
            this.nomeProfessor = oferta.getProf().getNome();
        }

        if (oferta.getInscricoes() != null) {
            this.inscricoes = oferta.getInscricoes()
                    .stream()
                    .map(InscricaoResumoDTO::new)
                    .toList();
        }
    }

    public Long getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public String getSemestre() {
        return semestre;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }

    public String getStatus() {
        return status;
    }

    public List<InscricaoResumoDTO> getInscricoes() {
        return inscricoes;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setInscricoes(List<InscricaoResumoDTO> inscricoes) {
        this.inscricoes = inscricoes;
    }
}