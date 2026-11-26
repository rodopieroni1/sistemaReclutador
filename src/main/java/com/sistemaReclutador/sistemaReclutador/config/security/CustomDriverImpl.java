package com.sistemaReclutador.sistemaReclutador.config.security;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import com.mysql.cj.jdbc.Driver;

/**
 * Driver JDBC que permite utilizar contraseñas
 * cifradas para establecer conexiones con MySQL.
 *
 * ntes de establecer la conexión, obtiene la ruta de la clave
 * configurada en database.properties y utiliza CipherService
 * para recuperar la contraseña original.</p>
 *
 * @author Sistema Reclutador
 * @version 1.0
 */
public class CustomDriverImpl extends Driver {

    /**
     * Servicio encargado de realizar el descifrado de la contraseña.
     */
    private final CipherService cipher = new CipherServiceImpl();
    /**
     * Construye el driver JDBC personalizado.
     *
     * @throws SQLException si ocurre un error al inicializar
     *                       el driver original de MySQL.
     */
    public CustomDriverImpl() throws SQLException {
        super();
    }

    /**
     * Establece una conexión con MySQL utilizando la contraseña descifrada previamente.
     *
     * @param url dirección JDBC de la base de datos.
     * @param info propiedades de conexión, incluyendo usuario y contraseña cifrada.
     * @return conexión establecida con MySQL.
     * @throws SQLException si ocurre un error al obtener la clave, descifrar la contraseña o conectar con MySQL.
     */ 
    
    //leido hasta aqui
    @Override
    public Connection connect(String url, Properties info) throws SQLException {

    	cipher.setPublicKeyPath(getKeyPath());

        String encryptedPassword = info.getProperty("password");

        if (encryptedPassword == null || encryptedPassword.isBlank()) {
            throw new SQLException("No se encontró la contraseña cifrada del DataSource.");
        }

        info.setProperty("password", cipher.decode(encryptedPassword));

        return super.connect(url, info);
    }

    /**
     * Obtiene la ruta del archivo de clave privada desde
     * database.properties.
     *
     * @return ruta configurada en la propiedad passwordKeyPath.
     * @throws SQLException si no se encuentra el archivo,
     *                      no puede leerse o no contiene la propiedad.
     */
    private String getKeyPath() throws SQLException {

        Properties properties = new Properties();

        try (InputStream input = CustomDriverImpl.class
                .getResourceAsStream("/database.properties")) {

            if (input == null) {
                throw new SQLException("No se encontró database.properties en resources.");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new SQLException("No se pudo leer database.properties.", e);
        }

        String keyPath = properties.getProperty("passwordKeyPath");

        if (keyPath == null || keyPath.isBlank()) {
            throw new SQLException("No se configuró passwordKeyPath en database.properties.");
        }

        return keyPath.trim();
    }
}
