package br.ufscar.pescd.dto;
import br.ufscar.pescd.model.Inscricao;

public class InscricaoResponseDTO {
    private Long id;
    private String statusPlano;
    private OfertaResponseDTO oferta;

    public InscricaoResponseDTO(Inscricao inscricao) {
        this.id = inscricao.getId();
        this.statusPlano = inscricao.getStatusPlano().name();
        // Reaproveita o DTO que você já tem para evitar o loop
        if (inscricao.getOferta() != null) {
            this.oferta = new OfertaResponseDTO(inscricao.getOferta());
        }
    }
    // Getters
    public Long getId() { return id; }
    public String getStatusPlano() { return statusPlano; }
    public OfertaResponseDTO getOferta() { return oferta; }
}