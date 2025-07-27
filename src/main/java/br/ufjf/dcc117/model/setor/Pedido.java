package br.ufjf.dcc117.model.setor;

import java.text.ParseException;
import java.util.Date;

import br.ufjf.dcc117.model.Auxiliar;

public class Pedido {

    // << Atributos >>

    private final int id;
    private final String setorSolicitante;
    private final String setorResponsavel;
    private final Date dataPedido;
    private final String produto;
    private final int quantidade;
    private String estado;
    private String detalhes;

    // << Construtores >>

    public Pedido(int id, String setorSolicitante, String setorResponsavel, String produto, int quantidade) {
        this.id = id;
        this.setorSolicitante = setorSolicitante;
        this.setorResponsavel = setorResponsavel;
        this.dataPedido = new Date();
        this.produto = produto;
        this.quantidade = quantidade;
        this.estado = "Pendente";
        this.detalhes = "";
    }

    public Pedido(int id, String setorSolicitante, String setorResponsavel, Date dataPedido, String produto, int quantidade, String estado, String detalhes) {
        this.id = id;
        this.setorSolicitante = setorSolicitante;
        this.setorResponsavel = setorResponsavel;
        this.dataPedido = dataPedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.estado = estado;
        this.detalhes = detalhes;
    }

    // << Persistência >>

    public static Pedido carregar(String linhaCsv) {
        String[] partes = linhaCsv.split(",");
        if (partes.length < 8) { // Deve ter pelo menos 8 colunas
            return null;
        }
        try {
            int id = Integer.parseInt(partes[0].trim());
            String setorSolicitante = partes[1].trim();
            String setorResponsavel = partes[2].trim();
            Date dataPedido = Auxiliar.SDF.parse(partes[3].trim());
            String produto = partes[4].trim();
            int quantidade = Integer.parseInt(partes[5].trim());
            String estado = partes[6].trim();
            String detalhes = partes[7].trim();
            if (detalhes.equalsIgnoreCase("NULL")) {
                detalhes = ""; // Se for "NULL", define como vazio
            }

            return new Pedido(id, setorSolicitante, setorResponsavel, dataPedido, produto, quantidade, estado, detalhes);
        } catch (NumberFormatException | ParseException e) {
            Auxiliar.error("Pedido.carregar: Falha ao parsear linha do CSV: " + linhaCsv + ". Erro: " + e.getMessage());
            return null;
        }
    }

    public String salvar() {
        return String.join(",",
                String.valueOf(this.id),
                this.setorSolicitante,
                this.setorResponsavel,
                Auxiliar.SDF.format(this.dataPedido),
                this.produto,
                String.valueOf(this.quantidade),
                this.estado,
                (this.detalhes != null && !this.detalhes.isEmpty()) ? this.detalhes : "NULL"
        );
    }

    // << Getters e Setters >>

    public int getId() {
        return id;
    }

    public String getSetorSolicitante() {
        return this.setorSolicitante;
    }

    public String getSetorResponsavel() {
        return this.setorResponsavel;
    }

    public Date getDataPedido() {
        return this.dataPedido;
    }

    public String getProduto() {
        return this.produto;
    }

    public int getQuantidade() {
        return this.quantidade;
    }

    public String getEstado() {
        return this.estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getDetalhes() {
        return this.detalhes;
    }

    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    // << Métodos adicionais >>

    @Override
    public String toString() {
        return "ID: " + this.id +
                ", Solicitante: " + this.setorSolicitante +
                ", Responsavel: " + this.setorResponsavel +
                ", Data: " + Auxiliar.SDF.format(this.dataPedido) +
                ", Produto: " + this.produto +
                ", Qtd: " + this.quantidade +
                ", Estado: " + this.estado;
    }

    public boolean compare(Pedido pedido) {
        if (this == pedido) return true;
        if (pedido == null) return false;
        return this.id == pedido.id &&
               this.setorSolicitante.equalsIgnoreCase(pedido.setorSolicitante) &&
               this.setorResponsavel.equalsIgnoreCase(pedido.setorResponsavel) &&
               this.produto.equalsIgnoreCase(pedido.produto) &&
               this.quantidade == pedido.quantidade;
    }

}
