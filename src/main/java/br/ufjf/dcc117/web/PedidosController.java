package br.ufjf.dcc117.web;

import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import br.ufjf.dcc117.model.PersistenceService;
import br.ufjf.dcc117.model.estoque.Produto;
import br.ufjf.dcc117.model.setor.Pedido;
import br.ufjf.dcc117.model.setor.Setor;
import br.ufjf.dcc117.model.setor.SetorCadastro;
import br.ufjf.dcc117.service.FornecedorService;
import br.ufjf.dcc117.service.PedidoService;
import jakarta.servlet.http.HttpSession;

@Controller
public class PedidosController {

    @Autowired
    private PedidoService pedidoService;

    @Autowired
    private FornecedorService fornecedorService;

    private Setor getLoggedUser(HttpSession session) {
        return (Setor) session.getAttribute("setorLogado");
    }

    @GetMapping("/pedidos")
    public ModelAndView listarPedidos(HttpSession session, @RequestParam(required = false) String estado) {
        if (getLoggedUser(session) == null) {
            return new ModelAndView("redirect:/");
        }
        ModelAndView mv = new ModelAndView("pedidos");
        Setor setor = getLoggedUser(session);
        
        // 1. Pega a lista de pedidos como antes
        List<Pedido> pedidos = pedidoService.listarPedidos(setor, estado);
        
        // 2. Cria um Map para associar cada Pedido ao seu Produto
        Map<Pedido, Produto> pedidosComProdutos = new LinkedHashMap<>();
        
        // 3. Carrega todos os produtos uma única vez para eficiência
        List<Produto> todosOsProdutos = PersistenceService.carregarProdutos(p -> true);

        // 4. Para cada pedido, encontra o produto correspondente e os adiciona ao Map
        for (Pedido pedido : pedidos) {
            Produto produtoAssociado = todosOsProdutos.stream()
                .filter(p -> p.getNome().equalsIgnoreCase(pedido.getProduto()))
                .findFirst()
                .orElse(null); // Pode ser nulo se o produto foi deletado, a view precisa lidar com isso
            pedidosComProdutos.put(pedido, produtoAssociado);
        }

        mv.addObject("pedidosComProdutos", pedidosComProdutos); // 5. Envia o Map para a view
        mv.addObject("setor", setor);
        mv.addObject("estadoFiltro", estado);
        return mv;
    }

    @GetMapping("/pedidos/{id}")
    public ModelAndView detalhePedido(@PathVariable int id, HttpSession session) {
        Setor setorLogado = getLoggedUser(session);
        if (setorLogado == null) {
            return new ModelAndView("redirect:/");
        }
        ModelAndView mv = new ModelAndView("pedidos-detalhe");
        Pedido pedido = pedidoService.getPedido(id);
        mv.addObject("pedido", pedido);

        if (pedido != null) {
            // Se for um pedido de cadastro, carrega os fornecedores para o formulário de aprovação
            if (pedido.getQuantidade() == 0 && setorLogado instanceof SetorCadastro) {
                mv.addObject("fornecedores", fornecedorService.listarFornecedores((SetorCadastro) setorLogado));
            }
            
            // CORREÇÃO: Busca o produto mestre diretamente pela persistência, sem depender de um setor específico.
            // Isso é mais robusto, pois busca pelo nome em todos os produtos cadastrados.
            Produto produtoMestre = PersistenceService.carregarProdutos(p -> p.getNome().equalsIgnoreCase(pedido.getProduto()))
                .stream()
                .findFirst()
                .orElse(null);
            mv.addObject("produtoMestre", produtoMestre);
        }
        return mv;
    }

    @PostMapping("/pedidos/responder")
    public String responderPedido(@RequestParam int pedidoId,
                                  @RequestParam String acao,
                                  @RequestParam(required = false) String responsavel,
                                  @RequestParam(required = false) String lote,
                                  @RequestParam(required = false) String validade,
                                  @RequestParam(required = false) Integer fornecedorId,
                                  @RequestParam(required = false) String tipoProduto,
                                  HttpSession session) {
        Setor setorLogado = getLoggedUser(session);
        if (setorLogado == null) {
            return "redirect:/";
        }
        
        boolean aprovado = "aprovar".equals(acao);
        String detalhes = (lote != null && validade != null && !lote.isBlank() && !validade.isBlank())
                        ? lote + " | " + validade
                        : null;

        pedidoService.responderPedido(setorLogado, pedidoId, aprovado, responsavel, detalhes, fornecedorId, tipoProduto);

        return "redirect:/pedidos";
    }

    @GetMapping("/pedidos/novo")
    public ModelAndView formNovoPedido(HttpSession session) {
        if (getLoggedUser(session) == null) {
            return new ModelAndView("redirect:/");
        }
        ModelAndView mv = new ModelAndView("pedidos-form");
        
        // Carrega todos os produtos do sistema
        List<Produto> todosOsProdutos = PersistenceService.carregarProdutos(p -> true);

        // Agrupa os produtos por código e nome para evitar duplicatas na lista
        Collection<Produto> produtosUnicos = todosOsProdutos.stream()
                .collect(Collectors.toMap(Produto::getCodigoProduto, p -> p, (p1, p2) -> p1))
                .values();

        mv.addObject("produtos", produtosUnicos);
        return mv;
    }

    @PostMapping("/pedidos/novo")
    public String gerarPedido(@RequestParam(required = false) Integer codigoProduto,
                              @RequestParam(required = false) String nomeProduto,
                              @RequestParam int quantidade,
                              HttpSession session) {
        Setor setorLogado = getLoggedUser(session);
        if (setorLogado == null) {
            return "redirect:/";
        }

        if (codigoProduto != null) {
            pedidoService.gerarPedido(setorLogado, codigoProduto, quantidade);
        } else if (nomeProduto != null && !nomeProduto.isBlank()) {
            pedidoService.gerarPedido(setorLogado, nomeProduto);
        }

        return "redirect:/pedidos";
    }
}