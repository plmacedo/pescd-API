package br.ufscar.pescd.controllers;

import br.ufscar.pescd.dto.*;
import br.ufscar.pescd.model.FraseConfirmacao;
import br.ufscar.pescd.model.Inscricao;
import br.ufscar.pescd.model.Oferta;
import br.ufscar.pescd.model.StatusPlano;
import br.ufscar.pescd.repositories.FraseRepository;
import br.ufscar.pescd.repositories.InscricaoRepository;
import br.ufscar.pescd.services.InscricaoService;
import br.ufscar.pescd.services.OfertaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/responsavel")
public class ResponsavelController {

    private final OfertaService ofertaService;
    private final InscricaoRepository inscricaoRepository;
    private final InscricaoService inscricaoService;
    private final FraseRepository fraseRepository;

    public ResponsavelController(OfertaService ofertaService,
                                 InscricaoRepository inscricaoRepository,
                                 InscricaoService inscricaoService,
                                 FraseRepository fraseRepository) {
        this.ofertaService = ofertaService;
        this.inscricaoRepository = inscricaoRepository;
        this.inscricaoService = inscricaoService;
        this.fraseRepository = fraseRepository;
    }

    @GetMapping("/main")
    public ResponseEntity<Map<String, Object>> main() {

        Map<String, Object> response = new HashMap<>();

        // Busca as ofertas
        List<Oferta> ofertas = ofertaService.listarPorFimMaisRecente();

        // Converte para DTO
        List<OfertaResponseDTO> ofertasDTO = ofertas.stream()
                .map(OfertaResponseDTO::new)
                .toList();

        response.put("ofertas", ofertasDTO);

        FraseConfirmacao mensagem = fraseRepository.findAll()
                .stream()
                .findFirst()
                .orElseThrow();

        response.put("mensagem", mensagem.getMensagem());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/finalizar/{id}")
    public ResponseEntity<?> finalizarOferta(@PathVariable Long id) {

        Oferta oferta = ofertaService.buscarPorId(id);

        List<Inscricao> inscricoes =
                inscricaoRepository.findByOfertaId(id);

        boolean podeFinalizar = inscricoes.stream()
                .allMatch(i -> i.getStatusPlano() == StatusPlano.CONCLUIDO_PELO_RESPONSAVEL);

        if (!podeFinalizar) {
            return ResponseEntity.badRequest()
                    .body("Não é possível finalizar: existem alunos não concluídos pelo responsável.");
        }

        oferta.setStatus("Aguardando encerramento do secretario");

        ofertaService.salvar(oferta);

        return ResponseEntity.ok("Oferta finalizada com sucesso.");
    }

    @GetMapping("/oferta/{id}/detalhes")
    public ResponseEntity<OfertaDetalhesDTO> detalhesOferta(@PathVariable Long id) {

        Oferta oferta = ofertaService.buscarPorId(id);

        return ResponseEntity.ok(new OfertaDetalhesDTO(oferta));
    }

    @GetMapping("/inscricao/{id}/detalhes")
    public ResponseEntity<InscricaoDetalhesDTO> detalhesAluno(@PathVariable Long id) {

        Inscricao inscricao = inscricaoService.buscarPorID(id);

        return ResponseEntity.ok(new InscricaoDetalhesDTO(inscricao));
    }
    @GetMapping("/concluir-relatorio/{id}")
    public ResponseEntity<?> exibirFormularioConclusao(@PathVariable Long id) {

        Inscricao inscricao = inscricaoService.buscarPorID(id);

        ConcluirRelatorioFormDTO form = new ConcluirRelatorioFormDTO();
        form.setInscricaoID(inscricao.getId());
        form.setFrequencia(inscricao.getFrequencia());
        form.setNota(inscricao.getNotaFinal());
        form.setParecer(inscricao.getParecerResponsavel());

        Map<String, Object> response = new HashMap<>();
        response.put("inscricao", new InscricaoDetalhesDTO(inscricao));
        response.put("formulario", form);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/concluir-relatorio")
    public ResponseEntity<?> processarConclusao(
            @Valid @RequestBody ConcluirRelatorioFormDTO form,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldErrors()
                            .stream()
                            .map(e -> e.getField() + ": " + e.getDefaultMessage())
                            .toList());
        }

        Inscricao inscricao = inscricaoService.buscarPorID(form.getInscricaoID());

        if (inscricao.getStatusPlano() != StatusPlano.RELATORIO_APROVADO_SUPERVISOR) {
            return ResponseEntity.badRequest()
                    .body("Relatório ainda não foi aprovado pelo supervisor.");
        }

        inscricaoService.concluirRelatorioResponsavel(form);

        return ResponseEntity.ok("Relatório concluído com sucesso.");
    }

    @GetMapping("/analisarDocumentacao/{id}")
    public ResponseEntity<?> exibirFormularioAnaliseDocumentacao(@PathVariable Long id) {

        Inscricao inscricao = inscricaoService.buscarPorID(id);

        if (inscricao.getStatusPlano() != StatusPlano.DOCUMENTACAO_ENVIADA) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A documentação ainda não pode ser analisada.");
        }

        Map<String, Object> response = new HashMap<>();
        response.put("inscricao", inscricao);
        response.put("formulario", new AnalisarDocumentacaoFormDTO());

        return ResponseEntity.ok(response);
    }

    @PostMapping("/analisarDocumentacao/{id}")
    public ResponseEntity<?> processarAnaliseDocumentacao(
            @PathVariable Long id,
            @Valid @RequestBody AnalisarDocumentacaoFormDTO dto) {

        Inscricao inscricao = inscricaoService.buscarPorID(id);

        if (inscricao.getStatusPlano() != StatusPlano.DOCUMENTACAO_ENVIADA) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("A documentação não está disponível para análise.");
        }

        inscricaoService.analisarDocumentacao(id, dto);

        return ResponseEntity.ok("Documentação analisada com sucesso.");
    }
}