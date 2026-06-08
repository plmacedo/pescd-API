package br.ufscar.pescd.dto;
import org.springframework.web.multipart.MultipartFile;

public class RelatorioFinalFormDTO {
    private Long inscricaoID;
    private MultipartFile arquivo;

    public Long getInscricaoID() { return inscricaoID; }
    public void setInscricaoID(Long inscricaoID) { this.inscricaoID = inscricaoID; }
    public MultipartFile getArquivo() { return arquivo; }
    public void setArquivo(MultipartFile arquivo) { this.arquivo = arquivo; }
}
