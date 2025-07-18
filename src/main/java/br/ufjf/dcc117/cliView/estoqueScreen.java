package br.ufjf.dcc117.cliView;

import java.util.Scanner;

import br.ufjf.dcc117.controller.Control;

public class estoqueScreen {

    public static void show(Scanner in) {
        int choice = -1;
        while (choice != 0) {
            CLI.clear();
            String[] produtos = Control.getListaProdutos();
            System.out.println("Lista de Produtos no Estoque:");
            for (String p : produtos) {
                System.out.println(p);
            }
            System.out.println("0 - Voltar ao menu anterior");
            System.out.print("\nEscolha um produto:");
            choice = in.nextInt(); in.nextLine();
            if (choice != 0) {
                produtoScreen.show(in, choice);
            }
        }

    }

}
