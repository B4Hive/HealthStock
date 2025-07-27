package br.ufjf.dcc117.cliView;

import java.util.Scanner;

import br.ufjf.dcc117.controller.Control;
import br.ufjf.dcc117.controller.EstoqueControl;
import br.ufjf.dcc117.controller.PedidosControl;

public class pedidosScreen {

    private static final Scanner in = new Scanner(System.in);

    public static void show() {
        int choice = -99;
        while (choice != 0) {
            CLI.clear();
            String[] pedidos = PedidosControl.getListaPedidos();
            CLI.printMenu("Lista de Pedidos", pedidos);
            System.out.println("-1 - Gerar novo pedido");
            choice = in.nextInt(); in.nextLine();
            if (choice != 0) {
                if (choice == -1) {
                    pedidosScreen.gerarPedido();
                } else {
                    if (choice > 0 && choice <= pedidos.length) {
                        String idString = pedidos[choice - 1].split(" ")[1];
                        int pedidoId = Integer.parseInt(idString);
                        pedidosScreen.show(pedidoId);
                    } else {
                        CLI.message("Seleção inválida.");
                    }
                }
            }
        }
    }

    private static void show(int pedidoId) {
        String[] pedido = PedidosControl.getPedido(pedidoId);
        if (pedido == null) {
            CLI.message("Pedido não encontrado.");
            return;
        }

        int option = -1;
        while (option != 0) {
            CLI.clear();
            System.out.println("Detalhes do Pedido " + pedido[0] + ":");
            System.out.println("ID do Pedido: " + pedido[0]);
            System.out.println("Setor Solicitante: " + pedido[1]);
            System.out.println("Setor Responsavel: " + pedido[2]);
            System.out.println("Data do Pedido: " + pedido[3]);
            System.out.println("Produto: " + pedido[4]);
            System.out.println("Quantidade: " + pedido[5]);
            System.out.println("Status: " + pedido[6]);
            System.out.println("Detalhes: " + pedido[7]);
            CLI.printMenu("Opções", new String[] { "Aprovar Pedido", "Rejeitar Pedido" });
            option = in.nextInt(); in.nextLine();
            switch (option) {
                case 1 -> {
                    if (!pedido[6].equalsIgnoreCase("Pendente")) {
                        CLI.message("Pedido já foi respondido.");
                        return;
                    }
                    if (!Control.getSetor().equalsIgnoreCase(pedido[2])) {
                        CLI.message("Você não tem permissão para aprovar este pedido.");
                        return;
                    }
                    if (!EstoqueControl.verificarProduto(pedido[4])) {
                        if (Control.setorCadastro()) {
                            estoqueScreen.cadastroProduto(pedido[4]);
                            
                        } else {
                            CLI.message("Produto não encontrado e setor não tem permissão para cadastrar.");
                            return;
                        }
                    }
                    // Verifica se o produto precisa de detalhes E se os detalhes ainda não foram preenchidos
                    if (EstoqueControl.produtoPrecisaDeDetalhes(pedido[4]) && (pedido[7] == null || pedido[7].isEmpty() || pedido[7].equalsIgnoreCase("NULL"))){
                        System.out.println("Digite o Lote do produto:");
                        String lote = in.nextLine();
                        System.out.println("Digite a Validade do produto (formato: YYYY-MM-DD):");
                        String validade = in.nextLine();
                        pedido[7] = lote + " | " + validade;
                    }
                    String responsavel = null;
                    if(!pedido[5].equals("0")){ // pedido[5] é a quantidade
                        System.out.println();
                        System.out.print("Responsável: ");
                        responsavel = in.nextLine();
                    }
                    if (PedidosControl.respostaPedido(pedidoId, true, responsavel, pedido[7])) {
                        CLI.message("Pedido aprovado com sucesso.");
                        // TODO: em caso de cadastro, verificar se o cadastro foi concluído com sucesso antes de aprovar
                    } else {
                        CLI.message("Falha ao aprovar o pedido.");
                    }
                    return;
                }
                case 2 -> {
                    if (!pedido[6].equalsIgnoreCase("Pendente")) {
                        CLI.message("Pedido já foi respondido.");
                        return;
                    }
                    if (PedidosControl.respostaPedido(pedidoId, false, null, null)) {
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
        String[] produtos = EstoqueControl.listarProdutosCadastrados();
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
            if (PedidosControl.gerarPedido(nomeProduto)) {
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
        if (PedidosControl.gerarPedido(produtoId, quantidade)) {
            CLI.message("Pedido gerado com sucesso.");
        } else {
            CLI.message("Falha ao gerar pedido.");
        }
    }

}
