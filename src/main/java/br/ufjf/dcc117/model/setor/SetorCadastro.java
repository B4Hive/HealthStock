package br.ufjf.dcc117.model.setor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufjf.dcc117.model.Auxiliar;
import br.ufjf.dcc117.model.estoque.Estoque;
import br.ufjf.dcc117.model.estoque.Fornecedor;
import br.ufjf.dcc117.model.estoque.Medicacao;
import br.ufjf.dcc117.model.estoque.Produto;

public class SetorCadastro extends Setor{

    public SetorCadastro(String nome, String senha, List<Pedido> pedidos, Estoque estoque) {
        super(nome, senha, pedidos, estoque);
    }

    public void cadastroFornecedor(String nome, String cnpj, String telefone, String endereco, String email) {
        File file = new File(Auxiliar.path(Auxiliar.SETOR_CADASTRO, "fornecedores", "csv"));
        Auxiliar.checkFile(file);

        List<Fornecedor> fornecedores = carregarFornecedores(file);
        
        int id = fornecedores.size() + 1; // Simples incremento para ID
        Fornecedor novoFornecedor = new Fornecedor(id, nome, cnpj, telefone, endereco, email);

        fornecedores.add(novoFornecedor);

        salvarFornecedores(file, fornecedores);
    }

    private void salvarFornecedores(File file, List<Fornecedor> fornecedores) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("ID,Nome,CNPJ,Telefone,Endereco,Email\n");
            for (Fornecedor fornecedor : fornecedores) {
                writer.write(fornecedor.salvar());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar fornecedores: " + file.getAbsolutePath());
            System.err.println("Mensagem de erro: " + e.getMessage());
            System.exit(1);
        }
    }

    private List<Fornecedor> carregarFornecedores(File file) {
        List<Fornecedor> fornecedores = new ArrayList<>();
        try(BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            reader.readLine(); // Pular cabeçalho, se houver
            while ((line = reader.readLine()) != null) {
                Fornecedor fornecedor = Fornecedor.carregar(line);
                if (fornecedor != null) {
                    fornecedores.add(fornecedor);
                }
            }
        } catch (Exception e) {
            System.err.println("Erro ao carregar fornecedores: " + file.getAbsolutePath());
            return fornecedores; // Retorna lista vazia em caso de erro
        }
        return fornecedores;
    }

    public void cadastroProduto(String nome, String tipo) {
        //TODO: Implementar lógica para cadastrar produto
        File file = new File(Auxiliar.path(Auxiliar.SETOR_CADASTRO, "produtos", "csv"));
        Auxiliar.checkFile(file);
        Estoque estoque = this.getEstoque();
        List<Produto> produtos = estoque.getProdutos();
        int id = produtos.size() + 1; // Simples incremento para ID
        Produto novoProduto;
        if (tipo.equals("Medicacao")) {
            novoProduto = new Medicacao(id, nome, 0, 0, "", null, "", null);
        } else {
            novoProduto = new Produto(id, nome, 0, 0);
        }
        produtos.add(novoProduto);
        salvarProdutos(file, produtos);
    }

    private void salvarProdutos(File file, List<Produto> produtos) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write("ID,Nome,Quantidade,FornecedorID,Lote,Validade,UltimoResponsavel,UltimaMovimentacao\n");
            for (Produto produto : produtos) {
                writer.write(produto.salvar());
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar produtos: " + file.getAbsolutePath());
            System.err.println("Mensagem de erro: " + e.getMessage());
            System.exit(1);
        }
    }

}
