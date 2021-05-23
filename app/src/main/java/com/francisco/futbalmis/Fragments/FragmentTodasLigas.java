package com.francisco.futbalmis.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.ListAdapter.ListAdapterLigas;
import com.francisco.futbalmis.MainActivity;
import com.francisco.futbalmis.R;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;

public class FragmentTodasLigas extends Fragment{
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private static ListAdapterLigas listAdapter;
    private List<Liga> ligas;
    private FragmentTransaction FT;
    MaterialToolbar appBar;

    public FragmentTodasLigas(Context context, FragmentTransaction FT, List<Liga> ligas) {
        this.context = context;
        this.FT = FT;
        this.ligas=ligas;
        listAdapter = new ListAdapterLigas(ligas, context);
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
        FT = getActivity().getSupportFragmentManager().beginTransaction();

        listAdapter.setOnItemClickListener((position, v) -> {
            if (listAdapter.getData().get(position).getId() > 0 &&
                    getActivity().getSupportFragmentManager().getFragments().get(getActivity().getSupportFragmentManager().getFragments().size() - 1) instanceof FragmentTodasLigas) {
                MainActivity.cambiaVisibilidadProgressBar(View.VISIBLE);
                FragmentTodosPartidosLiga fragmentPartidos = new FragmentTodosPartidosLiga(context, listAdapter.getData().get(position), getActivity().getSupportFragmentManager().beginTransaction());
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

    private void cargarFragment(Fragment f) {
        FT = getActivity().getSupportFragmentManager().beginTransaction();
        FT.add(R.id.ligasFragment, f);
        FT.commit();
        Log.e("Numero de fragment", getActivity().getSupportFragmentManager().getFragments().size() + "");
        FT = null;
    }
}