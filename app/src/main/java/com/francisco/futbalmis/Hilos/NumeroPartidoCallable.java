package com.francisco.futbalmis.Hilos;

import android.util.Log;
import android.view.View;

import com.francisco.futbalmis.Clases.Clasificacion;
import com.francisco.futbalmis.MainActivity;
import com.francisco.futbalmis.Servicios.ServicioApi;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class NumeroPartidoCallable implements Callable<Integer> {
    private String fecha;

    public NumeroPartidoCallable(String fecha) {
        MainActivity.cambiaVisibilidadProgressBar(View.VISIBLE);
        this.fecha = fecha;
    }

    @Override
    public Integer call() throws Exception {
        return ServicioApi.getNumeroPartidos(fecha);
    }
}
