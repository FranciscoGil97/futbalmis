package com.francisco.futbalmis.Clases;

import java.io.Serializable;

public class Clasificacion implements Serializable {
    private Equipo equipo;
    private int posicion;
    private int puntos;
    private int golesFavor;
    private int goleContra;
    private int partidosJugados;

    public Clasificacion(
            Equipo equipo,
            int posicion,
            int puntos,
            int golesFavor,
            int goleContra,
            int partidosJugados) {
        this.equipo = equipo;
        this.posicion = posicion;
        this.puntos = puntos;
        this.golesFavor = golesFavor;
        this.goleContra = goleContra;
        this.partidosJugados = partidosJugados;
    }

    public Equipo getEquipo() {
        return equipo;
    }

    public void setEquipo(Equipo equipo) {
        this.equipo = equipo;
    }

    public int getPosicion() {
        return posicion;
    }

    public void setPosicion(int posicion) {
        this.posicion = posicion;
    }

    public int getPuntos() {
        return puntos;
    }

    public void setPuntos(int puntos) {
        this.puntos = puntos;
    }

    public int getGolesFavor() {
        return golesFavor;
    }

    public void setGolesFavor(int golesFavor) {
        this.golesFavor = golesFavor;
    }

    public int getGoleContra() {
        return goleContra;
    }

    public void setGoleContra(int goleContra) {
        this.goleContra = goleContra;
    }

    public int getPartidosJugados() {
        return partidosJugados;
    }

    public void setPartidosJugados(int partidosJugados) {
        this.partidosJugados = partidosJugados;
    }
}

