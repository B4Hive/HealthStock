package br.ufjf.dcc117.model;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Predicate;

import br.ufjf.dcc117.model.estoque.Medicacao;
import br.ufjf.dcc117.model.estoque.Produto;
import br.ufjf.dcc117.model.setor.Pedido;

public class PersistenceService {

    private static final String PRODUTOS_CSV = "src/main/resources/produtos.csv";
    private static final String PEDIDOS_FILE = "src/main/resources/pedidos.csv";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");

    // Métodos para Produtos

    public static List<Produto> carregarProdutos(Predicate<Produto> filtro) {
        List<Produto> produtos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PRODUTOS_CSV))) {
            String line;
            br.readLine(); // Pular cabeçalho
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                Produto produto;
                String tipoProduto = values[5];
                if ("Medicacao".equalsIgnoreCase(tipoProduto)) {
                    produto = Medicacao.fromCSV(values);
                } else {
                    produto = Produto.fromCSV(values);
                }
                if (filtro.test(produto)) {
                    produtos.add(produto);
                }
            }
        } catch (IOException e) {
            Auxiliar.error("PersistenceService.carregarProdutos: " + e.getMessage());
        }
        return produtos;
    }

    public static void salvarProdutos(List<Produto> todosProdutos) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(PRODUTOS_CSV))) {
            pw.println("id,nome,quantidade,idFornecedor,setor,tipo,lote,validade,ultimoResponsavel,dataUltimoResponsavel");
            for (Produto p : todosProdutos) {
                pw.println(p.toCSV());
            }
        } catch (IOException e) {
            Auxiliar.error("PersistenceService.salvarProdutos: " + e.getMessage());
        }
    }

    public static void salvarProduto(Produto produto) {
        List<Produto> produtos = carregarProdutos(p -> true); // Carrega todos
        produtos.removeIf(p -> p.getID() == produto.getID() && p.getSetor().equalsIgnoreCase(produto.getSetor()));
        produtos.add(produto);
        salvarProdutos(produtos);
    }
    
    public static void removerProduto(Produto produto) {
        List<Produto> produtos = carregarProdutos(p -> true);
        produtos.removeIf(p -> p.getID() == produto.getID() && p.getSetor().equalsIgnoreCase(produto.getSetor()));
        salvarProdutos(produtos);
    }

    // Métodos para Pedidos

    public static int getNextPedidoId() {
        List<Pedido> pedidos = carregarPedidos(p -> true);
        return pedidos.stream().mapToInt(Pedido::getId).max().orElse(0) + 1;
    }

    public static List<Pedido> carregarPedidos(Predicate<Pedido> predicate) {
        List<Pedido> pedidos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(PEDIDOS_FILE))) {
            String line;
            br.readLine(); // Pular cabeçalho
            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length >= 8) {
                    try {
                        int id = Integer.parseInt(values[0]);
                        String setorSolicitante = values[1];
                        String setorResponsavel = values[2];
                        Date dataPedido = DATE_FORMAT.parse(values[3]);
                        String produto = values[4];
                        int quantidade = Integer.parseInt(values[5]);
                        String estado = values[6];
                        String detalhes = values[7].equalsIgnoreCase("NULL") ? "" : values[7];
                        
                        Pedido pedido = new Pedido(id, setorSolicitante, setorResponsavel, dataPedido, produto, quantidade, estado, detalhes);
                        if (predicate.test(pedido)) {
                            pedidos.add(pedido);
                        }
                    } catch (NumberFormatException | ParseException e) {
                        System.err.println("Erro ao parsear pedido: " + line + " | Erro: " + e.getMessage());
                    }
                }
            }
        } catch (IOException e) {
            // Arquivo pode não existir na primeira execução
        }
        return pedidos;
    }

    public static void salvarPedido(Pedido pedido) {
        List<Pedido> pedidos = carregarPedidos(p -> p.getId() != pedido.getId());
        pedidos.add(pedido);
        salvarPedidos(pedidos);
    }

    public static void salvarPedidos(List<Pedido> pedidos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(PEDIDOS_FILE))) {
            bw.write("id,setorSolicitante,setorResponsavel,dataPedido,produto,quantidade,estado,detalhes");
            bw.newLine();
            for (Pedido p : pedidos) {
                bw.write(p.salvar());
                bw.newLine();
            }
        } catch (IOException e) {
            Auxiliar.error("PersistenceService.salvarPedidos: " + e.getMessage());
        }
    }
}
