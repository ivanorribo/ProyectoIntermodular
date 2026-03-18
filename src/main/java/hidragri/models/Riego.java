package hidragri.models;

import java.time.LocalDateTime;

public class Riego {
    private int idRegistro;
    private int idParcela;
    private LocalDateTime fechaRiego;
    private double metrosCubicos;
    private String fuenteAgua;

    public Riego(int idRegistro, int idParcela, LocalDateTime fechaRiego, double metrosCubicos, String fuenteAgua) {
        this.idRegistro = idRegistro;
        this.idParcela = idParcela;
        this.fechaRiego = fechaRiego;
        this.metrosCubicos = metrosCubicos;
        this.fuenteAgua = fuenteAgua;
    }

    public int getIdRegistro() { return idRegistro; }
    public int getIdParcela() { return idParcela; }
    public LocalDateTime getFechaRiego() { return fechaRiego; }
    public double getMetrosCubicos() { return metrosCubicos; }
    public String getFuenteAgua() { return fuenteAgua; }
}