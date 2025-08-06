package br.ufjf.dcc117.model.setor;

public class SetorEntrada extends Setor {

    public SetorEntrada(String nome) {
        super(nome);
    }

    // OS MÉTODOS ABAIXO ERAM DA ARQUITETURA ANTIGA E FORAM REMOVIDOS
    /*
    @Override
    public void entradaProduto(Produto produto) {
        if (produto.getTipo().equals("Medicacao")) {
            Setor setorMedicacao = Setor.carregar(Auxiliar.SETOR_MEDICACAO);
            setorMedicacao.entradaProduto(produto);
        } else {
            super.entradaProduto(produto);
        }
    }

    @Override
    public Produto retiradaProduto(int id, int quantidade) {
        Produto produto = super.retiradaProduto(id, quantidade);
        if (produto != null) {
            Setor setorDestino = Setor.carregar(produto.getSetor());
            setorDestino.entradaProduto(produto);
        }
        return produto;
    }
    */
}
