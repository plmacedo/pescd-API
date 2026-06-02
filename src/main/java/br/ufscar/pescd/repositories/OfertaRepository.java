package br.ufscar.pescd.repositories;

import br.ufscar.pescd.model.Oferta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfertaRepository
        extends JpaRepository<Oferta, Long> {

    List<Oferta> findAllByOrderByFimDesc();
}