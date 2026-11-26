
package com.sistemaReclutador.sistemaReclutador.config.security;

/**
 * Define las operaciones necesarias para gestionar el mecanismo
 * criptográfico utilizado en las conexiones JDBC.
 *
 * <p>La interfaz permite configurar la ruta de la clave pública
 * y decodificar las contraseñas utilizadas por el DataSource.</p>
 *
 * @author Sistema Reclutador
 * @version 1.0
 */
public interface CipherService {

    /**
     * Configura la ubicación del archivo de clave pública.
     *
     * @param publicKeyPath ruta del archivo de clave pública.
     */
    void setPublicKeyPath(String publicKeyPath);

    /**
     * Decodifica una contraseña previamente cifrada.
     *
     * @param encryptedPassword contraseña cifrada.
     * @return contraseña decodificada.
     */
    String decode(String encryptedPassword);
}
