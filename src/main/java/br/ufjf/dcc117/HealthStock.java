package br.ufjf.dcc117;

import java.util.Scanner;

import br.ufjf.dcc117.cliView.loginScreen;

public class HealthStock {
    @SuppressWarnings("ConvertToTryWithResources")
    public static void main(String[] args) {
        // TODO: setar System.err para um arquivo de log
        // TODO: substituir os System.out.println() de erros por System.err.println()
        Scanner in = new Scanner(System.in);
        loginScreen.show(in);
        in.close();
    }
}
