package com.francisco.futbalmis.Clases;

import java.io.Serializable;

public class Partido implements Serializable {
    private int idPartido;
    private Equipo equipoLocal;
    private Equipo equipoVisitante;
    private String estadoPartido;
    private String fechaPartido;
    private String horaPartido;
    private String resultadoEquipoLocal;
    private String resultadoEquipoVisitante;

    public Partido(
            int idPartido,
            Equipo equipoLocal,
            Equipo equipoVisitante,
            String estadoPartido,
            String fechaPartido,
            String horaPartido,
            String resultadoEquipoLocal,
            String resultadoEquipoVisitante) {
        this.idPartido = idPartido;
        this.equipoLocal = equipoLocal;
        this.equipoVisitante = equipoVisitante;
        this.estadoPartido = estadoPartido;
        this.fechaPartido = fechaPartido;
        this.horaPartido = horaPartido;
        this.resultadoEquipoLocal = resultadoEquipoLocal;
        this.resultadoEquipoVisitante = resultadoEquipoVisitante;
    }

    public Partido() {
    }

    public int getIdPartido() {
        return idPartido;
    }

    public void setIdPartido(int idPartido) {
        this.idPartido = idPartido;
    }

    public Equipo getEquipoLocal() {
        return equipoLocal;
    }

    public void setEquipoLocal(Equipo equipoLocal) {
        this.equipoLocal = equipoLocal;
    }

    public Equipo getEquipoVisitante() {
        return equipoVisitante;
    }

    public void setEquipoVisitante(Equipo equipoVisitante) {
        this.equipoVisitante = equipoVisitante;
    }

    public String getEstadoPartido() {
        return estadoPartido;
    }

    public void setEstadoPartido(String estadoPartido) {
        this.estadoPartido = estadoPartido;
    }

    public String getFechaPartido() {
        return fechaPartido;
    }

    public void setFechaPartido(String fechaPartido) {
        this.fechaPartido = fechaPartido;
    }

    public String getHoraPartido() {
        return horaPartido;
    }

    public void setHoraPartido(String horaPartido) {
        this.horaPartido = horaPartido;
    }

    public String getResultadoEquipoLocal() {
        return resultadoEquipoLocal;
    }

    public void setResultadoEquipoLocal(String resultadoEquipoLocal) {
        this.resultadoEquipoLocal = resultadoEquipoLocal;
    }

    public String getResultadoEquipoVisitate() {
        return resultadoEquipoVisitante;
    }

    public void setResultadoEquipoVisitate(String resultadoEquipoVisitante) {
        this.resultadoEquipoVisitante = resultadoEquipoVisitante;
    }
}
