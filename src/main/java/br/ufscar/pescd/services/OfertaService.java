package br.ufscar.pescd.services;

import br.ufscar.pescd.dto.OfertaFormDTO;
import br.ufscar.pescd.model.Oferta;
import br.ufscar.pescd.model.Usuario;
import br.ufscar.pescd.repositories.OfertaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OfertaService {

    @Autowired
    private OfertaRepository repository;

    @Autowired
    private UsuarioService usuarioService;

    public List<Oferta> listarTodas() {
        return repository.findAll();
    }

    public List<Oferta> listarPorFimMaisRecente() {
        return repository.findAllByOrderByFimDesc();
    }

    public List<Oferta> listarComInscricoesPorFimMaisRecente() {
        return repository.buscarTodasComInscricoesOrdenadoPorFim();
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

    public void salvarOferta(OfertaFormDTO dto, String usernameSecretario){

        Usuario secretarioCriador = new Usuario();
        secretarioCriador = usuarioService.buscarPorUsername(usernameSecretario);

        Usuario professorResponsavel;
        professorResponsavel = usuarioService.buscarPorId(dto.getProfessorResponsavelId());
        // se der erro eh que preciso puxar pelo id o secretario criador


        String nomeOferta = dto.getNome();
        if(nomeOferta == null || nomeOferta.isBlank()){
            nomeOferta = "PESCD - " + dto.getSemestre();
        }

        // dto--> entidade
        Oferta novaOferta = new Oferta(
                null,                // gerado sozinho AUTO INCREMENT
                dto.getInicio(),
                dto.getFim(),
                nomeOferta,
                dto.getSemestre(),
                secretarioCriador,
                professorResponsavel,
                0
        );
        repository.save(novaOferta);

    }
}