package br.ufscar.pescd.controllers;

import br.ufscar.pescd.dto.AddAlunoFormDTO;
import br.ufscar.pescd.dto.PlanoTrabalhoFormDTO;
import br.ufscar.pescd.model.Inscricao;
import br.ufscar.pescd.model.Oferta;
import br.ufscar.pescd.model.Usuario;
import br.ufscar.pescd.services.InscricaoService;
import br.ufscar.pescd.services.OfertaService;
import br.ufscar.pescd.services.UsuarioService;
import br.ufscar.pescd.dto.DocumentacaoFormDTO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/aluno")
public class AlunoController {

    @Autowired
    private UsuarioService usuarioService;
    @Autowired
    private InscricaoService inscricaoService;

    // retorna todas as inscricoes de um aluno
    @GetMapping("/main")
    public String main(Model model) {

        // Pega o usuário logado atual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario aluno = usuarioService.buscarPorUsername(auth.getName());

        // ALTERAÇÃO AQUI: Buscamos as inscrições completas em vez de apenas a lista de Ofertas
        List<Inscricao> inscricoes = inscricaoService.buscarInscricoesPorAluno(aluno.getId());

        // Enviamos as "inscricoes" para o Thymeleaf com o nome de atributo "inscricoes"
        model.addAttribute("inscricoes", inscricoes);

        return "aluno/main";
    }

    @GetMapping("/enviarPlano/{idInscricao}")
    public String exibirFormularioPlano(@PathVariable Long idInscricao, Model model) {
        Inscricao inscricao = inscricaoService.buscarPorID(idInscricao);

        PlanoTrabalhoFormDTO dto = new PlanoTrabalhoFormDTO();
        dto.setInscricaoID(inscricao.getId());
        dto.setPlanoDeTrabalho(inscricao.getPlanoDeTrabalho());

        model.addAttribute("inscricao", inscricao);
        model.addAttribute("planoDTO", dto);

        return "aluno/enviar_plano";
    }

    @PostMapping("/enviarPlano")
    public String processarEnvioPlano(@Valid @ModelAttribute("planoDTO") PlanoTrabalhoFormDTO planoDTO, BindingResult result, Model model) {

        if (result.hasErrors()) {
            Inscricao inscricao = inscricaoService.buscarPorID(planoDTO.getInscricaoID());
            model.addAttribute("inscricao", inscricao);
            return "aluno/enviar_plano"; // volta pra tela se tiver erro
        }

        inscricaoService.enviarPlanoTrabalho(planoDTO.getInscricaoID(), planoDTO.getPlanoDeTrabalho());

        // redireciona para a lista com um aviso de sucesso na URL
        return "redirect:/aluno/main?sucesso";
    }

    @GetMapping("/enviarDocumentacao/{idInscricao}")
    public String exibirFormularioDocumentacao(@PathVariable Long idInscricao, Model model) {
        Inscricao inscricao = inscricaoService.buscarPorID(idInscricao);

        if(inscricao.getStatusPlano() != br.ufscar.pescd.model.StatusPlano.PENDENTE) {
            return "redirect:/aluno/main?erroStatus";
        }

        DocumentacaoFormDTO dto = new DocumentacaoFormDTO();
        dto.setInscricaoID(inscricao.getId());

        model.addAttribute("inscricao", inscricao);
        model.addAttribute("documentacaoDTO", dto);

        return "aluno/enviarDocumentacao";
    }

    @PostMapping("/enviarDocumentacao")
    public String processarEnvioDocumentacao(@Valid @ModelAttribute("documentacaoDTO") DocumentacaoFormDTO dto, BindingResult result, Model model) {
        Inscricao inscricao = inscricaoService.buscarPorID(dto.getInscricaoID());

        // RN-3 pdf ou não
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
            return "aluno/enviarDocumentacao"; // Retorna para a tela exibindo os erros
        }

        try {
            inscricaoService.enviarDocumentacao(dto.getInscricaoID(), dto);
        } catch (IOException e) {
            model.addAttribute("erro", "Erro inesperado ao salvar o arquivo.");
            model.addAttribute("inscricao", inscricao);
            return "aluno/enviarDocumentacao";
        }

        return "redirect:/aluno/main?sucessoDocumentacao";
    }
}