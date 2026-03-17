package hidragri.models;

public class Parcela {
    private int idParcela;
    private int idFinca;
    private String codigoSector;
    private String tipoSuelo;

    public Parcela(int idParcela, int idFinca, String codigoSector, String tipoSuelo) {
        this.idParcela = idParcela;
        this.idFinca = idFinca;
        this.codigoSector = codigoSector;
        this.tipoSuelo = tipoSuelo;
    }

    public int getIdParcela() { return idParcela; }
    public int getIdFinca() { return idFinca; }
    public String getCodigoSector() { return codigoSector; }
    public String getTipoSuelo() { return tipoSuelo; }

    @Override
    public String toString() {
        return "Sector: " + this.codigoSector;
    }
}
