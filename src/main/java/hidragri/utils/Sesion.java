package hidragri.utils;

import hidragri.models.Usuario;

public class Sesion {
    private static Sesion instance;
    private Usuario usuarioLogueado;

    private Sesion() {}

    public static Sesion getInstance() {
        if (instance == null) {
            instance = new Sesion();
        }
        return instance;
    }

    public void setUsuarioLogueado(Usuario usuario) {
        this.usuarioLogueado = usuario;
    }

    public Usuario getUsuarioLogueado() {
        return usuarioLogueado;
    }

    public void cerrarSesion() {
        this.usuarioLogueado = null;
    }
}