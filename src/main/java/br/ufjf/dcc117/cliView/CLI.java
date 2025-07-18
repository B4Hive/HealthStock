package br.ufjf.dcc117.cliView;

import java.io.IOException;
import java.util.Date;
import java.util.Scanner;

public class CLI {

    public static void clear() {
        try {
            if (System.getProperty("os.name").contains("Windows")) {
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            } else {
                new ProcessBuilder("clear").inheritIO().start().waitFor();
            }
        } catch (IOException | InterruptedException e) {
            System.err.println(new Date() + ": Error clearing the console: " + e.getMessage());
        }
    }

    public static void pause(Scanner in) {
        System.out.println("Press Enter to continue...");
        in.nextLine();
    }

    public static void printMenu(String title, String[] options) {
        System.out.println(title + ":");
        for (int i = 0; i < options.length; i++) {
            if (options[i] == null) continue; // Skip null options
            System.out.println((i + 1) + ". " + options[i]);
        }
        System.out.println("0. Exit");

    }

    public static void NYI(Scanner in) {
        clear();
        System.out.println("This feature is not yet implemented.");
        pause(in);
    }

}
