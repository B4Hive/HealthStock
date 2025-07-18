package br.ufjf.dcc117;

import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.util.Scanner;

import br.ufjf.dcc117.cliView.loginScreen;

public class HealthStock {
    @SuppressWarnings({ "ConvertToTryWithResources"})
    public static void main(String[] args) throws FileNotFoundException {
        PrintStream logErro = new PrintStream("erros.log");
        System.setErr(logErro);
        Scanner in = new Scanner(System.in);
        loginScreen.show(in);
        in.close();
    }
}
