package br.ufjf.dcc117.controller;

import java.util.List;

import br.ufjf.dcc117.model.Auxiliar;
import br.ufjf.dcc117.model.estoque.Fornecedor;
import br.ufjf.dcc117.model.setor.Setor;
import br.ufjf.dcc117.model.setor.SetorCadastro;

public class FornecedorControl extends Control {

    public static String[] listarFornecedores() {
        SetorCadastro setorCadastro = (SetorCadastro) Setor.carregar(Auxiliar.SETOR_CADASTRO);

        List<Fornecedor> fornecedores = setorCadastro.getFornecedores();
        String[] lista = new String[fornecedores.size()];
        for (int i = 0; i < fornecedores.size(); i++) {
            lista[i] = fornecedores.get(i).getId() + " - " + fornecedores.get(i).getNome();
        }
        return lista;
    }
}