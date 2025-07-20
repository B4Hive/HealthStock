package br.ufjf.dcc117.cliView;

import java.util.Scanner;

import br.ufjf.dcc117.controller.Control;

public class loginScreen {

    private static final Scanner in = new Scanner(System.in);

    public static void show() {
        while (true) {
            CLI.clear();

            System.out.println("+-------------+");
            System.out.println("| HealthStock |");
            System.out.println("+-------------+");
            System.out.println();
            System.out.println("Faça login para continuar.");
            System.out.println();

            System.out.print("Usuário: ");
            String username = in.nextLine();
            if (username.equalsIgnoreCase("exit") || username.equalsIgnoreCase("0")) {
                System.out.println("Saindo do login...");
                break;
            }
            System.out.print("Senha: ");
            String password = in.nextLine();
            System.out.println();
            if (password.equalsIgnoreCase("exit") || password.equalsIgnoreCase("0")) {
                System.out.println("Saindo do login...");
                break;
            }

            if (!Control.login(username, password)) {
                System.out.println("Usuário ou senha inválidos. Por favor, tente novamente.");
                CLI.pause();
            } else {
                System.out.println("Login bem-sucedido!");
                CLI.pause();
                homeScreen.show();
                System.out.println("Retornando à tela de login...");
            }
        }
        CLI.clear();
        System.out.println("Obrigado por usar o HealthStock!");
        System.out.println("Fechando o programa...");
    }

}
