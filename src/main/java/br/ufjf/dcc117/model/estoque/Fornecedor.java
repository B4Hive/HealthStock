package br.ufjf.dcc117.model.estoque;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Fornecedor {

    // << Atributos >>

    private static final String PATH = "src\\main\\resources\\compras\\fornecedores.csv";

    private final int id;
    private String nome;
    private String cnpj;
    private String telefone;
    private String endereco;
    private String email;

    // << Construtor >>

    public Fornecedor(int id, String nome, String cnpj, String telefone, String endereco, String email) {
        this.id = id;
        this.nome = nome;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.endereco = endereco;
        this.email = email;
    }

    public static Fornecedor carregar(int id) {
        File arquivo = new File(PATH);
        if (!arquivo.exists()) return null;

        List<String> linhas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            reader.readLine();
            while ((linha = reader.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            return null;
        }

        for (String linha : linhas) {
            String[] partes = linha.split(",");
            if (partes.length < 6) continue;
            int fornecedorId = Integer.parseInt(partes[0].trim());
            if (fornecedorId == id) {
                return new Fornecedor(
                    fornecedorId,
                    partes[1].trim(),
                    partes[2].trim(),
                    partes[3].trim(),
                    partes[4].trim(),
                    partes[5].trim()
                );
            }
        }

        return null;
    }
    
    public static void salvar(Fornecedor fornecedor) {
        File arquivo = new File(PATH);

        List<String> linhas = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            reader.readLine();
            while ((linha = reader.readLine()) != null) {
                linhas.add(linha);
            }
        } catch (IOException e) {
            linhas.add("ID,Nome,CNPJ,Telefone,Endereco,Email");
        }

        boolean encontrado = false;
        for (String linha : linhas) {
            String[] partes = linha.split(",");
            if (partes.length < 6) continue;
            int fornecedorId = Integer.parseInt(partes[0].trim());
            if (fornecedorId == fornecedor.getId()) {
                linha = fornecedor.getId() + "," +
                        fornecedor.getNome() + "," +
                        fornecedor.getCnpj() + "," +
                        fornecedor.getTelefone() + "," +
                        fornecedor.getEndereco() + "," +
                        fornecedor.getEmail();
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            linhas.add(fornecedor.getId() + "," +
                       fornecedor.getNome() + "," +
                       fornecedor.getCnpj() + "," +
                       fornecedor.getTelefone() + "," +
                       fornecedor.getEndereco() + "," +
                       fornecedor.getEmail());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(arquivo))) {
            writer.write("ID,Nome,CNPJ,Telefone,Endereco,Email");
            for (String linha : linhas) {
                writer.write(linha);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // << Getters e Setters >>

    public int getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCnpj() {
        return this.cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getTelefone() {
        return this.telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEndereco() {
        return this.endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
