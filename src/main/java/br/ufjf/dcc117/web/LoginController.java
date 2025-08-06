package br.ufjf.dcc117.web;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import br.ufjf.dcc117.model.setor.Setor;
import jakarta.servlet.http.HttpSession;

@Controller
public class LoginController {

    @GetMapping("/")
    public String showLoginForm(HttpSession session) {
        if (session.getAttribute("setorLogado") != null) {
            return "redirect:/home";
        }
        return "login";
    }

    @PostMapping("/login")
    public String handleLogin(@RequestParam String username, @RequestParam String password, HttpSession session, Model model) {
        Setor setor = Setor.carregar(username);
        if (setor != null && setor.validarSenha(password)) {
            session.setAttribute("setorLogado", setor);
            return "redirect:/home";
        } else {
            model.addAttribute("error", "Usuário ou senha inválidos.");
            return "login";
        }
    }

    @GetMapping("/logout")
    public String handleLogout(HttpSession session) {
        session.invalidate();
        return "redirect:/";
    }
}