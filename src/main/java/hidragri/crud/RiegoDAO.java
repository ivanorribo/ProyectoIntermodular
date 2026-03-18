package hidragri.crud;

import hidragri.models.Riego;
import hidragri.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RiegoDAO {

    // Create: Ingestar nuevo registro
    public boolean insertRiego(Riego riego) {
        String query = "INSERT INTO RIEGOS_CONSUMOS (id_parcela, fecha_riego, metros_cubicos, fuente_agua) VALUES (?,?,?,?)";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement prestatem = con.prepareStatement(query)) {

            prestatem.setInt(1, riego.getIdParcela());
            prestatem.setTimestamp(2, Timestamp.valueOf(riego.getFechaRiego()));
            prestatem.setDouble(3, riego.getMetrosCubicos());
            prestatem.setString(4, riego.getFuenteAgua());

            return prestatem.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Read: Extraer lecturas para la tabla gráfica
    public List<Riego> riegoUser(int idUsuario) {
        List<Riego> riegos = new ArrayList<>();
        String query = "SELECT r.* FROM RIEGOS_CONSUMOS r JOIN PARCELAS p ON r.id_parcela = p.id_parcela JOIN FINCAS f ON p.id_finca = f.id_finca WHERE f.id_usuario =? AND f.estado_activa = 1 ORDER BY r.fecha_riego DESC";

        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement prestatem = con.prepareStatement(query)) {

            prestatem.setInt(1, idUsuario);
            try (ResultSet rs = prestatem.executeQuery()) {
                while (rs.next()) {
                    riegos.add(new Riego(
                            rs.getInt("id_registro"),
                            rs.getInt("id_parcela"),
                            rs.getTimestamp("fecha_riego").toLocalDateTime(),
                            rs.getDouble("metros_cubicos"),
                            rs.getString("fuente_agua")
                    ));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return riegos;
    }
}