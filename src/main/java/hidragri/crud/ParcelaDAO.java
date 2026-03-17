package hidragri.crud;

import hidragri.models.Parcela;
import hidragri.utils.DBConnection;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ParcelaDAO {
    public boolean insertParcela(Parcela parcela) {
        String query = "INSERT INTO PARCELAS (id_finca, codigo_sector, tipo_suelo) VALUES (?,?,?)";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement prestatem = con.prepareStatement(query)) {
            prestatem.setInt(1, parcela.getIdFinca());
            prestatem.setString(2, parcela.getCodigoSector());
            prestatem.setString(3, parcela.getTipoSuelo());
            return prestatem.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    // Usamos JOIN para obtener solo las parcelas de las fincas que pertenecen al usuario logueado
    public List<Parcela> parcelaUser(int idUsuario) {
        List<Parcela> parcelas = new ArrayList<>();
        String query = "SELECT p.* FROM PARCELAS p JOIN FINCAS f ON p.id_finca = f.id_finca WHERE f.id_usuario =? AND f.estado_activa = 1";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement prestatem = con.prepareStatement(query)) {
            prestatem.setInt(1, idUsuario);
            try (ResultSet rs = prestatem.executeQuery()) {
                while (rs.next()) {
                    parcelas.add(new Parcela(rs.getInt("id_parcela"), rs.getInt("id_finca"), rs.getString("codigo_sector"), rs.getString("tipo_suelo")));
                }
            }
        } catch (SQLException e) { e.printStackTrace(); }
        return parcelas;
    }

    public boolean updateParcela(Parcela parcela) {
        String query = "UPDATE PARCELAS SET id_finca=?, codigo_sector=?, tipo_suelo=? WHERE id_parcela=?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement prestatem = con.prepareStatement(query)) {
            prestatem.setInt(1, parcela.getIdFinca());
            prestatem.setString(2, parcela.getCodigoSector());
            prestatem.setString(3, parcela.getTipoSuelo());
            prestatem.setInt(4, parcela.getIdParcela());
            return prestatem.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }

    public boolean deleteParcela(int idParcela) {
        String query = "DELETE FROM PARCELAS WHERE id_parcela =?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement prestatem = con.prepareStatement(query)) {
            prestatem.setInt(1, idParcela);
            return prestatem.executeUpdate() > 0;
        } catch (SQLException e) { e.printStackTrace(); return false; }
    }
}
