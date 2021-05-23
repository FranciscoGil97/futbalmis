package com.francisco.futbalmis.Hilos;

import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.Servicios.ServicioApi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.Callable;

public class LigasCallable implements Callable<ArrayList<Liga>> {
    boolean vieneDeLogin;

    public LigasCallable(boolean vieneDeLogin){
        this.vieneDeLogin=vieneDeLogin;
    }

    @Override
    public ArrayList<Liga> call() throws IOException {
        //se llama a una o a otra para que sepa cuando abtenga los datos donde llamar para que se rellene el ListAdapter
        if(vieneDeLogin) return ServicioApi.getLigasLogin();
        else return ServicioApi.getLigas();
    }
}
