package br.ufjf.dcc117.cliView;

import java.util.Scanner;

public class loginScreen {

    @SuppressWarnings("ConvertToTryWithResources")
    public static void show(){
        Scanner in = new Scanner(System.in);
        while (true) {
            // TODO: limpar tela
            System.out.println("* HealthStock *");
            System.out.println("Login");
            System.out.print("Digite seu usuário: ");
            String usuario = in.nextLine();
            if (usuario.equalsIgnoreCase("exit")) break;
            // TODO: verificat lista de usuários cadastrados
            System.out.println("Digite sua senha:");
            String senha = in.nextLine();
            // TODO: verificar se a senha está correta
            System.out.println("Login realizado com sucesso!");
            System.out.println("Pressione Enter para continuar...");
            in.next();
            // TODO: redirecionar para homeScreen
        }
        in.close();
    }

}
