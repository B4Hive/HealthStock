package br.ufjf.dcc117.controller;

import br.ufjf.dcc117.model.setor.Setor;
import br.ufjf.dcc117.model.setor.SetorCadastro;
import br.ufjf.dcc117.model.setor.SetorEntrada;

public class Controller {
    
    private static Setor setor;

    public static boolean login(String nome, String senha) {
        setor = Setor.carregar(nome);
        return setor != null && setor.validarSenha(senha);
    }

    public static String[] getHomeOptions() {
        if (setor instanceof Setor || setor instanceof SetorEntrada) {
            return new String[] {
                "Estoque",
                "Pedidos",
                "NULL"
            };
        } else if (setor instanceof SetorCadastro) {
            return new String[] {
                "Produtos",
                "Pedidos",
                "Fornecedores"
            };
        }
        // Default case if no specific setor type matches
        return new String[] {
            "Error: Unknown Setor Type"
        };
    }

}
