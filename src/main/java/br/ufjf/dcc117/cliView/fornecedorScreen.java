package br.ufjf.dcc117.cliView;

import java.util.Scanner;

import br.ufjf.dcc117.controller.Control;
import br.ufjf.dcc117.controller.FornecedorControl;

public class fornecedorScreen {

    private static final Scanner in = new Scanner(System.in);

    public static void show(){
        int choice = -99;
        while (choice != 0) {
            CLI.clear();
            String[] fornecedores = FornecedorControl.listarFornecedores();
            CLI.printMenu("Fornecedores", fornecedores);
            System.out.println("-1 - Cadastrar Fornecedor");
            System.out.println("Selecione um fornecedor para ver detalhes ou 0 para sair:");
            choice = in.nextInt(); in.nextLine();
            if (choice > 0 && choice <= fornecedores.length) {
                show(choice);
            } else if (choice == -1) {
                cadastroFornecedor();
            }
        }
    }

    public static void show(int fornecedorId) {
        int choice = -1;
        while (choice != 0) {
            CLI.clear();
            String[] fornecedor = FornecedorControl.getFornecedor(fornecedorId);
            if (fornecedor == null) {
                CLI.message("Fornecedor não encontrado.");
                return;
            }
            System.out.println("Detalhes do Fornecedor:");
            System.out.println("ID: " + fornecedor[0]);
            System.out.println("Nome: " + fornecedor[1]);
            System.out.println("CNPJ: " + fornecedor[2]);
            System.out.println("Telefone: " + fornecedor[3]);
            System.out.println("Endereço: " + fornecedor[4]);
            System.out.println("Email: " + fornecedor[5]);
            System.out.println();

            CLI.printMenu("Opções do Fornecedor", new String[]{"Editar"});
            choice = in.nextInt(); in.nextLine();
            switch (choice) {
                case 1 -> editarFornecedor(fornecedorId);
                case 0 -> {
                    // Voltar
                }
                default -> CLI.message("Opção inválida, tente novamente.");
            }
        }
    }

    public static void editarFornecedor(int fornecedorId) {
        CLI.NYI("Editar Fornecedor"); // TODO: Implementar edição de fornecedor
    }

    public static void cadastroFornecedor() {
        if (!Control.setorCadastro()){
            CLI.message("Setor não tem permissão para cadastrar produtos.");
            return;
        }
        CLI.clear();
        System.out.println("Cadastro de Fornecedor");
        System.out.print("Nome do Fornecedor: ");
        String nome = in.nextLine();
        System.out.print("CNPJ do Fornecedor: ");
        String cnpj = in.nextLine();
        System.out.print("Telefone do Fornecedor: ");
        String telefone = in.nextLine();
        System.out.print("Endereço do Fornecedor: ");
        String endereco = in.nextLine();
        System.out.print("Email do Fornecedor: ");
        String email = in.nextLine();

        CLI.clear();
        System.out.println("Dados do Fornecedor:");
        System.out.println("Nome: " + nome);
        System.out.println("CNPJ: " + cnpj);
        System.out.println("Telefone: " + telefone);
        System.out.println("Endereço: " + endereco);
        System.out.println("Email: " + email);
        CLI.printMenu("Cadastrar Fornecedor", new String[]{"Confirmar"});
        int confirm = in.nextInt(); in.nextLine();
        if (confirm == 1) {
            if (FornecedorControl.cadastrarFornecedor(nome, cnpj, telefone, endereco, email)) {
                CLI.message("Fornecedor cadastrado com sucesso!");
            } else {
                CLI.message("Erro ao cadastrar fornecedor.");
            }
        } else {
            CLI.message("Cadastro cancelado.");
        }
    }

}
