package br.ufjf.dcc117.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.ufjf.dcc117.model.PersistenceService;
import br.ufjf.dcc117.model.estoque.Medicacao;
import br.ufjf.dcc117.model.estoque.Produto;
import br.ufjf.dcc117.model.setor.Setor;
import br.ufjf.dcc117.model.setor.SetorCadastro;

@Service
public class EstoqueService {

    public boolean deletarProdutoVencido(Setor setor, int produtoId) {
        Produto produto = setor.getProduto(produtoId); // produtoId aqui é o ID da instância
        if (produto instanceof Medicacao m && m.isVencido()) {
            PersistenceService.removerProduto(produto);
            return true;
        }
        return false;
    }

    public void deletarProdutosVencidos(Setor setor) {
        setor.removerProdutosVencidos();
    }

    public List<Produto> listarProdutos(Setor setor) {
        return setor.getProdutos();
    }

    public Produto getProduto(Setor setor, int id) { // id aqui é o ID da instância
        return setor.getProduto(id);
    }

    public boolean consumirProduto(Setor setor, int produtoId, int quantidadeConsumida) { // produtoId é o ID da instância
        Produto produto = setor.getProduto(produtoId);
        if (produto == null || produto.getQuantidade() < quantidadeConsumida) {
            return false;
        }
        if (produto instanceof Medicacao m && m.isVencido()) {
            return false; // Impede o consumo de produto vencido
        }

        produto.setQuantidade(produto.getQuantidade() - quantidadeConsumida);

        if (produto.getQuantidade() <= 0) {
            PersistenceService.removerProduto(produto);
        } else {
            PersistenceService.salvarProduto(produto);
        }
        return true;
    }

    public void cadastrarNovoTipoProduto(SetorCadastro setorCadastro, String nome, int fornecedorId, String tipoProduto) {
        // Esta função agora cria um novo TIPO de produto, mas sem estoque inicial.
        // A entrada de estoque será feita pela resposta a um pedido.
        int novoCodigo = PersistenceService.getNextProdutoCodigo();
        int novoIdInstancia = PersistenceService.getNextProdutoInstanceId();

        // Cria uma instância "mestre" com quantidade zero no setor de compras.
        Produto produtoMestre;
        if ("Medicacao".equalsIgnoreCase(tipoProduto)) {
            produtoMestre = new Medicacao(novoIdInstancia, novoCodigo, nome, 0, fornecedorId, setorCadastro.getNome(), "MESTRE", null, null, null);
        } else {
            produtoMestre = new Produto(novoIdInstancia, novoCodigo, nome, 0, fornecedorId, setorCadastro.getNome());
        }
        PersistenceService.salvarProduto(produtoMestre);
    }

    public boolean editarProdutoMestre(int codigoProduto, String novoNome, int novoFornecedorId) {
        // Encontra todos os lotes/instâncias com o mesmo código de produto
        List<Produto> produtosParaAtualizar = PersistenceService.carregarProdutos(p -> p.getCodigoProduto() == codigoProduto);
        if (produtosParaAtualizar.isEmpty()) {
            return false;
        }
        for (Produto p : produtosParaAtualizar) {
            p.atualizar(novoNome, novoFornecedorId);
            PersistenceService.salvarProduto(p);
        }
        return true;
    }
}