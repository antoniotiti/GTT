package com.example.antonio.gestiontrabajotemporal.nominadetalle;

/**
 * Clase para la creación de objetos NominaTurno
 */
class NominaTurno {

    private String nombreTurno;
    private Double horasTrabajadas;
    private Double horasTrabajadasNocturnas;
    private Double horasTrabajadasExtras;
    private Double precioHoraTrabajada;
    private Double precioHoraTrabajadaExtras;
    private Double precioHoraTrabajadaNocturna;
    private Double totalEurosHorasTrabajadas;
    private Double totalEurosHorasTrabajadasNocturnas;
    private Double totalEurosHorasTrabajadasExtras;

    public NominaTurno() {
        super();
    }

    NominaTurno(String nombreTurno, Double horasTrabajadas, Double horasTrabajadasNocturnas, Double horasTrabajadasExtras, Double precioHoraTrabajada, Double precioHoraTrabajadaExtras, Double precioHoraTrabajadaNocturna, Double totalEurosHorasTrabajadas, Double totalEurosHorasTrabajadasNocturnas, Double totalEurosHorasTrabajadasExtras) {
        this.nombreTurno = nombreTurno;
        this.horasTrabajadas = horasTrabajadas;
        this.horasTrabajadasNocturnas = horasTrabajadasNocturnas;
        this.horasTrabajadasExtras = horasTrabajadasExtras;
        this.precioHoraTrabajada = precioHoraTrabajada;
        this.precioHoraTrabajadaExtras = precioHoraTrabajadaExtras;
        this.precioHoraTrabajadaNocturna = precioHoraTrabajadaNocturna;
        this.totalEurosHorasTrabajadas = totalEurosHorasTrabajadas;
        this.totalEurosHorasTrabajadasNocturnas = totalEurosHorasTrabajadasNocturnas;
        this.totalEurosHorasTrabajadasExtras = totalEurosHorasTrabajadasExtras;
    }

    String getNombreTurno() {
        return nombreTurno;
    }

    public void setNombreTurno(String nombreTurno) {
        this.nombreTurno = nombreTurno;
    }

    Double getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(Double horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    Double getHorasTrabajadasNocturnas() {
        return horasTrabajadasNocturnas;
    }

    public void setHorasTrabajadasNocturnas(Double horasTrabajadasNocturnas) {
        this.horasTrabajadasNocturnas = horasTrabajadasNocturnas;
    }

    Double getHorasTrabajadasExtras() {
        return horasTrabajadasExtras;
    }

    public void setHorasTrabajadasExtras(Double horasTrabajadasExtras) {
        this.horasTrabajadasExtras = horasTrabajadasExtras;
    }

    Double getPrecioHoraTrabajada() {
        return precioHoraTrabajada;
    }

    public void setPrecioHoraTrabajada(Double precioHoraTrabajada) {
        this.precioHoraTrabajada = precioHoraTrabajada;
    }

    Double getPrecioHoraTrabajadaExtras() {
        return precioHoraTrabajadaExtras;
    }

    public void setPrecioHoraTrabajadaExtras(Double precioHoraTrabajadaExtras) {
        this.precioHoraTrabajadaExtras = precioHoraTrabajadaExtras;
    }

    Double getPrecioHoraTrabajadaNocturna() {
        return precioHoraTrabajadaNocturna;
    }

    public void setPrecioHoraTrabajadaNocturna(Double precioHoraTrabajadaNocturna) {
        this.precioHoraTrabajadaNocturna = precioHoraTrabajadaNocturna;
    }

    Double getTotalEurosHorasTrabajadas() {
        return totalEurosHorasTrabajadas;
    }

    public void setTotalEurosHorasTrabajadas(Double totalEurosHorasTrabajadas) {
        this.totalEurosHorasTrabajadas = totalEurosHorasTrabajadas;
    }

    Double getTotalEurosHorasTrabajadasNocturnas() {
        return totalEurosHorasTrabajadasNocturnas;
    }

    public void setTotalEurosHorasTrabajadasNocturnas(Double totalEurosHorasTrabajadasNocturnas) {
        this.totalEurosHorasTrabajadasNocturnas = totalEurosHorasTrabajadasNocturnas;
    }

    Double getTotalEurosHorasTrabajadasExtras() {
        return totalEurosHorasTrabajadasExtras;
    }

    public void setTotalEurosHorasTrabajadasExtras(Double totalEurosHorasTrabajadasExtras) {
        this.totalEurosHorasTrabajadasExtras = totalEurosHorasTrabajadasExtras;
    }

    @Override
    public String toString() {
        return "NominaTurno{" +
                "nombreTurno='" + nombreTurno + '\'' +
                ", horasTrabajadas=" + horasTrabajadas +
                ", horasTrabajadasNocturnas=" + horasTrabajadasNocturnas +
                ", horasTrabajadasExtras=" + horasTrabajadasExtras +
                ", precioHoraTrabajada=" + precioHoraTrabajada +
                ", precioHoraTrabajadaExtras=" + precioHoraTrabajadaExtras +
                ", precioHoraTrabajadaNocturna=" + precioHoraTrabajadaNocturna +
                ", totalEurosHorasTrabajadas=" + totalEurosHorasTrabajadas +
                ", totalEurosHorasTrabajadasNocturnas=" + totalEurosHorasTrabajadasNocturnas +
                ", totalEurosHorasTrabajadasExtras=" + totalEurosHorasTrabajadasExtras +
                '}';
    }
}