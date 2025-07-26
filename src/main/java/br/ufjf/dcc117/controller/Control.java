package br.ufjf.dcc117.controller;

import br.ufjf.dcc117.model.Auxiliar;
import br.ufjf.dcc117.model.setor.Setor;
import br.ufjf.dcc117.model.setor.SetorCadastro;

public class Control {

    protected static Setor setor;

    public static boolean login(String nome, String senha) {
        setor = Setor.carregar(nome);
        if (setor == null) {
            Auxiliar.error("Control.login:Setor não encontrado: " + nome);
            return false;
        }
        return setor != null && setor.validarSenha(senha);
    }

    public static void logout() {
        // setor.salvar(); // A persistência agora é imediata, não precisa mais salvar no logout.
        setor = null;
    }

    public static boolean setorCadastro() {
        return setor instanceof SetorCadastro;
    }

    public static String getSetor() {
        return setor != null ? setor.getNome() : null;
    }

}