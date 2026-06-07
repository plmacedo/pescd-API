package br.ufscar.pescd.controllers;

import br.ufscar.pescd.dto.AddAlunoFormDTO;
import br.ufscar.pescd.dto.PlanoTrabalhoFormDTO;
import br.ufscar.pescd.model.Inscricao;
import br.ufscar.pescd.model.Oferta;
import br.ufscar.pescd.model.Usuario;
import br.ufscar.pescd.services.InscricaoService;
import br.ufscar.pescd.services.OfertaService;
import br.ufscar.pescd.services.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

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

        // pega o aluno atual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario aluno = usuarioService.buscarPorUsername(auth.getName());

        List<Oferta> ofertas = inscricaoService.inscricoesDoAluno(aluno.getId());

        model.addAttribute("ofertas", ofertas);

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
}