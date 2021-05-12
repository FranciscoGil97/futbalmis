package com.francisco.futbalmis.Hilos;



import com.francisco.futbalmis.Clases.Fecha;
import com.francisco.futbalmis.Clases.Partido;
import com.francisco.futbalmis.Servicios.ServicioApi;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class TodosPartidosLigaCallable implements Callable<ArrayList<Partido>> {
    private int idLiga;
    public TodosPartidosLigaCallable(int idLiga) {
        this.idLiga=idLiga;
    }

    @Override
    public ArrayList<Partido> call() throws Exception {
        ArrayList<Partido> partidos=ServicioApi.getTodosPartidosLiga(idLiga);

        return partidos;
    }
}
