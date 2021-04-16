package com.francisco.futbalmis.Hilos;



import com.francisco.futbalmis.Clases.Clasificacion;
import com.francisco.futbalmis.Clases.Partido;
import com.francisco.futbalmis.Servicios.ServicioApi;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class ClasificacionCallable implements Callable<ArrayList<Clasificacion>> {
    private int idLiga;
    public ClasificacionCallable(int idLiga) {
        this.idLiga=idLiga;
    }

    @Override
    public ArrayList<Clasificacion> call() throws Exception {
        ArrayList<Clasificacion> partidos=ServicioApi.getClasificacion(idLiga);

        return partidos;
    }
}
