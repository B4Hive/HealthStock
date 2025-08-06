package br.ufjf.dcc117.model.estoque;

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

    public static Fornecedor carregar(String fornecedor) {
        String[] partes = fornecedor.split(",");
        if (partes.length < 6) {
            return null; // Linha inválida
        }
        int id = Integer.parseInt(partes[0].trim());
        String nome = partes[1].trim();
        String cnpj = partes[2].trim();
        String telefone = partes[3].trim();
        String endereco = partes[4].trim();
        String email = partes[5].trim();

        return new Fornecedor(id, nome, cnpj, telefone, endereco, email);
    }

    public String salvar() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getId()).append(",");
        sb.append(this.getNome()).append(",");
        sb.append(this.getCnpj()).append(",");
        sb.append(this.getTelefone()).append(",");
        sb.append(this.getEndereco()).append(",");
        sb.append(this.getEmail());
        return sb.toString();
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEndereco() { return endereco; }

    public void atualizar(String nome, String cnpj, String telefone, String email, String endereco) {
        this.nome = nome;
        this.cnpj = cnpj;
        this.telefone = telefone;
        this.email = email;
        this.endereco = endereco;
    }

    public String toCSV() {
        return String.format("%d,%s,%s,%s,%s,%s",
                this.id, this.nome, this.cnpj, this.telefone, this.email, this.endereco);
    }
}
