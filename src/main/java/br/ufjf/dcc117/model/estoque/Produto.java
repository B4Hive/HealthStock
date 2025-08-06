package br.ufjf.dcc117.model.estoque;

import java.util.Objects;

public class Produto {
    private final int ID; // ID único da instância/lote
    private final int codigoProduto; // ID para agrupar produtos do mesmo tipo
    private String nome;
    private int quantidade;
    private int idFornecedor;
    private final String setor;

    public Produto(int ID, int codigoProduto, String nome, int quantidade, int idFornecedor, String setor) {
        this.ID = ID;
        this.codigoProduto = codigoProduto;
        this.nome = nome;
        this.quantidade = quantidade;
        this.idFornecedor = idFornecedor;
        this.setor = setor;
    }

    // Getters
    public int getID() { return ID; }
    public int getCodigoProduto() { return codigoProduto; }
    public String getNome() { return nome; }
    public int getQuantidade() { return quantidade; }
    public int getIdFornecedor() { return idFornecedor; }
    public String getSetor() { return setor; }

    // Setters
    public void setQuantidade(int quantidade) { this.quantidade = quantidade; }

    public void atualizar(String nome, int idFornecedor) {
        this.nome = nome;
        this.idFornecedor = idFornecedor;
    }

    public String toCSV() {
        // CORREÇÃO: Adiciona placeholders para manter a consistência das colunas no CSV.
        // Isso garante que toda linha no produtos.csv tenha o mesmo número de colunas.
        return String.format("%d,%d,%s,%d,%d,%s,%s,NULL,NULL,NULL,NULL",
                this.ID,
                this.codigoProduto,
                this.nome,
                this.quantidade,
                this.idFornecedor,
                this.setor,
                "Produto" // tipo
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Produto produto = (Produto) o;
        return ID == produto.ID; // A igualdade agora é definida pelo ID da instância
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID); // O hash code também é baseado no ID da instância
    }
}
