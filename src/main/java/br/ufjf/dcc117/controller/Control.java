package br.ufjf.dcc117.controller;

import java.util.List;

import br.ufjf.dcc117.model.Auxiliar;
import br.ufjf.dcc117.model.estoque.Medicacao;
import br.ufjf.dcc117.model.estoque.Produto;
import br.ufjf.dcc117.model.setor.Pedido;
import br.ufjf.dcc117.model.setor.Setor;
import br.ufjf.dcc117.model.setor.SetorCadastro;
import br.ufjf.dcc117.model.setor.SetorEntrada;

public class Control {
    
    private static Setor setor;

    public static boolean login(String nome, String senha) {
        setor = Setor.carregar(nome);
        if (setor == null) {
            Auxiliar.error("Setor não encontrado: " + nome);
            return false;
        }
        return setor != null && setor.validarSenha(senha);
    }

    public static void logout() {
        setor.salvar();
        setor = null;
    }

    public static String[] getHomeOptions() {
        if (setor instanceof Setor || setor instanceof SetorEntrada) {
            return new String[] {
                "Estoque",
                "Pedidos",
                null
            };
        } else if (setor instanceof SetorCadastro) {
            return new String[] {
                "Produtos",
                "Pedidos",
                "Fornecedores"
            };
        }
        return new String[] {
            "Error: Unknown Setor Type"
        };
    }

    public static String[] getListaProdutos() {
        List<Produto> produtos = setor.getProdutos();
        produtos.sort((p1, p2) -> Integer.compare(p2.getQuantidade(), p1.getQuantidade()));

        String[] lista = new String[produtos.size()];
        for (int i = 0; i < produtos.size(); i++) {
            Produto p = produtos.get(i);
            if (p == null) continue;
            if (p instanceof Medicacao m) {
                lista[i] = p.getID() + " - " + p.getNome() + " - Quantidade: " + p.getQuantidade() + " - Validade: " + m.getValidade();
            } else {
                lista[i] = p.getID() + " - " + p.getNome() + " - Quantidade: " + p.getQuantidade();
            }
        }
        return lista;
    }

    public static String[] getProduto(int produtoId) {
        if (setor == null) return null;
        List<Produto> produtos = setor.getProdutos();
        String[] produto = null;
        for (Produto p : produtos){
            if (p == null) continue;
            if (p.getID() == produtoId) {
                if (p instanceof Medicacao m) {
                    produto = new String[] { 
                        String.valueOf(p.getID()), 
                        p.getNome(), 
                        String.valueOf(p.getQuantidade()),
                        String.valueOf(p.getIdFornecedor()),
                        m.getLote(), 
                        m.getValidade() != null ? m.getValidade().toString() : "",
                        m.getUltimoResponsavel(),
                        m.getDataUltimoResponsavel() != null ? m.getDataUltimoResponsavel().toString() : ""
                    };
                } else {
                    produto = new String[] { 
                        String.valueOf(p.getID()), 
                        p.getNome(), 
                        String.valueOf(p.getQuantidade()), 
                        String.valueOf(p.getIdFornecedor())
                    };
                }
                break;
            }
                
        }
        return produto;
    }

    public static String[] getProdutoOptions(){
        if (setor instanceof SetorCadastro) {
            return new String[] {
                null,
                "Editar Produto",
                null
            };
        } else if (setor instanceof Setor || setor instanceof SetorEntrada) {
            return new String[] {
                "Consumir Produto",
                null,
                "Gerar Pedido"
            };
        }
        return new String[] {
            "Error: Unknown Setor Type"
        };
    }

    public static void consumirProduto(int produtoId, int quantidade) {
        if (setor == null) return;
        setor.consumirProduto(produtoId, quantidade);
    }

    public static String[] getListaPedidos() {
        List<Pedido> pedidos = setor.listarPedidos();
        String[] lista = new String[pedidos.size()];
        for (int i = 0; i < pedidos.size(); i++) {
            lista[i] = pedidos.get(i).toString();
        }
        return lista;
    }

    public static String[] getPedido(int choice) {
        List<Pedido> pedidos = setor.listarPedidos();
        if (choice < 0 || choice >= pedidos.size()) return null;
        Pedido pedido = pedidos.get(choice);
        return new String[] {
            pedido.getSetorSolicitante(),
            pedido.getSetorResponsavel(),
            Auxiliar.SDF.format(pedido.getDataPedido()),
            pedido.getProduto(),
            String.valueOf(pedido.getQuantidade()),
            pedido.getEstado()
        };
    }

    public static boolean respostaPedido(int choice, boolean aprovado, String responsavel) {
        List<Pedido> pedidos = setor.listarPedidos();
        if (choice < 0 || choice >= pedidos.size()) {
            Auxiliar.error("Pedido não encontrado.");
            return false;
        }
        Pedido pedido = pedidos.get(choice);
        if (pedido.getSetorResponsavel().equals(setor.getNome())) {
            Setor setorSolicitante = Setor.carregar(pedido.getSetorSolicitante());
            List<Pedido> pedidosSolicitante = setorSolicitante.listarPedidos();
            if (aprovado) {
                moverProduto(getProdutoId(pedido.getProduto()), pedido.getQuantidade(), pedido.getSetorSolicitante(), responsavel);
                for (Pedido p : pedidosSolicitante) {
                    if (p.compare(pedido)){
                        setorSolicitante.aprovarPedido(p, true);
                        setor.aprovarPedido(pedido, true);
                        return true;
                    }
                }
            } else {
                for (Pedido p : pedidosSolicitante) {
                    if (p.compare(pedido)){
                        setorSolicitante.aprovarPedido(p, false);
                        setor.aprovarPedido(pedido, false);
                        return true;
                    }
                }
            }
        } else {
            Auxiliar.error("Sem permissão para responder a este pedido.");
            return false;
        }
        Auxiliar.error("Control.respostaPedido: Pedido não encontrado ou não pertence ao setor atual.");
        return false;
    }

    private static int getProdutoId(String produtoNome) {
        List<Produto> produtos = setor.getProdutos();
        for (Produto p : produtos) {
            if (p.getNome().equalsIgnoreCase(produtoNome)) {
                return p.getID();
            }
        }
        return -1; // Produto não encontrado
    }

    private static void moverProduto(int produtoId, int quantidade, String setorSolicitante, String responsavel) {
        Setor setorSolicitanteObj = Setor.carregar(setorSolicitante);
        if (setorSolicitanteObj == null) {
            Auxiliar.error("Setor de solicitante não encontrado: " + setorSolicitante);
            return;
        }
        Produto produto = setor.retiradaProduto(produtoId, quantidade);
        if (produto == null) {
            Auxiliar.error("Control.moverProduto: Produto não encontrado: " + produtoId);
            return;
        }
        if (produto instanceof Medicacao m) {
            m.atualizarResponsavel(responsavel);
        }
        setorSolicitanteObj.entradaProduto(produto);
    }

    public static boolean setorCadastro() {
        return setor instanceof SetorCadastro;
    }

    public static boolean verificarProduto(String produtoNome) {
        List<Produto> produtos = setor.getProdutos();
        for (Produto p : produtos) {
            if (p.getNome().equalsIgnoreCase(produtoNome)) {
                return true;
            }
        }
        return false;
    }

    public static Object getSetor() {
        return setor != null ? setor.getNome() : null;
    }

}
