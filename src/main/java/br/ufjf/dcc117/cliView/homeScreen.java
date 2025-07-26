package br.ufjf.dcc117.cliView;

import java.util.Scanner;

import br.ufjf.dcc117.controller.Control;
import br.ufjf.dcc117.controller.HomeControl;

public class homeScreen {

    private static final Scanner in = new Scanner(System.in);

    public static void show() {
        String[] options = HomeControl.getHomeOptions();
        
        int choice = -1;
        while (choice != 0) {
            CLI.clear();
            CLI.printMenu(Control.getSetor().toUpperCase() + " Options",options);
            choice = in.nextInt(); in.nextLine();
            switch(choice){
                case 1 -> estoqueScreen.show();
                case 2 -> pedidosScreen.show();
                case 3 -> fornecedorScreen.show();
                case 0 -> System.out.println("Saindo...");
                default -> CLI.message("Opção inválida, por favor tente novamente.");
            }
        }
        Control.logout();
    }

}
