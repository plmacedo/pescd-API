package br.ufscar.pescd.dto;

import br.ufscar.pescd.model.Oferta;

public class OfertaResponseDTO {

    private Long id;
    private String nome;
    private String semestre;
    private String nomeProfessor;
    private String status;


    public OfertaResponseDTO() {
    }


    public OfertaResponseDTO(Oferta oferta) {
        this.id = oferta.getId();
        this.nome = oferta.getNome();
        this.semestre = oferta.getSemestre();
        this.status = oferta.getStatus();
        this.nomeProfessor = oferta.getProf().getNome();



    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSemestre() {
        return semestre;
    }

    public String getStatus() {
        return status;
    }


    public void setSemestre(String semestre) {
        this.semestre = semestre;
    }

    public String getNomeProfessor() {
        return nomeProfessor;
    }

    public void setNomeProfessor(String nomeProfessor) {
        this.nomeProfessor = nomeProfessor;
    }
}