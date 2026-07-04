package br.ufscar.pescd.controllers;

import br.ufscar.pescd.model.FraseConfirmacao;
import br.ufscar.pescd.model.Usuario;
import br.ufscar.pescd.repositories.FraseRepository;
import br.ufscar.pescd.repositories.OfertaRepository;
import br.ufscar.pescd.services.UsuarioService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;
import org.springframework.dao.DataIntegrityViolationException;

import java.beans.PropertyEditorSupport;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/administrador")
public class AdministradorController {

    @Autowired
    private FraseRepository fraseRepository;

    @Autowired
    private UsuarioService usuarioService;

    @Autowired
    private OfertaRepository ofertaRepository;

    @InitBinder
    protected void initBinder(WebDataBinder binder) {
        binder.registerCustomEditor(List.class, "cargos", new PropertyEditorSupport() {
            @Override
            public void setAsText(String text) {

                setValue(new ArrayList<>(Arrays.asList(text.split(","))));
            }
        });
    }

    @GetMapping("/main")
    public String main(Model model) {
        // Buscar usuarios
        List<Usuario> usuarios = usuarioService.buscarTodos();
        model.addAttribute("usuarios", usuarios);

        // mantendo a lógica da frase caso precise
        FraseConfirmacao mensagem = fraseRepository.findById(1L).orElse(new FraseConfirmacao());
        model.addAttribute("mensagem", mensagem.getMensagem());

        return "administrador/main";
    }

    @PostMapping("/alterarMensagem")
    public String alterarMensagem(String texto) {

        FraseConfirmacao mensagem = fraseRepository.findById(1L).orElseThrow();
        mensagem.setMensagem(texto);
        fraseRepository.save(mensagem);

        return "redirect:/administrador/main";
    }

    //---
    //CRUD USUARIOS
    //---

    //novo usuario
    @GetMapping("/novoUsuario")
    public String novoUsuario(Model model) {
        model.addAttribute("usuario", new Usuario());
        return "administrador/form_usuario";
    }

    @PostMapping("/salvarUsuario")
    public String salvarUsuario(@ModelAttribute Usuario usuario) {

        if (usuarioService.buscarOptionalPorUsername(usuario.getUsername()).isPresent()) {
            return "redirect:/administrador/novoUsuario?erro=usernameDuplicado";
        }
        usuarioService.salvar(usuario);
        return "redirect:/administrador/main";
    }

    //editar usuario existente
    @GetMapping("/editarUsuario/{id}")
    public String editarUsuario(@PathVariable("id") Long id, Model model) {
        Usuario usuarioBD = usuarioService.buscarPorId(id);

        // Cria um clone desvinculado do banco para enviar ao formulário sem corromper o banco
        Usuario usuarioForm = new Usuario();
        usuarioForm.setId(usuarioBD.getId());
        usuarioForm.setNome(usuarioBD.getNome());
        usuarioForm.setUsername(usuarioBD.getUsername());
        usuarioForm.setCargos(usuarioBD.getCargos());

        // Oculta a senha por segurança ao enviar para a view
        usuarioForm.setSenha("");

        model.addAttribute("usuario", usuarioForm);
        return "administrador/form_usuario";
    }

    @PostMapping("/atualizarUsuario/{id}")
    public String atualizarUsuario(@PathVariable("id") Long id, @ModelAttribute Usuario usuario) {
        usuarioService.atualizar(id, usuario);
        return "redirect:/administrador/main";
    }

    //excluir usuario
    @GetMapping("/excluirUsuario/{id}")
    public String excluirUsuario(@PathVariable("id") Long id) {
        try {
            usuarioService.excluir(id);
            return "redirect:/administrador/main?sucesso";
        } catch (DataIntegrityViolationException e) {
            return "redirect:/administrador/main?erro=Usuario+possui+vinculos+e+nao+pode+ser+excluido";
        }
    }

    @PostMapping("/buscarOferta")
    public String buscarOferta(
            @RequestParam String nome,
            Model model) {

        List<Usuario> usuarios = usuarioService.buscarTodos();

        FraseConfirmacao mensagem =
                fraseRepository.findById(1L)
                        .orElse(new FraseConfirmacao());

        model.addAttribute("usuarios", usuarios);
        model.addAttribute("mensagem", mensagem.getMensagem());

        model.addAttribute(
                "ofertasEncontradas",
                ofertaRepository.findByNome(nome)
        );

        return "administrador/main";
    }
}