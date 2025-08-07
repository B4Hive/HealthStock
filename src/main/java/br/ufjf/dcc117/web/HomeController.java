package br.ufjf.dcc117.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.servlet.ModelAndView;

import jakarta.servlet.http.HttpSession;

@Controller
public class HomeController {

    @GetMapping("/home")
    public ModelAndView home(HttpSession session) {
        if (session.getAttribute("setorLogado") == null) {
            return new ModelAndView("redirect:/");
        }

        ModelAndView mv = new ModelAndView("home");
        // Você pode adicionar mais dados ao modelo se necessário
        // mv.addObject("key", "value");
        return mv;
    }
}