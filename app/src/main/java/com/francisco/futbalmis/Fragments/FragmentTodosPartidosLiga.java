package com.francisco.futbalmis.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.Clases.Partido;
import com.francisco.futbalmis.Hilos.TodosPartidosLigaCallable;
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

public class FragmentTodosPartidosLiga extends Fragment implements View.OnClickListener{
    private View view;
    private static Context context;
    private static RecyclerView recyclerView;
    private static ListAdapterPartidos listAdapter;
    private ArrayList<Partido> partidos = new ArrayList<>();
    private Liga ligaPartido;
    TextView nombreLiga, paisLiga;
    public ImageView banderaPaisLiga;
    LinearLayout liga;
    MaterialToolbar appBar;
    FragmentTransaction FT;

    public FragmentTodosPartidosLiga(Context context, Liga ligaPartido, FragmentTransaction FT) {
        this.context = context;
        this.FT = FT;
        this.ligaPartido = new Liga(ligaPartido.getId(), ligaPartido.getNombre(), ligaPartido.getCodigoPais(), ligaPartido.getPais(), ligaPartido.getBanderaURL());

        try {
            ExecutorService es = Executors.newSingleThreadExecutor();
            Future<ArrayList<Partido>> result = es.submit(new TodosPartidosLigaCallable(ligaPartido.getId()));
            partidos = result.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        listAdapter = new ListAdapterPartidos(partidos, context,true);
        listAdapter.setData(new ArrayList<>());
        listAdapter.notifyDataSetChanged();
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
        FT = getActivity().getSupportFragmentManager().beginTransaction();

        Utils.fetchSvg(context, ligaPartido.getBanderaURL(), banderaPaisLiga);
        paisLiga.setText(ligaPartido.getPais() + ":");
        nombreLiga.setText(ligaPartido.getNombre());

        liga.setOnClickListener(this);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(listAdapter);
        return view;
    }

    public static void setData(ArrayList<Partido> partidos) {
        listAdapter.setData(partidos);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.partidosLiga && getActivity().getSupportFragmentManager().getFragments().get(getActivity().getSupportFragmentManager().getFragments().size() - 1) instanceof FragmentTodosPartidosLiga) {
            MainActivity.cambiaVisibilidadProgressBar(View.VISIBLE);
            FragmentTransaction FT = getActivity().getSupportFragmentManager().beginTransaction();
            FT.add(R.id.ligasFragment, new FragmentClasificacion(context, ligaPartido,FT));
            FT.commit();
        }
    }
}
