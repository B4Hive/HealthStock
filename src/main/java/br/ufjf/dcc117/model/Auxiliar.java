package br.ufjf.dcc117.model;

import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
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
                Auxiliar.error("Auxiliar.checkFile: Erro ao criar arquivo: " + file.getAbsolutePath() + "; Mensagem de erro: " + e.getMessage());
                System.exit(1);
            }
        }
    }

    public static void error(String message) {
        System.err.println(new Date() + ": " + message);
    }

    public static String encrypt(String setor, String senha) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(senha.getBytes());
            return bytesToHex(md.digest());
        } catch (NoSuchAlgorithmException e) {
            error("Auxiliar.encrypt: " + e.getMessage());
            return null;
        }
    }

    public static void senhas() {
        // Método para manipulação de senhas, a ser implementado conforme necessidade
    }

    public static String decrypt(String senha) {
        // A descriptografia de um hash MD5 não é possível, este método é conceitual.
        return senha;
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}
