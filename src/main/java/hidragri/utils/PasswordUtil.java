package hidragri.utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtil {
    public static String hashPassword(String plainPassword) {
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(12));
    }

    public static boolean checkPassword(String plainPassword, String hashedPassword) {
        if (hashedPassword == null || !hashedPassword.startsWith("$2a$") ) return false;
        return BCrypt.checkpw(plainPassword, hashedPassword);
    }

    // Metodo auxiliar para que puedas generar tu propia contraseña de prueba y guardarla en MySQL

    public static void main(String args) {
        String miContrasena = "admin123";
        String hashGenerado = hashPassword(miContrasena);
        System.out.println("Copia este hash e insértalo en tu base de datos: " + hashGenerado);
    }

}

