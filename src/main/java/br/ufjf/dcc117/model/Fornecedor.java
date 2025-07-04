package br.ufjf.dcc117.model;

public class Fornecedor {

    // << Atributos >>

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
