package br.ufjf.dcc117.model.estoque;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.ufjf.dcc117.model.Auxiliar;

public class Medicacao extends Produto {

    // << Atributos >>
    
    private String lote;
    private Date validade;
    private String ultimoResponsavel;
    private Date dataUltimoResponsavel;
    
    // << Construtor >>

    public Medicacao(int id, String nome, int quantidade, int idFornecedor, String setor, String lote, Date validade, String ultimoResponsavel, Date dataUltimoResponsavel) {
        super(id, nome, quantidade, idFornecedor, setor);
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
        if (validade == null) {
            Auxiliar.error("Medicacao.verificarValidade: Validade não definida.");
            return true;
        }
        Date hoje = new Date();
        return validade.after(hoje);
    }

    public void atualizarResponsavel(String responsavel) {
        setUltimoResponsavel(responsavel);
        setDataUltimoResponsavel(new Date());
    }

    @Override
    public Produto clone(int quantidade) {
        return new Medicacao(this.getID(), this.getNome(), quantidade, this.getIdFornecedor(), this.getSetor(), this.getLote(), this.getValidade(), this.getUltimoResponsavel(), this.getDataUltimoResponsavel());
    }

    public void atualizarDetalhes(String detalhes) {
        String[] partes = detalhes.split(" \\| ");
        if (partes.length == 2) {
            this.lote = partes[0].trim();
            try {
                this.validade = new SimpleDateFormat("yyyy-MM-dd").parse(partes[1].trim());
            } catch (ParseException ex) {
                Auxiliar.error("Medicacao.atualizarDetalhes: Erro ao atualizar validade: " + ex.getMessage());
            }
        }
    }

    @Override
    public String toCSV() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String validadeStr = getValidade() != null ? sdf.format(getValidade()) : "NULL";
        String dataUltimoResponsavelStr = getDataUltimoResponsavel() != null ? sdf.format(getDataUltimoResponsavel()) : "NULL";

        return String.format("%d,%s,%d,%d,%s,Medicacao,%s,%s,%s,%s",
                getID(), getNome(), getQuantidade(), getIdFornecedor(), getSetor(),
                getLote(), validadeStr, getUltimoResponsavel(), dataUltimoResponsavelStr);
    }

    public static Medicacao fromCSV(String[] values) {
        int id = Integer.parseInt(values[0]);
        String nome = values[1];
        int quantidade = Integer.parseInt(values[2]);
        int idFornecedor = Integer.parseInt(values[3]);
        String setor = values[4];
        String lote = "NULL".equals(values[6]) ? null : values[6];
        String ultimoResponsavel = "NULL".equals(values[8]) ? null : values[8];

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date validade = null;
        Date dataUltimoResponsavel = null;
        try {
            if (!"NULL".equals(values[7])) validade = sdf.parse(values[7]);
        } catch (ParseException e) {
            Auxiliar.error("Medicacao.fromCSV: Erro ao carregar validade: " + e.getMessage());
        }
        try {
            if (!"NULL".equals(values[9])) dataUltimoResponsavel = sdf.parse(values[9]);
        } catch (ParseException e) {
            Auxiliar.error("Medicacao.fromCSV: Erro ao carregar data do último responsável: " + e.getMessage());
        }

        return new Medicacao(id, nome, quantidade, idFornecedor, setor, lote, validade, ultimoResponsavel, dataUltimoResponsavel);
    }
}
