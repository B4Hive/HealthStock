package br.ufjf.dcc117.cliView;

import java.util.Scanner;

import br.ufjf.dcc117.controller.Control;

public class pedidosScreen {

    private static final Scanner in = new Scanner(System.in);

    public static void show() {
        int choice = -99;
        while (choice != 0) {
            CLI.clear();
            String[] pedidos = Control.getListaPedidos();
            System.out.println("Lista de Pedidos:");
            for (int i = 0; i < pedidos.length; i++) {
                System.out.println(i + 1 + " - " + pedidos[i]);
            }
            System.out.println("0 - Voltar ao menu anterior");
            System.out.println("-1 - Gerar novo pedido");
            System.out.print("\nEscolha um pedido:");
            choice = in.nextInt(); in.nextLine();
            if (choice != 0) {
                if (choice == -1) {
                    pedidosScreen.gerarPedido();
                } else {
                    pedidosScreen.show(choice - 1);
                }
            }
        }
    }

    private static void show(int pedidoId) {
        String[] pedido = Control.getPedido(pedidoId);
        if (pedido == null) {
            CLI.message("Pedido não encontrado.");
            return;
        }

        int option = -1;
        while (option != 0) {
            CLI.clear();
            System.out.println("Detalhes do Pedido " + (pedidoId + 1) + ":");
            System.out.println("Setor de Solicitante: " + pedido[0]);
            System.out.println("Setor de Responsavel: " + pedido[1]);
            System.out.println("Data do Pedido: " + pedido[2]);
            System.out.println("Produto: " + pedido[3]);
            System.out.println("Quantidade: " + pedido[4]);
            System.out.println("Status: " + pedido[5]);
            CLI.printMenu("Opções", new String[] { "Aprovar Pedido", "Rejeitar Pedido" });
            option = in.nextInt(); in.nextLine();
            switch (option) {
                case 1 -> {
                    if (!pedido[5].equalsIgnoreCase("Pendente")) {
                        CLI.message("Pedido já foi respondido.");
                        return;
                    }
                    if (!Control.getSetor().equalsIgnoreCase(pedido[1])) {
                        CLI.message("Você não tem permissão para aprovar este pedido.");
                        return;
                    }
                    if (!Control.verificarProduto(pedido[3])) {
                        if (Control.setorCadastro()) {
                            estoqueScreen.cadastroProduto(pedido[3]);
                        } else {
                            CLI.message("Produto não encontrado e setor não tem permissão para cadastrar.");
                            return;
                        }
                    }
                    if (Control.produtoPrecisaDeDetalhes(pedido[3])){
                        System.out.println("Digite o Lote do produto:");
                        String lote = in.nextLine();
                        System.out.println("Digite a Validade do produto (formato: YYYY-MM-DD):");
                        String validade = in.nextLine();
                        pedido[6] = lote + " | " + validade;
                    }
                    String responsavel = null;
                    if(!pedido[4].equals("0")){
                        System.out.println();
                        System.out.print("Responsável: ");
                        responsavel = in.nextLine();
                    }
                    if (Control.respostaPedido(pedidoId, true, responsavel, pedido[6])) {
                        CLI.message("Pedido aprovado com sucesso.");
                    } else {
                        CLI.message("Falha ao aprovar o pedido.");
                    }
                    return;
                }
                case 2 -> {
                    if (!pedido[5].equalsIgnoreCase("Pendente")) {
                        CLI.message("Pedido já foi respondido.");
                        return;
                    }
                    if (Control.respostaPedido(pedidoId, false, null, null)) {
                        CLI.message("Pedido rejeitado com sucesso.");
                    } else {
                        CLI.message("Falha ao rejeitar o pedido.");
                    }
                    return;
                }
                case 0 -> {
                }
                default -> CLI.message("Opção inválida.");
            }
        }
    }

    private static void gerarPedido() {
        CLI.clear();
        String[] produtos = Control.listarProdutosCadastrados();
        System.out.println("Produtos cadastrados:");
        for (String produto : produtos) {
            System.out.println("ID: " + produto);
        }
        System.out.println("ID: -1 - Não cadastrado");
        System.out.print("Digite o ID do produto ou 0 para cancelar: ");
        int produtoId = in.nextInt(); in.nextLine();
        if (produtoId == 0)
            return;
        if (produtoId == -1) {
            System.out.print("Digite o nome do produto: ");
            String nomeProduto = in.nextLine();
            if (Control.gerarPedido(nomeProduto)) {
                CLI.message("Pedido de cadastro gerado com sucesso.");
            } else {
                CLI.message("Falha ao gerar pedido de cadastro.");
            }
        } else {
            gerarPedido(produtoId);
        }
    }

    public static void gerarPedido(int produtoId) {
        System.out.print("Digite a quantidade: ");
        int quantidade = in.nextInt(); in.nextLine();
        if (Control.gerarPedido(produtoId, quantidade)) {
            CLI.message("Pedido gerado com sucesso.");
        } else {
            CLI.message("Falha ao gerar pedido.");
        }
    }

}
