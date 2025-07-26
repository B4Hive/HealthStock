package br.ufjf.dcc117.controller;

import java.text.SimpleDateFormat;
import java.util.List;

import br.ufjf.dcc117.model.Auxiliar;
import br.ufjf.dcc117.model.PersistenceService;
import br.ufjf.dcc117.model.estoque.Medicacao;
import br.ufjf.dcc117.model.estoque.Produto;
import br.ufjf.dcc117.model.setor.Pedido;
import br.ufjf.dcc117.model.setor.Setor;

public class PedidosControl extends Control {

    public static String[] getListaPedidos() {
        List<Pedido> pedidos = setor.listarPedidos();
        String[] lista = new String[pedidos.size()];
        for (int i = 0; i < pedidos.size(); i++) {
            lista[i] = "ID: " + pedidos.get(i).getId() + " - " + pedidos.get(i).getProduto() + " - " + pedidos.get(i).getEstado();
        }
        return lista;
    }

    public static String[] getPedido(int pedidoId) {
        Pedido pedido = PersistenceService.carregarPedidos(p -> p.getId() == pedidoId).stream().findFirst().orElse(null);
        if (pedido == null)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        return new String[] {
                String.valueOf(pedido.getId()),
                pedido.getSetorSolicitante(),
                pedido.getSetorResponsavel(),
                sdf.format(pedido.getDataPedido()),
                pedido.getProduto(),
                String.valueOf(pedido.getQuantidade()),
                pedido.getEstado(),
                pedido.getDetalhes() != null ? pedido.getDetalhes() : ""
        };
    }

    public static boolean respostaPedido(int pedidoId, boolean aprovado, String responsavel, String detalhes) {
        Pedido pedido = PersistenceService.carregarPedidos(p -> p.getId() == pedidoId).stream().findFirst().orElse(null);

        if (pedido == null) {
            Auxiliar.error("Control.respostaPedido: Pedido não encontrado.");
            return false;
        }

        if (aprovado) {
            boolean movido;
            if (pedido.getSetorResponsavel().equalsIgnoreCase(Auxiliar.SETOR_CADASTRO)) {
                movido = moverProduto(getProdutoId(pedido.getProduto()), pedido.getQuantidade(), Auxiliar.SETOR_ENTRADA, responsavel, detalhes);
            } else {
                movido = moverProduto(getProdutoId(pedido.getProduto()), pedido.getQuantidade(), pedido.getSetorSolicitante(), responsavel, detalhes);
            }
            if (!movido) {
                Auxiliar.error("Control.respostaPedido: Falha ao mover produto.");
                return false;
            }
        }
        
        // Atualiza o estado do pedido
        setor.aprovarPedido(pedido, aprovado);
        return true;
    }

    private static int getProdutoId(String produtoNome) {
        List<Produto> produtos = PersistenceService.carregarProdutos(p -> p.getSetor().equalsIgnoreCase(Auxiliar.SETOR_CADASTRO));
        for (Produto p : produtos) {
            if (p.getNome().equalsIgnoreCase(produtoNome)) {
                return p.getID();
            }
        }
        return -1; // Produto não encontrado
    }

    private static boolean moverProduto(int produtoId, int quantidade, String setorDestino, String responsavel, String detalhes) {
        Setor setorDestinoObj = Setor.carregar(setorDestino);
        if (setorDestinoObj == null) {
            Auxiliar.error("Control.moverProduto: Setor de destino não encontrado: " + setorDestino);
            return false;
        }
        
        // A retirada é sempre do setor logado (responsável pelo pedido)
        Produto produto = setor.retiradaProduto(produtoId, quantidade);
        if (produto == null) {
            Auxiliar.error("Control.moverProduto: Produto não encontrado ou sem estoque: " + produtoId);
            return false;
        }

        if (produto instanceof Medicacao m) {
            if (!m.verificarValidade()) {
                Auxiliar.error("Control.moverProduto: Medicamento inválido ou vencido: " + produtoId);
                return false;
            }
            m.atualizarDetalhes(detalhes);
            m.atualizarResponsavel(responsavel);
        }
        setorDestinoObj.entradaProduto(produto);
        return true;
    }

    public static boolean gerarPedido(int produtoId, int quantidade) {
        // Pedido de transferencia/compra de produto
        List<Produto> produtos = Setor.carregar(Auxiliar.SETOR_CADASTRO).getProdutos();
        for (Produto p : produtos) {
            if (p.getID() == produtoId) {
                String nomeProduto = p.getNome();
                String setorSolicitante = setor.getNome();
                if (setorSolicitante.equals(Auxiliar.SETOR_CADASTRO)) {
                    Auxiliar.error("Control.gerarPedido: Não é permitido gerar pedidos do setor de cadastro.");
                    return false;
                }
                String setorResponsavel;
                if (p instanceof Medicacao) {
                    setorResponsavel = Auxiliar.SETOR_MEDICACAO;
                    // Se for medicamento, o responsável é a farmacia
                } else {
                    setorResponsavel = "almoxarifado";
                    // Se for produto comum, o responsável é o almoxarifado
                }
                if (setorResponsavel.equals(setorSolicitante)) {
                    setorResponsavel = Auxiliar.SETOR_CADASTRO;
                    // Se o setor solicitante for o mesmo do responsável, direciona para o setor de cadastro
                }
                int pedidoId = PersistenceService.getNextPedidoId();
                Pedido pedido = new Pedido(pedidoId, setorSolicitante, setorResponsavel, nomeProduto, quantidade);
                
                // Salva o pedido uma única vez
                PersistenceService.salvarPedido(pedido);
                return true;
            }
        }
        Auxiliar.error("Control.gerarPedido: Produto não encontrado: " + produtoId);
        return false;
    }

    public static boolean gerarPedido(String nomeProduto) {
        // Pedido de cadastro de produto
        List<Produto> produtos = Setor.carregar(Auxiliar.SETOR_CADASTRO).getProdutos();
        for (Produto p : produtos) {
            if (p.getNome().equalsIgnoreCase(nomeProduto)) {
                Auxiliar.error("Control.gerarPedido: Produto já cadastrado: " + nomeProduto);
                return false;
            }
        }
        String setorSolicitante = setor.getNome();
        if (setorSolicitante.equals(Auxiliar.SETOR_CADASTRO)) {
            Auxiliar.error("Control.gerarPedido: Não é permitido gerar pedidos do setor de cadastro.");
            return false;
        }
        int pedidoId = PersistenceService.getNextPedidoId();
        Pedido pedido = new Pedido(pedidoId, setorSolicitante, Auxiliar.SETOR_CADASTRO, nomeProduto, 0);
        
        // Salva o pedido uma única vez
        PersistenceService.salvarPedido(pedido);
        return true;
    }
}