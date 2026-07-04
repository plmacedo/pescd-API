package br.ufscar.pescd.controllers;

import br.ufscar.pescd.dto.AddAlunoFormDTO;
import br.ufscar.pescd.dto.OfertaFormDTO;
import br.ufscar.pescd.dto.OfertaResponseDTO;
import br.ufscar.pescd.model.FraseConfirmacao;
import br.ufscar.pescd.model.Oferta;
import br.ufscar.pescd.model.Usuario;
import br.ufscar.pescd.model.Inscricao;
import br.ufscar.pescd.repositories.FraseRepository;
import br.ufscar.pescd.repositories.InscricaoRepository;
import br.ufscar.pescd.services.InscricaoService;
import br.ufscar.pescd.services.OfertaService;
import br.ufscar.pescd.services.UsuarioService;
import br.ufscar.pescd.dto.DocumentacaoFormDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/secretario")
public class SecretarioController {

    private final OfertaService ofertaService;
    private final FraseRepository fraseRepository;
    private final UsuarioService usuarioService;
    private final InscricaoService inscricaoService;
    private final InscricaoRepository inscricaoRepository;


    public SecretarioController(OfertaService ofertaService, FraseRepository fraseRepository,
                                UsuarioService usuarioService, InscricaoService inscricaoService,
                                InscricaoRepository inscricaoRepository) {
        this.ofertaService = ofertaService;
        this.fraseRepository = fraseRepository;
        this.usuarioService = usuarioService;
        this.inscricaoService = inscricaoService;
        this.inscricaoRepository = inscricaoRepository;
    }

    @GetMapping("/main")
    public ResponseEntity<Map<String, Object>> main() {

        //objeto que retornará em Json
        Map<String, Object> response = new HashMap<>();


        List<Oferta> ofertasDoBanco = ofertaService.listarPorFimMaisRecente();

        // converte para evitar loops
        List<OfertaResponseDTO> ofertasLimpas = ofertasDoBanco.stream()
                .map(OfertaResponseDTO::new)
                .toList();


        response.put("ofertas", ofertasLimpas);

        FraseConfirmacao mensagem = fraseRepository.findAll().stream().findFirst().orElseThrow();
        response.put("mensagem", mensagem.getMensagem());

        return ResponseEntity.ok(response); // Retorna 200 OK com o JSON
    }

    @PostMapping("/encerrar/{id}")
    public ResponseEntity<String> encerrarOferta(@PathVariable Long id) {

        // pega o usuario atual
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();

        Oferta oferta = ofertaService.buscarPorId(id);

        oferta.setStatus("Concluida");

        oferta.setEncerramento(LocalDateTime.now());

        Usuario usuario =
                usuarioService.buscarPorUsername(auth.getName());

        oferta.setEncerradoPor(usuario.getNome());

        ofertaService.salvar(oferta);

        return ResponseEntity.ok("Oferta encerrada com sucesso.");
    }

    //criar_oferta deletado pois não há uma tela para carregar


    //@Valid aplica restricoes do DTO nos campos recebidos
    @PostMapping("/criar_oferta")
    public ResponseEntity<?> salvarNovaOferta(@Valid @RequestBody OfertaFormDTO dto){

        //verifica se inicio e fim estao ok
        if(dto.getInicio() != null && dto.getFim() != null){
            if(dto.getInicio().isAfter(dto.getFim())){
                // envia erro 400
                return ResponseEntity.badRequest().body("A data de fim não pode ser anterior à de ínicio.");
            }
        }



        //se tudo ok:
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usernameSecretario = auth.getName();

        ofertaService.salvarOferta(dto, usernameSecretario);
        return ResponseEntity.status(HttpStatus.CREATED).body("Oferta criada com sucesso!!");


    }



    @PostMapping("/oferta/{id}/add_aluno_lista")
    public ResponseEntity<String> processarAlunoBD(@PathVariable Long id, @Valid @RequestBody AddAlunoFormDTO dto){

        inscricaoService.inscreverAluno(id, dto.getAlunoId());
        return ResponseEntity.status(HttpStatus.CREATED).body("Aluno inscrito com sucesso.");

    }


    @PostMapping("/oferta/{id}/alunos/upload")
    public ResponseEntity<String> processarUploadAlunos(@PathVariable Long id, @RequestParam("file") MultipartFile file){
        // 1. Verifica se está vazio
        if (file.isEmpty()) {
            return ResponseEntity.badRequest().body("O arquivo enviado está vazio.");
        }


        String filename = file.getOriginalFilename();

        if (filename == null || !filename.toLowerCase().endsWith(".csv")) {
            return ResponseEntity.badRequest().body("Formato inválido. O arquivo deve ser obrigatoriamente um .csv");
        }

        // Se passou pelas travas, tenta processar...
        try {
            inscricaoService.processarCsvInscricoes(id, file);
            return ResponseEntity.ok("Upload e inscrições processadas com sucesso.");

        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro no processamento do CSV.");
        }

    }


    @PostMapping("/enviarDocumentacao")
    public ResponseEntity<?> processarEnvioDocumentacao(@Valid @ModelAttribute DocumentacaoFormDTO dto) {
        //ainda usa Model por causa que há upload de arquivos além de JSON
        Inscricao inscricao = inscricaoService.buscarPorID(dto.getInscricaoID());

        // RN-3: Validação manual se o arquivo é um PDF e se respeita o limite de 5MB
        if (dto.getArquivo() == null || dto.getArquivo().isEmpty()) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Status não permite envio de documentação.");
        } else {
            if (!"application/pdf".equals(dto.getArquivo().getContentType())) {
                return ResponseEntity.badRequest().body("O arquivo deve ser obrigatoriamente no formato PDF.");
            }
            if (dto.getArquivo().getSize() > 5242880) { // 5MB convertidos em bytes
                return ResponseEntity.badRequest().body("O arquivo deve ter no máximo 5MB.");
            }
        }


        try {
            inscricaoService.enviarDocumentacao(dto.getInscricaoID(), dto);
            return ResponseEntity.ok("Documentação enviada com sucesso.");

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Erro inesperado ao salvar o arquivo.");
        }
    }

    @GetMapping("/oferta/{id}/detalhes")
    public ResponseEntity<Map<String, Object>> detalhesOferta(@PathVariable Long id) {
        Map<String, Object> response = new HashMap<>();

        Oferta oferta = ofertaService.buscarPorId(id);
        response.put("oferta", ofertaService.buscarPorId(id));
        response.put("inscricoes", inscricaoRepository.findByOfertaId(id));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/inscricao/{id}/detalhes")
    public ResponseEntity<Inscricao> detalhesAluno(@PathVariable Long id) {
        Inscricao inscricao = inscricaoService.buscarPorID(id);
        return ResponseEntity.ok(inscricaoService.buscarPorID(id));
    }

}