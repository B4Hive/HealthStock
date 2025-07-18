package br.ufjf.dcc117.controller;

import java.util.List;

import br.ufjf.dcc117.model.estoque.Medicacao;
import br.ufjf.dcc117.model.estoque.Produto;
import br.ufjf.dcc117.model.setor.Setor;
import br.ufjf.dcc117.model.setor.SetorCadastro;
import br.ufjf.dcc117.model.setor.SetorEntrada;

public class Control {
    
    private static Setor setor;

    public static boolean login(String nome, String senha) {
        setor = Setor.carregar(nome);
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

}
