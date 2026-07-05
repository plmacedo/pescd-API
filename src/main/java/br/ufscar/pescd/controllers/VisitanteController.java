package br.ufscar.pescd.controllers;

import br.ufscar.pescd.dto.OfertaResponseDTO;
import br.ufscar.pescd.services.OfertaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/visitante")
public class VisitanteController {

    @Autowired
    private OfertaService ofertaService;

    @GetMapping("/ofertas")
    public ResponseEntity<List<OfertaResponseDTO>> listarOfertasVisitante() {
        List<OfertaResponseDTO> ofertas = ofertaService.listarPorFimMaisRecente()
                .stream()
                .map(OfertaResponseDTO::new)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ofertas);
    }
}