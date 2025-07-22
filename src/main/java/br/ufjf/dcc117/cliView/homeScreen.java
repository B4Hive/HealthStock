package br.ufjf.dcc117.cliView;

import java.util.Scanner;

import br.ufjf.dcc117.controller.Control;

public class homeScreen {

    private static final Scanner in = new Scanner(System.in);

    public static void show() {
        String[] options = Control.getHomeOptions();
        
        int choice = -1;
        while (choice != 0) {
            CLI.clear();
            CLI.printMenu("Home Options",options);
            choice = in.nextInt(); in.nextLine();
            switch(choice){
                case 1 -> estoqueScreen.show();
                case 2 -> pedidosScreen.show();
                case 3 -> // TODO: Implementar fornecedorScreen() com show, show(int), editar(int), cadastrar()
                    CLI.NYI("Tela de Fornecedores");
                case 0 -> System.out.println("Saindo...");
                default -> {
                    CLI.message("Opção inválida, por favor tente novamente.");
                }
            }
        }
        Control.logout();
    }

}
