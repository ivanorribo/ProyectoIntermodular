package hidragri.control;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class Controller {

    @FXML private TextField txtUsername;
    @FXML private PasswordField txtPassword;
    private boolean modoAltoContraste = false;

    @FXML
    public void initialize() {
        // Mejorando la usabilidad: Permitir submit con ENTER
        txtPassword.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) { handleLogin(new ActionEvent()); }
        });
    }

    @FXML
    private void handleLogin(ActionEvent event) {
        // Lógica de validación con UsuarioDAO (Caso de Uso 1)
        System.out.println("Iniciando validación de MVP para: " + txtUsername.getText());
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