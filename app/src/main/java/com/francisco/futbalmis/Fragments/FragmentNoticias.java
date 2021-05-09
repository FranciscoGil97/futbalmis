package com.francisco.futbalmis.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Noticia;
import com.francisco.futbalmis.Hilos.LectorRssCallable;
import com.francisco.futbalmis.ListAdapter.ListAdapterNoticias;
import com.francisco.futbalmis.R;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FragmentNoticias extends Fragment {
    private View view;
    private static Context context;
    private static RecyclerView recyclerView;
    private static ListAdapterNoticias listAdapter;
    private List<Noticia> noticias = new ArrayList<>();
    FragmentTransaction FT;

    public FragmentNoticias(Context context, FragmentTransaction FT, String direccion) {
        this.context = context;
        this.FT = FT;
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<List<Noticia>> result = es.submit(new LectorRssCallable(direccion));

        listAdapter = new ListAdapterNoticias(context, noticias);

        try {
            noticias = result.get();

        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lista_noticias, container, false);
        recyclerView = view.findViewById(R.id.listanoticias);
        FT = getActivity().getSupportFragmentManager().beginTransaction();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(listAdapter);


        listAdapter.setOnItemClickListener((position, v) -> {
            FragmentTransaction FT = getActivity().getSupportFragmentManager().beginTransaction();
            FT.add(R.id.ligasFragment, new FragmentNoticiasCompleta(noticias.get(position)));
            FT.commit();
            System.out.println("Numero de fragments: "+getActivity().getSupportFragmentManager().getFragments().size());
        });

        return view;
    }

    public static void setData(List<Noticia> noticias) {
        listAdapter.setData(noticias);
        listAdapter.notifyDataSetChanged();
    }

}
