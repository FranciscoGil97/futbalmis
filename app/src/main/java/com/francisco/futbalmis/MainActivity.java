package com.francisco.futbalmis;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.francisco.futbalmis.Fragments.FragmentClasificacion;
import com.francisco.futbalmis.Fragments.FragmentLigas;
import com.francisco.futbalmis.Fragments.FragmentNoticias;
import com.francisco.futbalmis.Fragments.FragmentNoticiasCompleta;
import com.francisco.futbalmis.Fragments.FragmentPartidos;
import com.francisco.futbalmis.Fragments.FragmentPartidosEquipo;
import com.francisco.futbalmis.Hilos.ActualizaLigasRunnable;
import com.francisco.futbalmis.Servicios.ServicioApi;
import com.francisco.futbalmis.Servicios.Utils;
import com.google.android.material.tabs.TabLayout;

import java.util.Date;
import java.util.concurrent.TimeUnit;

@SuppressLint("StaticFieldLeak")
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, TabLayout.OnTabSelectedListener {
    public static FragmentTransaction FT;
    private static SwipeRefreshLayout swipeRefreshLayout;
    FragmentLigas fragmentLigas;
    FragmentNoticias fragmentNoticias;
    private static ProgressBar progressBar;
    private Date fechaUltimaActualizacion = null;
    static TabLayout tabs;
    int tabActual = 0;
    private final String URL_NOTICIAS = "https://as.com/rss/futbol/portada.xml";
    TabLayout.Tab[] tabArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        lanzaHiloActualizacionPartidos();
        progressBar = findViewById(R.id.progressBarLigas);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        swipeRefreshLayout.setDistanceToTriggerSync(500);
        tabs = findViewById(R.id.tab_layout);
        tabArray = new TabLayout.Tab[]{tabs.newTab().setText("Partidos").setIcon(R.drawable.partidos),
                tabs.newTab().setText("Noticias").setIcon(R.drawable.noticias)};

        tabs.addTab(tabArray[0]);
        tabs.addTab(tabArray[1]);

        tabs.addOnTabSelectedListener(this);

        cambiaVisibilidadProgressBar(View.VISIBLE);

        fragmentNoticias = new FragmentNoticias(this, getSupportFragmentManager().beginTransaction(), URL_NOTICIAS);

        FT = getSupportFragmentManager().beginTransaction();
        fragmentLigas = new FragmentLigas(this, FT, Utils.getFecha(0));
        cargarFragment(fragmentLigas);

        swipeRefreshLayout.setOnRefreshListener(this);
    }


    @Override
    public void onBackPressed() {
        Fragment fragmentActual = getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1);
        if (!(fragmentActual instanceof FragmentLigas)) {
            if ((fragmentActual instanceof FragmentNoticias)){
                swipeRefreshLayout.setEnabled(true);
                tabArray[0].select();
            }
            if (fragmentActual instanceof FragmentPartidos)
                cambiaVisibilidadTabLayout(View.VISIBLE);
            if (!(fragmentActual instanceof FragmentPartidos))
                cambiaVisibilidadProgressBar(View.GONE);
            desapilaFragment();
        } else finish();
    }

    @Override
    public void onRefresh() {
        gestionaActualizacionPartidosOnRefresh();
    }

    private void cargarFragment(Fragment f) {
        FT = getSupportFragmentManager().beginTransaction();
        FT.add(R.id.ligasFragment, f, "fragmentLigas");
        FT.commit();
        FT = null;
    }

    private void desapilaFragment() {
        FT = getSupportFragmentManager().beginTransaction();
        FT.remove(getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1)).commit();
        getSupportFragmentManager().popBackStack();
    }

    public static void cambiaVisibilidadProgressBar(int visibility) {
        progressBar.setVisibility(visibility);
    }

    public static void cambiaVisibilidadSwipeRefresh() {
        swipeRefreshLayout.setRefreshing(false);
    }

    private void lanzaHiloActualizacionPartidos() {
        Thread actualizaPartidos = new Thread(new ActualizaLigasRunnable());
        actualizaPartidos.setDaemon(true);
        actualizaPartidos.start();
    }

    private void gestionaActualizacionPartidosOnRefresh() {
        if (fechaUltimaActualizacion == null) {
            fechaUltimaActualizacion = new Date();
            ServicioApi.actualizaPartidosHoy();
            Log.e("Swipe", "Se actualiza primero");
        } else {
            long intervaloActualizacion = new Date().getTime() - fechaUltimaActualizacion.getTime();
            if (TimeUnit.MILLISECONDS.toMinutes(intervaloActualizacion) >= 1f) {
                ServicioApi.actualizaPartidosHoy();
                fechaUltimaActualizacion = new Date();
                Log.e("Swipe", "Se actualiza ha pasado 1 minutos");
            } else {
                cambiaVisibilidadSwipeRefresh();
                Log.e("Swipe", "No se actualiza");
            }
        }
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        if (tabActual != tab.getPosition()) {
//            tabs.getTabAt(tab.getPosition()).getIcon().setColorFilter(getResources().getColor(R.color.colorSecundario), PorterDuff.Mode.SRC_IN);

            if (tab.getText().toString().equalsIgnoreCase("noticias")) {//significa que antes estaba en partidos
                if (!getSupportFragmentManager().getFragments().contains(fragmentNoticias))
                    cargarFragment(fragmentNoticias);
                    swipeRefreshLayout.setEnabled(false);
            } else {
                    swipeRefreshLayout.setEnabled(true);
                if ((getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1) instanceof FragmentNoticiasCompleta)) {
                    for (int i = 2; i > 0; i--) {
                        FT = getSupportFragmentManager().beginTransaction();
                        FT.remove(getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - i)).commit();
                        getSupportFragmentManager().popBackStack();
                    }
                } else
                    desapilaFragment();

            }
            tabActual = tab.getPosition();
            tabArray[tabActual].select();
        }
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {
    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    public static void cambiaVisibilidadTabLayout(int visibility) {
        tabs.setVisibility(visibility);
    }

}
