package com.francisco.futbalmis.Clases;

public class Fecha {
    private String dia;
    private String mes;
    private String anyo;
    private String diaSemana;

    public Fecha(String dia, String mes, String anyo, String diaSemana) {
        this.dia = dia;
        this.mes = mes;
        this.anyo = anyo;
        this.diaSemana = diaSemana;
    }

    public Fecha(String dia, String mes, String diaSemana) {
        this.dia = dia;
        this.mes = mes;
        this.diaSemana = diaSemana;
    }

    public Fecha(Fecha fecha){
        dia = fecha.getDia();
        mes = fecha.getMes();
        anyo = fecha.getAnyo();
        diaSemana = fecha.getDiaSemana();
    }

    public String getDia() {
        return dia;
    }

    public void setDia(String dia) {
        this.dia = dia;
    }

    public String getMes() {
        return mes;
    }

    public void setMes(String mes) {
        this.mes = mes;
    }

    public String getAnyo() {
        return anyo;
    }

    public void setAnyo(String anyo) {
        this.anyo = anyo;
    }

    public String getDiaSemana() {
        return diaSemana;
    }

    public void setDiaSemana(String diaSemana) {
        this.diaSemana = diaSemana;
    }

    @Override
    public String toString() {
        return anyo+"-"+mes+"-"+dia;
    }
}
