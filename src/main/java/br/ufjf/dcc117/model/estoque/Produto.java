package br.ufjf.dcc117.model.estoque;

public class Produto {

    // << Atributos >>

    private final int id;
    private String nome;
    private int quantidade;
    private int idFornecedor;
    private String setor; // Novo campo

    // << Construtor >>

    public Produto(int id, String nome, int quantidade, int idFornecedor, String setor) {
        this.id = id;
        this.nome = nome;
        this.quantidade = quantidade;
        this.idFornecedor = idFornecedor;
        this.setor = setor; // Novo campo
    }

    // O método carregar foi movido para PersistenceService
    // public static Produto carregar(String produto) { ... }

    // O método salvar foi substituído por toCSV
    // public String salvar() { ... }

    // << Getters e Setters >>

    public int getID() {
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

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public String toCSV() {
        return String.format("%d,%s,%d,%d,%s,Produto,NULL,NULL,NULL,NULL",
                this.id, this.nome, this.quantidade, this.idFornecedor, this.setor);
    }

    public static Produto fromCSV(String[] values) {
        int id = Integer.parseInt(values[0]);
        String nome = values[1];
        int quantidade = Integer.parseInt(values[2]);
        int idFornecedor = Integer.parseInt(values[3]);
        String setor = values[4];
        return new Produto(id, nome, quantidade, idFornecedor, setor);
    }

    public Produto clone(int novaQuantidade) {
        return new Produto(this.id, this.nome, novaQuantidade, this.idFornecedor, this.setor);
    }
}
