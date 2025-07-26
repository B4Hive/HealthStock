package br.ufjf.dcc117.model.setor;

import br.ufjf.dcc117.model.Auxiliar;
import br.ufjf.dcc117.model.estoque.Medicacao;
import br.ufjf.dcc117.model.estoque.Produto;

public class SetorEntrada extends Setor {

    public SetorEntrada(String nome) {
        super(nome);
    }

    @Override
    public void entradaProduto(Produto produto) {
        distribuirProduto(produto);
    }
    
    private void distribuirProduto(Produto produto) {
        if (produto instanceof Medicacao) {
            Setor farmacia = Setor.carregar(Auxiliar.SETOR_MEDICACAO);
            if (farmacia != null) {
                farmacia.entradaProduto(produto);
            } else {
                Auxiliar.error("SetorEntrada.distribuirProduto: Setor de Farmácia não encontrado.");
            }
        } else {
            // Se não for medicação, adiciona ao estoque do próprio setor de entrada (almoxarifado)
            super.entradaProduto(produto);
        }
    }
}
