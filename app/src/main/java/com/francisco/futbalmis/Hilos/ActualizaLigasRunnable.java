package com.francisco.futbalmis.Hilos;

import com.francisco.futbalmis.Clases.Clasificacion;
import com.francisco.futbalmis.Servicios.ServicioApi;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ActualizaLigasRunnable implements Runnable {
    @Override
    public void run() {
        ServicioApi.actualizaPartidos();
        while (true) {
            try {
                Thread.sleep(150000);//2.5 minutos
                ServicioApi.actualizaPartidosHoy();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
}
