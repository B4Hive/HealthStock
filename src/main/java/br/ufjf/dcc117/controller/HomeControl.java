package br.ufjf.dcc117.controller;

import br.ufjf.dcc117.model.setor.SetorCadastro;
import br.ufjf.dcc117.model.setor.SetorEntrada;

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
        } else if (setor instanceof SetorEntrada) {
            return new String[] {
                    "Consumir Produto",
                    null,
                    "Gerar Pedido"
            };
        }
        return new String[] {
                "Error: Unknown Setor Type"
        };
    }
}