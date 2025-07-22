package br.ufjf.dcc117.model.estoque;

import java.text.ParseException;
import java.util.Date;

import br.ufjf.dcc117.model.Auxiliar;

public class Medicacao extends Produto {

    // << Atributos >>
    
    private String lote;
    private Date validade;
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

    public static Medicacao carregar(String produto) {
        String[] partes = produto.split(",");
        if (partes.length != 9 || !partes[4].trim().equalsIgnoreCase("Medicacao")) return null;

        try {
            int id = Integer.parseInt(partes[0].trim());
            String nome = partes[1].trim();
            int quantidade = Integer.parseInt(partes[2].trim());
            int idFornecedor = Integer.parseInt(partes[3].trim());
            String lote = partes[5].trim();
            Date validade = null;
            try {
                validade = Auxiliar.SDF.parse(partes[6].trim());
            } catch (ParseException e) {
                Auxiliar.error("Erro ao carregar validade: " + e.getMessage());
            }
            String ultimoResponsavel = partes[7].trim();
            Date dataUltimoResponsavel = null;
            try {
                dataUltimoResponsavel = Auxiliar.SDF.parse(partes[8].trim());
            } catch (ParseException e) {
                Auxiliar.error("Erro ao carregar data do último responsável: " + e.getMessage());
            }

            return new Medicacao(id, nome, quantidade, idFornecedor, lote, validade, ultimoResponsavel, dataUltimoResponsavel);
        } catch (NumberFormatException e) {
            return null;
        }
    }
    
    @Override
    public String salvar() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getID()).append(",");
        sb.append(this.getNome()).append(",");
        sb.append(this.getQuantidade()).append(",");
        sb.append(this.getIdFornecedor()).append(",");
        sb.append("Medicacao").append(",");
        sb.append(this.getLote()).append(",");
        try {
            sb.append(Auxiliar.SDF.format(this.getValidade())).append(",");
        } catch (Exception e) {
            Auxiliar.error("Erro ao formatar validade: " + e.getMessage());
            sb.append("NULL").append(",");
        }
        sb.append(this.getUltimoResponsavel()).append(",");
        try {
            sb.append(Auxiliar.SDF.format(this.getDataUltimoResponsavel()));
        } catch (Exception e) {
            Auxiliar.error("Erro ao formatar data do último responsável: " + e.getMessage());
            sb.append("NULL");
        }
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

    @Override
    public Medicacao clone(int quantidade) {
        return new Medicacao(this.getID(), this.getNome(), quantidade, this.getIdFornecedor(), this.getLote(), this.getValidade(), this.getUltimoResponsavel(), this.getDataUltimoResponsavel());
    }

    public void atualizarDetalhes(String detalhes) {
        String[] partes = detalhes.split(" \\| ");
        if (partes.length == 2) {
            this.lote = partes[0].trim();
            try {
                this.validade = Auxiliar.SDF.parse(partes[1].trim());
            } catch (ParseException ex) {
                Auxiliar.error("Erro ao atualizar validade: " + ex.getMessage());
            }
        }
    }

}
