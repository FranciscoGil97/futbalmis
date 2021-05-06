package com.francisco.futbalmis.Hilos;

import com.francisco.futbalmis.Clases.Clasificacion;
import com.francisco.futbalmis.Servicios.ServicioApi;

import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

public class ActualizaLigasRunnable implements Runnable {
    @Override
    public void run() {
        ServicioApi.actualizaPartidos();
        while (true) {
            try {
                ServicioApi.actualizaPartidosHoy();
                Thread.sleep(TimeUnit.MILLISECONDS.toMinutes((long) 2.5));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
