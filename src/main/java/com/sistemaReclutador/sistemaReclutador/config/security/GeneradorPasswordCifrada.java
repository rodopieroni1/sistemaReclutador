
package com.sistemaReclutador.sistemaReclutador.config.security;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;
import java.security.spec.MGF1ParameterSpec;

import javax.swing.JOptionPane;
import javax.swing.JPasswordField;

/**
 * Genera una contraseña cifrada utilizando la clave pública RSA.
 */
public class GeneradorPasswordCifrada {

    /**
     * Solicita la contraseña y genera su versión cifrada.
     *
     * @param args argumentos de ejecución
     * @throws Exception si ocurre un error durante el cifrado
     */
    public static void main(String[] args) throws Exception {

        JPasswordField campoPassword = new JPasswordField();

        int opcion = JOptionPane.showConfirmDialog(
                null,
                campoPassword,
                "Ingresar contraseña del DataSource",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (opcion != JOptionPane.OK_OPTION) {
            return;
        }

        char[] password = campoPassword.getPassword();

        try {
            String clavePublica = Files.readString(
                    Path.of("keys/public.key"),
                    StandardCharsets.UTF_8
            );

            clavePublica = clavePublica
                    .replace("-----BEGIN PUBLIC KEY-----", "")
                    .replace("-----END PUBLIC KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] datosClave = Base64.getDecoder().decode(clavePublica);

            PublicKey publicKey = KeyFactory.getInstance("RSA")
                    .generatePublic(new X509EncodedKeySpec(datosClave));

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");

            OAEPParameterSpec parametros = new OAEPParameterSpec(
                    "SHA-256",
                    "MGF1",
                    MGF1ParameterSpec.SHA256,
                    PSource.PSpecified.DEFAULT
            );

            cipher.init(Cipher.ENCRYPT_MODE, publicKey, parametros);

            byte[] passwordCifrada = cipher.doFinal(
                    new String(password).getBytes(StandardCharsets.UTF_8)
            );

            System.out.println("Contraseña cifrada:");
            System.out.println(Base64.getEncoder().encodeToString(passwordCifrada));

        } finally {
            java.util.Arrays.fill(password, '\0');
        }
    }
}
