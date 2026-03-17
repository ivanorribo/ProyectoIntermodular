package hidragri.control;

import hidragri.crud.UsuarioDAO;
import hidragri.models.Usuario;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.stage.Stage;
import java.io.IOException;

public class Controller {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    @FXML private Label lblFeedback;

    private boolean modoAltoContraste = false;
    private final UsuarioDAO usuarioDAO = new UsuarioDAO();

    @FXML
    public void initialize() {
        txtPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) { handleLogin(new ActionEvent()); }
        });
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        String username = txtUsername.getText().trim();
        String password = txtPassword.getText();

        if (username.isEmpty() || password.isEmpty()) {
            mostrarError("Por favor, rellena todos los campos.");
            return;
        }

        Usuario usuarioValidado = usuarioDAO.autenticar(username, password);

        if (usuarioValidado!= null) {
            lblFeedback.setVisible(false);
            cargarVentanaPrincipal();
        } else {
            mostrarError("Usuario o contraseña incorrectos.");
        }
    }

    private void mostrarError(String mensaje) {
        lblFeedback.setText(mensaje);
        lblFeedback.setVisible(true);
    }

    // Método que realiza la transición de escenas una vez logueado
    private void cargarVentanaPrincipal() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/Dashboard.fxml"));
            Parent root = loader.load();

            // Obtenemos el Stage (ventana) actual desde cualquier elemento de la interfaz
            Stage stage = (Stage) txtUsername.getScene().getWindow();
            Scene scene = new Scene(root, 1024, 768);

            // Mantenemos el estado de accesibilidad si estaba activado
            if (modoAltoContraste) {
                scene.getStylesheets().add(getClass().getResource("/css/high-contrast.css").toExternalForm());
            }

            stage.setScene(scene);
            stage.setTitle("HidrAgri - Dashboard Principal");

        } catch (IOException e) {
            e.printStackTrace();
            mostrarError("Fallo crítico cargando la interfaz del sistema.");
        }
    }

    @FXML
    private void toggleHighContrast(ActionEvent event) {
        modoAltoContraste =!modoAltoContraste;
        String cssPath = getClass().getResource("/css/high-contrast.css").toExternalForm();

        if (modoAltoContraste) {
            txtUsername.getScene().getStylesheets().add(cssPath);
        } else {
            txtUsername.getScene().getStylesheets().remove(cssPath);
        }
    }
}