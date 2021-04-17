package com.francisco.futbalmis.Hilos;

import com.francisco.futbalmis.Clases.Partido;
import com.francisco.futbalmis.Servicios.ServicioApi;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class PartidosEquipoCallable implements Callable<ArrayList<Partido>> {
    private int idEquipo;

    public PartidosEquipoCallable(int idEquipo){
        this.idEquipo=idEquipo;
    }

    @Override
    public ArrayList<Partido> call() throws Exception {
        return ServicioApi.getPartidosEquipo(idEquipo);
    }
}
