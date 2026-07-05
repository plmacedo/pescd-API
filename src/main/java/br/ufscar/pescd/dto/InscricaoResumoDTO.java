package br.ufscar.pescd.dto;

import br.ufscar.pescd.model.Inscricao;
import br.ufscar.pescd.model.StatusPlano;

public class InscricaoResumoDTO {

    private Long idInscricao;
    private Long idAluno;
    private String nomeAluno;
    private String statusPlano;

    public InscricaoResumoDTO() {
    }

    public InscricaoResumoDTO(Inscricao inscricao) {
        this.idInscricao = inscricao.getId();

        if (inscricao.getAluno() != null) {
            this.idAluno = inscricao.getAluno().getId();
            this.nomeAluno = inscricao.getAluno().getNome();
        }

        // Passamos o Enum diretamente para a nossa função tradutora
        this.statusPlano = formatarStatus(inscricao.getStatusPlano());
    }

    // Função que converte o Enum para as strings exigidas nos requisitos
    private String formatarStatus(StatusPlano statusEnum) {
        if (statusEnum == null) {
            return "não enviado";
        }

        return switch (statusEnum) {
            case PENDENTE -> "não enviado";
            case ENVIADO -> "plano enviado";
            case APROVADO -> "plano aprovado";
            case REJEITADO -> "plano rejeitado"; // Suposição ajustável
            case DOCUMENTACAO_ENVIADA -> "documentação enviada";
            case RELATORIO_ENVIADO -> "relatório enviado";
            case RELATORIO_APROVADO_SUPERVISOR -> "relatório aprovado pelo supervisor";
            case CONCLUIDO_PELO_RESPONSAVEL -> "concluído pelo responsável";
        };
    }

    // Getters
    public Long getIdInscricao() { return idInscricao; }
    public Long getIdAluno() { return idAluno; }
    public String getNomeAluno() { return nomeAluno; }
    public String getStatusPlano() { return statusPlano; }

    // Setters
    public void setIdInscricao(Long idInscricao) { this.idInscricao = idInscricao; }
    public void setIdAluno(Long idAluno) { this.idAluno = idAluno; }
    public void setNomeAluno(String nomeAluno) { this.nomeAluno = nomeAluno; }
    public void setStatusPlano(String statusPlano) { this.statusPlano = statusPlano; }
}