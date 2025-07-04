package br.ufjf.dcc117.model;

public class Produto {

    // << Atributos >>

    private final int id;
    private String nome;
    private int quantidade;
    private int idFornecedor;

    // << Construtor >>

    public Produto(int id, String nome, int quantidade, int idFornecedor) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.idFornecedor = idFornecedor;
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

    public int getQuantidade() {
        return this.quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public int getIdFornecedor() {
        return this.idFornecedor;
    }

    public void setIdFornecedor(int idFornecedor) {
        this.idFornecedor = idFornecedor;
    }

}
