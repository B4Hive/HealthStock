package br.ufjf.dcc117.cliView;

import java.util.Scanner;

import br.ufjf.dcc117.controller.Control;

public class produtoScreen {
    
    public static void show(Scanner in, int produtoId) {
        int choice = -1;
        while (choice != 0) {
            CLI.clear();
            String[] produto = Control.getProduto(produtoId);
            if (produto == null) {
                System.out.println("Produto não encontrado.");
                CLI.pause(in);
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
                    editar(in, produtoId);
                    break;
                }
                case 3 -> {
                    gerarPedido(in, produtoId);
                    break;
                }
                case 0 -> {
                    // Voltar
                }
                default -> {
                    System.out.println("Opção inválida, tente novamente.");
                    CLI.pause(in);
                }
            }
        }
        
    }
    
    public static void editar(Scanner in, int produtoId) {
        CLI.clear();
        CLI.NYI(in); // TODO: Implementar edição de produto
    }

    public static void gerarPedido(Scanner in, int produtoId) {
        CLI.clear();
        CLI.NYI(in); // TODO: Implementar geração de pedido
    }

}
