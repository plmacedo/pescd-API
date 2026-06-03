package br.ufscar.pescd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import br.ufscar.pescd.model.Usuario;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByUsername(String username);

     List<Usuario> findByCargosContaning(String cargo);
}
