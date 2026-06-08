package br.ufscar.pescd.dto;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

public class RelatorioFinalFormDTO {
    private Long inscricaoID;
    private MultipartFile arquivo;

    @NotNull(message = "{aluno.enviarRelatorio.erroFrequencia}")
    @Min(0) @Max(100)
    private Integer frequencia;

    public Long getInscricaoID() { return inscricaoID; }
    public void setInscricaoID(Long inscricaoID) { this.inscricaoID = inscricaoID; }
    public MultipartFile getArquivo() { return arquivo; }
    public void setArquivo(MultipartFile arquivo) { this.arquivo = arquivo; }

    public Integer getFrequencia() { return frequencia; }

    public void setFrequencia(Integer frequencia) { this.frequencia = frequencia; }
}
