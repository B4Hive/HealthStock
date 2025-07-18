package br.ufjf.dcc117.model.setor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.ufjf.dcc117.model.Auxiliar;
import br.ufjf.dcc117.model.estoque.Estoque;
import br.ufjf.dcc117.model.estoque.Produto;

public class Setor {

    // << Atributos >>

    private String nome;
    private String senha;
    private final List<Pedido> pedidos;
    private final Estoque estoque;

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
    
    public List<Produto> getProdutos() {
        return new ArrayList<>(this.estoque.getProdutos());
    }
    // << Métodos Setor >>
    
    public boolean validarSenha(String senha) {
        return this.senha.equals(senha);
    }

    public static Setor carregar(String nome) {
        File senhaFile = new File(Auxiliar.path(nome, nome, "pw"));
        if (!senhaFile.exists()) {
            System.err.println(new Date() + ":Arquivo de senha não encontrado para o setor: " + nome);
            return null;
        }
        String senha;
        try (BufferedReader br = new BufferedReader(new FileReader(senhaFile))) {
            senha = Auxiliar.decrypt(br.readLine());
        } catch (IOException e) {
            System.err.println(new Date() + ":Erro ao ler senha do setor " + nome);
            System.err.println(new Date() + ":Mensagem de erro: " + e.getMessage());
            return null; // Retorna null em caso de erro
        }
        
        Estoque estoque = Estoque.carregar(nome);
        List<Pedido> pedidos = Pedido.carregarPedidos(nome);

        if (estoque != null && pedidos != null) {
            return new Setor(nome, senha, pedidos, estoque);
        }
        return null;
    }

    public void salvar() {

        //Salvar Senha
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Auxiliar.path(nome, nome, "pw")))) {
            bw.write(Auxiliar.encrypt(this.senha));
        } catch (IOException e) {
            System.err.println(new Date() + ":Erro ao salvar senha do setor " + nome);
            System.err.println(new Date() + ":Mensagem de erro: " + e.getMessage());
            System.exit(1); // Encerra o programa em caso de erro crítico
        }

        //Salvar Pedidos
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Auxiliar.path(nome, "pedidos", "csv")))) {
            bw.write("Setor Origem,Setor Destino,Data,Produto,Quantidade,Estado");
            bw.newLine();
            for (Pedido pedido : pedidos) {
                bw.write(pedido.salvar());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println(new Date() + ":Erro ao salvar pedidos do setor " + nome);
            System.err.println(new Date() + ":Mensagem de erro: " + e.getMessage());
            System.exit(1); // Encerra o programa em caso de erro crítico
        }

        //Salvar Estoque
        estoque.salvar(nome);
    }

    // << Métodos de Pedido >>
    
    public void gerarPedido(String setorDestino, String produto, int quantidade) {
        Pedido pedido = new Pedido(this.nome, setorDestino, produto, quantidade);
        pedidos.add(pedido);
    }
    
    public void adicionarPedido(Pedido pedido) {
        pedidos.add(pedido);
    }

    public List<Pedido> listarPedidos() {
        return pedidos;
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
