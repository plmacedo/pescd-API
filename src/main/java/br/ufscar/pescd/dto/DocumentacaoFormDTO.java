package br.ufscar.pescd.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.web.multipart.MultipartFile;

public class DocumentacaoFormDTO {

    private Long inscricaoID;

    @NotBlank(message = "O nome da instituição é obrigatório")
    private String instituicao;

    @NotBlank(message = "O nome da disciplina é obrigatório")
    private String nomeDisciplina;

    @NotBlank(message = "O curso da disciplina é obrigatório")
    private String cursoDisciplina;

    @NotNull(message = "A carga horária é obrigatória")
    @Positive(message = "A carga horária deve ser maior que zero")
    private Integer cargaHoraria;

    // representa o arquivo PDF que será anexado
    private MultipartFile arquivo;
    // MultipartFile = abstração do Spring pra lidar com o upload de arquivos por forms

    // RN-1
    public Long getInscricaoID() { return inscricaoID; }
    public void setInscricaoID(Long inscricaoID) { this.inscricaoID = inscricaoID; }

    public String getInstituicao() { return instituicao; }
    public void setInstituicao(String instituicao) { this.instituicao = instituicao; }

    public String getNomeDisciplina() { return nomeDisciplina; }
    public void setNomeDisciplina(String nomeDisciplina) { this.nomeDisciplina = nomeDisciplina; }

    public String getCursoDisciplina() { return cursoDisciplina; }
    public void setCursoDisciplina(String cursoDisciplina) { this.cursoDisciplina = cursoDisciplina; }

    public Integer getCargaHoraria() { return cargaHoraria; }
    public void setCargaHoraria(Integer cargaHoraria) { this.cargaHoraria = cargaHoraria; }

    public MultipartFile getArquivo() { return arquivo; }
    public void setArquivo(MultipartFile arquivo) { this.arquivo = arquivo; }
}