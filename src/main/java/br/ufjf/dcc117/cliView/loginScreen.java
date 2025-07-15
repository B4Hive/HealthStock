package br.ufjf.dcc117.cliView;

import java.util.Scanner;

import br.ufjf.dcc117.controller.Controller;

public class loginScreen {

    public static void show(Scanner in) {
        while (true) {
            CLI.clear();

            System.out.println("+-------------+");
            System.out.println("| HealthStock |");
            System.out.println("+-------------+");
            System.out.println();
            System.out.println("Please log in to continue.");
            System.out.println();
            System.out.print("Username: ");
            String username = in.nextLine();

            System.out.print("Password: ");
            String password = in.nextLine();
            System.out.println();

            if (username.equalsIgnoreCase("exit")
             || password.equalsIgnoreCase("exit")) {
                System.out.println("Exiting login...");
                break;
            }

            if (!Controller.login(username, password)) {
                System.out.println("Invalid username or password. Please try again.");
                CLI.pause(in);
            } else {
                System.out.println("Login successful!");
                CLI.pause(in);
                homeScreen.show(in);
                System.out.println("Returning to login screen...");
            }
        }
        System.out.println("Closing Program...");
    }

}
