package br.ufscar.pescd.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ConcluirRelatorioFormDTO {

    @NotNull
    private Long inscricaoID;

    @NotBlank(message = "{Validacao.parecer}")
    private String parecer;

    @NotNull(message = "{Validacao.frequencia}")
    @Min(0) @Max(100)
    private Integer frequencia;

    @NotBlank(message = "{Validacao.nota}")
    private String nota;

    // Getters e Setters
    public Long getInscricaoID() { return inscricaoID; }
    public void setInscricaoID(Long inscricaoID) { this.inscricaoID = inscricaoID; }

    public String getParecer() { return parecer; }
    public void setParecer(String parecer) { this.parecer = parecer; }

    public Integer getFrequencia() { return frequencia; }
    public void setFrequencia(Integer frequencia) { this.frequencia = frequencia; }

    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }
}