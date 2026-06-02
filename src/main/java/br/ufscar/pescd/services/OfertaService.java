package br.ufscar.pescd.services;

import br.ufscar.pescd.model.Oferta;
import br.ufscar.pescd.repositories.OfertaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfertaService {

    @Autowired
    private OfertaRepository repository;

    public List<Oferta> listarTodas() {
        return repository.findAll();
    }

    public List<Oferta> listarPorFimMaisRecente() {
        return repository.findAllByOrderByFimDesc();
    }

    public Oferta buscarPorId(Long id) {

        return repository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Oferta não encontrada"
                        ));
    }

    public Oferta salvar(Oferta oferta) {
        return repository.save(oferta);
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }
}