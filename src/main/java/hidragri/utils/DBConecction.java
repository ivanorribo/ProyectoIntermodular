package hidragri.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConecction {
    private static DBConecction instance;
    private Connection connection;
    private final String url = "jdbc:mysql://localhost:3306/hidragri_db";
    private final String user = "orribo";
    private final String password = "Stormbreaker24.";

    private DBConecction() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            this.connection = DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Error de inicialización JDBC", e);
        }
    }

    public static DBConecction getInstance() {
        try {
            if (instance == null | instance.getConnection().isClosed()) {
                instance = new DBConecction();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return instance;
    }
    public Connection getConnection() {
        return connection;
    }
}
