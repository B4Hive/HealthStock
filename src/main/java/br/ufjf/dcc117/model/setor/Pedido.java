package br.ufjf.dcc117.model.setor;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Pedido {
    // << Atributos >>

    private final int id;
    private final String setorSolicitante;
    private final String setorResponsavel;
    private final String produto;
    private final int quantidade;
    private String estado;
    private String detalhes; // ← REMOVIDO final
    private Date dataPedido;

    // << Construtores >>

    public Pedido(int id, String setorSolicitante, String setorResponsavel, String produto, int quantidade) {
        this.id = id;
        this.setorSolicitante = setorSolicitante;
        this.setorResponsavel = setorResponsavel;
        this.produto = produto;
        this.quantidade = quantidade;
        this.estado = "Pendente";
        this.detalhes = "NULL";
        this.dataPedido = new Date(); // Data atual
    }

    // NOVO CONSTRUTOR para carregar do CSV
    public Pedido(int id, String setorSolicitante, String setorResponsavel, String produto, int quantidade, String estado, String detalhes, Date dataPedido) {
        this.id = id;
        this.setorSolicitante = setorSolicitante;
        this.setorResponsavel = setorResponsavel;
        this.produto = produto;
        this.quantidade = quantidade;
        this.estado = estado;
        this.detalhes = detalhes;
        this.dataPedido = dataPedido;
    }

    // Construtor para carregar do CSV sem data (compatibilidade)
    public Pedido(int id, String setorSolicitante, String setorResponsavel, String produto, int quantidade, String estado, String detalhes) {
        this.id = id;
        this.setorSolicitante = setorSolicitante;
        this.setorResponsavel = setorResponsavel;
        this.produto = produto;
        this.quantidade = quantidade;
        this.estado = estado;
        this.detalhes = detalhes;
        this.dataPedido = new Date(); // Data atual como fallback
    }

    // Getters
    public int getId() { return id; }
    public String getSetorSolicitante() { return setorSolicitante; }
    public String getSetorResponsavel() { return setorResponsavel; }
    public String getProduto() { return produto; }
    public int getQuantidade() { return quantidade; }
    public String getEstado() { return estado; }
    public String getDetalhes() { return detalhes; }
    public Date getDataPedido() { return dataPedido; }

    // Setters
    public void setEstado(boolean aprovado) {
        this.estado = aprovado ? "Aprovado" : "Rejeitado";
    }

    public void setDataPedido(Date dataPedido) {
        this.dataPedido = dataPedido;
    }

    // Adicionar setter para detalhes se necessário
    public void setDetalhes(String detalhes) {
        this.detalhes = detalhes;
    }

    // NOVO MÉTODO para salvar no CSV
    public String toCSV() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        return String.format("%d,%s,%s,%s,%d,%s,%s,%s",
                this.id,
                this.setorSolicitante,
                this.setorResponsavel,
                this.produto,
                this.quantidade,
                this.estado,
                this.detalhes != null ? this.detalhes : "NULL",
                sdf.format(this.dataPedido));
    }
}
