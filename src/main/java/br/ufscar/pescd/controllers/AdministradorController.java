package br.ufscar.pescd.controllers;

import br.ufscar.pescd.dto.OfertaResponseDTO;
import br.ufscar.pescd.dto.UsuarioResponseDTO;
import br.ufscar.pescd.model.FraseConfirmacao;
import br.ufscar.pescd.model.Usuario;
import br.ufscar.pescd.repositories.FraseRepository;
import br.ufscar.pescd.repositories.OfertaRepository;
import br.ufscar.pescd.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/administrador")
public class AdministradorController {

    @Autowired
    private FraseRepository fraseRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OfertaRepository ofertaRepository;


    @GetMapping("/usuarios")
    public ResponseEntity<List<UsuarioResponseDTO>> listarUsuarios() {
        // Agora retornamos o DTO seguro, sem a senha, mas com os cargos
        List<UsuarioResponseDTO> usuarios = usuarioService.buscarTodos()
                .stream()
                .map(UsuarioResponseDTO::new)
                .collect(Collectors.toList());
        return ResponseEntity.ok(usuarios);
    }

    @PostMapping("/usuarios")
    public ResponseEntity<?> criarUsuario(@RequestBody Usuario usuario) {
        if (usuarioService.buscarOptionalPorUsername(usuario.getUsername()).isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Map.of("erro", "Nome de usuário já existe no sistema."));
        }

        Usuario usuarioSalvo = usuarioService.salvar(usuario);

        return ResponseEntity.status(HttpStatus.CREATED).body(new UsuarioResponseDTO(usuarioSalvo));
    }

    @PutMapping("/usuarios/{id}")
    public ResponseEntity<?> atualizarUsuario(@PathVariable("id") Long id, @RequestBody Usuario usuario) {
        try {
            // O UsuarioService.atualizar já ignora a senha se ela vier vazia no JSON
            Usuario atualizado = usuarioService.atualizar(id, usuario);
            return ResponseEntity.ok(new UsuarioResponseDTO(atualizado));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Usuário não encontrado."));
        }
    }

    @DeleteMapping("/usuarios/{id}")
    public ResponseEntity<?> excluirUsuario(@PathVariable("id") Long id) {
        try {
            usuarioService.excluir(id);
            return ResponseEntity.ok(Map.of("mensagem", "Usuário excluído com sucesso."));
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of("erro", "Usuário possui vínculos com outras entidades e não pode ser excluído."));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("erro", "Usuário não encontrado."));
        }
    }

    @GetMapping("/mensagem")
    public ResponseEntity<Map<String, String>> obterMensagem() {
        FraseConfirmacao mensagem = fraseRepository.findById(1L).orElse(new FraseConfirmacao());
        return ResponseEntity.ok(Map.of("mensagem", mensagem.getMensagem() != null ? mensagem.getMensagem() : ""));
    }

    @PutMapping("/mensagem")
    public ResponseEntity<?> alterarMensagem(@RequestBody Map<String, String> payload) {
        String texto = payload.get("texto");
        if (texto == null || texto.isBlank()) {
            return ResponseEntity.badRequest().body(Map.of("erro", "O texto da mensagem não pode estar vazio."));
        }

        FraseConfirmacao mensagem = fraseRepository.findById(1L).orElseThrow();
        mensagem.setMensagem(texto);
        fraseRepository.save(mensagem);

        return ResponseEntity.ok(Map.of("mensagem", "Mensagem atualizada com sucesso."));
    }

    @GetMapping("/ofertas/buscar")
    public ResponseEntity<List<OfertaResponseDTO>> buscarOfertaPorNome(@RequestParam String nome) {
        List<OfertaResponseDTO> ofertasEncontradas = ofertaRepository.findByNome(nome)
                .stream()
                .map(OfertaResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ofertasEncontradas);
    }
}