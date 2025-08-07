package br.ufjf.dcc117.service;

import java.util.List;

import org.springframework.stereotype.Service;

import br.ufjf.dcc117.model.estoque.Fornecedor;
import br.ufjf.dcc117.model.setor.SetorCadastro;

@Service
public class FornecedorService {

    public List<Fornecedor> listarFornecedores(SetorCadastro setorCadastro) {
        return setorCadastro.getFornecedores();
    }

    public Fornecedor getFornecedor(SetorCadastro setorCadastro, int id) {
        return setorCadastro.getFornecedor(id);
    }

    public void cadastrarFornecedor(SetorCadastro setorCadastro, String nome, String cnpj, String telefone, String email, String endereco) {
        List<Fornecedor> fornecedores = setorCadastro.getFornecedores();
        int novoId = fornecedores.stream().mapToInt(Fornecedor::getId).max().orElse(0) + 1;
        fornecedores.add(new Fornecedor(novoId, nome, cnpj, telefone, email, endereco));
        setorCadastro.salvarFornecedores();
    }

    public void editarFornecedor(SetorCadastro setorCadastro, int id, String nome, String cnpj, String telefone, String email, String endereco) {
        Fornecedor fornecedor = setorCadastro.getFornecedor(id);
        if (fornecedor != null) {
            fornecedor.atualizar(nome, cnpj, telefone, email, endereco);
            setorCadastro.salvarFornecedores();
        }
    }

    public void removerFornecedor(SetorCadastro setorCadastro, int id) {
        List<Fornecedor> fornecedores = setorCadastro.getFornecedores();
        fornecedores.removeIf(f -> f.getId() == id);
        setorCadastro.salvarFornecedores();
    }
}