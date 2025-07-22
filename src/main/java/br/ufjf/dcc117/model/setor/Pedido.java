package br.ufjf.dcc117.model.setor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufjf.dcc117.model.Auxiliar;

public class Pedido {

    // << Atributos >>

    private final String setorSolicitante;
    private final String setorResponsavel;
    private final Date dataPedido;
    private final String produto;
    private final int quantidade;
    private String estado;
    private String detalhes;

    // << Construtor >>

    public Pedido(String setorSolicitante, String setorResponsavel, String produto, int quantidade) {
        this.setorSolicitante = setorSolicitante;
        this.setorResponsavel = setorResponsavel;
        this.dataPedido = new Date();
        this.produto = produto;
        this.quantidade = quantidade;
        this.estado = "Pendente";
        this.detalhes = null;
    }

    public Pedido(String setorSolicitante, String setorResponsavel, Date dataPedido, String produto, int quantidade, String estado, String detalhes) {
        this.setorSolicitante = setorSolicitante;
        this.setorResponsavel = setorResponsavel;
        this.dataPedido = dataPedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.estado = estado;
        this.detalhes = detalhes;
    }

    public static Pedido carregar(String pedido) {
        String[] partes = pedido.split(",");
        if (partes.length != 7) {
            return null;
        }
        try {
            String setorSolicitante = partes[0].trim();
            String setorResponsavel = partes[1].trim();
            Date dataPedido = Auxiliar.SDF.parse(partes[2].trim());
            String produto = partes[3].trim();
            int quantidade = Integer.parseInt(partes[4].trim());
            String estado = partes[5].trim();
            String detalhes = partes[6].trim();
            if (detalhes.isEmpty()) {
                detalhes = null; // Se não houver detalhes, define como null
            }

            Pedido pedidoObj = new Pedido(setorSolicitante, setorResponsavel, dataPedido, produto, quantidade, estado, detalhes);
            return pedidoObj;
        } catch (NumberFormatException | ParseException e) {
            return null;
        }
    }

    public static List<Pedido> carregarPedidos(String nomeSetor) {
        List<Pedido> pedidos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(Auxiliar.path(nomeSetor, "pedidos","csv")))) {
            String linha;
            br.readLine();
            while ((linha = br.readLine()) != null) {
                Pedido pedido = Pedido.carregar(linha);
                if (pedido != null) {
                    pedidos.add(pedido);
                }
            }
        } catch (IOException e) {
            Auxiliar.error("Erro ao carregar pedidos do setor " + nomeSetor);
            Auxiliar.error("Mensagem de erro: " + e.getMessage());
            return pedidos; // Retorna lista vazia em caso de erro
        }
        return pedidos;
    }

    public String salvar() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getSetorSolicitante()).append(",");
        sb.append(this.getSetorResponsavel()).append(",");
        sb.append(Auxiliar.SDF.format(this.getDataPedido())).append(",");
        sb.append(this.getProduto()).append(",");
        sb.append(this.getQuantidade()).append(",");
        sb.append(this.getEstado());
        return sb.toString();
    }

    // << Getters e Setters >>

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
        return "Solicitante = " + this.setorSolicitante +
                ", Responsavel = " + this.setorResponsavel +
                ", Data do Pedido = " + this.dataPedido +
                ", Produto = " + this.produto +
                ", Quantidade = " + this.quantidade +
                ", Estado = " + this.estado +
                ';';
    }

    public boolean compare(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Pedido)) return false;
        Pedido pedido = (Pedido) obj;
        return this.setorSolicitante.equalsIgnoreCase(pedido.setorSolicitante) &&
               this.setorResponsavel.equalsIgnoreCase(pedido.setorResponsavel) &&
               this.dataPedido.equals(pedido.dataPedido) &&
               this.produto.equalsIgnoreCase(pedido.produto) &&
               this.quantidade == pedido.quantidade &&
               this.estado.equalsIgnoreCase(pedido.estado);
    }

}
