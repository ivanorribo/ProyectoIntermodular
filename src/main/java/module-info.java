module hidragri {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires java.desktop;
    requires mysql.connector.j;
    requires jbcrypt;
    requires com.opencsv;
    requires jasperreports;

    opens hidragri.control to javafx.fxml;
    opens hidragri.models to javafx.base;
    exports hidragri;
}