package com.francisco.futbalmis;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.francisco.futbalmis.Clases.Fecha;
import com.francisco.futbalmis.Fragments.FragmentLigas;
import com.francisco.futbalmis.Fragments.FragmentPartidos;
import com.francisco.futbalmis.Hilos.ActualizaLigasRunnable;
import com.francisco.futbalmis.Servicios.ServicioApi;
import com.francisco.futbalmis.Servicios.Utils;

import java.util.Date;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    public static FragmentTransaction FT;
    private static SwipeRefreshLayout swipeRefreshLayout;
    FragmentLigas fragmentLigas;
    private static ProgressBar progressBar;
    private Date fechaUltimaActualizacion = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        Lanzar hilo de actualizacion de partidos
//        Thread actualizaPartidos = new Thread(new ActualizaLigasRunnable());
//        actualizaPartidos.setDaemon(true);
//        actualizaPartidos.start();

        progressBar = findViewById(R.id.progressBarLigas);
        MainActivity.cambiaVisibilidadProgressBar(View.VISIBLE);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        Fecha fechaActual = Utils.getFecha(0);

        FT = getSupportFragmentManager().beginTransaction();
        fragmentLigas = new FragmentLigas(this, FT, fechaActual);
        cargarFragment(fragmentLigas);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (fechaUltimaActualizacion == null) {
                    fechaUltimaActualizacion = new Date();
                    ServicioApi.actualizaPartidosHoy();
                    Log.e("Swipe","Se actualiza primero");
                }
                else {
                    long intervaloActualizacion=new Date().getTime()-fechaUltimaActualizacion.getTime();
                    if(TimeUnit.MILLISECONDS.toMinutes(intervaloActualizacion)>=1f){
                        ServicioApi.actualizaPartidosHoy();
                        fechaUltimaActualizacion=new Date();
                        Log.e("Swipe","Se actualiza ha pasado 1 minutos");
                    }else{
                        cambiaVisibilidadSwipeRefresh();
                        Log.e("Swipe","No se actualiza");
                    }
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (!(getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1) instanceof FragmentLigas)) {
            desapilaFragment();
        } else finish();
    }

    private void cargarFragment(Fragment f) {
        FT = getSupportFragmentManager().beginTransaction();
        FT.add(R.id.ligasFragment, f, "fragmentLigas");
        FT.commit();
    }

    private void desapilaFragment() {
        FT = getSupportFragmentManager().beginTransaction();
        FT.remove(getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1)).commit();
        getSupportFragmentManager().popBackStack();
    }

    public static void cambiaVisibilidadProgressBar(int visibility) {
        progressBar.setVisibility(visibility);
    }
    public static void cambiaVisibilidadSwipeRefresh(){
        swipeRefreshLayout.setRefreshing(false);
    }
}