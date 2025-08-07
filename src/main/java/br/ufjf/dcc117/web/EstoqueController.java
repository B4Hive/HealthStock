package br.ufjf.dcc117.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.ufjf.dcc117.model.PersistenceService; // <-- ADICIONAR ESTA IMPORTAÇÃO
import br.ufjf.dcc117.model.estoque.Medicacao; // Adicionar esta importação
import br.ufjf.dcc117.model.estoque.Produto;
import br.ufjf.dcc117.model.setor.Setor;
import br.ufjf.dcc117.model.setor.SetorCadastro;
import br.ufjf.dcc117.service.EstoqueService;
import br.ufjf.dcc117.service.FornecedorService;
import jakarta.servlet.http.HttpSession;

@Controller
public class EstoqueController {

    @Autowired
    private EstoqueService estoqueService;
    @Autowired
    private FornecedorService fornecedorService;

    private boolean isUserLoggedIn(HttpSession session) {
        return session.getAttribute("setorLogado") != null;
    }

    private Setor getLoggedUser(HttpSession session) {
        return (Setor) session.getAttribute("setorLogado");
    }

    @GetMapping("/estoque")
    public ModelAndView listarProdutos(HttpSession session) {
        if (!isUserLoggedIn(session)) {
            return new ModelAndView("redirect:/");
        }
        Setor setor = getLoggedUser(session);
        ModelAndView mv = new ModelAndView("estoque");
        mv.addObject("produtos", estoqueService.listarProdutos(setor));
        mv.addObject("setor", setor);
        // Adicionamos uma forma segura de verificar o tipo do produto na view
        mv.addObject("Medicacao.class", Medicacao.class);
        return mv;
    }

    @GetMapping("/estoque/{id}")
    public ModelAndView detalheProduto(@PathVariable int id, HttpSession session) {
        if (!isUserLoggedIn(session)) {
            return new ModelAndView("redirect:/");
        }
        ModelAndView mv = new ModelAndView("estoque-detalhe");
        Setor setor = getLoggedUser(session);
        Produto produto = estoqueService.getProduto(setor, id);
        mv.addObject("produto", produto);
        mv.addObject("setor", setor);
        return mv;
    }

    @PostMapping("/estoque/consumir")
    public String consumirProduto(@RequestParam int produtoId, @RequestParam int quantidade, HttpSession session) {
        if (!isUserLoggedIn(session)) {
            return "redirect:/";
        }
        Setor setor = getLoggedUser(session);
        boolean sucesso = estoqueService.consumirProduto(setor, produtoId, quantidade);
        if (!sucesso) {
            return "redirect:/estoque?error=vencido";
        }
        return "redirect:/estoque";
    }

    @PostMapping("/estoque/deletar-vencidos")
    public String deletarVencidos(HttpSession session) {
        if (!isUserLoggedIn(session)) {
            return "redirect:/";
        }
        Setor setor = getLoggedUser(session);
        estoqueService.deletarProdutosVencidos(setor);
        return "redirect:/estoque?vencidos_removidos=true";
    }

    @PostMapping("/estoque/deletar")
    public String deletarProduto(@RequestParam int produtoId, HttpSession session) {
        if (!isUserLoggedIn(session)) {
            return "redirect:/";
        }
        Setor setor = getLoggedUser(session);
        estoqueService.deletarProdutoVencido(setor, produtoId);
        return "redirect:/estoque?removido=true";
    }

    @GetMapping("/estoque/novo")
    public ModelAndView formNovoProduto(HttpSession session) {
        if (!isUserLoggedIn(session)) {
            return new ModelAndView("redirect:/");
        }
        Setor setor = getLoggedUser(session);
        if (!(setor instanceof SetorCadastro)) {
            return new ModelAndView("redirect:/home?error=Acesso negado");
        }
        ModelAndView mv = new ModelAndView("estoque-form");
        // Corrigido para usar o novo construtor. Valores vazios/zero para o formulário.
        mv.addObject("produto", new Produto(0, 0, "", 0, 0, "")); 
        mv.addObject("fornecedores", fornecedorService.listarFornecedores((SetorCadastro) setor));
        mv.addObject("action", "novo");
        return mv;
    }

    @PostMapping("/estoque/novo")
    public String cadastrarProduto(@RequestParam String nome, @RequestParam int fornecedorId, @RequestParam String tipoProduto, HttpSession session) {
        if (!isUserLoggedIn(session)) {
            return "redirect:/";
        }
        Setor setor = getLoggedUser(session);
        if (setor instanceof SetorCadastro sc) {
            estoqueService.cadastrarNovoTipoProduto(sc, nome, fornecedorId, tipoProduto);
        }
        return "redirect:/estoque";
    }

    @GetMapping("/estoque/editar/{codigoProduto}")
    public ModelAndView formEditarProduto(@PathVariable int codigoProduto, HttpSession session) {
        Setor setor = getLoggedUser(session);
        if (setor == null || !(setor instanceof SetorCadastro)) {
            return new ModelAndView("redirect:/home?error=Acesso negado");
        }
        // Pega a primeira instância que encontrar com este código para preencher os dados do formulário
        Produto produto = PersistenceService.carregarProdutos(p -> p.getCodigoProduto() == codigoProduto).stream().findFirst().orElse(null);
        
        ModelAndView mv = new ModelAndView("estoque-form");
        mv.addObject("produto", produto);
        mv.addObject("fornecedores", fornecedorService.listarFornecedores((SetorCadastro) setor));
        mv.addObject("action", "editar");
        return mv;
    }

    @PostMapping("/estoque/editar")
    public String editarProduto(@RequestParam int codigoProduto, @RequestParam String nome, @RequestParam int idFornecedor, HttpSession session) {
        if (!isUserLoggedIn(session)) {
            return "redirect:/";
        }
        Setor setor = getLoggedUser(session);
        if (setor instanceof SetorCadastro) {
            estoqueService.editarProdutoMestre(codigoProduto, nome, idFornecedor);
        }
        return "redirect:/estoque";
    }
}