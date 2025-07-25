package br.ufjf.dcc117.controller;

import br.ufjf.dcc117.model.setor.SetorCadastro;

public class HomeControl extends Control {

    public static String[] getHomeOptions() {
        if (setor instanceof SetorCadastro) {
            return new String[] {
                    "Produtos",
                    "Pedidos",
                    "Fornecedores"
            };
        } else {
            return new String[] {
                    "Produtos",
                    "Pedidos"
            };
        }
    }

    public static String[] getProdutoOptions() {
        if (setor instanceof SetorCadastro) {
            return new String[] {
                    null,
                    "Editar Produto",
                    null
            };
        } else {
            return new String[] {
                    "Consumir Produto",
                    null,
                    "Gerar Pedido"
            };
        }
    }

}