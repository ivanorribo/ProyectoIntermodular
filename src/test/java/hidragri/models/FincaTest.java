package hidragri.models;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class FincaTest {

    @Test
    public void testCreacionYGettersDeFinca() {
        Finca finca = new Finca(10, 1, "Finca La Esperanza", "Valle de Orotava", "12345ABCDE", 2.5, true);

        assertEquals(10, finca.getIdFinca());
        assertEquals("Finca La Esperanza", finca.getNombre());
        assertEquals("12345ABCDE", finca.getRefCatastral());
        assertTrue(finca.isEstadoActiva(), "La finca debe crearse como activa por defecto.");
    }

    @Test
    public void testToStringFormatoCorrecto() {
        Finca finca = new Finca(1, 1, "Finca Sur", "Arona", "REF-999", 5.0, true);
        String textoEsperado = "Finca Sur (REF-999)";

        assertEquals(textoEsperado, finca.toString(), "El método toString no devuelve el formato adaptado para los ComboBox.");
    }
}