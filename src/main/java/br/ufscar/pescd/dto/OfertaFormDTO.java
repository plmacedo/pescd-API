package br.ufscar.pescd.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class OfertaFormDTO {


    private String nome;

    @NotBlank(message = "O semestre não pode estar em branco")
    private String semestre;

    @NotNull(message = "A data de inicio  não pode estar em branco")
    private LocalDate inicio;

    @NotNull(message = "A data de fim não pode estar em branco")
    private LocalDate fim;

    @NotNull(message = "O Professor responsável não pode estar em branco")
    private long professorResponsavelId;



    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getSemestre() { return semestre; }
    public void setSemestre(String semestre) { this.semestre = semestre; }

    public LocalDate getInicio() { return inicio; }
    public void setInicio(LocalDate inicio) { this.inicio = inicio; }

    public LocalDate getFim() { return fim; }
    public void setFim(LocalDate fim) { this.fim = fim; }

    public long getProfessorResponsavelId() { return professorResponsavelId; }
    public void setProfessorResponsavelId(long professorResponsavelId) { this.professorResponsavelId = professorResponsavelId; }
}


