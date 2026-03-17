package hidragri.control;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;

public class DashboardController {

    @FXML
    private void handleExit(ActionEvent event) {
        // Cierre seguro de la aplicación
        Platform.exit();
    }
}
