package com.francisco.futbalmis.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Fecha;
import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.Hilos.LigasCallable;
import com.francisco.futbalmis.ListAdapter.ListAdapterDias;
import com.francisco.futbalmis.MainActivity;
import com.francisco.futbalmis.R;
import com.francisco.futbalmis.Servicios.Utils;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FragmentDialogoFecha extends DialogFragment {
    Context context;
    ArrayList<Fecha> fechas = new ArrayList<>();
    ListAdapterDias listAdapter;
    private static RecyclerView recyclerView;
    ImageButton botonCancelar;
    FragmentTransaction FT;
    int itemSelected;
    public FragmentDialogoFecha(Context context,Fecha fechaDiaSeleccionado) {
        this.context = context;
        itemSelected=7;
        for (int i = 0; i <= 14; i++) {
            Fecha fecha = Utils.getFecha(i - 7);
            fechas.add(fecha);
            if (i == 7) {
                fechas.get(i).setDiaSemana("HOY");
            }
        }

        listAdapter = new ListAdapterDias(fechas, context,fechaDiaSeleccionado);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialogo_fragment, container, false);
        botonCancelar = view.findViewById(R.id.cancelarDialogo);
        botonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        recyclerView = view.findViewById(R.id.recyclerFecha);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(listAdapter);
        FT = getActivity().getSupportFragmentManager().beginTransaction();

        listAdapter.setOnItemClickListener((position, v) -> {
            try {
                itemSelected=position;
                vuelveALigas();
                MainActivity.cambiaVisibilidadProgressBar(View.VISIBLE);
                Fecha fechaDiaSeleccionado = Utils.getFecha(position - Utils.DIAS_SEMANA);
                FragmentLigas.setFecha(fechaDiaSeleccionado);
                FragmentLigas.setData(new ArrayList<>());
                ExecutorService es = Executors.newSingleThreadExecutor();
                Future<ArrayList<Liga>> result = es.submit(new LigasCallable(fechaDiaSeleccionado));
                result.get();

            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        });
        return view;
    }

    private void vuelveALigas() {
        if (getActivity().getSupportFragmentManager().findFragmentById(R.id.ligasFragment) instanceof FragmentPartidos)
            getActivity().onBackPressed();
        else dismiss();

    }
}
