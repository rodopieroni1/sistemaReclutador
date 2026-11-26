
package com.sistemaReclutador.sistemaReclutador.config.security;

/**
 * Comprueba el descifrado de la contraseña configurada para el DataSource.
 */
public class PruebaCifradoRSA {

    /**
     * Verifica que la contraseña cifrada pueda descifrarse.
     *
     * @param args argumentos de ejecución
     */
    public static void main(String[] args) {

        String contraseñaCifrada = "t/536yF2t6fJC28PQUZiWw5Z/1sqB0ekw8fFCqaNwFL1v84PJ8Rz2qTp+tHPagTr6CmEeQTLdgmkK8ZBQ39+OYcb5SYZGRFbAJ6gmnTSM10qrdWb2wyFTY0b8oQhjkAouxkxMAmjel4FZajrrOOzRqXt465UPjR1dl2BJ5VOi8wxcMLYG5upMBdqGKwcjxIfAecLjCkO4mMBYCZsR5qInuf2QJWHoYmhlMBDrvKMzCzV54GoytftWpS56VilB5lQ5qLl2B5RbdPEPBvX4XLi0ejDccyZClb45jGxNsz/jLQMRaxcGCxswhGIz2xXF5Lfg+tYPRJiPZiW4mm1wDSFcQ==";

        CipherServiceImpl servicio = new CipherServiceImpl();
        servicio.setPublicKeyPath("keys/private.key");

        String contraseñaDescifrada = servicio.decode(contraseñaCifrada);

        if ("root".equals(contraseñaDescifrada)) {
            System.out.println("PRUEBA EXITOSA: la contraseña descifrada coincide con root.");
        } else {
            System.out.println("ERROR: la contraseña descifrada no coincide con root.");
        }
    }
}
