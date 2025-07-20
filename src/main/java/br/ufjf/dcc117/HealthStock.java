package br.ufjf.dcc117;

import java.io.FileNotFoundException;
import java.io.PrintStream;

import br.ufjf.dcc117.cliView.loginScreen;

public class HealthStock {

    public static void main(String[] args) throws FileNotFoundException {
        PrintStream logErro = new PrintStream("erros.log");
        System.setErr(logErro);
        loginScreen.show();
    }
    
}
