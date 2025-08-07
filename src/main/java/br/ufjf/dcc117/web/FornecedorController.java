package br.ufjf.dcc117.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.ufjf.dcc117.model.estoque.Fornecedor;
import br.ufjf.dcc117.model.setor.Setor;
import br.ufjf.dcc117.model.setor.SetorCadastro;
import br.ufjf.dcc117.service.FornecedorService;
import jakarta.servlet.http.HttpSession;

@Controller
public class FornecedorController {

    @Autowired
    private FornecedorService fornecedorService;

    private SetorCadastro getCadastroSetor(HttpSession session) {
        Setor setor = (Setor) session.getAttribute("setorLogado");
        if (setor instanceof SetorCadastro setorCadastro) {
            return setorCadastro;
        }
        return null;
    }

    @GetMapping("/fornecedores")
    public ModelAndView listarFornecedores(HttpSession session) {
        SetorCadastro setorCadastro = getCadastroSetor(session);
        if (setorCadastro == null) {
            return new ModelAndView("redirect:/home?error=Acesso negado");
        }
        ModelAndView mv = new ModelAndView("fornecedores");
        mv.addObject("fornecedores", fornecedorService.listarFornecedores(setorCadastro));
        return mv;
    }

    @GetMapping("/fornecedores/{id}")
    public ModelAndView detalheFornecedor(@PathVariable int id, HttpSession session) {
        SetorCadastro setorCadastro = getCadastroSetor(session);
        if (setorCadastro == null) {
            return new ModelAndView("redirect:/home?error=Acesso negado");
        }
        ModelAndView mv = new ModelAndView("fornecedores-detalhe");
        mv.addObject("fornecedor", fornecedorService.getFornecedor(setorCadastro, id));
        return mv;
    }

    @GetMapping("/fornecedores/novo")
    public ModelAndView formNovoFornecedor(HttpSession session) {
        if (getCadastroSetor(session) == null) {
            return new ModelAndView("redirect:/home?error=Acesso negado");
        }
        ModelAndView mv = new ModelAndView("fornecedores-form");
        mv.addObject("fornecedor", new Fornecedor(0, "", "", "", "", ""));
        mv.addObject("action", "novo");
        return mv;
    }

    @PostMapping("/fornecedores/novo")
    public String cadastrarFornecedor(@RequestParam String nome, @RequestParam String cnpj, @RequestParam String telefone, @RequestParam String endereco, @RequestParam String email, HttpSession session) {
        SetorCadastro setorCadastro = getCadastroSetor(session);
        if (setorCadastro == null) {
            return "redirect:/home?error=Acesso negado";
        }
        fornecedorService.cadastrarFornecedor(setorCadastro, nome, cnpj, telefone, endereco, email);
        return "redirect:/fornecedores";
    }

    @GetMapping("/fornecedores/editar/{id}")
    public ModelAndView formEditarFornecedor(@PathVariable int id, HttpSession session) {
        SetorCadastro setorCadastro = getCadastroSetor(session);
        if (setorCadastro == null) {
            return new ModelAndView("redirect:/home?error=Acesso negado");
        }
        ModelAndView mv = new ModelAndView("fornecedores-form");
        mv.addObject("fornecedor", fornecedorService.getFornecedor(setorCadastro, id));
        mv.addObject("action", "editar");
        return mv;
    }

    @PostMapping("/fornecedores/editar")
    public String editarFornecedor(@RequestParam int id, @RequestParam String nome, @RequestParam String cnpj, @RequestParam String telefone, @RequestParam String endereco, @RequestParam String email, HttpSession session) {
        SetorCadastro setorCadastro = getCadastroSetor(session);
        if (setorCadastro == null) {
            return "redirect:/home?error=Acesso negado";
        }
        fornecedorService.editarFornecedor(setorCadastro, id, nome, cnpj, telefone, endereco, email);
        return "redirect:/fornecedores";
    }
}