package com.francisco.futbalmis.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Fecha;
import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.Hilos.LigasFechaCallable;
import com.francisco.futbalmis.ListAdapter.ListAdapterLigas;
import com.francisco.futbalmis.ListAdapter.ListAdapterPartidos;
import com.francisco.futbalmis.MainActivity;
import com.francisco.futbalmis.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FragmentLigas extends Fragment implements Toolbar.OnMenuItemClickListener {
    private View view;
    private Context context;
    private static RecyclerView recyclerViewLigasFavoritas;
    private static RecyclerView recyclerViewLigasNoFavoritas;
    private static ListAdapterLigas listAdapterLigasFavoritas;
    private static ListAdapterLigas listAdapterLigasNoFavoritas;
    private static ArrayList<Liga> ligas;
    private static ArrayList<Liga> ligasFavoritas = new ArrayList<>();
    private static ArrayList<Liga> ligasNoFavoritas = new ArrayList<>();
    private static Fecha fecha;
    private FragmentTransaction FT;
    MaterialToolbar appBar;
    private static List<Integer> idLigasFavoritas;
    private static LinearLayout layoutLigasFavoritas;
    private static LinearLayout layoutLigasNoFavoritas;
    private static final int ALTURA_MINIMA_LIGA = 200;


    public FragmentLigas(Context context, FragmentTransaction FT, Fecha fecha, List<Integer> idLigasFavoritas) {
        this.context = context;
        this.FT = FT;
        this.fecha = fecha;
        this.idLigasFavoritas = idLigasFavoritas;

        try {
            ExecutorService es = Executors.newSingleThreadExecutor();
            Future<ArrayList<Liga>> result = es.submit(new LigasFechaCallable(fecha));
            ligas = result.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        if (!ligas.isEmpty()) {
            rellenaListasLiga();
        }

        listAdapterLigasFavoritas = new ListAdapterLigas(ligasFavoritas, context);
        listAdapterLigasNoFavoritas = new ListAdapterLigas(ligasNoFavoritas, context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lista_ligas_favoritos, container, false);
        setHasOptionsMenu(true);
        layoutLigasFavoritas = view.findViewById(R.id.cabeceraFavoritas);
        layoutLigasNoFavoritas = view.findViewById(R.id.cabeceraNoFavoritas);
        recyclerViewLigasFavoritas = view.findViewById(R.id.listaLigasFavoritas);
        recyclerViewLigasNoFavoritas = view.findViewById(R.id.listaLigasNoFavoritas);
        gestionaAlturaRecyclers();
        recyclerViewLigasFavoritas.setHasFixedSize(true);
        recyclerViewLigasNoFavoritas.setHasFixedSize(true);
        recyclerViewLigasNoFavoritas.setLayoutManager(new LinearLayoutManager(context));
        recyclerViewLigasFavoritas.setLayoutManager(new LinearLayoutManager(context));

        recyclerViewLigasFavoritas.setAdapter(listAdapterLigasFavoritas);
        recyclerViewLigasNoFavoritas.setAdapter(listAdapterLigasNoFavoritas);
        appBar = view.findViewById(R.id.materialAppBar);
        appBar.setOnMenuItemClickListener(this);
        FT = getActivity().getSupportFragmentManager().beginTransaction();

        listAdapterLigasFavoritas.setOnItemClickListener((position, v) -> {
            if (listAdapterLigasFavoritas.getData().get(position).getId() > 0 &&
                    getActivity().getSupportFragmentManager().getFragments().get(getActivity().getSupportFragmentManager().getFragments().size() - 1) instanceof FragmentLigas) {
                lanzarFragmentPartidosLiga(position, listAdapterLigasFavoritas);
            }
        });

        listAdapterLigasNoFavoritas.setOnItemClickListener((position, v) -> {
            if (listAdapterLigasNoFavoritas.getData().get(position).getId() > 0 &&
                    getActivity().getSupportFragmentManager().getFragments().get(getActivity().getSupportFragmentManager().getFragments().size() - 1) instanceof FragmentLigas) {
                lanzarFragmentPartidosLiga(position, listAdapterLigasNoFavoritas);
            }
        });

        return view;
    }

    public void lanzarFragmentPartidosLiga(int position, ListAdapterLigas listAdapter) {
        MainActivity.cambiaVisibilidadProgressBar(View.VISIBLE);
        FragmentPartidos fragmentPartidos = new FragmentPartidos(context, listAdapter.getData().get(position), fecha, getActivity().getSupportFragmentManager().beginTransaction());
        cargarFragment(fragmentPartidos);
        MainActivity.cambiaVisibilidadTabLayout(View.GONE);
    }

    public static void setData(ArrayList<Liga> ligasNuevas) {
        ligas = ligasNuevas;
        rellenaListasLiga();
        listAdapterLigasFavoritas.setData(ligasFavoritas);
        listAdapterLigasNoFavoritas.setData(ligasNoFavoritas);

        listAdapterLigasFavoritas.notifyDataSetChanged();
        listAdapterLigasNoFavoritas.notifyDataSetChanged();
        System.out.println("Set data\n Favoritas: " + ligasFavoritas.size() + "\nNO favoritas: " + ligasNoFavoritas.size());
        gestionaAlturaRecyclers();
    }

    public static void setFecha(Fecha fechaNueva) {
        fecha = new Fecha(fechaNueva);
    }

    private void cargarFragment(Fragment f) {
        FT = getActivity().getSupportFragmentManager().beginTransaction();
        FT.add(R.id.ligasFragment, f);
        FT.commit();
        Log.e("Numero de fragment", getActivity().getSupportFragmentManager().getFragments().size() + "");
        FT = null;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        FragmentDialogoFecha fragmentDialogoFecha = new FragmentDialogoFecha(context, fecha);
        fragmentDialogoFecha.show(getActivity().getSupportFragmentManager(), "FragmentDialogoFechaLigas");

        return false;
    }

    private static void rellenaListasLiga() {
        ligasFavoritas = new ArrayList<>();
        ligasNoFavoritas = new ArrayList<>();

        ligas.forEach(liga -> {
            if (idLigasFavoritas.contains(liga.getId())) ligasFavoritas.add(liga);
            else ligasNoFavoritas.add(liga);
        });
    }

    public static void actualizaVista(List<Integer> idLigasFavoritasNuevas) {
        idLigasFavoritas = idLigasFavoritasNuevas;
        rellenaListasLiga();
        listAdapterLigasFavoritas.setData(ligasFavoritas);
        listAdapterLigasNoFavoritas.setData(ligasNoFavoritas);
        listAdapterLigasFavoritas.notifyDataSetChanged();
        listAdapterLigasNoFavoritas.notifyDataSetChanged();
        System.out.println("Actualiza vista\n Favoritas: " + ligasFavoritas.size() + "NO favoritas: " + ligasNoFavoritas.size());
        gestionaAlturaRecyclers();
    }

    private static void gestionaAlturaRecyclers() {
        ViewGroup.LayoutParams params;
        if (ligas.size()>0 && ligas.get(0).getId() == -1) {
            recyclerViewLigasNoFavoritas.setVisibility(View.GONE);
            layoutLigasNoFavoritas.setVisibility(View.GONE);
            layoutLigasFavoritas.setVisibility(View.GONE);
            recyclerViewLigasFavoritas.setVisibility(View.VISIBLE);
            listAdapterLigasFavoritas.setData(ligas);
            listAdapterLigasFavoritas.notifyDataSetChanged();
        } else {
            if (ligasFavoritas.size() > 0) {
                recyclerViewLigasFavoritas.setVisibility(View.VISIBLE);
                layoutLigasFavoritas.setVisibility(View.VISIBLE);
                params = recyclerViewLigasFavoritas.getLayoutParams();
                params.height = ALTURA_MINIMA_LIGA * ligasFavoritas.size();
                recyclerViewLigasFavoritas.setLayoutParams(params);
            } else {
                System.out.println("ligas favoritas no tiene elementos" + listAdapterLigasFavoritas.getData().size());
                recyclerViewLigasFavoritas.setVisibility(View.GONE);
                layoutLigasFavoritas.setVisibility(View.GONE);
            }

            if (ligasNoFavoritas.size() > 0) {
                recyclerViewLigasNoFavoritas.setVisibility(View.VISIBLE);
                layoutLigasNoFavoritas.setVisibility(View.VISIBLE);
                params = recyclerViewLigasNoFavoritas.getLayoutParams();
                params.height = ALTURA_MINIMA_LIGA * ligasNoFavoritas.size();
                recyclerViewLigasNoFavoritas.setLayoutParams(params);
            } else {
//            System.out.println("ligas NO favoritas no tiene elementos"+listAdapterLigasNoFavoritas.getData().size());
                recyclerViewLigasNoFavoritas.setVisibility(View.GONE);
                layoutLigasNoFavoritas.setVisibility(View.GONE);
            }
        }
    }
}