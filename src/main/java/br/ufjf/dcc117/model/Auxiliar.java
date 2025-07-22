package br.ufjf.dcc117.model;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Auxiliar {

    // << Constantes >>

    private static final String DIR_SEPARATOR = System.getProperty("file.separator");
    private static final String PATH[] = {"src","main","resources"};
    public static final String SETOR_MEDICACAO = "farmacia";
    public static final String SETOR_ENTRADA = "almoxarifado";
    public static final String SETOR_CADASTRO = "compras";
    public static final SimpleDateFormat SDF = new SimpleDateFormat("yyyy-MM-dd");

    // << Métodos Auxiliar >>

    public static String path(String setor, String arquivo, String tipo) {
        StringBuilder sb = new StringBuilder();
        if (setor == null && arquivo == null && tipo == null) {
            for (String dir : PATH) sb.append(dir).append(DIR_SEPARATOR);
            return sb.toString();
        }
        if (setor == null || arquivo == null || tipo == null) {
            return null;
        }
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
                Auxiliar.error("Erro ao criar arquivo: " + file.getAbsolutePath());
                Auxiliar.error("Mensagem de erro: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    public static void error(String message) {
        System.err.println(new Date() + ": " + message);
    }

    public static String encrypt(String senha) {
        return senha;
    }

    public static String decrypt(String senha) {
        return senha;
    }

}
