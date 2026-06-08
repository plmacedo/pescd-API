package br.ufscar.pescd.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class AnalisarDocumentacaoFormDTO {

    @NotBlank(message = "{responsavel.parecer.obrigatorio}")
    private String parecer;

    @NotNull(message = "{responsavel.frequencia.obrigatoria}")
    @Min(value = 0, message = "{responsavel.frequencia.min}")
    @Max(value = 100, message = "{responsavel.frequencia.max}")
    private Integer frequencia;

    @NotBlank(message = "{responsavel.nota.obrigatoria}")
    @Pattern(regexp = "^[A-E]$", message = "{responsavel.nota.invalida}")
    private String nota;

    public String getParecer() { return parecer; }
    public void setParecer(String parecer) { this.parecer = parecer; }

    public Integer getFrequencia() { return frequencia; }
    public void setFrequencia(Integer frequencia) { this.frequencia = frequencia; }

    public String getNota() { return nota; }
    public void setNota(String nota) { this.nota = nota; }
}