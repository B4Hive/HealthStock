package br.ufjf.dcc117.model.setor;

import java.util.List;

import br.ufjf.dcc117.model.estoque.Estoque;
import br.ufjf.dcc117.model.estoque.Fornecedor;
import br.ufjf.dcc117.model.estoque.Produto;

public class SetorCadastro extends Setor{

    public SetorCadastro(String nome, String senha, List<Pedido> pedidos, Estoque estoque) {
        super(nome, senha, pedidos, estoque);
    }

    private void cadastroFornecedor(Fornecedor fornecedor) {

    }

    private void cadastroProduto(Produto produto) {
        
    }
}
