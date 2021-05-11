package com.francisco.futbalmis.Hilos;


import com.francisco.futbalmis.Clases.Fecha;
import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.Servicios.ServicioApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class LigasFechaCallable implements Callable<ArrayList<Liga>> {
    Fecha fecha;

    public LigasFechaCallable(Fecha fecha) {
        this.fecha = fecha;
    }

    @Override
    public ArrayList<Liga> call() throws IOException {
        return ServicioApi.getLigasFecha(fecha.toString());
    }
}
