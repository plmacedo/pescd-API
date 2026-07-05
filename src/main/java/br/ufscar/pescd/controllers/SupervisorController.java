package br.ufscar.pescd.controllers;

import br.ufscar.pescd.dto.AprovarPlanoFormDTO;
import br.ufscar.pescd.dto.AprovarRelatorioFormDTO;
import br.ufscar.pescd.dto.InscricaoResumoDTO;
import br.ufscar.pescd.dto.InscricaoDetalhesDTO;
import br.ufscar.pescd.model.Inscricao;
import br.ufscar.pescd.model.StatusPlano;
import br.ufscar.pescd.model.Usuario;
import br.ufscar.pescd.services.InscricaoService;
import br.ufscar.pescd.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/supervisor")
public class SupervisorController {

    private final UsuarioService usuarioService;
    private final InscricaoService inscricaoService;

    public SupervisorController(UsuarioService usuarioService,
                                    InscricaoService inscricaoService) {
        this.usuarioService = usuarioService;
        this.inscricaoService = inscricaoService;
    }

    @GetMapping("/main")
    public ResponseEntity<?> main(Principal principal) {

        Usuario supervisor = usuarioService.buscarPorUsername(principal.getName());

        List<Inscricao> lista = inscricaoService.buscarPorSupervisor(supervisor);

        List<InscricaoResumoDTO> dtos = lista.stream()
                .map(InscricaoResumoDTO::new)
                .toList();

        Map<String, Object> response = new HashMap<>();
        response.put("inscricoes", dtos);

        return ResponseEntity.ok(response);
    }

    @GetMapping("/aprovar-plano/{id}")
    public ResponseEntity<?> exibirFormularioAprovacao(@PathVariable Long id) {

        Inscricao inscricao = inscricaoService.buscarPorID(id);

        if (inscricao.getStatusPlano() != StatusPlano.ENVIADO) {
            return ResponseEntity.badRequest()
                    .body("Status inválido para aprovação de plano.");
        }

        AprovarPlanoFormDTO form = new AprovarPlanoFormDTO();
        form.setInscricaoID(id);

        Map<String, Object> response = new HashMap<>();
        response.put("inscricao", new InscricaoDetalhesDTO(inscricao));
        response.put("formulario", form);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/aprovar-plano")
    public ResponseEntity<?> processarAprovacao(@Valid @RequestBody AprovarPlanoFormDTO form) {
        inscricaoService.aprovarPlano(form);
        return ResponseEntity.ok("Plano aprovado com sucesso.");
    }

    @GetMapping("/aprovar-relatorio/{id}")
    public ResponseEntity<?> exibirFormularioAprovacaoRelatorio(@PathVariable Long id) {

        Inscricao inscricao = inscricaoService.buscarPorID(id);

        if (inscricao.getStatusPlano() != StatusPlano.RELATORIO_ENVIADO) {
            return ResponseEntity.badRequest()
                    .body("Status inválido para aprovação de relatório.");
        }

        AprovarRelatorioFormDTO form = new AprovarRelatorioFormDTO();
        form.setInscricaoID(id);
        form.setFrequencia(inscricao.getFrequencia());

        Map<String, Object> response = new HashMap<>();
        response.put("inscricao", new InscricaoDetalhesDTO(inscricao));
        response.put("formulario", form);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/aprovar-relatorio")
    public ResponseEntity<?> processarAprovacaoRelatorio(
            @Valid @RequestBody AprovarRelatorioFormDTO form,
            BindingResult result) {

        if (result.hasErrors()) {
            return ResponseEntity.badRequest()
                    .body(result.getFieldErrors()
                            .stream()
                            .map(e -> e.getField() + ": " + e.getDefaultMessage())
                            .toList());
        }

        inscricaoService.aprovarRelatorio(form);

        return ResponseEntity.ok("Relatório aprovado com sucesso.");
    }
}