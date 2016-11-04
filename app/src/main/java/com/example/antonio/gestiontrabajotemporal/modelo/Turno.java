package com.example.antonio.gestiontrabajotemporal.modelo;


public class Turno {

    public String idTurno;

    public String nombreTurno;

    public String abreviaturaNombreTurno;

    public String horaInicio1;

    public String horaFin1;

    public int turnoPartido;

    public String horaInicio2;

    public String horaFin2;

    public float horasTrabajadas;

    public float horasTrabajadasNocturnas;

    public double precioHora;

    public double precioHoraNocturnas;

    public double precioHoraExtra;

    public int aviso;

    public String sonidoAlarma;

    public int avisoDiaAntes;

    public String horaAviso;

    public String modoTelefono;


    public Turno(String idTurno, String nombreTurno, String abreviaturaNombreTurno, String horaInicio1, String horaFin1, int turnoPartido, String horaInicio2, String horaFin2, float horasTrabajadas, float horasTrabajadasNocturnas, double precioHora, double precioHoraNocturnas, double precioHoraExtra, int aviso, String sonidoAlarma, int avisoDiaAntes, String horaAviso, String modoTelefono) {
        this.idTurno = idTurno;
        this.nombreTurno = nombreTurno;
        this.abreviaturaNombreTurno = abreviaturaNombreTurno;
        this.horaInicio1 = horaInicio1;
        this.horaFin1 = horaFin1;
        this.turnoPartido = turnoPartido;
        this.horaInicio2 = horaInicio2;
        this.horaFin2 = horaFin2;
        this.horasTrabajadas = horasTrabajadas;
        this.horasTrabajadasNocturnas = horasTrabajadasNocturnas;
        this.precioHora = precioHora;
        this.precioHoraNocturnas = precioHoraNocturnas;
        this.precioHoraExtra = precioHoraExtra;
        this.aviso = aviso;
        this.sonidoAlarma = sonidoAlarma;
        this.avisoDiaAntes = avisoDiaAntes;
        this.horaAviso = horaAviso;
        this.modoTelefono = modoTelefono;
    }

    public String getIdTurno() {
        return idTurno;
    }

    public void setIdTurno(String idTurno) {
        this.idTurno = idTurno;
    }

    public String getNombreTurno() {
        return nombreTurno;
    }

    public void setNombreTurno(String nombreTurno) {
        this.nombreTurno = nombreTurno;
    }

    public String getAbreviaturaNombreTurno() {
        return abreviaturaNombreTurno;
    }

    public void setAbreviaturaNombreTurno(String abreviaturaNombreTurno) {
        this.abreviaturaNombreTurno = abreviaturaNombreTurno;
    }

    public String getHoraInicio1() {
        return horaInicio1;
    }

    public void setHoraInicio1(String horaInicio1) {
        this.horaInicio1 = horaInicio1;
    }

    public String getHoraFin1() {
        return horaFin1;
    }

    public void setHoraFin1(String horaFin1) {
        this.horaFin1 = horaFin1;
    }

    public int getTurnoPartido() {
        return turnoPartido;
    }

    public void setTurnoPartido(int turnoPartido) {
        this.turnoPartido = turnoPartido;
    }

    public String getHoraInicio2() {
        return horaInicio2;
    }

    public void setHoraInicio2(String horaInicio2) {
        this.horaInicio2 = horaInicio2;
    }

    public String getHoraFin2() {
        return horaFin2;
    }

    public void setHoraFin2(String horaFin2) {
        this.horaFin2 = horaFin2;
    }

    public float getHorasTrabajadas() {
        return horasTrabajadas;
    }

    public void setHorasTrabajadas(float horasTrabajadas) {
        this.horasTrabajadas = horasTrabajadas;
    }

    public float getHorasTrabajadasNocturnas() {
        return horasTrabajadasNocturnas;
    }

    public void setHorasTrabajadasNocturnas(float horasTrabajadasNocturnas) {
        this.horasTrabajadasNocturnas = horasTrabajadasNocturnas;
    }

    public double getPrecioHora() {
        return precioHora;
    }

    public void setPrecioHora(float precioHora) {
        this.precioHora = precioHora;
    }

    public double getPrecioHoraNocturnas() {
        return precioHoraNocturnas;
    }

    public void setPrecioHoraNocturnas(float precioHoraNocturnas) {
        this.precioHoraNocturnas = precioHoraNocturnas;
    }

    public double getPrecioHoraExtra() {
        return precioHoraExtra;
    }

    public void setPrecioHoraExtra(float precioHoraExtra) {
        this.precioHoraExtra = precioHoraExtra;
    }

    public int getAviso() {
        return aviso;
    }

    public void setAviso(int aviso) {
        this.aviso = aviso;
    }

    public String getSonidoAlarma() {
        return sonidoAlarma;
    }

    public void setSonidoAlarma(String sonidoAlarma) {
        this.sonidoAlarma = sonidoAlarma;
    }

    public int getAvisoDiaAntes() {
        return avisoDiaAntes;
    }

    public void setAvisoDiaAntes(int avisoDiaAntes) {
        this.avisoDiaAntes = avisoDiaAntes;
    }

    public String getHoraAviso() {
        return horaAviso;
    }

    public void setHoraAviso(String horaAviso) {
        this.horaAviso = horaAviso;
    }

    public String getModoTelefono() {
        return modoTelefono;
    }

    public void setModoTelefono(String modoTelefono) {
        this.modoTelefono = modoTelefono;
    }

    @Override
    public String toString() {
        return "Turno{" +
                "idTurno=" + idTurno +
                ", nombreTurno='" + nombreTurno + '\'' +
                ", abreviaturaNombreTurno='" + abreviaturaNombreTurno + '\'' +
                ", horaInicio1='" + horaInicio1 + '\'' +
                ", horaFin1='" + horaFin1 + '\'' +
                ", turnoPartido=" + turnoPartido +
                ", horaInicio2='" + horaInicio2 + '\'' +
                ", horaFin2='" + horaFin2 + '\'' +
                ", horasTrabajadas=" + horasTrabajadas +
                ", horasTrabajadasNocturnas=" + horasTrabajadasNocturnas +
                ", precioHora=" + precioHora +
                ", precioHoraNocturnas=" + precioHoraNocturnas +
                ", precioHoraExtra=" + precioHoraExtra +
                ", aviso=" + aviso +
                ", sonidoAlarma='" + sonidoAlarma + '\'' +
                ", avisoDiaAntes=" + avisoDiaAntes +
                ", horaAviso='" + horaAviso + '\'' +
                ", modoTelefono='" + modoTelefono + '\'' +
                '}';
    }
}