package hidragri.control;

import hidragri.crud.CultivoDAO;
import hidragri.crud.FincaDAO;
import hidragri.crud.ParcelaDAO;
import hidragri.models.Cultivo;
import hidragri.models.Finca;
import hidragri.models.Parcela;
import hidragri.utils.Sesion;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
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

    // --- DAOs y Variables Globales ---
    private final FincaDAO fincaDAO = new FincaDAO();
    private final ParcelaDAO parcelaDAO = new ParcelaDAO();
    private final CultivoDAO cultivoDAO = new CultivoDAO();
    private int idUsuarioActual;

    @FXML
    public void initialize() {
        if (Sesion.getInstance().getUsuarioLogueado()!= null) {
            idUsuarioActual = Sesion.getInstance().getUsuarioLogueado().getIdUsuario();
            lblBienvenida.setText("Sesión Activa: " + Sesion.getInstance().getUsuarioLogueado().getUsername());
        }

        // Cargar Enumerado del Cultivo
        cbxFase.setItems(FXCollections.observableArrayList("SIEMBRA", "CRECIMIENTO", "COSECHA"));

        // Listeners para rellenar los formularios al hacer clic en las tablas
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

    // --- MÉTODOS DE RECARGA DE DATOS ---
    private void cargarTodosLosDatos() {
        // Cargar Tablas
        List<Finca> fincas = fincaDAO.fincaUser(idUsuarioActual);
        tablaFincas.setItems(FXCollections.observableArrayList(fincas));

        List<Parcela> parcelas = parcelaDAO.parcelaUser(idUsuarioActual);
        tablaParcelas.setItems(FXCollections.observableArrayList(parcelas));

        List<Cultivo> cultivos = cultivoDAO.cultivoUser(idUsuarioActual);
        tablaCultivos.setItems(FXCollections.observableArrayList(cultivos));

        // Rellenar las listas desplegables (ComboBox) para las relaciones
        cbxFincaPadre.setItems(FXCollections.observableArrayList(fincas));
        cbxParcelaPadre.setItems(FXCollections.observableArrayList(parcelas));
    }

    // =========================================================
    //                    CRUD DE FINCAS
    // =========================================================
    @FXML private void crearFinca(ActionEvent event) {
        try {
            double hectareas = Double.parseDouble(txtHectareas.getText().replace(",", "."));
            Finca nueva = new Finca(0, idUsuarioActual, txtNombre.getText(), txtUbicacion.getText(), txtRefCatastral.getText(), hectareas, true);
            if (fincaDAO.insertFinca(nueva)) {
                lblMensajeFinca.setText("Finca creada."); lblMensajeFinca.setVisible(true);
                cargarTodosLosDatos(); limpiarFormFinca();
            }
        } catch (Exception e) { lblMensajeFinca.setText("Error en los datos."); lblMensajeFinca.setVisible(true); }
    }

    @FXML private void actualizarFinca(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtIdFinca.getText());
            double hectareas = Double.parseDouble(txtHectareas.getText().replace(",", "."));
            if (fincaDAO.updateFinca(new Finca(id, idUsuarioActual, txtNombre.getText(), txtUbicacion.getText(), txtRefCatastral.getText(), hectareas, true))) {
                cargarTodosLosDatos(); limpiarFormFinca();
            }
        } catch (Exception e) { }
    }

    @FXML private void eliminarFinca(ActionEvent event) {
        try {
            if (fincaDAO.deleteFinca(Integer.parseInt(txtIdFinca.getText()))) {
                cargarTodosLosDatos(); limpiarFormFinca();
            }
        } catch (Exception e) { }
    }

    // =========================================================
    //                    CRUD DE PARCELAS
    // =========================================================
    @FXML private void crearParcela(ActionEvent event) {
        if (cbxFincaPadre.getValue() == null) {
            lblMensajeParcela.setText("Debes seleccionar una finca padre."); lblMensajeParcela.setVisible(true); return;
        }
        Parcela nueva = new Parcela(0, cbxFincaPadre.getValue().getIdFinca(), txtSector.getText(), txtSuelo.getText());
        if (parcelaDAO.insertParcela(nueva)) {
            lblMensajeParcela.setText("Parcela creada."); lblMensajeParcela.setVisible(true);
            cargarTodosLosDatos(); limpiarFormParcela();
        }
    }

    @FXML private void actualizarParcela(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtIdParcela.getText());
            Parcela p = new Parcela(id, cbxFincaPadre.getValue().getIdFinca(), txtSector.getText(), txtSuelo.getText());
            if (parcelaDAO.updateParcela(p)) { cargarTodosLosDatos(); limpiarFormParcela(); }
        } catch (Exception e) { }
    }

    @FXML private void eliminarParcela(ActionEvent event) {
        try {
            if (parcelaDAO.deleteParcela(Integer.parseInt(txtIdParcela.getText()))) {
                cargarTodosLosDatos(); limpiarFormParcela();
            }
        } catch (Exception e) { }
    }

    // =========================================================
    //                    CRUD DE CULTIVOS
    // =========================================================
    @FXML private void crearCultivo(ActionEvent event) {
        try {
            if (cbxParcelaPadre.getValue() == null || dpSiembra.getValue() == null || cbxFase.getValue() == null) {
                lblMensajeCultivo.setText("Rellena todos los combos y fechas."); lblMensajeCultivo.setVisible(true); return;
            }
            int numPlantas = Integer.parseInt(txtDensidad.getText());
            Cultivo nuevo = new Cultivo(0, cbxParcelaPadre.getValue().getIdParcela(), txtEspecie.getText(), dpSiembra.getValue(), cbxFase.getValue(), numPlantas);
            if (cultivoDAO.insertCultivo(nuevo)) {
                lblMensajeCultivo.setText("Cultivo registrado."); lblMensajeCultivo.setVisible(true);
                cargarTodosLosDatos(); limpiarFormCultivo();
            }
        } catch (Exception e) { lblMensajeCultivo.setText("Cantidad inválida."); lblMensajeCultivo.setVisible(true); }
    }

    @FXML private void actualizarCultivo(ActionEvent event) {
        try {
            int id = Integer.parseInt(txtIdCultivo.getText());
            int numPlantas = Integer.parseInt(txtDensidad.getText());
            Cultivo c = new Cultivo(id, cbxParcelaPadre.getValue().getIdParcela(), txtEspecie.getText(), dpSiembra.getValue(), cbxFase.getValue(), numPlantas);
            if (cultivoDAO.updateCultivo(c)) { cargarTodosLosDatos(); limpiarFormCultivo(); }
        } catch (Exception e) { }
    }

    @FXML private void eliminarCultivo(ActionEvent event) {
        try {
            if (cultivoDAO.deleteCultivo(Integer.parseInt(txtIdCultivo.getText()))) {
                cargarTodosLosDatos(); limpiarFormCultivo();
            }
        } catch (Exception e) { }
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