package br.ufjf.dcc117.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Estoque {

    // << Atributos >>

    private final List<Produto> produtos;

    // << Construtor >>

    public Estoque(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public Estoque() {
        this.produtos = new ArrayList<>();
    }

    // << Métodos >>
    
    public void adicionarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Produto não pode ser nulo"); // TODO: Lidar com exceção de forma mais adequada
        }
        for (Produto p : this.produtos) {
            if (p.getId() == produto.getId()) {
                p.setQuantidade(p.getQuantidade() + produto.getQuantidade());
                return;
            }
        }
        produtos.add(produto);
    }

    public Produto retirarProduto(int id, int quantidade) {
        for (Produto p : this.produtos) {
            if (p.getId() == id) {
                if (p.getQuantidade() >= quantidade) {
                    p.setQuantidade(p.getQuantidade() - quantidade);
                    // TODO: Se a quantidade do produto for 0, remove o produto da lista?
                    return new Produto(p.getId(), p.getNome(), quantidade, p.getIdFornecedor());
                } else if (p.getQuantidade() > 0) {
                    quantidade = p.getQuantidade();
                    p.setQuantidade(0);
                    // TODO: Retorna o produto com a quantidade disponível
                    return new Produto(p.getId(), p.getNome(), quantidade, p.getIdFornecedor());
                }
            }
        }
        return null; // TODO: Retorna null se o produto não for encontrado
    }

    public Map<Integer, String> listarProdutos() {
        return produtos.stream().collect(Collectors.toMap(Produto::getId, Produto::getNome));
    }

    public Produto getProduto(int id) {
        for (Produto p : this.produtos) {
            if (p.getId() == id) {
                return p;
            }
        }
        return null; // TODO: Retorna null se o produto não for encontrado
    }
    
}
