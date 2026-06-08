package br.ufscar.pescd.services;

import br.ufscar.pescd.model.Usuario;
import br.ufscar.pescd.repositories.UsuarioRepository;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.List;

@Service
public class   UsuarioService {

    @Autowired
    private UsuarioRepository repository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public Usuario salvar(Usuario usuario) {

        usuario.setSenha(
                passwordEncoder.encode(
                        usuario.getSenha()
                )
        );

        return repository.save(usuario);
    }

    public Usuario buscarPorUsername(String username) {

        return repository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Nao existe usuario com username = " + username));
    }


    public Optional<Usuario> buscarOptionalPorUsername(String username) {
        return repository.findByUsername(username);
    }

    public Usuario buscarPorId(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Nao existe usuario com o id = " + id));
    }

    public List<Usuario> filtrarPorCargo(String cargo){
        return repository.findByCargosContaining(cargo);
    }

    public List<Usuario> buscarTodos() {
        return repository.findAll();
    }

    public void excluir(Long id) {
        repository.deleteById(id);
    }

    public Usuario atualizar(Long id, Usuario usuarioAtualizado) {
        Usuario usuarioExistente = buscarPorId(id);

        usuarioExistente.setNome(usuarioAtualizado.getNome());
        usuarioExistente.setUsername(usuarioAtualizado.getUsername());
        usuarioExistente.setCargos(usuarioAtualizado.getCargos());

        // se a senhafoi prenchida na edicao, codifica e altera
        //se veio vazia, mantem a antiga
        if (usuarioAtualizado.getSenha() != null && !usuarioAtualizado.getSenha().trim().isEmpty()) {
            usuarioExistente.setSenha(passwordEncoder.encode(usuarioAtualizado.getSenha()));
        }

        return repository.save(usuarioExistente);
    }

}