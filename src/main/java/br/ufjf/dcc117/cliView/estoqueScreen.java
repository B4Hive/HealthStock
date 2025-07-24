package br.ufjf.dcc117.cliView;

import java.util.Scanner;

import br.ufjf.dcc117.controller.Control;
import br.ufjf.dcc117.controller.EstoqueControl;
import br.ufjf.dcc117.controller.FornecedorControl;
import br.ufjf.dcc117.controller.HomeControl;

public class estoqueScreen {

    private static final Scanner in = new Scanner(System.in);

    public static void show() {
        int choice = -1;
        while (choice != 0) {
            CLI.clear();
            String[] produtos = EstoqueControl.getListaProdutos();
            System.out.println("Lista de Produtos no Estoque:");
            for (String p : produtos) {
                System.out.println(p);
            }
            System.out.println("0 - Voltar ao menu anterior");
            System.out.print("\nEscolha um produto:");
            choice = in.nextInt(); in.nextLine();
            if (choice != 0) {
                show(choice);
            }
        }
    }

    public static void show(int produtoId) {
        int choice = -1;
        while (choice != 0) {
            CLI.clear();
            String[] produto = EstoqueControl.getProduto(produtoId);
            if (produto == null) {
                CLI.message("Produto não encontrado.");
                return;
            }
            String[] options = HomeControl.getProdutoOptions();
            System.out.println("Informações do Produto:");
            System.out.println("ID: " + produto[0]);
            System.out.println("Nome: " + produto[1]);
            System.out.println("Quantidade: " + produto[2]);
            System.out.println("Fornecedor: " + produto[3]);
            if (produto.length > 4) {
                System.out.println("Lote: " + produto[4]);
                System.out.println("Validade: " + produto[5]);
                System.out.println("Último Responsável: " + produto[6]);
                System.out.println("Última Movimentação: " + produto[7]);
            }
            System.out.println();
            
            CLI.printMenu("Opções do Produto", options);
            choice = in.nextInt(); in.nextLine();
            switch (choice) {
                case 1 -> {
                    System.out.println("Quantidade a consumir (Max:" + produto[2] + "):");
                    int quantidade = in.nextInt(); in.nextLine();
                    EstoqueControl.consumirProduto(produtoId, quantidade);
                    return;
                }
                case 2 -> {
                    editar(produtoId);
                    return;
                }
                case 3 -> {
                    pedidosScreen.gerarPedido(produtoId);
                    return;
                }
                case 0 -> {
                    // Voltar
                }
                default -> {
                    CLI.message("Opção inválida, tente novamente.");
                }
            }
        }
    }

    public static void editar(int produtoId) {
        CLI.NYI("Edição de produto"); // TODO: Implementar edição de produto
    }

    public static void cadastroProduto(String nome) {
        CLI.clear();
        if (!Control.setorCadastro()){
            CLI.message("Setor não tem permissão para cadastrar produtos.");
            return;
        }
        System.out.println("Cadastro de Produto");
        System.out.println("Nome do Produto: " + nome);
        CLI.printMenu("Tipo de Produto", new String[] { "Medicamento", "Produto" });
        int tipo = in.nextInt(); in.nextLine();
        if (tipo < 1 || tipo > 2) {
            CLI.message("Tipo inválido, operação cancelada.");
            return;
        }
        String tipoProduto = tipo == 1 ? "Medicacao" : "Produto";
        System.out.println("Selecione o Fornecedor:");
        String[] fornecedores = FornecedorControl.listarFornecedores();
        for (int i = 0; i < fornecedores.length; i++) {
            System.out.println((i + 1) + " - " + fornecedores[i]);
        }
        int fornecedorId = in.nextInt(); in.nextLine();
        if (fornecedorId < 1) {
            CLI.message("Fornecedor inválido, operação cancelada.");
            return;
        }
        if (EstoqueControl.cadastroProduto(nome, fornecedorId, tipoProduto)){
            CLI.message("Produto cadastrado com sucesso.");
        } else {
            CLI.message("Erro ao cadastrar produto.");
        }
    }

}
