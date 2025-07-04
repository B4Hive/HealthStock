package br.ufjf.dcc117.model;

import java.util.Date;

public class Medicacao extends Produto {

    // << Atributos >>

    private final String lote;
    private final Date validade;
    private String ultimoResponsavel;
    private Date dataUltimoResponsavel;

    // << Construtor >>
    
    public Medicacao(int id, String nome, int quantidade, int idFornecedor, String lote, Date validade,
            String ultimoResponsavel, Date dataUltimoResponsavel) {
        super(id, nome, quantidade, idFornecedor);
        this.lote = lote;
        this.validade = validade;
        this.ultimoResponsavel = ultimoResponsavel;
        this.dataUltimoResponsavel = dataUltimoResponsavel;
    }

    // << Getters e Setters >>
    
    public String getLote() {
        return this.lote;
    }

    public Date getValidade() {
        return this.validade;
    }

    public String getUltimoResponsavel() {
        return this.ultimoResponsavel;
    }

    private void setUltimoResponsavel(String ultimoResponsavel) {
        this.ultimoResponsavel = ultimoResponsavel;
    }

    public Date getDataUltimoResponsavel() {
        return this.dataUltimoResponsavel;
    }

    private void setDataUltimoResponsavel(Date dataUltimoResponsavel) {
        this.dataUltimoResponsavel = dataUltimoResponsavel;
    }

    // << Métodos adicionais >>

    public boolean verificarValidade() {
        Date hoje = new Date();
        return validade.after(hoje);
    }

    public void atualizarResponsavel(String responsavel) {
        setUltimoResponsavel(responsavel);
        setDataUltimoResponsavel(new Date());
    }

}
