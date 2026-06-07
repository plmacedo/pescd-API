package br.ufscar.pescd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class PlanoTrabalhoFormDTO {

    @NotNull
    private Long inscricaoID;

    @NotBlank(message = "Validacao.planoDeTrabalho.vazio")
    private String PlanoDeTrabalho;


    //getters e setters
    public Long getInscricaoID() {
        return inscricaoID;
    }

    public void setInscricaoID(Long inscricaoID) {
        this.inscricaoID = inscricaoID;
    }

    public String getPlanoDeTrabalho() {
        return PlanoDeTrabalho;
    }

    public void setPlanoDeTrabalho(String PlanoDeTrabalho) {
        this.PlanoDeTrabalho = PlanoDeTrabalho;
    }
}