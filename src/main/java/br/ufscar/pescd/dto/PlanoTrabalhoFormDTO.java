package br.ufscar.pescd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class PlanoTrabalhoFormDTO {

    @NotNull
    private Long inscricaoID;

    @NotBlank(message = "O código da disciplina é obrigatório")
    private String codigoDisciplina;

    @NotBlank(message = "O nome da disciplina é obrigatório")
    private String nomeDisciplina;

    @NotBlank(message = "O curso da disciplina é obrigatório")
    private String cursoDisciplina;

    @NotNull(message = "O professor supervisor é obrigatório")
    private Long professorSupervisorId;

    @NotNull(message = "O arquivo do plano de trabalho é obrigatório")
    private MultipartFile arquivoPlano;

    // Getters e Setters
    public Long getInscricaoID() { return inscricaoID; }
    public void setInscricaoID(Long inscricaoID) { this.inscricaoID = inscricaoID; }

    public String getCodigoDisciplina() { return codigoDisciplina; }
    public void setCodigoDisciplina(String codigoDisciplina) { this.codigoDisciplina = codigoDisciplina; }

    public String getNomeDisciplina() { return nomeDisciplina; }
    public void setNomeDisciplina(String nomeDisciplina) { this.nomeDisciplina = nomeDisciplina; }

    public String getCursoDisciplina() { return cursoDisciplina; }
    public void setCursoDisciplina(String cursoDisciplina) { this.cursoDisciplina = cursoDisciplina; }

    public Long getProfessorSupervisorId() { return professorSupervisorId; }
    public void setProfessorSupervisorId(Long professorSupervisorId) { this.professorSupervisorId = professorSupervisorId; }

    public MultipartFile getArquivoPlano() { return arquivoPlano; }
    public void setArquivoPlano(MultipartFile arquivoPlano) { this.arquivoPlano = arquivoPlano; }
}