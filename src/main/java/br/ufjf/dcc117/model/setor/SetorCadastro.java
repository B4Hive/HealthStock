package br.ufjf.dcc117.model.setor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.ufjf.dcc117.model.estoque.Fornecedor;

public class SetorCadastro extends Setor {
    private final List<Fornecedor> fornecedores;

    public SetorCadastro(String nome) {
        super(nome);
        this.fornecedores = carregarFornecedores();
    }

    public List<Fornecedor> getFornecedores() {
        return this.fornecedores;
    }

    public Fornecedor getFornecedor(int id){
        return this.fornecedores.stream().filter(f -> f.getId() == id).findFirst().orElse(null);
    }

    public void salvarFornecedores() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter("src/main/resources/fornecedores.csv"))) {
            bw.write("id,nome,cnpj,telefone,email,endereco");
            bw.newLine();
            for (Fornecedor f : this.fornecedores) {
                bw.write(f.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
        }
    }

    private List<Fornecedor> carregarFornecedores() {
        List<Fornecedor> fornecedoresCarregados = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/fornecedores.csv"))) {
            br.readLine(); // Pula cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] dados = linha.split(",");
                fornecedoresCarregados.add(new Fornecedor(
                        Integer.parseInt(dados[0]), dados[1], dados[2], dados[3], dados[4], dados[5]
                ));
            }
        } catch (IOException e) {
        }
        return fornecedoresCarregados;
    }
}
