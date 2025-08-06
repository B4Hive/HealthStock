package br.ufjf.dcc117.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import br.ufjf.dcc117.model.estoque.Medicacao;
import br.ufjf.dcc117.model.estoque.Produto;
import br.ufjf.dcc117.model.setor.Pedido;

public class PersistenceService {

    private static final String PEDIDOS_FILE = "src/main/resources/pedidos.csv";

    public static int getNextProdutoInstanceId() {
        List<Produto> produtos = carregarProdutos(p -> true);
        return produtos.stream().mapToInt(Produto::getID).max().orElse(0) + 1;
    }

    public static int getNextProdutoCodigo() {
        List<Produto> produtos = carregarProdutos(p -> true);
        return produtos.stream().mapToInt(Produto::getCodigoProduto).max().orElse(0) + 1;
    }

    public static List<Produto> carregarProdutos(Predicate<Produto> condicao) {
        String path = "src/main/resources/produtos.csv";
        List<Produto> produtos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            br.readLine(); // Pula o cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                try { // <-- O TRY-CATCH DEVE ENVOLVER A LÓGICA DE PARSE
                    String[] dados = linha.split(",");
                    int id = Integer.parseInt(dados[0]);
                    int codigoProduto = Integer.parseInt(dados[1]);
                    String nome = dados[2];
                    int quantidade = Integer.parseInt(dados[3]);
                    int idFornecedor = Integer.parseInt(dados[4]);
                    String setor = dados[5];
                    String tipo = dados[6];

                    Produto p;
                    if ("Medicacao".equalsIgnoreCase(tipo)) {
                        String lote = dados[7];
                        Date validade = "NULL".equals(dados[8]) ? null : new SimpleDateFormat("yyyy-MM-dd").parse(dados[8]);
                        String ultimoResponsavel = "NULL".equals(dados[9]) ? null : dados[9];
                        Date dataUltimoResponsavel = "NULL".equals(dados[10]) ? null : new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dados[10]);
                        p = new Medicacao(id, codigoProduto, nome, quantidade, idFornecedor, setor, lote, validade, ultimoResponsavel, dataUltimoResponsavel);
                    } else {
                        p = new Produto(id, codigoProduto, nome, quantidade, idFornecedor, setor);
                    }
                    produtos.add(p);
                } catch (ParseException | NumberFormatException e) {
                    System.err.println("Erro ao processar linha do CSV de produtos: " + linha);
                }
            }
        } catch (IOException e) {
        }
        return produtos.stream().filter(condicao).collect(Collectors.toList());
    }

    public static void salvarProduto(Produto produto) {
        List<Produto> produtos = carregarProdutos(p -> p.getID() != produto.getID());
        produtos.add(produto);
        salvarTodosProdutos(produtos);
    }
    
    public static void removerProduto(Produto produtoParaRemover) {
        List<Produto> todosProdutos = carregarProdutos(p -> p.getID() != produtoParaRemover.getID());
        salvarTodosProdutos(todosProdutos);
    }

    public static void removerProdutos(List<Produto> produtosParaRemover) {
        List<Produto> todosProdutos = carregarProdutos(p -> true);
        todosProdutos.removeAll(produtosParaRemover);
        salvarTodosProdutos(todosProdutos);
    }

    private static void salvarTodosProdutos(List<Produto> produtos) {
        String path = "src/main/resources/produtos.csv";
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            bw.write("id,codigoProduto,nome,quantidade,idFornecedor,setor,tipo,lote,validade,ultimoResponsavel,dataUltimoResponsavel");
            bw.newLine();
            for (Produto p : produtos) {
                bw.write(p.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
        }
    }

    // MÉTODOS DE PEDIDO CORRIGIDOS
    public static int getNextPedidoId() {
        List<Pedido> pedidos = carregarPedidos(p -> true);
        return pedidos.stream().mapToInt(Pedido::getId).max().orElse(0) + 1;
    }

    public static List<Pedido> carregarPedidos(Predicate<Pedido> condicao) {
        List<Pedido> pedidos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PEDIDOS_FILE))) {
            br.readLine(); // Pula cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                try {
                    String[] dados = linha.split(",");
                    int id = Integer.parseInt(dados[0]);
                    String setorSolicitante = dados[1];
                    String setorResponsavel = dados[2];
                    String produto = dados[3];
                    int quantidade = Integer.parseInt(dados[4]);
                    String estado = dados[5];
                    String detalhes = dados[6];
                    
                    // Verificar se tem campo de data (novo formato)
                    if (dados.length > 7) {
                        Date dataPedido = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss").parse(dados[7]);
                        pedidos.add(new Pedido(id, setorSolicitante, setorResponsavel, produto, quantidade, estado, detalhes, dataPedido));
                    } else {
                        // Formato antigo sem data
                        pedidos.add(new Pedido(id, setorSolicitante, setorResponsavel, produto, quantidade, estado, detalhes));
                    }
                } catch (ParseException | NumberFormatException e) {
                    System.err.println("Erro ao processar linha do CSV de pedidos: " + linha);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao ler arquivo de pedidos: " + e.getMessage());
        }
        return pedidos.stream().filter(condicao).collect(Collectors.toList());
    }

    public static void salvarPedido(Pedido pedido) {
        List<Pedido> pedidos = carregarPedidos(p -> p.getId() != pedido.getId());
        pedidos.add(pedido);
        salvarTodosPedidos(pedidos);
    }

    private static void salvarTodosPedidos(List<Pedido> pedidos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PEDIDOS_FILE))) {
            bw.write("id,setorSolicitante,setorResponsavel,produto,quantidade,estado,detalhes,dataPedido");
            bw.newLine();
            for (Pedido p : pedidos) {
                bw.write(p.toCSV());
                bw.newLine();
            }
        } catch (IOException e) {
            System.err.println("Erro ao salvar pedidos: " + e.getMessage());
        }
    }
}
