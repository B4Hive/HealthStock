package br.ufjf.dcc117.controller;

import java.util.List;

import br.ufjf.dcc117.model.Auxiliar;
import br.ufjf.dcc117.model.estoque.Medicacao;
import br.ufjf.dcc117.model.estoque.Produto;
import br.ufjf.dcc117.model.setor.Pedido;
import br.ufjf.dcc117.model.setor.Setor;

public class PedidosControl extends Control {

    public static String[] getListaPedidos() {
        List<Pedido> pedidos = setor.listarPedidos();
        String[] lista = new String[pedidos.size()];
        for (int i = 0; i < pedidos.size(); i++) {
            lista[i] = pedidos.get(i).getProduto() + " - " + pedidos.get(i).getEstado();
        }
        return lista;
    }

    public static String[] getPedido(int choice) {
        List<Pedido> pedidos = setor.listarPedidos();
        if (choice < 0 || choice >= pedidos.size())
            return null;
        Pedido pedido = pedidos.get(choice);
        return new String[] {
                pedido.getSetorSolicitante(),
                pedido.getSetorResponsavel(),
                Auxiliar.SDF.format(pedido.getDataPedido()),
                pedido.getProduto(),
                String.valueOf(pedido.getQuantidade()),
                pedido.getEstado(),
                pedido.getDetalhes() != null ? pedido.getDetalhes() : ""
        };
    }

    public static boolean respostaPedido(int choice, boolean aprovado, String responsavel, String detalhes) {
        List<Pedido> pedidos = setor.listarPedidos();
        if (choice < 0 || choice >= pedidos.size()) {
            Auxiliar.error("Control.respostaPedido: Pedido não encontrado.");
            return false;
        }
        Pedido pedido = pedidos.get(choice);
        if (pedido.getSetorResponsavel().equalsIgnoreCase(setor.getNome())) {
            Setor setorSolicitante = Setor.carregar(pedido.getSetorSolicitante());
            List<Pedido> pedidosSolicitante = setorSolicitante.listarPedidos();
            if (aprovado) {
                if (pedido.getSetorResponsavel().equalsIgnoreCase(Auxiliar.SETOR_CADASTRO)) {
                    moverProduto(getProdutoId(pedido.getProduto()), pedido.getQuantidade(), Auxiliar.SETOR_ENTRADA, responsavel, detalhes);
                } else {
                    moverProduto(getProdutoId(pedido.getProduto()), pedido.getQuantidade(), pedido.getSetorSolicitante(), responsavel, detalhes);
                }
                
                for (Pedido p : pedidosSolicitante) {
                    if (p.compare(pedido)) {
                        setorSolicitante.aprovarPedido(p, true);
                        setor.aprovarPedido(pedido, true);
                        return true;
                    }
                }
            } else {
                for (Pedido p : pedidosSolicitante) {
                    if (p.compare(pedido)) {
                        setorSolicitante.aprovarPedido(p, false);
                        setor.aprovarPedido(pedido, false);
                        return true;
                    }
                }
            }
        } else {
            Auxiliar.error("Control.respostaPedido: Sem permissão para responder a este pedido.");
            return false;
        }
        Auxiliar.error("Control.respostaPedido: Pedido não encontrado ou não pertence ao setor atual.");
        return false;
    }

    private static int getProdutoId(String produtoNome) {
        List<Produto> produtos = Setor.carregar(Auxiliar.SETOR_CADASTRO).getProdutos();
        for (Produto p : produtos) {
            if (p.getNome().equalsIgnoreCase(produtoNome)) {
                return p.getID();
            }
        }
        return -1; // Produto não encontrado
    }

    private static void moverProduto(int produtoId, int quantidade, String setorSolicitante, String responsavel, String detalhes) {
        Setor setorSolicitanteObj = Setor.carregar(setorSolicitante);
        if (setorSolicitanteObj == null) {
            Auxiliar.error("Control.moverProduto: Setor de solicitante não encontrado: " + setorSolicitante);
            return;
        }
        Produto produto = setor.retiradaProduto(produtoId, quantidade);
        if (produto == null) {
            Auxiliar.error("Control.moverProduto: Produto não encontrado: " + produtoId);
            return;
        }
        if (produto instanceof Medicacao m) {
            // TODO: if (m.verificarValidade()) {} // impedir movimentação de medicamentos vencidos
            m.atualizarDetalhes(detalhes);
            m.atualizarResponsavel(responsavel);
        }
        setorSolicitanteObj.entradaProduto(produto);
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
                    setorResponsavel = Auxiliar.SETOR_ENTRADA;
                    // Se for produto comum, o responsável é o almoxarifado
                }
                if (setorResponsavel.equals(setorSolicitante)) {
                    setorResponsavel = Auxiliar.SETOR_CADASTRO;
                    // Se o setor solicitante for o mesmo do responsável, direciona para o setor de cadastro
                }
                Pedido pedido = new Pedido(setorSolicitante, setorResponsavel, nomeProduto, quantidade);

                Setor.carregar(setorResponsavel).adicionarPedido(pedido);
                Setor.carregar(setorSolicitante).adicionarPedido(pedido);
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
        Pedido pedido = new Pedido(setorSolicitante, Auxiliar.SETOR_CADASTRO, nomeProduto, 0);
        Setor.carregar(Auxiliar.SETOR_CADASTRO).adicionarPedido(pedido);
        setor.adicionarPedido(pedido);
        return true;
    }
}