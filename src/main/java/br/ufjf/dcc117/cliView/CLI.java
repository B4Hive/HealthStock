package br.ufjf.dcc117.cliView;

import java.io.IOException;
import java.util.Scanner;

import br.ufjf.dcc117.model.Auxiliar;

public class CLI {

    private static final Scanner in = new Scanner(System.in);

    public static void clear() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            Auxiliar.error("CLI.clear: Error clearing the console: " + e.getMessage());
        }
    }

    public static void pause() {
        System.out.println("Pressione Enter para continuar...");
        in.nextLine();
    }

    public static void printMenu(String title, String[] options) {
        System.out.println(title + ":");
        for (int i = 0; i < options.length; i++) {
            if (options[i] == null) continue; // Skip null options
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("0. Voltar");
    }

    public static void NYI() {
        clear();
        System.out.println("Esta funcionalidade ainda não foi implementada.");
        pause();
    }

    public static void NYI(String message) {
        clear();
        System.out.println("Esta funcionalidade ainda não foi implementada:" + message);
        pause();
    }

    public static void message(String message) {
        clear();
        System.out.println(message);
        pause();
    }
}
