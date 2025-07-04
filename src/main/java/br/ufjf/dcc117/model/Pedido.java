package br.ufjf.dcc117.model;

import java.util.Date;

public class Pedido {

    // << Atributos >>

    private final String setorOrigem;
    private final String setorDestino;
    private final Date dataPedido;
    private final String produto;
    private final int quantidade;
    private String estado;

    // << Construtor >>

    public Pedido(String setorOrigem, String setorDestino, String produto, int quantidade) {
        this.setorOrigem = setorOrigem;
        this.setorDestino = setorDestino;
        this.dataPedido = new Date();
        this.produto = produto;
        this.quantidade = quantidade;
        this.estado = "Pendente";
    }

    // << Getters e Setters >>

    public String getSetorOrigem() {
        return this.setorOrigem;
    }

    public String getSetorDestino() {
        return this.setorDestino;
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

    // << Métodos adicionais >>

    @Override
    public String toString() {
        return "Pedido: " +
                "origem = [" + this.setorOrigem + ']' +
                ", destino = [" + this.setorDestino + ']' +
                ", data do Pedido = [" + this.dataPedido + ']' +
                ", produto = [" + this.produto + ']' +
                ", quantidade = [" + this.quantidade + ']' +
                ", estado = [" + this.estado + ']' +
                ';';
    }

}
