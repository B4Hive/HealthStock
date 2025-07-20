package br.ufjf.dcc117.model.setor;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
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
            Auxiliar.error("Arquivo de senha não encontrado para o setor: " + nome);
            return null;
        }
        String senha;
        try (BufferedReader br = new BufferedReader(new FileReader(senhaFile))) {
            senha = Auxiliar.decrypt(br.readLine());
        } catch (IOException e) {
            Auxiliar.error("Erro ao ler senha do setor " + nome);
            Auxiliar.error("Mensagem de erro: " + e.getMessage());
            return null; // Retorna null em caso de erro
        }
        
        Estoque estoque = Estoque.carregar(nome);
        List<Pedido> pedidos = Pedido.carregarPedidos(nome);

        if (estoque != null && pedidos != null) {
            if (nome.equals(Auxiliar.SETOR_CADASTRO)) {
                return new SetorCadastro(nome, senha, pedidos, estoque);
            } else if (nome.equals(Auxiliar.SETOR_ENTRADA)) {
                return new SetorEntrada(nome, senha, pedidos, estoque);
            }
            return new Setor(nome, senha, pedidos, estoque);
        }
        return null;
    }

    public void salvar() {

        //Salvar Senha
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Auxiliar.path(nome, nome, "pw")))) {
            bw.write(Auxiliar.encrypt(this.senha));
        } catch (IOException e) {
            Auxiliar.error("Erro ao salvar senha do setor " + nome);
            Auxiliar.error("Mensagem de erro: " + e.getMessage());
            System.exit(1); // Encerra o programa em caso de erro crítico
        }

        salvarPedidos();

        //Salvar Estoque
        estoque.salvar(nome);
    }

    private void salvarPedidos() {
        //Salvar Pedidos
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(Auxiliar.path(nome, "pedidos", "csv")))) {
            bw.write("Setor Solicitante,Setor Responsavel,Data,Produto,Quantidade,Estado");
            bw.newLine();
            for (Pedido pedido : pedidos) {
                bw.write(pedido.salvar());
                bw.newLine();
            }
        } catch (IOException e) {
            Auxiliar.error("Erro ao salvar pedidos do setor " + nome);
            Auxiliar.error("Mensagem de erro: " + e.getMessage());
            System.exit(1); // Encerra o programa em caso de erro crítico
        }
    }

    // << Métodos de Pedido >>
    
    public void gerarPedido(String setorResponsavel, String produto, int quantidade) {
        Pedido pedido = new Pedido(this.nome, setorResponsavel, produto, quantidade);
        pedidos.add(pedido);
        salvarPedidos();
    }
    
    public void adicionarPedido(Pedido pedido) {
        pedidos.add(pedido);
        salvarPedidos();
    }

    public List<Pedido> listarPedidos() {
        return pedidos;
    }
    
    public List<String> listarPedidosPorEstado(String estado) {
        return pedidos.stream().filter(pedido -> estado.equals(pedido.getEstado())).map(Pedido::toString).toList();
    }
    
    public void aprovarPedido(Pedido pedido, boolean resposta) {
        for (Pedido p : this.pedidos) {
            if (p.compare(pedido)) {
                if (p.getEstado().equals("Pendente")) {
                    p.setEstado(resposta ? "Aprovado" : "Rejeitado");
                    salvarPedidos();
                    return;
                }
            }
        }
    }

    // << Métodos de Estoque >>

    public void entradaProduto(Produto produto) {
        estoque.adicionarProduto(produto);
        estoque.salvar(nome);
    }

    public Produto retiradaProduto(int id, int qtd) {
        Produto produto = estoque.retirarProduto(id, qtd);
        estoque.salvar(nome);
        return produto;
    }

    public void consumirProduto(int id, int qtd) {
        estoque.retirarProduto(id, qtd);
        estoque.salvar(nome);
    }

}
