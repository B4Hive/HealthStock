package br.ufjf.dcc117.service;

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.ufjf.dcc117.model.Auxiliar;
import br.ufjf.dcc117.model.PersistenceService;
import br.ufjf.dcc117.model.estoque.Medicacao;
import br.ufjf.dcc117.model.estoque.Produto;
import br.ufjf.dcc117.model.setor.Pedido;
import br.ufjf.dcc117.model.setor.Setor;
import br.ufjf.dcc117.model.setor.SetorCadastro;

@Service
public class PedidoService {

    @Autowired
    private EstoqueService estoqueService;

    public List<Pedido> listarPedidos(Setor setor, String estadoFiltro) {
        List<Pedido> pedidos = setor.listarPedidos();
        if (estadoFiltro != null && !estadoFiltro.isEmpty()) {
            return pedidos.stream()
                    .filter(p -> estadoFiltro.equalsIgnoreCase(p.getEstado()))
                    .collect(Collectors.toList());
        }
        return pedidos;
    }

    public Pedido getPedido(int pedidoId) {
        return PersistenceService.carregarPedidos(p -> p.getId() == pedidoId).stream().findFirst().orElse(null);
    }

    public boolean gerarPedido(Setor setorSolicitante, int codigoProduto, int quantidade) {
        Produto produtoMestre = PersistenceService.carregarProdutos(p -> p.getCodigoProduto() == codigoProduto && p.getSetor().equalsIgnoreCase(Auxiliar.SETOR_CADASTRO)).stream().findFirst().orElse(null);
        if (produtoMestre == null) return false;

        String setorResponsavel = (produtoMestre instanceof Medicacao) ? Auxiliar.SETOR_MEDICACAO : Auxiliar.SETOR_ENTRADA;
        if (setorResponsavel.equals(setorSolicitante.getNome())) {
            setorResponsavel = Auxiliar.SETOR_CADASTRO;
        }

        int novoPedidoId = PersistenceService.getNextPedidoId();
        Pedido pedido = new Pedido(novoPedidoId, setorSolicitante.getNome(), setorResponsavel, produtoMestre.getNome(), quantidade);
        PersistenceService.salvarPedido(pedido);
        return true;
    }

    public boolean gerarPedido(Setor setorSolicitante, String nomeProduto) {
        int novoPedidoId = PersistenceService.getNextPedidoId();
        Pedido pedido = new Pedido(novoPedidoId, setorSolicitante.getNome(), Auxiliar.SETOR_CADASTRO, nomeProduto, 0);
        PersistenceService.salvarPedido(pedido);
        return true;
    }

    public boolean responderPedido(Setor setorResponsavel, int pedidoId, boolean aprovado, String responsavel, String detalhes, Integer fornecedorId, String tipoProduto) {
        Pedido pedido = getPedido(pedidoId);
        if (pedido == null || !pedido.getSetorResponsavel().equals(setorResponsavel.getNome())) return false;

        if (aprovado) {
            if (setorResponsavel instanceof SetorCadastro setorCadastro) {
                if (pedido.getQuantidade() == 0) { // Cadastro de novo produto
                    if (fornecedorId == null || tipoProduto == null) return false;
                    estoqueService.cadastrarNovoTipoProduto(setorCadastro, pedido.getProduto(), fornecedorId, tipoProduto);
                } else { // Compra e reposição de estoque
                    // O setor de compras gera o produto e o envia para o solicitante
                    // CORREÇÃO: Passa lote e validade (se houver) explicitamente.
                    // O campo 'detalhes' do formulário é usado para o lote.
                    // O campo 'tipoProduto' do formulário é usado para a data de validade.
                    if (!estoqueService.adicionarEstoqueParaSetor(pedido.getProduto(), pedido.getQuantidade(), pedido.getSetorSolicitante(), responsavel, detalhes, tipoProduto)) {
                        return false; // Falha ao gerar o novo estoque
                    }
                }
            } else {
                // Lógica FEFO (First-Expired, First-Out) para setores que gerenciam estoque (ex: almoxarifado)
                Optional<Produto> loteParaMoverOpt = PersistenceService.carregarProdutos(p ->
                        p.getSetor().equalsIgnoreCase(setorResponsavel.getNome()) &&
                        p.getNome().equalsIgnoreCase(pedido.getProduto()) &&
                        p.getQuantidade() >= pedido.getQuantidade()
                ).stream().min(Comparator.comparing(p -> {
                    if (p instanceof Medicacao m && m.getValidade() != null) {
                        return m.getValidade();
                    }
                    return new Date(Long.MAX_VALUE); // Produtos sem validade vão para o fim da fila
                }));

                if (loteParaMoverOpt.isEmpty()) return false; // Não há lote disponível com a quantidade necessária

                Produto loteParaMover = loteParaMoverOpt.get();
                String setorDestino = pedido.getSetorResponsavel().equalsIgnoreCase(Auxiliar.SETOR_CADASTRO) ? Auxiliar.SETOR_ENTRADA : pedido.getSetorSolicitante();
                if (!moverProduto(setorResponsavel, loteParaMover.getID(), pedido.getQuantidade(), setorDestino, responsavel, detalhes)) {
                    return false;
                }
            }
        }
        pedido.setDetalhes(detalhes != null ? detalhes : "NULL");
        setorResponsavel.aprovarPedido(pedido, aprovado);
        return true;
    }

    private boolean moverProduto(Setor setorOrigem, int instanciaId, int quantidade, String nomeSetorDestino, String responsavel, String detalhes) {
        Setor setorDestino = Setor.carregar(nomeSetorDestino);
        if (setorDestino == null) return false;

        Produto produtoRetirado = setorOrigem.getProduto(instanciaId);
        if (produtoRetirado == null || produtoRetirado.getQuantidade() < quantidade) return false;

        // Diminui a quantidade no setor de origem
        produtoRetirado.setQuantidade(produtoRetirado.getQuantidade() - quantidade);
        if (produtoRetirado.getQuantidade() == 0) {
            PersistenceService.removerProduto(produtoRetirado);
        } else {
            PersistenceService.salvarProduto(produtoRetirado);
        }

        // Cria uma nova instância no setor de destino
        int novaInstanciaId = PersistenceService.getNextProdutoInstanceId();
        Produto produtoMovido;
        if (produtoRetirado instanceof Medicacao m) {
            produtoMovido = new Medicacao(novaInstanciaId, m.getCodigoProduto(), m.getNome(), quantidade, m.getIdFornecedor(), nomeSetorDestino, m.getLote(), m.getValidade(), responsavel, new Date());
            // Se for uma nova entrada do setor de compras, atualiza os detalhes do lote/validade
            if (setorOrigem.getNome().equalsIgnoreCase(Auxiliar.SETOR_CADASTRO)) {
                ((Medicacao) produtoMovido).atualizarDetalhes(detalhes);
            }
        } else {
            produtoMovido = new Produto(novaInstanciaId, produtoRetirado.getCodigoProduto(), produtoRetirado.getNome(), quantidade, produtoRetirado.getIdFornecedor(), nomeSetorDestino);
        }
        PersistenceService.salvarProduto(produtoMovido);
        return true;
    }
}