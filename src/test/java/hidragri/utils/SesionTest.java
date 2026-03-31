package hidragri.utils;

import hidragri.models.Usuario;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SesionTest {

    @Test
    public void testSingletonDevuelveMismaInstancia() {
        Sesion sesion1 = Sesion.getInstance();
        Sesion sesion2 = Sesion.getInstance();

        assertSame(sesion1, sesion2, "El patrón Singleton ha fallado, hay más de una sesión activa.");
    }

    @Test
    public void testManejoDeUsuarioLogueado() {
        Sesion sesion = Sesion.getInstance();
        Usuario usuarioMock = new Usuario(1, "pruebaUser", "hash123", "ADMIN");

        // Simular inicio de sesión
        sesion.setUsuarioLogueado(usuarioMock);
        assertNotNull(sesion.getUsuarioLogueado(), "El usuario debería estar guardado en sesión.");
        assertEquals("pruebaUser", sesion.getUsuarioLogueado().getUsername());

        // Simular cierre de sesión
        sesion.cerrarSesion();
        assertNull(sesion.getUsuarioLogueado(), "La sesión debería estar limpia tras hacer logout.");
    }
}