package hidragri.services;

import hidragri.utils.DBConnection;
import net.sf.jasperreports.engine.*;
import java.io.InputStream;
import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

public class ReportManager {
    public boolean exportarInforme(String rutaAbsolutaDestino, int idFincaConsultada) {
        try {
            InputStream flujoDiseno = getClass().getResourceAsStream("/reports/PlantillaCuadroAnalitico.jrxml");
            if (flujoDiseno == null) {
                System.err.println("Plantilla JRXML no hallada.");
                return false;
            }

            // 1. Compila la plantilla XML
            JasperReport reporteCompilado = JasperCompileManager.compileReport(flujoDiseno);

            // 2. Inyecta el ID de la Finca para que Jasper filtre la Base de Datos
            Map<String, Object> parametrosInyectados = new HashMap<>();
            parametrosInyectados.put("ID_FINCA_SELECCIONADA", idFincaConsultada);

            Connection conexionViva = DBConnection.getInstance().getConnection();

            // 3. Ejecuta la consulta SQL interna y rellena el documento
            JasperPrint renderizadoPaginado = JasperFillManager.fillReport(reporteCompilado, parametrosInyectados, conexionViva);

            // 4. Escribe el documento PDF en el disco duro del agricultor
            JasperExportManager.exportReportToPdfFile(renderizadoPaginado, rutaAbsolutaDestino);
            return true;

        } catch (JRException e) {
            e.printStackTrace();
            return false;
        }
    }
}