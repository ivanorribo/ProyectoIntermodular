module com.hidragri {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires mysql.connector.j;
    requires jbcrypt;

    opens hidragri.control to javafx.fxml;
    opens hidragri.models to javafx.base;
    exports hidragri;
}