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
import br.ufjf.dcc117.model.PersistenceService;
import br.ufjf.dcc117.model.estoque.Fornecedor;
import br.ufjf.dcc117.model.estoque.Medicacao;
import br.ufjf.dcc117.model.estoque.Produto;

public class SetorCadastro extends Setor{

    public SetorCadastro(String nome) {
        super(nome);
    }

    public void cadastroFornecedor(String nome, String cnpj, String telefone, String endereco, String email) {
        File file = new File("src/main/resources/fornecedores.csv");
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
            Auxiliar.error("SetorCadastro.salvarFornecedores: Erro ao salvar fornecedores: " + file.getAbsolutePath() + ". Mensagem de erro: " + e.getMessage());
            System.exit(1);
        }
    }

    public List<Fornecedor> carregarFornecedores(File file) {
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
            Auxiliar.error("SetorCadastro.carregarFornecedores: Erro ao carregar fornecedores: " + file.getAbsolutePath() + ". Mensagem de erro: " + e.getMessage());
            return fornecedores; // Retorna lista vazia em caso de erro
        }
        return fornecedores;
    }

    public List<Fornecedor> getFornecedores() {
        File file = new File("src/main/resources/fornecedores.csv");
        Auxiliar.checkFile(file);
        return carregarFornecedores(file);
    }

    public Fornecedor getFornecedor(int id) {
        for (Fornecedor fornecedor : getFornecedores()) {
            if (fornecedor.getId() == id) {
                return fornecedor;
            }
        }
        return null; // Retorna null se não encontrar o fornecedor
    }

    public void cadastroProduto(String nome, int fornecedorId, String tipoProduto) {
        List<Produto> todosProdutos = PersistenceService.carregarProdutos(p -> true);
        int novoId = todosProdutos.stream().mapToInt(Produto::getID).max().orElse(0) + 1;

        Produto novoProduto;
        if ("Medicacao".equalsIgnoreCase(tipoProduto)) {
            novoProduto = new Medicacao(novoId, nome, 0, fornecedorId, this.getNome(), null, null, null, null);
        } else {
            novoProduto = new Produto(novoId, nome, 0, fornecedorId, this.getNome());
        }
        PersistenceService.salvarProduto(novoProduto);
    }

    @Override
    public Produto retiradaProduto(int id, int quantidade) {
        Produto produtoModelo = getProduto(id);
        if (produtoModelo != null) {
            return produtoModelo.clone(quantidade);
        }
        return null;
    }

}
