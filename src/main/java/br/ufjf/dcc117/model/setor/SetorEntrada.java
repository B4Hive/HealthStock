package br.ufjf.dcc117.model.setor;

import java.util.List;

import br.ufjf.dcc117.model.Auxiliar;
import br.ufjf.dcc117.model.estoque.Estoque;
import br.ufjf.dcc117.model.estoque.Medicacao;
import br.ufjf.dcc117.model.estoque.Produto;

public class SetorEntrada extends Setor {

    public SetorEntrada(String nome, String senha, List<Pedido> pedidos, Estoque estoque) {
        super(nome, senha, pedidos, estoque);
    }

    @Override
    public void entradaProduto(Produto produto) {
        distribuirProduto(produto);
    }
    
    private void distribuirProduto(Produto produto) {
        if (produto instanceof Medicacao) {
            // TODO: Precisa de validação de lote e validade
            Setor farmacia = Setor.carregar(Auxiliar.SETOR_MEDICACAO);
            if (farmacia == null) {
                Auxiliar.error("Setor de Farmácia não encontrado para distribuição de medicamento: " + produto.getNome());
                return;
            }
            farmacia.entradaProduto(produto);
        } else {
            getEstoque().adicionarProduto(produto);
            getEstoque().salvar(getNome());
        }
    }

}
