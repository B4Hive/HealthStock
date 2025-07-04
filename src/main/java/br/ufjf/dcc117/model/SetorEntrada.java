package br.ufjf.dcc117.model;

import java.util.List;

public class SetorEntrada extends Setor {

    // TODO: private final Setor setorMedicamentos;

    public SetorEntrada(String nome, String senha, List<Pedido> pedidos, Estoque estoque) {
        super(nome, senha, pedidos, estoque);
        // TODO: this.setorMedicamentos = Farmacia;
    }

    @Override
    public void entradaProduto(Produto produto) {
        distribuirProduto(produto);
    }

    private void distribuirProduto(Produto produto) {
        if (produto instanceof Medicacao) {
            // TODO: Transferir medicamento para o setor de medicamentos
        } else
        getEstoque().adicionarProduto(produto);
    }

}
