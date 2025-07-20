package br.ufjf.dcc117.cliView;

import java.util.Scanner;

import br.ufjf.dcc117.controller.Control;

public class pedidosScreen {

    private static final Scanner in = new Scanner(System.in);

    public static void show() {
        int choice = -1;
        while (choice != 0) {
            CLI.clear();
            String[] pedidos = Control.getListaPedidos();
            System.out.println("Lista de Pedidos:");
            for (int i = 0; i < pedidos.length; i++) {
                System.out.println(i + 1 + " - " + pedidos[i]);
            }
            System.out.println("0 - Voltar ao menu anterior");
            System.out.print("\nEscolha um pedido:");
            choice = in.nextInt();
            in.nextLine();
            if (choice != 0) {
                pedidosScreen.showPedido(in, choice - 1);
            }
        }
    }

    private static void showPedido(Scanner in, int choice) {
        String[] pedido = Control.getPedido(choice);
        if (pedido == null) {
            System.out.println("Pedido não encontrado.");
            CLI.pause();
            return;
        }

        int option = -1;
        while (option != 0) {
            CLI.clear();
            System.out.println("Detalhes do Pedido " + (choice + 1) + ":");
            System.out.println("Setor de Solicitante: " + pedido[0]);
            System.out.println("Setor de Responsavel: " + pedido[1]);
            System.out.println("Data do Pedido: " + pedido[2]);
            System.out.println("Produto: " + pedido[3]);
            System.out.println("Quantidade: " + pedido[4]);
            System.out.println("Status: " + pedido[5]);
            CLI.printMenu("Opções", new String[] { "Aprovar Pedido", "Rejeitar Pedido" });
            option = in.nextInt();
            in.nextLine();
            switch (option) {
                case 1 -> {
                    if (!Control.getSetor().equals(pedido[1])) {
                        System.out.println("Você não tem permissão para aprovar este pedido.");
                        CLI.pause();
                        return;
                    }
                    if (!Control.verificarProduto(pedido[3])) {
                        if (Control.setorCadastro()) {
                            // TODO: Cadastrar produto se não existir
                            CLI.NYI("Cadastro de produto");
                        } else {
                            System.out.println("Produto não encontrado e setor não tem permissão para cadastrar.");
                            CLI.pause();
                            return;
                        }
                    }
                    System.out.println();
                    System.out.print("Responsável: ");
                    if (Control.respostaPedido(choice, true, in.nextLine())) {
                        System.out.println("Pedido aprovado com sucesso.");
                    } else {
                        System.out.println("Falha ao aprovar o pedido.");
                    }
                    CLI.pause();
                    return;
                }
                case 2 -> {
                    if (Control.respostaPedido(choice, false, null)) {
                        System.out.println("Pedido rejeitado com sucesso.");
                    } else {
                        System.out.println("Falha ao rejeitar o pedido.");
                    }
                    CLI.pause();
                    return;
                }
                case 0 -> {
                }
                default -> System.out.println("Opção inválida.");
            }
        }
    }

}
