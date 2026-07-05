package br.ufscar.pescd.dto;

public class MensagemDTO {
    private String texto;

    // Construtor padrão necessário para o Jackson
    public MensagemDTO() {
    }

    public MensagemDTO(String texto) {
        this.texto = texto;
    }

    // Getter e Setter
    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }
}