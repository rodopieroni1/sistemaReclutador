
package com.sistemaReclutador.sistemaReclutador.config.security;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.MGF1ParameterSpec;
import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.OAEPParameterSpec;
import javax.crypto.spec.PSource;

import org.springframework.stereotype.Service;

/**
 * Implementa el descifrado de contraseñas mediante RSA.
 *
 * @author Sistema Reclutador
 * @version 1.0
 */
@Service
public class CipherServiceImpl implements CipherService {

    /**
     * Ruta del archivo de clave privada RSA.
     */
    private String publicKeyPath;

    /**
     * Configura la ruta de la clave utilizada para descifrar.
     *
     * @param publicKeyPath ruta configurada para la clave.
     */
    @Override
    public void setPublicKeyPath(String publicKeyPath) {
        this.publicKeyPath = publicKeyPath;
    }

    /**
     * Descifra una contraseña cifrada mediante RSA-OAEP SHA-256.
     *
     * @param encryptedPassword contraseña cifrada en Base64.
     * @return contraseña original.
     * @throws IllegalArgumentException si la contraseña es inválida.
     * @throws IllegalStateException si ocurre un error de lectura
     *                                o descifrado.
     */
    @Override
    public String decode(String encryptedPassword) {

        if (encryptedPassword == null || encryptedPassword.isBlank()) {
            throw new IllegalArgumentException(
                    "La contraseña cifrada no puede estar vacía.");
        }

        if (publicKeyPath == null || publicKeyPath.isBlank()) {
            throw new IllegalStateException(
                    "No se configuró la ruta de la clave privada.");
        }

        try {
            String contenido = Files.readString(
                    Path.of(publicKeyPath), StandardCharsets.UTF_8);

            String encodedKey = contenido
                    .replace("-----BEGIN PRIVATE KEY-----", "")
                    .replace("-----END PRIVATE KEY-----", "")
                    .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(encodedKey);

            PKCS8EncodedKeySpec keySpec =
                    new PKCS8EncodedKeySpec(keyBytes);

            PrivateKey privateKey = KeyFactory.getInstance("RSA")
                    .generatePrivate(keySpec);

            Cipher cipher = Cipher.getInstance("RSA/ECB/OAEPPadding");

            OAEPParameterSpec parametros = new OAEPParameterSpec(
                    "SHA-256", "MGF1",
                    MGF1ParameterSpec.SHA256,
                    PSource.PSpecified.DEFAULT);

            cipher.init(Cipher.DECRYPT_MODE, privateKey, parametros);

            byte[] encryptedBytes = Base64.getDecoder()
                    .decode(encryptedPassword.trim());

            byte[] decryptedBytes = cipher.doFinal(encryptedBytes);

            return new String(decryptedBytes, StandardCharsets.UTF_8);

        } catch (IOException | GeneralSecurityException
                | IllegalArgumentException e) {
            throw new IllegalStateException(
                    "No se pudo descifrar la contraseña.", e);
        }
    }
}
