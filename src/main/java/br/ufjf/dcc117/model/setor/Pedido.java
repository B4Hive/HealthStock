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

    public Pedido(String setorOrigem, String setorDestino, Date dataPedido, String produto, int quantidade, String estado) {
        this.setorOrigem = setorOrigem;
        this.setorDestino = setorDestino;
        this.dataPedido = dataPedido;
        this.produto = produto;
        this.quantidade = quantidade;
        this.estado = estado;
    }

    public static Pedido carregar(String pedido) {
        String[] partes = pedido.split(",");
        if (partes.length < 5) {
            return null;
        }
        try {
            String setorOrigem = partes[0].trim();
            String setorDestino = partes[1].trim();
            Date dataPedido = Auxiliar.SDF.parse(partes[2].trim());
            String produto = partes[3].trim();
            int quantidade = Integer.parseInt(partes[4].trim());
            String estado = partes[5].trim();

            Pedido pedidoObj = new Pedido(setorOrigem, setorDestino, dataPedido, produto, quantidade, estado);
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
            System.err.println(new Date() + ":Erro ao carregar pedidos do setor " + nomeSetor);
            System.err.println(new Date() + ":Mensagem de erro: " + e.getMessage());
            return pedidos; // Retorna lista vazia em caso de erro
        }
        return pedidos;
    }

    public String salvar() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getSetorOrigem()).append(",");
        sb.append(this.getSetorDestino()).append(",");
        sb.append(Auxiliar.SDF.format(this.getDataPedido())).append(",");
        sb.append(this.getProduto()).append(",");
        sb.append(this.getQuantidade()).append(",");
        sb.append(this.getEstado());
        return sb.toString();
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
