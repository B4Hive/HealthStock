package br.ufjf.dcc117.controller;

public class CSV {

    // << Atributos >>
    private String nomeArquivo;

    // << Construtor >>
    public CSV(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    // << Getters e Setters >>
    public String getNomeArquivo() {
        return this.nomeArquivo;
    }

    public void setNomeArquivo(String nomeArquivo) {
        this.nomeArquivo = nomeArquivo;
    }

    // << Métodos CSV >>
    public void lerCSV() {
        // Implementar lógica para ler arquivo CSV
    }

    public void escreverCSV() {
        // Implementar lógica para escrever arquivo CSV
    }
    
}
