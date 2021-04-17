package com.francisco.futbalmis;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.francisco.futbalmis.Clases.Fecha;
import com.francisco.futbalmis.Fragments.FragmentLigas;
import com.francisco.futbalmis.Fragments.FragmentPartidos;
import com.francisco.futbalmis.Servicios.Utils;

public class MainActivity extends AppCompatActivity {
    public static FragmentTransaction FT;
    FragmentLigas fragmentLigas;
    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar = findViewById(R.id.progressBarLigas);
        MainActivity.cambiaVisibilidadProgressBar(View.VISIBLE);
        Fecha fechaActual = Utils.getFecha(0);

        FT = getSupportFragmentManager().beginTransaction();
        fragmentLigas = new FragmentLigas(this, FT, fechaActual);
        cargarFragment(fragmentLigas);

//        Lanzar hilo de actualizacion de partidos

        Thread actualizaPartidos = new Thread(new ActualizaLigasRunnable());
        actualizaPartidos.setDaemon(true);
        actualizaPartidos.start();

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
}