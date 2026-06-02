package br.ufscar.pescd.controllers;

import br.ufscar.pescd.model.Usuario;
import br.ufscar.pescd.repositories.UsuarioRepository;
import org.springframework.ui.Model;
import org.springframework.security.core.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @GetMapping("/home")
    public String home(
            Authentication authentication,
            Model model) {

        Usuario usuario =
                usuarioRepository
                        .findByUsername(authentication.getName())
                        .orElseThrow();

        model.addAttribute("usuario", usuario);

        return "home";
    }
}