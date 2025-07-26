package br.ufjf.dcc117.controller;

import java.util.List;

import br.ufjf.dcc117.model.Auxiliar;
import br.ufjf.dcc117.model.PersistenceService;
import br.ufjf.dcc117.model.estoque.Medicacao;
import br.ufjf.dcc117.model.estoque.Produto;
import br.ufjf.dcc117.model.setor.SetorCadastro;

public class EstoqueControl extends Control {

    public static String[] getListaProdutos() {
        List<Produto> produtos = setor.getProdutos();
        produtos.sort((p1, p2) -> Integer.compare(p2.getQuantidade(), p1.getQuantidade()));

        String[] lista = new String[produtos.size()];
        for (int i = 0; i < produtos.size(); i++) {
            Produto p = produtos.get(i);
            if (p == null)
                continue;
            if (p instanceof Medicacao m) {
                lista[i] = "ID: " + p.getID() + " - " + p.getNome() + " - Quantidade: " + p.getQuantidade() + " - Validade: "
                        + m.getValidade();
            } else {
                lista[i] = "ID: " + p.getID() + " - " + p.getNome() + " - Quantidade: " + p.getQuantidade();
            }
        }
        return lista;
    }

    public static String[] getProduto(int produtoId) {
        if (setor == null)
            return null;
        List<Produto> produtos = setor.getProdutos();
        String[] produto = null;
        for (Produto p : produtos) {
            if (p == null)
                continue;
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

    public static void consumirProduto(int produtoId, int quantidade) {
        if (setor == null)
            return;
        setor.consumirProduto(produtoId, quantidade);
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

    public static String[] listarProdutosCadastrados() {
        return PersistenceService.carregarProdutos(p -> p.getSetor().equalsIgnoreCase(Auxiliar.SETOR_CADASTRO)).stream()
                .map(produto -> produto.getID() + " - " + produto.getNome())
                .toArray(String[]::new);
    }

    public static boolean produtoPrecisaDeDetalhes(String produtoNome) {
        List<Produto> produtosModelo = PersistenceService.carregarProdutos(p -> p.getSetor().equalsIgnoreCase(Auxiliar.SETOR_CADASTRO));
        for (Produto p : produtosModelo) {
            if (p.getNome().equalsIgnoreCase(produtoNome)) {
                return p instanceof Medicacao;
            }
        }
        return false;
    }

    public static boolean cadastroProduto(String nome, int fornecedorId, String tipoProduto) {
        if (setor instanceof SetorCadastro s) {
            if (s.getFornecedor(fornecedorId) == null) {
                Auxiliar.error("Control.cadastroProduto: Fornecedor não encontrado: " + fornecedorId);
                return false;
            }
            s.cadastroProduto(nome, fornecedorId, tipoProduto);
            return true;
        } else {
            Auxiliar.error("Control.cadastroProduto: Setor não tem permissão para cadastrar produtos.");
        }
        return false;
    }
}