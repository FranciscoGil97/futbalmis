package com.francisco.futbalmis.Clases;

import java.io.Serializable;

public class Liga implements Serializable {
    private int id;
    private String nombre;
    private String codigoPais;
    private String pais;
    private String banderaURL;

    public Liga(
            int id,
            String nombre,
            String codigoPais,
            String pais,
            String banderaURL) {
        this.id = id;
        this.nombre = nombre;
        this.codigoPais = codigoPais;
        this.pais = pais;
        this.banderaURL = banderaURL;
    }

    public Liga() { }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCodigoPais() {
        return codigoPais;
    }

    public void setCodigoPais(String codigoPais) {
        this.codigoPais = codigoPais;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public String getBanderaURL() {
        return banderaURL;
    }

    public void setBanderaURL(String banderaURL) {
        this.banderaURL = banderaURL;
    }
}
