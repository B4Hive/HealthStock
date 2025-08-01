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
            CLI.printMenu("Produtos", produtos);
            System.out.print("\nEscolha um produto (pelo número) ou 0 para voltar:");
            choice = in.nextInt(); in.nextLine();
            if (choice > 0 && choice <= produtos.length) {
                try {
                    String idString = produtos[choice - 1].split(" ")[1];
                    int produtoId = Integer.parseInt(idString);
                    show(produtoId);
                } catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
                    CLI.message("Seleção inválida. Tente novamente.");
                }
            } else if (choice != 0) {
                CLI.message("Opção inválida.");
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
            
            String[] options = HomeControl.getProdutoOptions();
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
        if (!Control.setorCadastro()) {
            CLI.message("Você não tem permissão para editar produtos.");
            return;
        }

        String[] produtoOriginal = EstoqueControl.getProdutoMestre(produtoId);
        if (produtoOriginal == null) {
            CLI.message("Produto mestre não encontrado para edição.");
            return;
        }

        String novoNome = produtoOriginal[1];
        int novoFornecedorId = Integer.parseInt(produtoOriginal[3]);

        int choice = -1;
        while (choice != 0) {
            CLI.clear();
            System.out.println("Editando Produto: " + produtoOriginal[1]);
            System.out.println("\nValores Atuais:");
            System.out.println("1. Nome: " + novoNome);
            System.out.println("2. Fornecedor ID: " + novoFornecedorId);
            
            CLI.printMenu("\nO que você deseja alterar?", new String[]{"Nome", "Fornecedor"});
            System.out.println("3. Salvar e Sair");

            choice = in.nextInt(); in.nextLine();

            switch (choice) {
                case 1 -> {
                    System.out.print("Digite o novo nome: ");
                    novoNome = in.nextLine();
                }
                case 2 -> {
                    System.out.println("Selecione o novo fornecedor:");
                    String[] fornecedores = FornecedorControl.listarFornecedores();
                    CLI.printMenu("Fornecedores", fornecedores);
                    System.out.print("ID do novo fornecedor: ");
                    int idSelecionado = in.nextInt(); in.nextLine();
                    if (idSelecionado > 0 && idSelecionado <= fornecedores.length) {
                        novoFornecedorId = idSelecionado;
                    } else {
                        CLI.message("Seleção de fornecedor inválida. Nenhuma alteração feita.");
                    }
                }
                case 3 -> {
                    if (EstoqueControl.editarProduto(produtoId, novoNome, novoFornecedorId)) {
                        CLI.message("Produto editado com sucesso!");
                    } else {
                        CLI.message("Falha ao editar o produto.");
                    }
                    choice = 0; // Força a saída do loop
                }
                case 0 -> CLI.message("Edição cancelada.");
                default -> CLI.message("Opção inválida.");
            }
        }
    }

    public static boolean cadastroProduto(String nome) {
        CLI.clear();
        if (!Control.setorCadastro()){
            CLI.message("Setor não tem permissão para cadastrar produtos.");
            return false;
        }
        System.out.println("Cadastro de Produto");
        System.out.println("Nome do Produto: " + nome);
        CLI.printMenu("Tipo de Produto", new String[] { "Medicamento", "Produto" });
        int tipo = in.nextInt(); in.nextLine();
        if (tipo < 1 || tipo > 2) {
            CLI.message("Tipo inválido, operação cancelada.");
            return false;
        }
        String tipoProduto = tipo == 1 ? "Medicacao" : "Produto";
        System.out.println("Selecione o Fornecedor:");
        String[] fornecedores = FornecedorControl.listarFornecedores();
        CLI.printMenu("Fornecedores", fornecedores);
        int fornecedorId = in.nextInt(); in.nextLine();
        if (fornecedorId > 0 && fornecedorId <= fornecedores.length) {
            if (EstoqueControl.cadastroProduto(nome, fornecedorId, tipoProduto)){
                CLI.message("Produto cadastrado com sucesso.");
                return true;
            } else {
                CLI.message("Erro ao cadastrar produto.");
                return false;
            }
        } else if (fornecedorId == 0) {
            CLI.message("Cadastro de produto cancelado.");
            return false;
        } else {
            CLI.message("Fornecedor inválido, operação cancelada.");
            return false;
        }
    }

}
