package hidragri.crud;

import hidragri.models.Finca;
import hidragri.utils.DBConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FincaDAO {
    // Para insertar nuevas fincas
    public boolean insertFinca (Finca finca) {
        String query = "INSERT INTO FINCAS (id_usuario, nombre, ubicacion, ref_catastral, hectareas) VALUES (?,?,?,?,?)";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement prestatem = con.prepareStatement(query)) {
            prestatem.setInt(1, finca.getIdUsuario());
            prestatem.setString(2, finca.getNombre());
            prestatem.setString(3, finca.getUbicacion());
            prestatem.setString(4, finca.getRefCatastral());
            prestatem.setDouble(5, finca.getHectareas());

            return prestatem.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    // Para lectura de fincas activas por idusuario
    public List<Finca> fincaUser(int idUsuario) {
        List<Finca> fincas = new ArrayList<>();
        String query = "SELECT * FROM FINCAS WHERE id_usuario =? AND estado_activa = 1";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement prestatem = con.prepareStatement(query)) {
            prestatem.setInt(1, idUsuario);
            try (ResultSet rs = prestatem.executeQuery()) {
                while (rs.next()) {
                    fincas.add(new Finca(
                            rs.getInt("id_finca"), rs.getInt("id_usuario"), rs.getString("nombre"),
                            rs.getString("ubicacion"), rs.getString("ref_catastral"),
                            rs.getDouble("hectareas"), rs.getBoolean("estado_activa")
                    ));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return fincas;
    }

    // Actualizar datos finca existente
    public boolean updateFinca (Finca finca) {
        String query = "UPDATE FINCAS SET nombre=?, ubicacion=?, ref_catastral=?, hectareas=? WHERE id_finca=?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement prestatem = con.prepareStatement(query)) {
            prestatem.setString(1, finca.getNombre());
            prestatem.setString(2, finca.getUbicacion());
            prestatem.setString(3, finca.getRefCatastral());
            prestatem.setDouble(4, finca.getHectareas());
            prestatem.setInt(5, finca.getIdFinca());

            return prestatem.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Borrado de finca (solamente se desactivan. no se borran para evitar perdida de informacion)
    public boolean deleteFinca (int idFinca) {
        String query = "UPDATE FINCAS SET estado_activa = 0 WHERE id_finca =?";
        try (Connection con = DBConnection.getInstance().getConnection();
             PreparedStatement prestatem = con.prepareStatement(query)) {
            prestatem.setInt(1, idFinca);
            return prestatem.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
