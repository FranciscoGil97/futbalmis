package com.francisco.futbalmis.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Fecha;
import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.Clases.Partido;
import com.francisco.futbalmis.Hilos.PartidosCallable;
import com.francisco.futbalmis.ListAdapter.ListAdapterPartidos;
import com.francisco.futbalmis.MainActivity;
import com.francisco.futbalmis.R;
import com.francisco.futbalmis.Servicios.Utils;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FragmentPartidos extends Fragment implements View.OnClickListener, Toolbar.OnMenuItemClickListener {
    private View view;
    private static Context context;
    private static RecyclerView recyclerView;
    private static ListAdapterPartidos listAdapter;
    private ArrayList<Partido> partidos = new ArrayList<>();
    private Liga ligaPartido;
    private Fecha fecha;
    TextView nombreLiga, paisLiga;
    public ImageView banderaPaisLiga;
    LinearLayout liga;
    MaterialToolbar appBar;
    FragmentTransaction FT;

    public FragmentPartidos(Context context, Liga ligaPartido, Fecha fecha,FragmentTransaction FT) {
        this.context = context;
        this.fecha = fecha;
        this.FT = FT;
        this.ligaPartido = new Liga(ligaPartido.getId(), ligaPartido.getNombre(), ligaPartido.getCodigoPais(), ligaPartido.getPais(), ligaPartido.getBanderaURL());
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<ArrayList<Partido>> result = es.submit(new PartidosCallable(ligaPartido.getId(), fecha));
        listAdapter = new ListAdapterPartidos(partidos, context);

        try {
            partidos = result.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lista_partidos, container, false);
        recyclerView = view.findViewById(R.id.listaPartidos);
        banderaPaisLiga = view.findViewById(R.id.banderaPaisLigaPartidos);
        nombreLiga = view.findViewById(R.id.nombreLigaPartidos);
        paisLiga = view.findViewById(R.id.paisLigaPartidos);
        liga = view.findViewById(R.id.partidosLiga);
        appBar = view.findViewById(R.id.materialAppBar);
        appBar.setOnMenuItemClickListener(this);
        FT = getActivity().getSupportFragmentManager().beginTransaction();

        Utils.fetchSvg(context, ligaPartido.getBanderaURL(), banderaPaisLiga);
        paisLiga.setText(ligaPartido.getPais() + ":");
        nombreLiga.setText(ligaPartido.getNombre());

        liga.setOnClickListener(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(listAdapter);

        if (listAdapter != null) {
            listAdapter.setOnItemClickListener(new ListAdapterPartidos.onClickListnerMiInterfaz() {
                @Override
                public void onItemLongClick(int position, View v) {
                }

                @Override
                public void onItemClick(int position, View v) {
                }
            });
        }
        return view;
    }

    public static void setData(ArrayList<Partido> partidos) {
        listAdapter.setData(partidos);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.partidosLiga && getActivity().getSupportFragmentManager().getFragments().get(getActivity().getSupportFragmentManager().getFragments().size() - 1) instanceof FragmentPartidos) {
            MainActivity.cambiaVisibilidadProgressBar(View.VISIBLE);
            FragmentTransaction FT = getActivity().getSupportFragmentManager().beginTransaction();
            FT.add(R.id.ligasFragment, new FragmentClasificacion(context, ligaPartido,FT));
            FT.commit();
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        FragmentDialogoFecha fragmentDialogoFecha = new FragmentDialogoFecha(context);
        fragmentDialogoFecha.show(getActivity().getSupportFragmentManager(), "FragmentDialogoFechaPartidos");

        return false;
    }
}
