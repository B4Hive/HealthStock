package br.ufjf.dcc117.model.setor;

import java.util.List;

import br.ufjf.dcc117.model.estoque.Estoque;
import br.ufjf.dcc117.model.estoque.Produto;

public class Setor {

    // << Atributos >>

    private String nome;
    private String senha;
    private final List<Pedido> pedidos;
    private final Estoque estoque;

    public static final String SETOR_MEDICACAO = "farmacia";
    public static final String SETOR_ENTRADA = "almoxarifado";
    public static final String SETOR_CADASTRO = "compras";

    // << Construtor >>

    public Setor(String nome, String senha, List<Pedido> pedidos, Estoque estoque) {
        this.nome = nome;
        this.senha = senha;
        this.pedidos = pedidos;
        this.estoque = estoque;
    }

    // << Getters e Setters >>

    public String getNome() {
        return this.nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    protected Estoque getEstoque() {
        return this.estoque;
    }
    
    // << Métodos Setor >>
    
    public boolean validarSenha(String senha) {
        return this.senha.equals(senha);
    }

    public static Setor carregarSetor(String nome) {
        //TODO: Implementar lógica para carregar o setor a partir do csv
        return null;
    }

    // << Métodos de Pedido >>
    
    public void gerarPedido(String setorDestino, String produto, int quantidade) {
        Pedido pedido = new Pedido(this.nome, setorDestino, produto, quantidade);
        pedidos.add(pedido);
    }
    
    public void adicionarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public List<String> listarPedidos() {
        return pedidos.stream().map(Pedido::toString).toList();
    }
    
    public List<String> listarPedidosPorEstado(String estado) {
        return pedidos.stream().filter(pedido -> estado.equals(pedido.getEstado())).map(Pedido::toString).toList();
    }
    
    public boolean aprovarPedido(int idPedido, boolean resposta) {
        Pedido pedido = pedidos.get(idPedido);
        if (!pedido.getSetorDestino().equals(this.nome)) return false;
        if (resposta) {
            pedido.setEstado("Aprovado");
            return true;
        } else {
            pedido.setEstado("Rejeitado");
            return false;
        }
    }

    // << Métodos de Estoque >>

    public void entradaProduto(Produto produto) {
        estoque.adicionarProduto(produto);
    }

    public Produto retiradaProduto(int id, int qtd) {
        return estoque.retirarProduto(id, qtd);
    }

    public void consumirProduto(int id, int qtd) {
        estoque.retirarProduto(id, qtd);
    }

}
