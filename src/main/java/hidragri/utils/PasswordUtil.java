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
    public static void main(String[] args) {
        String myPass = "Testing";
        String hashGenerado = hashPassword(myPass);
        System.out.println("Hash para contraseña en base de datos: " + hashGenerado);
    }

}

