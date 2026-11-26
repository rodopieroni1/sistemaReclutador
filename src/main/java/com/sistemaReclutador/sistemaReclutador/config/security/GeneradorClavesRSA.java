
package com.sistemaReclutador.sistemaReclutador.config.security;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.util.Base64;

/**
 * Genera un par de claves RSA de 2048 bits para las pruebas
 * locales del mecanismo de protección de contraseñas.
 *
 * @author Sistema Reclutador
 * @version 1.0
 */
public class GeneradorClavesRSA {

    /**
     * Genera y guarda las claves pública y privada.
     *
     * @param args argumentos de ejecución.
     * @throws Exception si ocurre un error al generar o guardar las claves.
     */
    public static void main(String[] args) throws Exception {

        Path carpetaClaves = Path.of("keys");

        Files.createDirectories(carpetaClaves);

        KeyPairGenerator generador = KeyPairGenerator.getInstance("RSA");
        generador.initialize(2048);

        KeyPair parClaves = generador.generateKeyPair();

        String clavePublica = convertirPEM(
                "PUBLIC KEY",
                parClaves.getPublic().getEncoded());

        String clavePrivada = convertirPEM(
                "PRIVATE KEY",
                parClaves.getPrivate().getEncoded());

        Files.writeString(
                carpetaClaves.resolve("public.key"),
                clavePublica,
                StandardCharsets.UTF_8);

        Files.writeString(
                carpetaClaves.resolve("private.key"),
                clavePrivada,
                StandardCharsets.UTF_8);

        System.out.println("Claves RSA generadas correctamente.");
    }

    /**
     * Convierte los bytes de una clave al formato PEM.
     *
     * @param tipo tipo de clave.
     * @param contenido bytes codificados de la clave.
     * @return contenido de la clave en formato PEM.
     */
    private static String convertirPEM(String tipo, byte[] contenido) {

        String base64 = Base64.getMimeEncoder(64, new byte[] {'\n'})
                .encodeToString(contenido);

        return "-----BEGIN " + tipo + "-----\n"
                + base64 + "\n"
                + "-----END " + tipo + "-----\n";
    }
}
