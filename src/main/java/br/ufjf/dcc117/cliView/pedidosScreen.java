package br.ufjf.dcc117.cliView;

import java.util.Scanner;

import br.ufjf.dcc117.controller.Control;

public class pedidosScreen {

    public static void show(Scanner in) {
        int choice = -1;
        while (choice != 0) {
            CLI.clear();
            String[] pedidos = Control.getListaPedidos();
            System.out.println("Lista de Pedidos:");
            for (String p : pedidos) {
                System.out.println(p);
            }
            System.out.println("0 - Voltar ao menu anterior");
            System.out.print("\nEscolha um pedido:");
            choice = in.nextInt(); in.nextLine();
            if (choice != 0) {
                pedidosScreen.showPedido(in, choice);
            }
        }
    }

    private static void showPedido(Scanner in, int choice) {
        System.out.println("Detalhes do Pedido " + choice + ":");
        String[] pedido = Control.getPedido(choice);
        if (pedido == null) {
            System.out.println("Pedido não encontrado.");
            CLI.pause(in);
            return;
        }
        
        int option = -1;
        while (option != 0) {
            System.out.println("Detalhes do Pedido " + choice + ":");
            System.out.println("Setor de Origem: " + pedido[1]);
            System.out.println("Setor de Destino: " + pedido[2]);
            System.out.println("Data do Pedido: " + pedido[3]);
            System.out.println("Produto: " + pedido[4]);
            System.out.println("Quantidade: " + pedido[5]);
            System.out.println("Status: " + pedido[6]);
            if (Control.getTipoSetor().equals("Cadastro")) {
                CLI.printMenu("Opções", new String[]{"Aprovar Pedido", "Rejeitar Pedido"});
            }
            option = in.nextInt(); in.nextLine();
            switch (option) {
                case 1 -> CLI.NYI(in); // TODO: Implementar aprovação de pedido
                case 2 -> CLI.NYI(in); // TODO: Implementar rejeição de pedido
                case 0 -> {}
                default -> System.out.println("Opção inválida.");
            }
        }
    }

}
