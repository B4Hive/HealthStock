package br.ufjf.dcc117.model.estoque;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Estoque {

    // << Atributos >>

    private final List<Produto> produtos;
    private static final String PATH = "src\\main\\resources\\";

    // << Construtor >>

    public Estoque(List<Produto> produtos) {
        this.produtos = produtos;
    }

    public Estoque() {
        this.produtos = new ArrayList<>();
    }

    public static Estoque carregar(String setor) {
        File arquivo = new File(PATH + setor + "\\estoque.csv");
        if (!arquivo.exists()) {
            return null;
        }
        List<Produto> produtos = new ArrayList<>();
        try (java.io.BufferedReader br = new java.io.BufferedReader(new java.io.FileReader(arquivo))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                produtos.add(Produto.carregar(linha));
            }
        } catch (java.io.IOException e) {
            return null;
        }
        if (!produtos.isEmpty()) {
            return new Estoque(produtos);
        }
        return null;
    }

    public static void salvarEstoque(String setor, Estoque estoque) {
        File arquivo = new File(PATH + setor + "\\estoque.csv");
        try (java.io.BufferedWriter writer = new java.io.BufferedWriter(new java.io.FileWriter(arquivo))) {
            for (Produto produto : estoque.produtos) {
                writer.write(produto.toString());
                writer.newLine();
            }
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        
    }

    // << Métodos >>
    
    public void adicionarProduto(Produto produto) {
        if (produto == null) {
            return;
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
                    return new Produto(p.getId(), p.getNome(), quantidade, p.getIdFornecedor());
                } else if (p.getQuantidade() > 0) {
                    quantidade = p.getQuantidade();
                    p.setQuantidade(0);
                    return new Produto(p.getId(), p.getNome(), quantidade, p.getIdFornecedor());
                }
            }
        }
        return null;
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
        return null;
    }
    
}
