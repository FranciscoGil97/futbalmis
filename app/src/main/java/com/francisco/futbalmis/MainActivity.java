package com.francisco.futbalmis;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.francisco.futbalmis.Fragments.FragmentElegirLigasFavoritas;
import com.francisco.futbalmis.Fragments.FragmentLigas;
import com.francisco.futbalmis.Fragments.FragmentNoticias;
import com.francisco.futbalmis.Fragments.FragmentNoticiasCompleta;
import com.francisco.futbalmis.Fragments.FragmentPartidos;
import com.francisco.futbalmis.Servicios.ServicioApi;
import com.francisco.futbalmis.Servicios.Utils;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.shape.CornerFamily;
import com.google.android.material.tabs.TabLayout;
import com.squareup.picasso.Picasso;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;

@SuppressLint("StaticFieldLeak")
public class MainActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, TabLayout.OnTabSelectedListener, View.OnClickListener,NavigationView.OnNavigationItemSelectedListener {
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
    Button logoutButton;
    TextView emailUsuario;
    CircleImageView imagenUsuario;
    NavigationView navigationView;
    String email, urlFoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        email=getIntent().getStringExtra("email");
        urlFoto=getIntent().getStringExtra("foto");
        gestionaInicio();
    }

    private void gestionaInicio() {
        progressBar = findViewById(R.id.progressBarLigas);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        logoutButton=findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(this);
        navigationView=findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        View header=navigationView.getHeaderView(0);
        emailUsuario=header.findViewById(R.id.emailUsuario);
        imagenUsuario=header.findViewById(R.id.imagenUsuario);
        emailUsuario.setText(email);
        if(urlFoto!=null){
            Picasso.get().load(urlFoto).into(imagenUsuario);
        }

        swipeRefreshLayout.setDistanceToTriggerSync(500);
        tabs = findViewById(R.id.tab_layout);
        tabArray = new TabLayout.Tab[]{tabs.newTab().setText("Partidos").setIcon(R.drawable.partidos),
                tabs.newTab().setText("Noticias").setIcon(R.drawable.noticias)};
        tabs.addTab(tabArray[0]);
        tabs.addTab(tabArray[1]);

        tabs.addOnTabSelectedListener(this);
        cambiaVisibilidadProgressBar(View.VISIBLE);

        FT = getSupportFragmentManager().beginTransaction();
        fragmentLigas = new FragmentLigas(this, FT, Utils.getFecha(0));
        cargarFragment(fragmentLigas);

        fragmentNoticias = new FragmentNoticias(this, getSupportFragmentManager().beginTransaction(), URL_NOTICIAS);
        swipeRefreshLayout.setOnRefreshListener(this);
        setTheme(R.style.Theme_Futbalmis);
    }


    @Override
    public void onBackPressed() {
        Fragment fragmentActual = getSupportFragmentManager().getFragments().get(getSupportFragmentManager().getFragments().size() - 1);
        int numeroFragments = getSupportFragmentManager().getFragments().size();

        if (!(fragmentActual instanceof FragmentLigas)) {
            if ((fragmentActual instanceof FragmentNoticias)) {
                swipeRefreshLayout.setEnabled(true);
                tabArray[0].select();
            }
            if (fragmentActual instanceof FragmentPartidos)
                cambiaVisibilidadTabLayout(View.VISIBLE);
            if (!(fragmentActual instanceof FragmentPartidos))
                cambiaVisibilidadProgressBar(View.GONE);
            desapilaFragment();
        } else {
            try {
                finish();
            } catch (Exception ex) {
                System.err.println(ex.getMessage());
            }
        }
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

    private void logout() {
        SharedPreferences prefs = getSharedPreferences("preferencias", MODE_PRIVATE);
        prefs.edit()
                .clear()
                .apply();
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==logoutButton.getId()){
            logout();
            Intent loginActivity = new Intent(this, LoginActivity.class);
            startActivityForResult(loginActivity,102);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==102){
            finish();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.todasLigas:

                break;
            case R.id.modificarFavoritos:
                cargarFragment(new FragmentElegirLigasFavoritas(this,email));
                break;
        }

        return true;
    }
}
