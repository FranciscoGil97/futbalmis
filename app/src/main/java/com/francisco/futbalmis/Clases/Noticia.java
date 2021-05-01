package com.francisco.futbalmis.Clases;

import java.util.Date;

public class Noticia {
    private String titulo;
    private String descripcion;
    private String contenido;
    private Fecha fechaPublicacion;
    private String urlImagenNoticia;

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getContenido() {
        return contenido;
    }

    public void setContenido(String contenido) {
        this.contenido = contenido;
    }

    public Fecha getFechaPublicacion() {
        return fechaPublicacion;
    }

    public void setFechaPublicacion(Fecha fechaPublicacion) {
        this.fechaPublicacion = fechaPublicacion;
    }

    public String getUrlImagenNoticia() {
        return urlImagenNoticia;
    }

    public void setUrlImagenNoticia(String urlImagenNoticia) {
        this.urlImagenNoticia = urlImagenNoticia;
    }

    @Override
    public String toString() {
        return "Noticia{" +
                "titulo= " + titulo + '\n' +
                ", descripcion=" + descripcion + '\n' +
                ", contenido=" + contenido + '\n' +
                ", fechaPublicacion=" + fechaPublicacion + '\n' +
                ", urlImagenNoticia=" + urlImagenNoticia + '\n' +
                '}';
    }
}
