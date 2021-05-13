package com.francisco.futbalmis.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Equipo;
import com.francisco.futbalmis.Clases.Partido;
import com.francisco.futbalmis.Hilos.PartidosEquipoCallable;
import com.francisco.futbalmis.ListAdapter.ListAdapterClasificacion;
import com.francisco.futbalmis.ListAdapter.ListAdapterPartidos;
import com.francisco.futbalmis.R;
import com.francisco.futbalmis.Servicios.Utils;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FragmentPartidosEquipo extends Fragment {
    private View view;
    private static Context context;
    private static RecyclerView recyclerView;
    private static ListAdapterPartidos listAdapter;
    private ArrayList<Partido> partidos = new ArrayList<>();
    private Equipo equipo;
    TextView nombreEquipo;
    public ImageView escudoEquipo;

    public FragmentPartidosEquipo(Context context, Equipo equipo) {
        this.context = context;
        this.equipo=equipo;
        try {
            ExecutorService es = Executors.newSingleThreadExecutor();
            Future<ArrayList<Partido>> result = es.submit(new PartidosEquipoCallable(equipo.getId()));
            partidos = result.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        listAdapter = new ListAdapterPartidos(partidos,context,true);
        listAdapter.setData(new ArrayList<>());
        listAdapter.notifyDataSetChanged();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lista_partidos_equipo, container, false);
        recyclerView = view.findViewById(R.id.listaPartidosEquipo);

        nombreEquipo = view.findViewById(R.id.nombreEquipo);
        escudoEquipo = view.findViewById(R.id.escudoEquipo);

        Utils.fetchSvg(context, equipo.getURLEscudo(), escudoEquipo);
        nombreEquipo.setText(equipo.getNombre());

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(listAdapter);
        return view;
    }

    public static void setData(ArrayList<Partido> partidos) {
        listAdapter.setData(partidos);
        listAdapter.notifyDataSetChanged();
    }
}
