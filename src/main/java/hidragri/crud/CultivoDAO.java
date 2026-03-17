package hidragri.crud;

import hidragri.models.Cultivo;
import hidragri.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CultivoDAO {
    public boolean insertCultivo(Cultivo cultivo) {
        String query = "INSERT INTO CULTIVOS (id_parcela, especie, fecha_siembra, fase_actual, densidad_plantas) VALUES (?,?,?,?,?)";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement prestatem = con.prepareStatement(query)) {
            prestatem.setInt(1, cultivo.getIdParcela());
            prestatem.setDate(2, Date.valueOf(cultivo.getFechaSiembra()));
            prestatem.setString(3, cultivo.getFaseActual());
            prestatem.setInt(4, cultivo.getDensidadPlantas());
            return prestatem.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public List<Cultivo> cultivoUser(int idUsuario) {
        List<Cultivo> cultivos = new ArrayList<>();
        String query = "SELECT c.* FROM CULTIVOS c JOIN PARCELAS p ON c.id_parcela = p.id_parcela JOIN FINCAS f ON p.id_finca = f.id_finca WHERE f.id_usuario =? AND f.estado_activa = 1";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement prestatem = con.prepareStatement(query)) {
            prestatem.setInt(1, idUsuario);
            try (ResultSet rs = prestatem.executeQuery()) {
                while (rs.next()) {
                    cultivos.add(new Cultivo(rs.getInt("id_cultivo"), rs.getInt("id_parcela"), rs.getString("especie"),
                            rs.getDate("fecha_siembra").toLocalDate(), rs.getString("fase_actual"), rs.getInt("densidad_plantas")));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return cultivos;
    }

    public boolean updateCultivo(Cultivo cultivo) {
        String query = "UPDATE CULTIVOS SET id_parcela=?, especie=?, fecha_siembra=?, fase_actual=?, densidad_plantas=? WHERE id_cultivo=?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement prestatem = con.prepareStatement(query)) {
            prestatem.setInt(1, cultivo.getIdParcela());
            prestatem.setDate(2, Date.valueOf(cultivo.getFechaSiembra()));
            prestatem.setString(3, cultivo.getFaseActual());
            prestatem.setInt(4, cultivo.getDensidadPlantas());
            prestatem.setInt(5, cultivo.getIdCultivo());
            return prestatem.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteCultivo(int idCultivo) {
        String query = "DELETE FROM CULTIVOS WHERE id_cultivo =?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement prestatem = con.prepareStatement(query)) {
            prestatem.setInt(1, idCultivo);
            return prestatem.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
