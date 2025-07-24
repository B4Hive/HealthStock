package br.ufjf.dcc117.model.estoque;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import br.ufjf.dcc117.model.Auxiliar;

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

    public static Estoque carregar(String setor) {
        File arquivo = new File(Auxiliar.path(setor,"estoque","csv"));
        Auxiliar.checkFile(arquivo);
        List<Produto> produtos = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            reader.readLine(); // Pular cabeçalho, se houver
            while ((linha = reader.readLine()) != null) {
                Produto produto = Produto.carregar(linha);
                if (produto != null) {
                    produtos.add(produto);
                }
            }
        } catch (IOException e) {
            Auxiliar.error("Estoque.carregar: Erro ao carregar estoque. Mensagem de erro: " + e.getMessage());
            Estoque estoque = new Estoque();
            estoque.salvar(setor); // Tenta salvar o estoque vazio
            Estoque.carregar(setor);
        }
        return new Estoque(produtos);
    }

    public void salvar(String setor) {
        File arquivo = new File(Auxiliar.path(setor,"estoque","csv"));
        Auxiliar.checkFile(arquivo);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            writer.write("ID,Nome,Quantidade,IDFornecedor,Tipo,Lote,Validade,UltimoResponsavel,DataUltimoResponsavel\n");
            for (Produto produto : this.produtos) {
                writer.write(produto.salvar());
                Auxiliar.error("Estoque.salvar: Setor: " + setor + ", Produto: " + produto.salvar());
                writer.newLine();
            }
        } catch (IOException e) {
            Auxiliar.error("Estoque.salvar: Erro ao salvar estoque. Mensagem de erro: " + e.getMessage());
            System.exit(1); // Encerra o programa em caso de erro crítico
        }
    }

    // << Métodos >>
    
    public void adicionarProduto(Produto produto) {
        if (produto == null) {
            return;
        }
        for (Produto p : this.produtos) {
            if (p.getID() == produto.getID()) {
                int quantidade = produto.getQuantidade() + p.getQuantidade();
                p.setQuantidade(quantidade);
                return;
            }
        }
        produtos.add(produto);
    }

    public Produto retirarProduto(int id, int quantidade) {
        if (quantidade < 0) {
            Auxiliar.error("Estoque.retirarProduto: Quantidade inválida para retirada: " + quantidade);
            return null; // Retorna null se a quantidade for inválida
        }
        for (Produto p : this.produtos) {
            if (p.getID() == id) {
                if (p.getQuantidade() >= quantidade) {
                    p.setQuantidade(p.getQuantidade() - quantidade);
                    return p.clone(quantidade);
                } else if (p.getQuantidade() > 0) {
                    quantidade = p.getQuantidade();
                    p.setQuantidade(0);
                    return p.clone(quantidade);
                } else {
                    return p.clone(0);
                }
            }
        }
        return null; // Produto não encontrado
    }

    public Map<Integer, String> listarProdutos() {
        return produtos.stream().collect(Collectors.toMap(Produto::getID, Produto::getNome));
    }

    public Produto getProduto(int id) {
        for (Produto p : this.produtos) {
            if (p.getID() == id) {
                return p;
            }
        }
        return null;
    }
    
    public List<Produto> getProdutos() {
        return this.produtos;
    }

}
