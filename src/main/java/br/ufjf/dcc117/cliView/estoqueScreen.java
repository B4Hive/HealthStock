package br.ufjf.dcc117.cliView;

import java.util.Scanner;

import br.ufjf.dcc117.controller.Control;

public class estoqueScreen {

    private static final Scanner in = new Scanner(System.in);

    public static void show() {
        int choice = -1;
        while (choice != 0) {
            CLI.clear();
            String[] produtos = Control.getListaProdutos();
            System.out.println("Lista de Produtos no Estoque:");
            for (String p : produtos) {
                System.out.println(p);
            }
            System.out.println("0 - Voltar ao menu anterior");
            System.out.print("\nEscolha um produto:");
            choice = in.nextInt(); in.nextLine();
            if (choice != 0) {
                show(choice);
            }
        }
    }

    public static void show(int produtoId) {
        int choice = -1;
        while (choice != 0) {
            CLI.clear();
            String[] produto = Control.getProduto(produtoId);
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                CLI.pause();
                return;
            }
            String[] options = Control.getProdutoOptions();
            System.out.println("Informações do Produto:");
            System.out.println("ID: " + produto[0]);
            System.out.println("Nome: " + produto[1]);
            System.out.println("Quantidade: " + produto[2]);
            System.out.println("Fornecedor: " + produto[3]);
            if (produto.length > 4) {
                System.out.println("Lote: " + produto[4]);
                System.out.println("Validade: " + produto[5]);
                System.out.println("Último Responsável: " + produto[6]);
                System.out.println("Última Movimentação: " + produto[7]);
            }
            System.out.println();
            
            CLI.printMenu("Opções do Produto", options);
            choice = in.nextInt(); in.nextLine();
            switch (choice) {
                case 1 -> {
                    System.out.println("Quantidade a consumir (Max:" + produto[2] + "):");
                    int quantidade = in.nextInt(); in.nextLine();
                    Control.consumirProduto(produtoId, quantidade);
                    break;
                }
                case 2 -> {
                    editar(produtoId);
                    break;
                }
                case 3 -> {
                    gerarPedido(produtoId);
                    break;
                }
                case 0 -> {
                    // Voltar
                }
                default -> {
                    System.out.println("Opção inválida, tente novamente.");
                    CLI.pause();
                }
            }
        }
    }

    public static void editar(int produtoId) {
        CLI.clear();
        CLI.NYI(); // TODO: Implementar edição de produto
    }

    public static void gerarPedido(int produtoId) {
        CLI.clear();
        CLI.NYI(); // TODO: Implementar geração de pedido (lá na pedidosScreen)
    }

}
