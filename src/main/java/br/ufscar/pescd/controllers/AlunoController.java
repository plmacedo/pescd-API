package br.ufscar.pescd.controllers;

import br.ufscar.pescd.dto.*;
import br.ufscar.pescd.model.Inscricao;
import br.ufscar.pescd.model.Usuario;
import br.ufscar.pescd.model.StatusPlano;
import br.ufscar.pescd.services.InscricaoService;
import br.ufscar.pescd.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/aluno")
public class AlunoController {

    private final UsuarioService usuarioService;
    private final InscricaoService inscricaoService;

    public AlunoController(UsuarioService usuarioService, InscricaoService inscricaoService) {
        this.usuarioService = usuarioService;
        this.inscricaoService = inscricaoService;
    }

    @GetMapping("/main")
    public ResponseEntity<List<InscricaoResponseDTO>> main() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario aluno = usuarioService.buscarPorUsername(auth.getName());

        List<Inscricao> inscricoes = inscricaoService.buscarInscricoesPorAluno(aluno.getId());

        // Converte as entidades para DTOs antes de devolver na resposta
        List<InscricaoResponseDTO> inscricoesDTO = inscricoes.stream()
                .map(InscricaoResponseDTO::new)
                .toList();

        return ResponseEntity.ok(inscricoesDTO);
    }

    @GetMapping("/enviarPlano/{idInscricao}")
    public ResponseEntity<Map<String, Object>> exibirFormularioPlano(@PathVariable Long idInscricao) {
        Inscricao inscricao = inscricaoService.buscarPorID(idInscricao);

        Map<String, Object> response = new HashMap<>();

        response.put("inscricao", new InscricaoResponseDTO(inscricao));

        List<UsuarioResponseDTO> professoresDTO = usuarioService.filtrarPorCargo("ROLE_SUPERVISOR")
                .stream()
                .map(UsuarioResponseDTO::new)
                .toList();

        response.put("professores", professoresDTO);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/enviarPlano")
    public ResponseEntity<?> processarEnvioPlano(@Valid @ModelAttribute PlanoTrabalhoFormDTO planoDTO) {
        MultipartFile arquivo = planoDTO.getArquivoPlano();

        if (arquivo == null || arquivo.isEmpty() || !"application/pdf".equals(arquivo.getContentType())) {
            return ResponseEntity.badRequest().body("O arquivo deve ser um PDF válido.");
        }

        inscricaoService.enviarPlanoTrabalho(planoDTO);
        return ResponseEntity.ok("Plano de trabalho enviado com sucesso para aprovação.");
    }

    @GetMapping("/enviarDocumentacao/{idInscricao}")
    public ResponseEntity<?> exibirFormularioDocumentacao(@PathVariable Long idInscricao) {
        Inscricao inscricao = inscricaoService.buscarPorID(idInscricao);

        if (inscricao.getStatusPlano() != StatusPlano.PENDENTE) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Status inválido para envio de documentação comprobatória.");
        }

        return ResponseEntity.ok(new InscricaoResponseDTO(inscricao));
    }

    @PostMapping("/enviarDocumentacao")
    public ResponseEntity<?> processarEnvioDocumentacao(@Valid @ModelAttribute DocumentacaoFormDTO dto) {
        MultipartFile arquivo = dto.getArquivo();

        if (arquivo == null || arquivo.isEmpty()) {
            return ResponseEntity.badRequest().body("O arquivo com a documentação comprobatória é obrigatório.");
        }
        if (!"application/pdf".equals(arquivo.getContentType())) {
            return ResponseEntity.badRequest().body("O arquivo deve ser obrigatoriamente no formato PDF.");
        }
        if (arquivo.getSize() > 5242880) { // verificação do limite de 5MB
            return ResponseEntity.badRequest().body("O arquivo deve ter no máximo 5MB.");
        }

        try {
            inscricaoService.enviarDocumentacao(dto.getInscricaoID(), dto);
            return ResponseEntity.ok("Documentação comprobatória enviada com sucesso.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado ao processar o armazenamento do arquivo.");
        }
    }

    @GetMapping("/enviarRelatorio/{idInscricao}")
    public ResponseEntity<InscricaoResponseDTO> exibirFormularioRelatorio(@PathVariable Long idInscricao) {
        Inscricao inscricao = inscricaoService.buscarPorID(idInscricao);

        return ResponseEntity.ok(new InscricaoResponseDTO(inscricao));
    }
    
    @PostMapping("/enviarRelatorio")
    public ResponseEntity<?> processarEnvioRelatorio(@Valid @ModelAttribute RelatorioFinalFormDTO dto) {
        MultipartFile arquivo = dto.getArquivo();

        if (arquivo == null || arquivo.isEmpty() || !"application/pdf".equals(arquivo.getContentType())) {
            return ResponseEntity.badRequest().body("O arquivo deve ser um PDF válido e menor que 5MB.");
        }

        try {
            inscricaoService.enviarRelatorioFinal(dto.getInscricaoID(), dto);
            return ResponseEntity.ok("Relatório final enviado com sucesso.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Erro inesperado ao salvar o relatório final.");
        }
    }
}