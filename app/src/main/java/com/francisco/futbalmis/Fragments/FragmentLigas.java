package com.francisco.futbalmis.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

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
import com.francisco.futbalmis.MainActivity;
import com.francisco.futbalmis.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FragmentLigas extends Fragment implements Toolbar.OnMenuItemClickListener {
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private static ListAdapterLigas listAdapter;
    private ArrayList<Liga> ligas;
    private static Fecha fecha;
    private FragmentTransaction FT;
    MaterialToolbar appBar;

    public FragmentLigas(Context context, FragmentTransaction FT, Fecha fecha) {
        this.context = context;
        this.FT = FT;
        this.fecha = fecha;

        try {
            ExecutorService es = Executors.newSingleThreadExecutor();
            Future<ArrayList<Liga>> result = es.submit(new LigasFechaCallable(fecha));
            ligas = result.get();
            listAdapter = new ListAdapterLigas(ligas, context);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lista_ligas, container, false);
        setHasOptionsMenu(true);
        recyclerView = view.findViewById(R.id.listaLigas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(listAdapter);
        appBar = view.findViewById(R.id.materialAppBar);
        appBar.setOnMenuItemClickListener(this);
        FT = getActivity().getSupportFragmentManager().beginTransaction();

        listAdapter.setOnItemClickListener((position, v) -> {
            if (listAdapter.getData().get(position).getId() > 0 &&
                    getActivity().getSupportFragmentManager().getFragments().get(getActivity().getSupportFragmentManager().getFragments().size() - 1) instanceof FragmentLigas) {
                MainActivity.cambiaVisibilidadProgressBar(View.VISIBLE);
                FragmentPartidos fragmentPartidos = new FragmentPartidos(context, listAdapter.getData().get(position), fecha, getActivity().getSupportFragmentManager().beginTransaction());
                cargarFragment(fragmentPartidos);
                MainActivity.cambiaVisibilidadTabLayout(View.GONE);
            }
        });

        return view;
    }

    public static void setData(ArrayList<Liga> ligas) {
        listAdapter.setData(ligas);
        listAdapter.notifyDataSetChanged();
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
}