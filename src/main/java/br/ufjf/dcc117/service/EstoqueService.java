package br.ufjf.dcc117.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.stereotype.Service;

import br.ufjf.dcc117.model.Auxiliar;
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

    public Produto adicionarProduto(Setor setor, String nome, int quantidade, int fornecedorId, String tipoProduto, String detalhes) {
        int novoCodigo = PersistenceService.getNextProdutoCodigo();
        int novoIdInstancia = PersistenceService.getNextProdutoInstanceId();
        Produto novoProduto;

        if ("Medicacao".equalsIgnoreCase(tipoProduto)) {
            // Para medicamentos, extrai lote e validade dos detalhes, se disponíveis
            String lote = detalhes != null && detalhes.contains("Lote:") ? detalhes.split("Lote:")[1].split(",")[0].trim() : "N/A";
            Date validade = null;
            if (detalhes != null && detalhes.contains("Validade:")) {
                try {
                    String dataStr = detalhes.split("Validade:")[1].trim();
                    validade = new SimpleDateFormat("yyyy-MM-dd").parse(dataStr);
                } catch (ParseException e) { /* ignora erro de parse */ }
            }
            novoProduto = new Medicacao(novoIdInstancia, novoCodigo, nome, quantidade, fornecedorId, setor.getNome(), lote, validade, "Responsavel", new Date());
        } else {
            novoProduto = new Produto(novoIdInstancia, novoCodigo, nome, quantidade, fornecedorId, setor.getNome());
        }

        PersistenceService.salvarProduto(novoProduto);
        return novoProduto;
    }

    public boolean adicionarEstoqueParaSetor(String nomeProduto, int quantidade, String nomeSetorDestino, String responsavel, String detalhes) {
        // Encontra o produto "mestre" no cadastro para obter o código e fornecedor
        Produto produtoMestre = PersistenceService.carregarProdutos(p -> 
            p.getNome().equalsIgnoreCase(nomeProduto) && p.getSetor().equalsIgnoreCase(Auxiliar.SETOR_CADASTRO)
        ).stream().findFirst().orElse(null);

        if (produtoMestre == null) {
            return false; // Não é possível adicionar estoque de um produto não cadastrado
        }

        int novaInstanciaId = PersistenceService.getNextProdutoInstanceId();
        Produto produtoAdicionado;

        if (produtoMestre instanceof Medicacao m) {
            // Extrai lote e validade dos detalhes, se houver
            String lote = detalhes != null && detalhes.contains("Lote:") ? detalhes.split("Lote:")[1].split(",")[0].trim() : "N/A";
            Date validade = null;
            if (detalhes != null && detalhes.contains("Validade:")) {
                try {
                    String dataStr = detalhes.split("Validade:")[1].trim();
                    validade = new SimpleDateFormat("yyyy-MM-dd").parse(dataStr);
                } catch (ParseException e) { /* ignora erro de parse */ }
            }
            produtoAdicionado = new Medicacao(novaInstanciaId, m.getCodigoProduto(), m.getNome(), quantidade, m.getIdFornecedor(), nomeSetorDestino, lote, validade, responsavel, new Date());
        } else {
            produtoAdicionado = new Produto(novaInstanciaId, produtoMestre.getCodigoProduto(), produtoMestre.getNome(), quantidade, produtoMestre.getIdFornecedor(), nomeSetorDestino);
        }
        
        PersistenceService.salvarProduto(produtoAdicionado);
        return true;
    }
}