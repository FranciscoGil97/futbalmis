package com.francisco.futbalmis.Clases;

import java.io.Serializable;

public class Equipo implements Serializable {
    private int id;
    private int idLiga;
    private String nombre;
    private String URLEscudo;

    public Equipo(int id,
                  int idLiga,
                  String nombre,
                  String URLEscudo) {
        this.id = id;
        this.idLiga = idLiga;
        this.nombre = nombre;
        this.URLEscudo = URLEscudo;
    }

    public Equipo() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIdLiga() {
        return idLiga;
    }

    public void setIdLiga(int idLiga) {
        this.idLiga = idLiga;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getURLEscudo() {
        return URLEscudo;
    }

    public void setURLEscudo(String URLEscudo) {
        this.URLEscudo = URLEscudo;
    }
}
