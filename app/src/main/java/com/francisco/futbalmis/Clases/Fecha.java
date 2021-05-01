package com.francisco.futbalmis.Clases;

import android.util.Log;

import androidx.annotation.NonNull;

import com.francisco.futbalmis.Servicios.Utils;

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

    public Fecha(String fechaString) {
        Fecha fecha = Utils.getParseFecha(fechaString);
        dia = fecha.getDia();
        mes = fecha.getMes();
        anyo = fecha.getAnyo();
        diaSemana = fecha.getDiaSemana();
    }

    public Fecha(Fecha fecha) {
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

    @NonNull
    @Override
    public String toString() {
        return anyo + "-" + mes + "-" + dia;
    }

    public String fechaPartido() {
        try {
            return dia + "/" + mes + "/" + (Integer.parseInt(anyo) % 1000);
        } catch (Exception ex) {
            Log.e("ERROR", ex.getMessage());
        }
        return "MAL";
    }
}
