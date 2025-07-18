package br.ufjf.dcc117.cliView;

import java.util.Scanner;

import br.ufjf.dcc117.controller.Control;

public class homeScreen {

    public static void show(Scanner in) {
        String[] options = Control.getHomeOptions();
        
        int choice = -1;
        while (choice != 0) {
            CLI.clear();
            CLI.printMenu("Home Options",options);
            choice = in.nextInt(); in.nextLine();
            switch(choice){
                case 1 -> estoqueScreen.show(in);
                case 2 -> pedidosScreen.show(in);
                case 3 -> // TODO: Implement fornecedorScreen(in);
                    CLI.NYI(in);
                case 0 -> System.out.println("Exiting...");
                default -> {
                    System.out.println("Invalid option, please try again.");
                    CLI.pause(in);
                }
            }
        }
        Control.logout();
    }

}
