package br.ufjf.dcc117.model.estoque;

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

    public static Produto carregar(String produto) {
        String[] partes = produto.split(",");
        if (partes.length != 9) return null;
        if (partes[4].trim().equals("Medicacao")) return Medicacao.carregar(produto);

        try {
            int id = Integer.parseInt(partes[0].trim());
            String nome = partes[1].trim();
            int quantidade = Integer.parseInt(partes[2].trim());
            int idFornecedor = Integer.parseInt(partes[3].trim());
            return new Produto(id, nome, quantidade, idFornecedor);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static void salvar(Produto produto) {
        //TODO: Implementar a lógica para salvar o produto no arquivo CSV
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
