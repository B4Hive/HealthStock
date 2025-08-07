package br.ufjf.dcc117.model.setor;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import br.ufjf.dcc117.model.Auxiliar;
import br.ufjf.dcc117.model.PersistenceService;
import br.ufjf.dcc117.model.estoque.Medicacao;
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
        pedido.setEstado(aprovado); // CORRIGIDO: O método agora aceita um boolean diretamente.
        PersistenceService.salvarPedido(pedido); // CORRIGIDO: O método de salvar um único pedido já existe.
    }

    public List<Pedido> listarPedidos() {
        return PersistenceService.carregarPedidos(p -> p.getSetorResponsavel().equals(this.nome) || p.getSetorSolicitante().equals(this.nome));
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
    public void removerProdutosVencidos() {
        // 1. Carrega todos os produtos do setor.
        List<Produto> produtosDoSetor = this.getProdutos();

        // 2. Filtra para encontrar apenas os medicamentos vencidos.
        List<Produto> produtosVencidos = produtosDoSetor.stream()
                .filter(p -> p instanceof Medicacao && ((Medicacao) p).isVencido())
                .collect(Collectors.toList());

        // 3. Remove cada produto vencido usando o serviço de persistência.
        if (!produtosVencidos.isEmpty()) {
            PersistenceService.removerProdutos(produtosVencidos);
        }
    }

    // MÉTODOS RESTAURADOS PARA CORRIGIR ERROS EM CASCATA
    public List<Produto> getProdutos() {
        return PersistenceService.carregarProdutos(p -> p.getSetor().equalsIgnoreCase(this.nome));
    }

    public Produto getProduto(int id) {
        return this.getProdutos().stream().filter(p -> p.getID() == id).findFirst().orElse(null);
    }
}
