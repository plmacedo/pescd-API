package br.ufscar.pescd.controllers;

import br.ufscar.pescd.dto.ConcluirRelatorioFormDTO;
import br.ufscar.pescd.model.Inscricao;
import br.ufscar.pescd.model.Oferta;
import br.ufscar.pescd.services.OfertaService;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/responsavel")
public class ResponsavelController {

    @Autowired
    private OfertaService ofertaService;

    @Autowired
    private br.ufscar.pescd.repositories.InscricaoRepository inscricaoRepository;

    @Autowired
    private br.ufscar.pescd.services.InscricaoService inscricaoService;

    @GetMapping("/main")
    public String main(Model model) {

        model.addAttribute(
                "ofertas",
                ofertaService.listarComInscricoesPorFimMaisRecente()
        );

        return "responsavel/main";
    }

    @PostMapping("/finalizar/{id}")
    public String finalizarOferta(@PathVariable Long id) {

        Oferta oferta = ofertaService.buscarPorId(id);

        oferta.setStatus(
                "Aguardando encerramento do secretario"
        );

        ofertaService.salvar(oferta);

        return "redirect:/responsavel/main";
    }

    @GetMapping("/oferta/{id}/detalhes")
    public String detalhesOfertaResponsavel(@PathVariable Long id, Model model) {
        Oferta oferta = ofertaService.buscarPorId(id);
        model.addAttribute("oferta", oferta);
        model.addAttribute("inscricoes", inscricaoRepository.findByOfertaId(id));
        return "responsavel/detalhes_oferta";
    }

    @GetMapping("/inscricao/{id}/detalhes")
    public String detalhesAlunoResponsavel(@PathVariable Long id, Model model) {
        br.ufscar.pescd.model.Inscricao inscricao = inscricaoService.buscarPorID(id);
        model.addAttribute("inscricao", inscricao);
        return "responsavel/detalhes_aluno";
    }

    @GetMapping("/concluir-relatorio/{id}")
    public String exibirFormularioConclusao(@PathVariable("id") Long id, Model model) {
        Inscricao inscricao = inscricaoService.buscarPorID(id);

        ConcluirRelatorioFormDTO form = new ConcluirRelatorioFormDTO();
        form.setInscricaoID(inscricao.getId());
        form.setFrequencia(inscricao.getFrequencia());
        form.setNota(inscricao.getSugestaoNota());

        model.addAttribute("inscricao", inscricao);
        model.addAttribute("concluirRelatorioForm", form);
        return "responsavel/concluir_relatorio";
    }

    @PostMapping("/concluir-relatorio")
    public String processarConclusao(@Valid @ModelAttribute("concluirRelatorioForm") ConcluirRelatorioFormDTO form,
                                     BindingResult result, Model model) {
        if (result.hasErrors()) {
            if (form.getInscricaoID() == null) {
                return "redirect:/responsavel/main";
            }
            Inscricao inscricao = inscricaoService.buscarPorID(form.getInscricaoID());
            model.addAttribute("inscricao", inscricao);
            return "responsavel/concluir_relatorio";
        }

        inscricaoService.concluirRelatorioResponsavel(form);
        return "redirect:/responsavel/main?sucesso=relatorio_concluido";
    }
}