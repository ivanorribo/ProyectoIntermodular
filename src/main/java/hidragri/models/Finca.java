package hidragri.models;

public class Finca {
    private int idFinca;
    private int idUsuario;
    private String nombre;
    private String ubicacion;
    private String refCatastral;
    private double hectareas;
    private boolean estadoActiva;

    public Finca() {}

    public Finca(int idFinca, int idUsuario, String nombre, String ubicacion, String refCatastral, double hectareas, boolean estadoActiva) {
        this.idFinca = idFinca;
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.ubicacion = ubicacion;
        this.refCatastral = refCatastral;
        this.hectareas = hectareas;
        this.estadoActiva = estadoActiva;
    }

    // Getters y Setters
    public int getIdFinca() { return idFinca; }
    public void setIdFinca(int idFinca) { this.idFinca = idFinca; }
    public int getIdUsuario() { return idUsuario; }
    public void setIdUsuario(int idUsuario) { this.idUsuario = idUsuario; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public String getRefCatastral() { return refCatastral; }
    public void setRefCatastral(String refCatastral) { this.refCatastral = refCatastral; }
    public double getHectareas() { return hectareas; }
    public void setHectareas(double hectareas) { this.hectareas = hectareas; }
    public boolean isEstadoActiva() { return estadoActiva; }
    public void setEstadoActiva(boolean estadoActiva) { this.estadoActiva = estadoActiva; }

    @Override
    public String toString() {
        return this.nombre + " (" + this.refCatastral + ")";
    }
}
