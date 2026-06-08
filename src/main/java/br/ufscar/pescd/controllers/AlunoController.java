package br.ufscar.pescd.controllers;

import br.ufscar.pescd.dto.AddAlunoFormDTO;
import br.ufscar.pescd.dto.PlanoTrabalhoFormDTO;
import br.ufscar.pescd.dto.RelatorioFinalFormDTO;
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
import org.springframework.web.multipart.MultipartFile;

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
        // pega o aluno atual
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Usuario aluno = usuarioService.buscarPorUsername(auth.getName());

        List<Inscricao> inscricoes = inscricaoService.buscarInscricoesPorAluno(aluno.getId());

        model.addAttribute("inscricoes", inscricoes);

        return "aluno/main";
    }

    @GetMapping("/enviarPlano/{idInscricao}")
    public String exibirFormularioPlano(@PathVariable Long idInscricao, Model model) {
        Inscricao inscricao = inscricaoService.buscarPorID(idInscricao);

        PlanoTrabalhoFormDTO dto = new PlanoTrabalhoFormDTO();
        dto.setInscricaoID(inscricao.getId());

        model.addAttribute("inscricao", inscricao);
        model.addAttribute("planoDTO", dto);

        // Carrega os professores do BD para o select no HTML
        model.addAttribute("professores", usuarioService.filtrarPorCargo("ROLE_SUPERVISOR"));

        return "aluno/enviar_plano";
    }

    @PostMapping("/enviarPlano")
    public String processarEnvioPlano(@Valid @ModelAttribute("planoDTO") PlanoTrabalhoFormDTO planoDTO, BindingResult result, Model model) {
        MultipartFile arquivo = planoDTO.getArquivoPlano();

        // Validar se é PDF
        if (arquivo == null || arquivo.isEmpty() || !arquivo.getContentType().equals("application/pdf")) {
            result.rejectValue("arquivoPlano", "error.arquivoPlano", "O arquivo deve ser um PDF válido.");
        }

        if (result.hasErrors()) {
            Inscricao inscricao = inscricaoService.buscarPorID(planoDTO.getInscricaoID());
            model.addAttribute("inscricao", inscricao);
            model.addAttribute("professores", usuarioService.filtrarPorCargo("ROLE_PROFESSOR"));
            return "aluno/enviar_plano"; // volta pra tela se tiver erro
        }

        // Você precisará atualizar o InscricaoService para aceitar o DTO completo
        // e salvar os novos campos juntamente com o arquivo
        inscricaoService.enviarPlanoTrabalho(planoDTO);

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

    @GetMapping("/enviarRelatorio/{idInscricao}")
    public String exibirFormularioRelatorio(@PathVariable Long idInscricao, Model model) {
        Inscricao inscricao = inscricaoService.buscarPorID(idInscricao);

        RelatorioFinalFormDTO dto = new RelatorioFinalFormDTO();
        dto.setInscricaoID(inscricao.getId());

        model.addAttribute("inscricao", inscricao);
        model.addAttribute("relatorioDTO", dto);
        return "aluno/enviar_relatorio";
    }

    @PostMapping("/enviarRelatorio")
    public String processarEnvioRelatorio(@Valid @ModelAttribute("relatorioDTO") RelatorioFinalFormDTO dto, BindingResult result, Model model) {
        // Validando tamanho e tipo seguindo o padrão do método enviarDocumentacao
        if (dto.getArquivo() == null || dto.getArquivo().isEmpty() || !"application/pdf".equals(dto.getArquivo().getContentType())) {
            result.rejectValue("arquivo", "error.arquivo.pdf");
        }

        if (result.hasErrors()) {
            Inscricao inscricao = inscricaoService.buscarPorID(dto.getInscricaoID());
            model.addAttribute("inscricao", inscricao);
            return "aluno/enviar_relatorio";
        }

        try {
            inscricaoService.enviarRelatorioFinal(dto.getInscricaoID(), dto);
        } catch (IOException e) {
            model.addAttribute("erro", "Erro ao salvar o arquivo.");
            return "aluno/enviar_relatorio";
        }

        return "redirect:/aluno/main?sucessoRelatorio";
    }
}