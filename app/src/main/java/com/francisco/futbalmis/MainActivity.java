package com.francisco.futbalmis;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Fecha;
import com.francisco.futbalmis.Fragments.FragmentLigas;
import com.francisco.futbalmis.Fragments.FragmentPartidos;
import com.francisco.futbalmis.Hilos.ActualizaLigasRunnable;
import com.francisco.futbalmis.Hilos.NumeroPartidoCallable;
import com.francisco.futbalmis.ListAdapter.ListAdapterPartidos;
import com.francisco.futbalmis.Servicios.Utils;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class MainActivity extends AppCompatActivity {

    public static RecyclerView recyclerView;
    public static ListAdapterPartidos listAdapter;
    public static FragmentTransaction FT;
    FragmentLigas fragmentLigas;
    private static ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        progressBar=findViewById(R.id.progressBarLigas);
        MainActivity.cambiaVisibilidadProgressBar(View.VISIBLE);
        try {
            Fecha fechaActual = Utils.getFecha(0);
            ExecutorService servicio = Executors.newSingleThreadExecutor();
            Future<Integer> resultado = servicio.submit(new NumeroPartidoCallable(fechaActual.toString()));
            int numeroPartidos = resultado.get();
            System.out.println("Numero partido: " + numeroPartidos);
            numeroPartidos+=1;
            FT = getSupportFragmentManager().beginTransaction();
            fragmentLigas = new FragmentLigas(this, FT, fechaActual, numeroPartidos);
            cargarFragment(fragmentLigas);


        } catch (ExecutionException e) {
            e.printStackTrace();
            System.out.println("ERROR numero partidos");
        } catch (InterruptedException e) {
            e.printStackTrace();
            System.out.println("ERROR numero partidos");
        }


//        Lanzar hilo de actualizacion de partidos
        Thread actualizaPartidos = new Thread(new ActualizaLigasRunnable());
        actualizaPartidos.setDaemon(true);
        actualizaPartidos.start();

    }

    @Override
    public void onBackPressed() {
        if (!(getSupportFragmentManager().findFragmentById(R.id.ligasFragment) instanceof FragmentLigas)) {
            Log.e("Numero de fragment", getSupportFragmentManager().getFragments().size() + "");
            if ((getSupportFragmentManager().findFragmentById(R.id.ligasFragment) instanceof FragmentPartidos)) {
                FT = getSupportFragmentManager().beginTransaction();
                FT.remove(getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1))
                        .commit();
                getSupportFragmentManager().popBackStack();
                if (!((getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 2)) instanceof FragmentLigas)) {
                    FT = getSupportFragmentManager().beginTransaction();
                    FT.remove(getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 2))
                            .commit();
                    getSupportFragmentManager().popBackStack();

                }
            } else {
                FT = getSupportFragmentManager().beginTransaction();
                FT.remove(getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1))
                        .commit();
                getSupportFragmentManager().popBackStack();
            }
        } else finish();
    }

    private void cargarFragment(Fragment f) {
        FT = getSupportFragmentManager().beginTransaction();
        FT.add(R.id.ligasFragment, f, "fragmentLigas");

        FT.commit();
    }

    public static void cambiaVisibilidadProgressBar(int visibility){
            progressBar.setVisibility(visibility);
    }
}