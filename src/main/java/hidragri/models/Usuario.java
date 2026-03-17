package hidragri.models;

public class Usuario {
    private int idUsuario;
    private String username;
    private String passwordHash;
    private String rol;

    public Usuario(int idUsuario, String username, String passwordHash, String rol) {
        this.idUsuario = idUsuario;
        this.username = username;
        this.passwordHash = passwordHash;
        this.rol = rol;
    }
    public int getIdUsuario() { return idUsuario; }
    public String getUsername() { return username; }
    public String getPasswordHash() { return passwordHash; }
    public String getRol() { return rol; }

}
