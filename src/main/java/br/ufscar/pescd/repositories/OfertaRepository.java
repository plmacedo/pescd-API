package br.ufscar.pescd.repositories;

import br.ufscar.pescd.model.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query; // IMPORTANTE ADICIONAR ESTE IMPORT
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfertaRepository extends JpaRepository<Oferta, Long> {

    List<Oferta> findAllByOrderByFimDesc();

    // === NOVA QUERY ADICIONADA PARA RESOLVER O ERRO ===
    // O "LEFT JOIN FETCH" obriga o Spring a buscar a lista de inscrições de imediato
    @Query("SELECT DISTINCT o FROM Oferta o LEFT JOIN FETCH o.inscricoes ORDER BY o.fim DESC")
    List<Oferta> buscarTodasComInscricoesOrdenadoPorFim();
    List<Oferta> findByNome(String nome);
}