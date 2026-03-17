package hidragri.models;

import java.time.LocalDate;

public class Cultivo {
    private int idCultivo;
    private int idParcela;
    private String especie;
    private LocalDate fechaSiembra;
    private String faseActual;
    private int densidadPlantas;

    public Cultivo(int idCultivo, int idParcela, String especie, LocalDate fechaSiembra, String faseActual, int densidadPlantas) {
        this.idCultivo = idCultivo;
        this.idParcela = idParcela;
        this.especie = especie;
        this.fechaSiembra = fechaSiembra;
        this.faseActual = faseActual;
        this.densidadPlantas = densidadPlantas;
    }

    public int getIdCultivo() { return idCultivo; }
    public int getIdParcela() { return idParcela; }
    public String getEspecie() { return especie; }
    public LocalDate getFechaSiembra() { return fechaSiembra; }
    public String getFaseActual() { return faseActual; }
    public int getDensidadPlantas() { return densidadPlantas; }
}
