package br.ufjf.dcc117.controller;

import java.util.List;

import br.ufjf.dcc117.cliView.CLI;
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

    public static String[] getFornecedor(int fornecedorId) {
        SetorCadastro setorCadastro = (SetorCadastro) Setor.carregar(Auxiliar.SETOR_CADASTRO);
        Fornecedor fornecedor = setorCadastro.getFornecedor(fornecedorId);
        if (fornecedor == null) {
            return null;
        }
        return new String[]{
            String.valueOf(fornecedor.getId()),
            fornecedor.getNome(),
            fornecedor.getCnpj(),
            fornecedor.getTelefone(),
            fornecedor.getEndereco(),
            fornecedor.getEmail()
        };
    }

    public static boolean cadastrarFornecedor(String nome, String cnpj, String telefone, String endereco, String email) {
        SetorCadastro setorCadastro = (SetorCadastro) Setor.carregar(Auxiliar.SETOR_CADASTRO);
        if (setorCadastro == null) {
            CLI.message("Erro ao carregar o setor de cadastro.");
            return false;
        }
        setorCadastro.cadastroFornecedor(nome, cnpj, telefone, endereco, email);
        return true;
    }

    public static boolean editarFornecedor(int id, String nome, String cnpj, String telefone, String endereco, String email) {
        if (!(setor instanceof SetorCadastro)) {
            Auxiliar.error("FornecedorControl.editarFornecedor: Apenas o setor de cadastro pode editar fornecedores.");
            return false;
        }
        SetorCadastro setorCadastro = (SetorCadastro) setor;
        return setorCadastro.editarFornecedor(id, nome, cnpj, telefone, endereco, email);
    }
}