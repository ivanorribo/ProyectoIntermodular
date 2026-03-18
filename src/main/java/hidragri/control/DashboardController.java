package hidragri.control;

import hidragri.crud.*;
import hidragri.models.*;
import hidragri.services.CsvImport;
import hidragri.services.ReportManager;
import hidragri.utils.Sesion;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.FileChooser;
import java.io.File;
import java.time.LocalDateTime;
import java.util.List;

public class DashboardController {

    @FXML private Label lblBienvenida;

    // --- VARIABLES FINCAS ---
    @FXML private TableView<Finca> tablaFincas;
    @FXML private TextField txtIdFinca, txtNombre, txtUbicacion, txtRefCatastral, txtHectareas;
    @FXML private Label lblMensajeFinca;

    // --- VARIABLES PARCELAS ---
    @FXML private TableView<Parcela> tablaParcelas;
    @FXML private ComboBox<Finca> cbxFincaPadre;
    @FXML private TextField txtIdParcela, txtSector, txtSuelo;
    @FXML private Label lblMensajeParcela;

    // --- VARIABLES CULTIVOS ---
    @FXML private TableView<Cultivo> tablaCultivos;
    @FXML private ComboBox<Parcela> cbxParcelaPadre;
    @FXML private TextField txtIdCultivo, txtEspecie, txtDensidad;
    @FXML private DatePicker dpSiembra;
    @FXML private ComboBox<String> cbxFase;
    @FXML private Label lblMensajeCultivo;

    // --- VARIABLES RIEGOS (NUEVO) ---
    @FXML private TableView<Riego> tablaRiegos;
    @FXML private ComboBox<Parcela> cbxParcelaRiego;
    @FXML private TextField txtMetros;
    @FXML private ComboBox<String> cbxFuenteAgua;
    @FXML private Label lblMensajeRiego;

    // --- VARIABLES REPORTES (NUEVO) ---
    @FXML private ComboBox<Finca> cbxFincaReporte;
    @FXML private Label lblMensajeReporte;

    // --- DAOs ---
    private final FincaDAO fincaDAO = new FincaDAO();
    private final ParcelaDAO parcelaDAO = new ParcelaDAO();
    private final CultivoDAO cultivoDAO = new CultivoDAO();
    private final RiegoDAO riegoDAO = new RiegoDAO();
    private int idUsuarioActual;

    @FXML
    public void initialize() {
        if (Sesion.getInstance().getUsuarioLogueado()!= null) {
            idUsuarioActual = Sesion.getInstance().getUsuarioLogueado().getIdUsuario();
            lblBienvenida.setText("Sesión Activa: " + Sesion.getInstance().getUsuarioLogueado().getUsername());
        }

        // Cargar Enumerados del Sistema
        cbxFase.setItems(FXCollections.observableArrayList("SIEMBRA", "CRECIMIENTO", "COSECHA"));
        cbxFuenteAgua.setItems(FXCollections.observableArrayList("AGUA_REGENERADA", "POZO", "GALERIA", "RED_PUBLICA"));

        // Listeners
        tablaFincas.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV!= null) cargarFincaEnFormulario(newV);
        });
        tablaParcelas.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV!= null) cargarParcelaEnFormulario(newV);
        });
        tablaCultivos.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV!= null) cargarCultivoEnFormulario(newV);
        });

        cargarTodosLosDatos();
    }

    private void cargarTodosLosDatos() {
        List<Finca> fincas = fincaDAO.fincaUser(idUsuarioActual);
        tablaFincas.setItems(FXCollections.observableArrayList(fincas));

        List<Parcela> parcelas = parcelaDAO.parcelaUser(idUsuarioActual);
        tablaParcelas.setItems(FXCollections.observableArrayList(parcelas));

        List<Cultivo> cultivos = cultivoDAO.cultivoUser(idUsuarioActual);
        tablaCultivos.setItems(FXCollections.observableArrayList(cultivos));

        List<Riego> riegos = riegoDAO.riegoUser(idUsuarioActual);
        tablaRiegos.setItems(FXCollections.observableArrayList(riegos));

        // Rellenar ComboBoxs
        cbxFincaPadre.setItems(FXCollections.observableArrayList(fincas));
        cbxFincaReporte.setItems(FXCollections.observableArrayList(fincas));
        cbxParcelaPadre.setItems(FXCollections.observableArrayList(parcelas));
        cbxParcelaRiego.setItems(FXCollections.observableArrayList(parcelas));
    }

    private void mostrarMensaje(String texto, boolean esError, Label etiqueta) {
        etiqueta.setText(texto);
        etiqueta.setStyle(esError? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        etiqueta.setVisible(true);
    }

    // ================== CRUD FINCAS ==================
    @FXML private void crearFinca(ActionEvent event) {
        try {
            double hectareas = Double.parseDouble(txtHectareas.getText().replace(",", "."));
            Finca nueva = new Finca(0, idUsuarioActual, txtNombre.getText(), txtUbicacion.getText(), txtRefCatastral.getText(), hectareas, true);
            if (fincaDAO.insertFinca(nueva)) {
                mostrarMensaje("Finca creada.", false, lblMensajeFinca);
                cargarTodosLosDatos(); limpiarFormFinca();
            }
        } catch (Exception e) { mostrarMensaje("Error en los datos.", true, lblMensajeFinca); }
    }

    @FXML private void actualizarFinca(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtIdFinca.getText());
            double hectareas = Double.parseDouble(txtHectareas.getText().replace(",", "."));
            if (fincaDAO.updateFinca(new Finca(id, idUsuarioActual, txtNombre.getText(), txtUbicacion.getText(), txtRefCatastral.getText(), hectareas, true))) {
                cargarTodosLosDatos(); limpiarFormFinca();
            }
        } catch (Exception e) {}
    }

    @FXML private void eliminarFinca(ActionEvent event) {
        try {
            if (fincaDAO.deleteFinca(Integer.parseInt(txtIdFinca.getText()))) {
                cargarTodosLosDatos(); limpiarFormFinca();
            }
        } catch (Exception e) {}
    }

    // ================== CRUD PARCELAS ==================
    @FXML private void crearParcela(ActionEvent event) {
        if (cbxFincaPadre.getValue() == null) {
            mostrarMensaje("Selecciona una finca.", true, lblMensajeParcela); return;
        }
        Parcela nueva = new Parcela(0, cbxFincaPadre.getValue().getIdFinca(), txtSector.getText(), txtSuelo.getText());
        if (parcelaDAO.insertParcela(nueva)) {
            mostrarMensaje("Parcela creada.", false, lblMensajeParcela);
            cargarTodosLosDatos(); limpiarFormParcela();
        }
    }

    @FXML private void actualizarParcela(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtIdParcela.getText());
            Parcela p = new Parcela(id, cbxFincaPadre.getValue().getIdFinca(), txtSector.getText(), txtSuelo.getText());
            if (parcelaDAO.updateParcela(p)) { cargarTodosLosDatos(); limpiarFormParcela(); }
        } catch (Exception e) {}
    }

    @FXML private void eliminarParcela(ActionEvent event) {
        try {
            if (parcelaDAO.deleteParcela(Integer.parseInt(txtIdParcela.getText()))) {
                cargarTodosLosDatos(); limpiarFormParcela();
            }
        } catch (Exception e) {}
    }

    // ================== CRUD CULTIVOS ==================
    @FXML private void crearCultivo(ActionEvent event) {
        try {
            if (cbxParcelaPadre.getValue() == null || dpSiembra.getValue() == null || cbxFase.getValue() == null) {
                mostrarMensaje("Rellena todos los campos.", true, lblMensajeCultivo); return;
            }
            int numPlantas = Integer.parseInt(txtDensidad.getText());
            Cultivo nuevo = new Cultivo(0, cbxParcelaPadre.getValue().getIdParcela(), txtEspecie.getText(), dpSiembra.getValue(), cbxFase.getValue(), numPlantas);
            if (cultivoDAO.insertCultivo(nuevo)) {
                mostrarMensaje("Cultivo registrado.", false, lblMensajeCultivo);
                cargarTodosLosDatos(); limpiarFormCultivo();
            }
        } catch (Exception e) { mostrarMensaje("Cantidad inválida.", true, lblMensajeCultivo); }
    }

    @FXML private void actualizarCultivo(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtIdCultivo.getText());
            int numPlantas = Integer.parseInt(txtDensidad.getText());
            Cultivo c = new Cultivo(id, cbxParcelaPadre.getValue().getIdParcela(), txtEspecie.getText(), dpSiembra.getValue(), cbxFase.getValue(), numPlantas);
            if (cultivoDAO.updateCultivo(c)) { cargarTodosLosDatos(); limpiarFormCultivo(); }
        } catch (Exception e) {}
    }

    @FXML private void eliminarCultivo(ActionEvent event) {
        try {
            if (cultivoDAO.deleteCultivo(Integer.parseInt(txtIdCultivo.getText()))) {
                cargarTodosLosDatos(); limpiarFormCultivo();
            }
        } catch (Exception e) {}
    }

    // ================== CASO DE USO 5: RIEGOS Y CONSUMOS ==================
    @FXML private void guardarRiegoManual(ActionEvent event) {
        try {
            if (cbxParcelaRiego.getValue() == null || cbxFuenteAgua.getValue() == null) {
                mostrarMensaje("Faltan datos por seleccionar.", true, lblMensajeRiego); return;
            }
            double metros = Double.parseDouble(txtMetros.getText().replace(",", "."));
            Riego rc = new Riego(0, cbxParcelaRiego.getValue().getIdParcela(), LocalDateTime.now(), metros, cbxFuenteAgua.getValue());

            if (riegoDAO.insertRiego(rc)) {
                mostrarMensaje("Riego registrado manualmente.", false, lblMensajeRiego);
                cargarTodosLosDatos();
                txtMetros.clear(); cbxFuenteAgua.setValue(null);
            }
        } catch (Exception e) { mostrarMensaje("El valor métrico es inválido.", true, lblMensajeRiego); }
    }

    @FXML private void importarCsvRiegos(ActionEvent event) {
        if (cbxParcelaRiego.getValue() == null) {
            mostrarMensaje("Selecciona la Parcela de destino primero.", true, lblMensajeRiego); return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de consumos (.csv)");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Archivos CSV", "*.csv"));
        File archivoCsv = fileChooser.showOpenDialog(txtIdFinca.getScene().getWindow());

        if (archivoCsv!= null) {
            CsvImport tarea = new CsvImport(archivoCsv, cbxParcelaRiego.getValue().getIdParcela());

            tarea.setOnSucceeded(e -> {
                mostrarMensaje("Importación masiva completada: " + tarea.getValue() + " registros.", false, lblMensajeRiego);
                cargarTodosLosDatos();
            });

            tarea.setOnFailed(e -> mostrarMensaje("El archivo CSV tiene un formato no válido.", true, lblMensajeRiego));

            Thread hiloSecundario = new Thread(tarea);
            hiloSecundario.setDaemon(true);
            hiloSecundario.start();
        }
    }

    // ================== CASO DE USO 6: INFORMES Y AUDITORÍA ==================
    @FXML private void generarReportePdf(ActionEvent event) {
        if (cbxFincaReporte.getValue() == null) {
            mostrarMensaje("Por favor, selecciona una Finca.", true, lblMensajeReporte); return;
        }

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Guardar Informe Analítico");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Documentos PDF", "*.pdf"));
        fileChooser.setInitialFileName("Auditoria_Finca_" + cbxFincaReporte.getValue().getIdFinca() + ".pdf");

        File archivoDestino = fileChooser.showSaveDialog(txtIdFinca.getScene().getWindow());

        if (archivoDestino!= null) {
            ReportManager gestorReportes = new ReportManager();
            boolean exito = gestorReportes.exportarInforme(archivoDestino.getAbsolutePath(), cbxFincaReporte.getValue().getIdFinca());

            if (exito) {
                mostrarMensaje("PDF exportado correctamente en tu disco duro.", false, lblMensajeReporte);
            } else {
                mostrarMensaje("Fallo en el motor de JasperReports. Revisa la consola.", true, lblMensajeReporte);
            }
        }
    }

    // --- MÉTODOS DE RELLENADO DE FORMULARIO Y LIMPIEZA ---
    private void cargarFincaEnFormulario(Finca f) {
        txtIdFinca.setText(String.valueOf(f.getIdFinca())); txtNombre.setText(f.getNombre());
        txtUbicacion.setText(f.getUbicacion()); txtRefCatastral.setText(f.getRefCatastral()); txtHectareas.setText(String.valueOf(f.getHectareas()));
    }
    @FXML private void limpiarFormFinca() {
        txtIdFinca.clear(); txtNombre.clear(); txtUbicacion.clear(); txtRefCatastral.clear(); txtHectareas.clear(); tablaFincas.getSelectionModel().clearSelection();
    }
    private void cargarParcelaEnFormulario(Parcela p) {
        txtIdParcela.setText(String.valueOf(p.getIdParcela())); txtSector.setText(p.getCodigoSector()); txtSuelo.setText(p.getTipoSuelo());
        cbxFincaPadre.getItems().stream().filter(f -> f.getIdFinca() == p.getIdFinca()).findFirst().ifPresent(cbxFincaPadre::setValue);
    }
    @FXML private void limpiarFormParcela() {
        txtIdParcela.clear(); txtSector.clear(); txtSuelo.clear(); cbxFincaPadre.setValue(null); tablaParcelas.getSelectionModel().clearSelection();
    }
    private void cargarCultivoEnFormulario(Cultivo c) {
        txtIdCultivo.setText(String.valueOf(c.getIdCultivo())); txtEspecie.setText(c.getEspecie());
        dpSiembra.setValue(c.getFechaSiembra()); cbxFase.setValue(c.getFaseActual()); txtDensidad.setText(String.valueOf(c.getDensidadPlantas()));
        cbxParcelaPadre.getItems().stream().filter(p -> p.getIdParcela() == c.getIdParcela()).findFirst().ifPresent(cbxParcelaPadre::setValue);
    }
    @FXML private void limpiarFormCultivo() {
        txtIdCultivo.clear(); txtEspecie.clear(); txtDensidad.clear(); dpSiembra.setValue(null); cbxFase.setValue(null); cbxParcelaPadre.setValue(null); tablaCultivos.getSelectionModel().clearSelection();
    }
    @FXML private void handleExit(ActionEvent event) { Platform.exit(); }
}