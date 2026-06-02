package br.ufscar.pescd.controllers;

import br.ufscar.pescd.services.OfertaService;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.Model;

import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VisitanteController {

    @Autowired
    private OfertaService ofertaService;

    @GetMapping("/visitante")
    public String visitante(Model model) {

        model.addAttribute(
                "ofertas",
                ofertaService.listarPorFimMaisRecente()
        );

        return "visitante";
    }
}