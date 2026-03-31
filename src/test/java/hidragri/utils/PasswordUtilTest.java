package hidragri.utils;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    public void testHashPasswordGeneraHashValido() {
        String passwordPlana = "AgricultorSecreto123!";
        String hashGenerado = PasswordUtil.hashPassword(passwordPlana);

        assertNotNull(hashGenerado, "El hash no debe ser nulo");
        assertNotEquals(passwordPlana, hashGenerado, "El hash debe ser diferente a la contraseña plana");
        assertTrue(hashGenerado.startsWith("$2a$"), "El hash debe tener el formato válido de BCrypt");
    }

    @Test
    public void testCheckPasswordValidacionExitosa() {
        String passwordPlana = "FincaTenerife2026";
        String hashGenerado = PasswordUtil.hashPassword(passwordPlana);
        assertTrue(PasswordUtil.checkPassword(passwordPlana, hashGenerado), "La validación debe ser exitosa");
    }
}