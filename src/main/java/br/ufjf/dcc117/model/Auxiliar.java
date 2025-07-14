package br.ufjf.dcc117.model;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class Auxiliar {

    // << Constantes >>

    private static final String DIR_SEPARATOR = System.getProperty("file.separator");
    public static final String PATH[] = {"src","main","resources"};
    public static final String SETOR_MEDICACAO = "farmacia";
    public static final String SETOR_ENTRADA = "almoxarifado";
    public static final String SETOR_CADASTRO = "compras";
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    // << Métodos Auxiliar >>

    public static String path(String setor, String arquivo, String tipo) {
        if (setor == null || arquivo == null || tipo == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (String dir : PATH) {
            sb.append(dir).append(DIR_SEPARATOR);
        }
        sb.append(setor).append(DIR_SEPARATOR).append(arquivo).append(".").append(tipo);
        return sb.toString();
    }

    public static void checkFile(File file) {
        if (!file.exists()) {
            try {
                file.getParentFile().mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                System.err.println("Erro ao criar arquivo: " + file.getAbsolutePath());
                System.err.println("Mensagem de erro: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    // TODO: Implementar lógica de criptografia (talvez)

    public static String encrypt(String senha) {
        return senha;
    }

    public static String decrypt(String senha) {
        return senha;
    }

}
