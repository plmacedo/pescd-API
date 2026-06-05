package br.ufscar.pescd.repositories;

import br.ufscar.pescd.model.Inscricao;
import br.ufscar.pescd.model.Oferta;
import br.ufscar.pescd.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InscricaoRepository extends JpaRepository<Inscricao, Long> {


    boolean existsByAlunoAndOferta(Usuario aluno, Oferta oferta);
    List<Inscricao> findByOfertaId(Long ofertaId);
    List<Inscricao> findByAlunoId(Long alunoId);

}