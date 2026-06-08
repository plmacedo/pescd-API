package br.ufscar.pescd.controllers;

import br.ufscar.pescd.dto.AddAlunoFormDTO;
import br.ufscar.pescd.dto.OfertaFormDTO;
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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.multipart.MultipartFile;import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping("/secretario")
public class SecretarioController {

    @Autowired
    private OfertaService ofertaService;
    @Autowired
    private FraseRepository fraseRepository;
    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private InscricaoService inscricaoService;

    @Autowired
    private InscricaoRepository inscricaoRepository;

    @GetMapping("/main")
    public String main(Model model) {

        model.addAttribute(
                "ofertas",
                ofertaService.listarPorFimMaisRecente()
        );

        FraseConfirmacao mensagem =
                fraseRepository
                        .findAll()
                        .stream()
                        .findFirst()
                        .orElseThrow();

        model.addAttribute(
                "mensagem",
                mensagem.getMensagem()
        );

        return "secretario/main";
    }

    @PostMapping("/encerrar/{id}")
    public String encerrarOferta(@PathVariable Long id) {

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

        return "redirect:/secretario/main";
    }


    @GetMapping("/criar_oferta")
    public String criarOferta(Model model) {

        model.addAttribute(
                "professores",
                usuarioService.filtrarPorCargo("ROLE_RESPONSAVEL")
        );



        model.addAttribute(
                "ofertaFormDTO",
                new OfertaFormDTO());
        return "secretario/criar_oferta";
    }

    //@Valid aplica restricoes do DTO nos campos recebidos
    @PostMapping("/criar_oferta")
    public String salvarNovaOferta(
            @Valid @ModelAttribute("ofertaFormDTO")OfertaFormDTO dto,
            BindingResult result,
            Model model){

        //verifica se inicio e fim estao ok
        if(dto.getInicio() != null && dto.getFim() != null){
            if(dto.getInicio().isAfter(dto.getFim())){
                // anota erro no binding result
                result.rejectValue("fim", "error.ofertaFormDTO",
                        "A data de fim não pode ser anterior à data de início.");
            }
        }

        //Se houver erros volta para mesma tela
        if(result.hasErrors()){
            model.addAttribute("professores",
                    usuarioService.filtrarPorCargo("ROLE_RESPONSAVEL"));
                return "secretario/criar_oferta";
        }

        //se tudo ok:
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String usernameSecretario = auth.getName();

        ofertaService.salvarOferta(dto, usernameSecretario);
        return "redirect:/secretario/main";


    }


    @GetMapping("/oferta/{id}/alunos")
    public String adicionarAlunos(@PathVariable Long id, Model model){
        Oferta oferta = ofertaService.buscarPorId(id);
        model.addAttribute("oferta", oferta);

        model.addAttribute("alunos", usuarioService.filtrarPorCargo("ROLE_ALUNO") );
        model.addAttribute("addAlunoFormDTO", new AddAlunoFormDTO());
        model.addAttribute("inscricoes", inscricaoRepository.findByOfertaId(id));
        return "secretario/add_aluno";
    }


    @PostMapping("/oferta/{id}/add_aluno_lista")
    public String processarAlunoBD(@PathVariable Long id,
                                   @Valid @ModelAttribute("addAlunoFormDTO") AddAlunoFormDTO dto,
                                   BindingResult result,
                                   Model model){
        if(result.hasErrors()){
            model.addAttribute("oferta", ofertaService.buscarPorId(id));
            model.addAttribute("alunos", usuarioService.filtrarPorCargo("ROLE_ALUNO"));
            model.addAttribute("inscricoes", inscricaoRepository.findByOfertaId(id));
            return "secretario/add_aluno";
        }


        inscricaoService.inscreverAluno(id, dto.getAlunoId());

        return "redirect:/secretario/oferta/" + id + "/alunos?sucesso=true";
    }


    @PostMapping("/oferta/{id}/alunos/upload")
    public String processarUploadAlunos(@PathVariable Long id,
                                        @RequestParam("file") MultipartFile file,
                                        Model model){
        if (file.isEmpty()) {
            return "redirect:/secretario/oferta/" + id + "/alunos?erro=arquivo_vazio";
        }

        try {
             inscricaoService.processarCsvInscricoes(id, file);
             return "redirect:/secretario/oferta/" + id + "/alunos?sucesso=true";

        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/secretario/oferta/" + id + "/alunos?erro=processamento";
        }

    }

    @GetMapping("/enviarDocumentacao/{idInscricao}")
    public String exibirFormularioDocumentacao(@PathVariable Long idInscricao, Model model) {
        Inscricao inscricao = inscricaoService.buscarPorID(idInscricao);

        // A regra diz que o status do aluno nesta oferta deve ser "não enviado" (PENDENTE no sistema)
        if(inscricao.getStatusPlano() != br.ufscar.pescd.model.StatusPlano.PENDENTE) {
            return "redirect:/aluno/main?erroStatus";
        }

        DocumentacaoFormDTO dto = new DocumentacaoFormDTO();
        dto.setInscricaoID(inscricao.getId());

        model.addAttribute("inscricao", inscricao);
        model.addAttribute("documentacaoDTO", dto);

        return "aluno/enviar_documentacao";
    }

    @PostMapping("/enviarDocumentacao")
    public String processarEnvioDocumentacao(@Valid @ModelAttribute("documentacaoDTO") DocumentacaoFormDTO dto, BindingResult result, Model model) {
        Inscricao inscricao = inscricaoService.buscarPorID(dto.getInscricaoID());

        // RN-3: Validação manual se o arquivo é um PDF e se respeita o limite de 5MB
        if (dto.getArquivo() == null || dto.getArquivo().isEmpty()) {
            result.rejectValue("arquivo", "error.documentacao", "O arquivo com a documentação comprobatória é obrigatório.");
        } else {
            if (!"application/pdf".equals(dto.getArquivo().getContentType())) {
                result.rejectValue("arquivo", "error.documentacao", "O arquivo deve ser obrigatóriamente no formato PDF.");
            }
            if (dto.getArquivo().getSize() > 5242880) { // 5MB convertidos em bytes
                result.rejectValue("arquivo", "error.documentacao", "O arquivo deve ter no máximo 5MB.");
            }
        }

        if (result.hasErrors()) {
            model.addAttribute("inscricao", inscricao);
            return "aluno/enviar_documentacao"; // Retorna para a tela exibindo os erros
        }

        try {
            inscricaoService.enviarDocumentacao(dto.getInscricaoID(), dto);
        } catch (IOException e) {
            model.addAttribute("erro", "Erro inesperado ao salvar o arquivo.");
            model.addAttribute("inscricao", inscricao);
            return "aluno/enviar_documentacao";
        }

        // RN-4: Envio com sucesso
        return "redirect:/aluno/main?sucessoDocumentacao";
    }

    @GetMapping("/oferta/{id}/detalhes")
    public String detalhesOferta(@PathVariable Long id, Model model) {
        Oferta oferta = ofertaService.buscarPorId(id);
        model.addAttribute("oferta", oferta);
        model.addAttribute("inscricoes", inscricaoRepository.findByOfertaId(id));
        return "secretario/detalhes_oferta";
    }

    @GetMapping("/inscricao/{id}/detalhes")
    public String detalhesAluno(@PathVariable Long id, Model model) {
        Inscricao inscricao = inscricaoService.buscarPorID(id);
        model.addAttribute("inscricao", inscricao);
        return "secretario/detalhes_aluno";
    }

}