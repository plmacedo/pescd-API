package br.ufscar.pescd.controllers;

import br.ufscar.pescd.model.Inscricao;
import br.ufscar.pescd.services.InscricaoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DownloadController {

    @Autowired
    private InscricaoService inscricaoService;

    // 1. Endpoint para o Plano de Trabalho
    @GetMapping("/download/plano/{id}")
    public ResponseEntity<byte[]> baixarPlanoTrabalho(@PathVariable Long id) {
        Inscricao inscricao = inscricaoService.buscarPorID(id);
        byte[] arquivoPdf = inscricao.getArquivoPlano();

        if (arquivoPdf == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        // "inline" tenta abrir o PDF diretamente no separador do navegador.
        // Se preferir forçar o download direto, altere para "attachment"
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"plano_trabalho_" + id + ".pdf\"");

        return new ResponseEntity<>(arquivoPdf, headers, HttpStatus.OK);
    }

    // 2. Endpoint para o Relatório Final
    @GetMapping("/download/relatorio/{id}")
    public ResponseEntity<byte[]> baixarRelatorioFinal(@PathVariable Long id) {
        Inscricao inscricao = inscricaoService.buscarPorID(id);
        byte[] arquivoPdf = inscricao.getArquivoRelatorioFinal();

        if (arquivoPdf == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"relatorio_final_" + id + ".pdf\"");

        return new ResponseEntity<>(arquivoPdf, headers, HttpStatus.OK);
    }

    // 3. Endpoint para a Documentação de Ensino
    @GetMapping("/download/documentacao/{id}")
    public ResponseEntity<byte[]> baixarDocumentacao(@PathVariable Long id) {
        Inscricao inscricao = inscricaoService.buscarPorID(id);
        byte[] arquivoPdf = inscricao.getArquivoDocumentacao();

        if (arquivoPdf == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"documentacao_" + id + ".pdf\"");

        return new ResponseEntity<>(arquivoPdf, headers, HttpStatus.OK);
    }
}