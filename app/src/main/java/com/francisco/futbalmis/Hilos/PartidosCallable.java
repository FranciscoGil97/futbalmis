package com.francisco.futbalmis.Hilos;



import com.francisco.futbalmis.Clases.Fecha;
import com.francisco.futbalmis.Clases.Partido;
import com.francisco.futbalmis.MainActivity;
import com.francisco.futbalmis.Servicios.ServicioApi;

import java.util.ArrayList;
import java.util.concurrent.Callable;

public class PartidosCallable implements Callable<ArrayList<Partido>> {
    private int idLiga;
    private Fecha fecha;
    public PartidosCallable(int idLiga,Fecha fecha) {
        this.idLiga=idLiga;
        this.fecha=fecha;
    }

    @Override
    public ArrayList<Partido> call() throws Exception {
        ArrayList<Partido> partidos=ServicioApi.getPartidosFechaLiga(fecha,idLiga);

        return partidos;
    }
}
