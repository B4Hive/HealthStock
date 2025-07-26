package br.ufjf.dcc117.model.setor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import br.ufjf.dcc117.model.Auxiliar;
import br.ufjf.dcc117.model.PersistenceService;
import br.ufjf.dcc117.model.estoque.Produto;

public class Setor {
    private final String nome;
    private String senha;

    protected Setor(String nome) {
        this.nome = nome;
    }

    public String getNome() {
        return nome;
    }

    public static Setor carregar(String nomeSetor) {
        String path = "src/main/resources/senhas.csv";
        try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
            String line;
            reader.readLine(); // Pula o cabeçalho
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                if (values[0].equalsIgnoreCase(nomeSetor)) {
                    String senha = values[1];
                    Setor setor;
                    if (nomeSetor.equalsIgnoreCase(Auxiliar.SETOR_CADASTRO)) {
                        setor = new SetorCadastro(nomeSetor);
                    } else if (nomeSetor.equalsIgnoreCase(Auxiliar.SETOR_ENTRADA)) {
                        setor = new SetorEntrada(nomeSetor);
                    } else {
                        setor = new Setor(nomeSetor);
                    }
                    setor.setSenha(senha);
                    return setor;
                }
            }
        } catch (IOException e) {
            Auxiliar.error("Setor.carregar: Arquivo de senhas não encontrado.");
            return null;
        }
        // Se o loop terminar e não encontrar o setor
        Auxiliar.error("Setor.carregar: Setor não encontrado no arquivo de senhas: " + nomeSetor);
        return null;
    }

    // << Métodos de Pedidos >>
    public void criarPedido(Pedido pedido) {
        PersistenceService.salvarPedido(pedido);
    }

    public void aprovarPedido(Pedido pedido, boolean aprovado) {
        List<Pedido> todosPedidos = PersistenceService.carregarPedidos(p -> true);
        for (Pedido p : todosPedidos) {
            if (p.getId() == pedido.getId()) {
                p.setEstado(aprovado ? "Aprovado" : "Rejeitado");
                break;
            }
        }
        PersistenceService.salvarPedidos(todosPedidos);
    }

    public List<Pedido> listarPedidos() {
        return PersistenceService.carregarPedidos(p -> p.getSetorSolicitante().equalsIgnoreCase(this.nome) || p.getSetorResponsavel().equalsIgnoreCase(this.nome));
    }

    public List<String> listarPedidosPorEstado(String estado) {
        return listarPedidos().stream()
                .filter(pedido -> estado.equalsIgnoreCase(pedido.getEstado()))
                .map(Pedido::toString)
                .collect(Collectors.toList());
    }

    public boolean validarSenha(String senha) {
        return this.senha.equals(Auxiliar.encrypt(this.nome, senha));
    }

    private void setSenha(String senha) {
        this.senha = senha;
    }

    // << Métodos de Estoque >>
    public void entradaProduto(Produto produto) {
        produto.setSetor(this.nome);
        Produto existente = getProduto(produto.getID());
        if (existente != null) {
            existente.setQuantidade(existente.getQuantidade() + produto.getQuantidade());
            PersistenceService.salvarProduto(existente);
        } else {
            PersistenceService.salvarProduto(produto);
        }
    }

    public Produto retiradaProduto(int id, int quantidade) {
        Produto produto = getProduto(id);
        if (produto != null && produto.getQuantidade() >= quantidade) {
            produto.setQuantidade(produto.getQuantidade() - quantidade);
            PersistenceService.salvarProduto(produto);
            Produto produtoRetirado = produto.clone(quantidade);
            return produtoRetirado;
        }
        return null;
    }

    public void consumirProduto(int id, int quantidade) {
        Produto produto = getProduto(id);
        if (produto != null && produto.getQuantidade() >= quantidade) {
            produto.setQuantidade(produto.getQuantidade() - quantidade);
            if (produto.getQuantidade() == 0) {
                PersistenceService.removerProduto(produto);
            } else {
                PersistenceService.salvarProduto(produto);
            }
        }
    }

    public List<Produto> getProdutos() {
        return PersistenceService.carregarProdutos(p -> p.getSetor().equalsIgnoreCase(this.nome));
    }

    public Produto getProduto(int id) {
        return PersistenceService.carregarProdutos(p -> p.getSetor().equalsIgnoreCase(this.nome) && p.getID() == id)
                .stream().findFirst().orElse(null);
    }
}
