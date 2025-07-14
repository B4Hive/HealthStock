package br.ufjf.dcc117.model.estoque;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Medicacao extends Produto {

    // << Atributos >>

    private final String lote;
    private final Date validade;
    private String ultimoResponsavel;
    private Date dataUltimoResponsavel;

    // << Construtor >>

    public Medicacao(int id, String nome, int quantidade, int idFornecedor, String lote, Date validade, String ultimoResponsavel, Date dataUltimoResponsavel) {
        super(id, nome, quantidade, idFornecedor);
        this.lote = lote;
        this.validade = validade;
        this.ultimoResponsavel = ultimoResponsavel;
        this.dataUltimoResponsavel = dataUltimoResponsavel;
    }

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

    public static Medicacao carregar(String produto) {
        String[] partes = produto.split(",");
        if (partes.length != 9 || !partes[4].trim().equals("Medicacao")) return null;

        try {
            int id = Integer.parseInt(partes[0].trim());
            String nome = partes[1].trim();
            int quantidade = Integer.parseInt(partes[2].trim());
            int idFornecedor = Integer.parseInt(partes[3].trim());
            String lote = partes[5].trim();
            Date validade = sdf.parse(partes[6].trim());
            String ultimoResponsavel = partes[7].trim();
            Date dataUltimoResponsavel = sdf.parse(partes[8].trim());

            return new Medicacao(id, nome, quantidade, idFornecedor, lote, validade, ultimoResponsavel, dataUltimoResponsavel);
        } catch (NumberFormatException | ParseException e) {
            return null;
        }
    }

    public static String salvar(Medicacao medicacao) {
        StringBuilder sb = new StringBuilder();
        sb.append(medicacao.getId()).append(",");
        sb.append(medicacao.getNome()).append(",");
        sb.append(medicacao.getQuantidade()).append(",");
        sb.append(medicacao.getIdFornecedor()).append(",");
        sb.append("Medicacao").append(",");
        sb.append(medicacao.getLote()).append(",");
        sb.append(sdf.format(medicacao.getValidade())).append(",");
        sb.append(medicacao.getUltimoResponsavel()).append(",");
        sb.append(sdf.format(medicacao.getDataUltimoResponsavel()));
        return sb.toString();
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
