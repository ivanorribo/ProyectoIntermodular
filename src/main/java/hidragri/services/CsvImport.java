package hidragri.services;

import hidragri.crud.RiegoDAO;
import hidragri.models.Riego;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;
import javafx.concurrent.Task;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class CsvImport extends Task<Integer> {
    private final File archivoFuente;
    private final int idParcelaDestino;
    private final RiegoDAO riegoDAO = new RiegoDAO();

    public CsvImport(File archivoFuente, int idParcelaDestino) {
        this.archivoFuente = archivoFuente;
        this.idParcelaDestino = idParcelaDestino;
    }

    @Override
    protected Integer call() throws Exception {
        int contadorInserciones = 0;

        // Implementación del patrón try-with-resources para garantizar el cierre del flujo de I/O
        try (CSVReader lector = new CSVReader(new FileReader(archivoFuente))) {
            String filaActual[];

            // Se avanza el cursor inicial evadiendo la primera línea que contiene las cabeceras descriptivas
            lector.readNext();

            // Adaptación a OpenCSV 5.12.0 requiriendo la gestión potencial de CsvValidationException
            while ((filaActual = lector.readNext())!= null) {
                // Punto de interrupción cooperativo para detener el hilo si la UI cancela la tarea
                if (isCancelled()) break;

                // Defensa estructural inicial contra filas truncadas o líneas en blanco remanentes
                if (filaActual.length < 3) continue;

                try {
                    // Refactorización sintáctica: indexación correcta de matrices y encadenamiento seguro de métodos
                    LocalDateTime fecha = LocalDateTime.parse(filaActual[0].trim());
                    double metros = Double.parseDouble(filaActual[1].trim());
                    String fuente = filaActual[2].trim().toUpperCase();

                    Riego rc = new Riego(0, idParcelaDestino, fecha, metros, fuente);

                    if (riegoDAO.insertRiego(rc)) {
                        contadorInserciones++;
                    }
                } catch (NumberFormatException | DateTimeParseException parsingException) {
                    // Este bloque catch interno captura errores de formato aislados y transitorios,
                    // permitiendo que el bucle while principal continúe iterando e ingiriendo las filas válidas.
                    System.err.println("Advertencia de Integridad: Ignorando fila malformada en archivo CSV. Detalle: " + parsingException.getMessage());
                }
            }
        } catch (IOException | CsvValidationException ex) {
            // Escalado de excepciones críticas de hardware o de formato irrecuperable al gestor del Task
            throw new Exception("Fallo crítico e irrecuperable durante la lectura secuencial del archivo CSV de telemetría.", ex);
        }

        // El valor de retorno alimenta el manejador de éxito del controlador UI
        return contadorInserciones;
    }
}