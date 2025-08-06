package br.ufjf.dcc117.model.estoque;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Medicacao extends Produto{
    private String lote;
    private Date validade;
    private String ultimoResponsavel;
    private Date dataUltimoResponsavel;

    public Medicacao(int ID, int codigoProduto, String nome, int quantidade, int idFornecedor, String setor, String lote, Date validade, String ultimoResponsavel, Date dataUltimoResponsavel) {
        super(ID, codigoProduto, nome, quantidade, idFornecedor, setor);
        this.lote = lote;
        this.validade = validade;
        this.ultimoResponsavel = ultimoResponsavel;
        this.dataUltimoResponsavel = dataUltimoResponsavel;
    }

    // Getters
    public String getLote() { return lote; }
    public Date getValidade() { return validade; }
    public String getUltimoResponsavel() { return ultimoResponsavel; }
    public Date getDataUltimoResponsavel() { return dataUltimoResponsavel; }

    public void atualizarDetalhes(String detalhes) {
        if (detalhes == null || detalhes.isBlank()) return;
        String[] parts = detalhes.split("\\s*\\|\\s*");
        if (parts.length >= 2) {
            this.lote = parts[0];
            try {
                this.validade = new SimpleDateFormat("yyyy-MM-dd").parse(parts[1]);
            } catch (ParseException e) {
                this.validade = new Date();
            }
        }
    }

    public void atualizarResponsavel(String nome) {
        this.ultimoResponsavel = nome;
        this.dataUltimoResponsavel = new Date();
    }

    @Override
    public String toCSV() {
        // Formata as datas para o padrão do CSV, tratando valores nulos.
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat sdfDateTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

        String validadeStr = this.validade != null ? sdfDate.format(this.validade) : "NULL";
        String dataUltimoResponsavelStr = this.dataUltimoResponsavel != null ? sdfDateTime.format(this.dataUltimoResponsavel) : "NULL";
        String ultimoResponsavelStr = this.ultimoResponsavel != null ? this.ultimoResponsavel : "NULL";
        String loteStr = this.lote != null ? this.lote : "NULL";

        // CORREÇÃO: Gera a linha CSV completa com os dados específicos da medicação.
        return String.format("%d,%d,%s,%d,%d,%s,%s,%s,%s,%s,%s",
                this.getID(),
                this.getCodigoProduto(),
                this.getNome(),
                this.getQuantidade(),
                this.getIdFornecedor(),
                this.getSetor(),
                "Medicacao", // tipo
                loteStr,
                validadeStr,
                ultimoResponsavelStr,
                dataUltimoResponsavelStr
        );
    }

    public boolean isVencido() {
        if (this.validade == null) {
            return false;
        }
        return new Date().after(this.validade);
    }

    public boolean isProximoVencimento() {
        if (this.validade == null || isVencido()) {
            return false;
        }
        long diffInMillis = Math.abs(this.validade.getTime() - new Date().getTime());
        long diffInDays = TimeUnit.DAYS.convert(diffInMillis, TimeUnit.MILLISECONDS);
        return diffInDays <= 7;
    }
}
