package hidragri.crud;

import hidragri.models.Usuario;
import hidragri.utils.DBConnection;
import hidragri.utils.PasswordUtil;

import javax.xml.transform.Result;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UsuarioDAO {

    public Usuario autenticar(String username, String password) {
        String query = "SELECT id_usuario, username, password_hash, rol FROM USUARIOS WHERE username =?";

        try (Connection con = DBConnection.getInstance().getConnection();
        PreparedStatement prestatem = con.prepareStatement(query)) {
            prestatem.setString(1, username);
            try (ResultSet rs = prestatem.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");

                    //si encriptado coincide, devuelve isntancia user
                    if (PasswordUtil.checkPassword(password, storedHash)) {
                        return new Usuario(
                                rs.getInt("id_usuario"),
                                rs.getString("username"),
                                storedHash,
                                rs.getString("rol")
                        );
                    }
                }
            }
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;
    }

}
