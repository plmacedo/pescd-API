package br.ufscar.pescd.controllers;

import br.ufscar.pescd.dto.AddAlunoFormDTO;
import br.ufscar.pescd.model.Oferta;
import br.ufscar.pescd.model.Usuario;
import br.ufscar.pescd.services.InscricaoService;
import br.ufscar.pescd.services.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
        Authentication auth =
                SecurityContextHolder.getContext().getAuthentication();
        Usuario aluno =
                usuarioService.buscarPorUsername(auth.getName());

         List<Oferta> ofertas = inscricaoService.inscricoesDoAluno(aluno.getId());

         model.addAttribute("ofertas", ofertas);

        return "aluno/main";
    }
}