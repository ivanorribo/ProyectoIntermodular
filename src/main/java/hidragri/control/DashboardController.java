package hidragri.control;

import hidragri.crud.FincaDAO;
import hidragri.models.Finca;
import hidragri.utils.Sesion;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import java.util.List;

public class DashboardController {

    @FXML private Label lblBienvenida;
    @FXML private TableView<Finca> tablaFincas;

    @FXML private TextField txtIdFinca;
    @FXML private TextField txtNombre;
    @FXML private TextField txtUbicacion;
    @FXML private TextField txtRefCatastral;
    @FXML private TextField txtHectareas;
    @FXML private Label lblMensaje;

    private final FincaDAO fincaDAO = new FincaDAO();
    private ObservableList<Finca> listaObservableFincas;
    private int idUsuarioActual;

    @FXML
    public void initialize() {
        // Recuperar el usuario de la sesión iniciada
        if (Sesion.getInstance().getUsuarioLogueado()!= null) {
            idUsuarioActual = Sesion.getInstance().getUsuarioLogueado().getIdUsuario();
            String nombreUsuario = Sesion.getInstance().getUsuarioLogueado().getUsername();
            lblBienvenida.setText("Bienvenido/a, " + nombreUsuario + ". Gestiona tus fincas a continuación:");
        }

        // Listener para detectar clicks en la tabla y rellenar el formulario
        tablaFincas.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue!= null) {
                cargarFincaEnFormulario(newValue);
            }
        });

        cargarDatosTabla();
    }

    private void cargarDatosTabla() {
        List<Finca> fincasBD = fincaDAO.fincaUser(idUsuarioActual);
        listaObservableFincas = FXCollections.observableArrayList(fincasBD);
        tablaFincas.setItems(listaObservableFincas);
    }

    @FXML
    private void crearFinca(ActionEvent event) {
        try {
            if (txtNombre.getText().isEmpty() || txtRefCatastral.getText().isEmpty()) {
                mostrarMensaje("El nombre y la referencia catastral son obligatorios.", true);
                return;
            }

            double hectareas = Double.parseDouble(txtHectareas.getText().replace(",", "."));

            Finca nuevaFinca = new Finca(0, idUsuarioActual, txtNombre.getText(), txtUbicacion.getText(), txtRefCatastral.getText(), hectareas, true);

            if (fincaDAO.insertFinca(nuevaFinca)) {
                mostrarMensaje("Finca creada correctamente.", false);
                cargarDatosTabla();
                limpiarFormulario();
            } else {
                mostrarMensaje("Error al crear la finca (¿Referencia duplicada?).", true);
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("El valor de hectáreas debe ser numérico.", true);
        }
    }

    @FXML
    private void actualizarFinca(ActionEvent event) {
        try {
            if (txtIdFinca.getText().isEmpty()) {
                mostrarMensaje("Selecciona una finca de la tabla para actualizarla.", true);
                return;
            }

            int idFinca = Integer.parseInt(txtIdFinca.getText());
            double hectareas = Double.parseDouble(txtHectareas.getText().replace(",", "."));

            Finca fincaActualizada = new Finca(idFinca, idUsuarioActual, txtNombre.getText(), txtUbicacion.getText(), txtRefCatastral.getText(), hectareas, true);

            if (fincaDAO.updateFinca(fincaActualizada)) {
                mostrarMensaje("Finca actualizada correctamente.", false);
                cargarDatosTabla();
                limpiarFormulario();
            } else {
                mostrarMensaje("No se pudo actualizar la finca.", true);
            }
        } catch (NumberFormatException e) {
            mostrarMensaje("Error en el formato numérico.", true);
        }
    }

    @FXML
    private void eliminarFinca(ActionEvent event) {
        if (txtIdFinca.getText().isEmpty()) {
            mostrarMensaje("Selecciona una finca para eliminar.", true);
            return;
        }

        int idFinca = Integer.parseInt(txtIdFinca.getText());

        // Ejecuta el borrado lógico en la base de datos
        if (fincaDAO.deleteFinca(idFinca)) {
            mostrarMensaje("Finca eliminada del registro.", false);
            cargarDatosTabla();
            limpiarFormulario();
        } else {
            mostrarMensaje("Error al eliminar la finca.", true);
        }
    }

    private void cargarFincaEnFormulario(Finca finca) {
        txtIdFinca.setText(String.valueOf(finca.getIdFinca()));
        txtNombre.setText(finca.getNombre());
        txtUbicacion.setText(finca.getUbicacion());
        txtRefCatastral.setText(finca.getRefCatastral());
        txtHectareas.setText(String.valueOf(finca.getHectareas()));
        lblMensaje.setVisible(false);
    }

    @FXML
    private void limpiarFormulario() {
        txtIdFinca.clear();
        txtNombre.clear();
        txtUbicacion.clear();
        txtRefCatastral.clear();
        txtHectareas.clear();
        tablaFincas.getSelectionModel().clearSelection();
        lblMensaje.setVisible(false);
    }

    private void mostrarMensaje(String texto, boolean esError) {
        lblMensaje.setText(texto);
        lblMensaje.setStyle(esError? "-fx-text-fill: red;" : "-fx-text-fill: green;");
        lblMensaje.setVisible(true);
    }

    @FXML
    private void handleExit(ActionEvent event) {
        Platform.exit();
    }
}