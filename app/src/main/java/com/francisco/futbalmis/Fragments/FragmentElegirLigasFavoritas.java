package com.francisco.futbalmis.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.Hilos.LigasCallable;
import com.francisco.futbalmis.ListAdapter.ListAdapterElegirLigasFavoritas;
import com.francisco.futbalmis.MainActivity;
import com.francisco.futbalmis.R;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FragmentElegirLigasFavoritas extends Fragment implements View.OnClickListener {
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private static ListAdapterElegirLigasFavoritas listAdapter;
    Button siguienteButton;
    String email;
    boolean isLogin;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    List<Integer> idLigasSeleccionas = new ArrayList<>();

    private final int MAINACTIVITY_CODE = 101;

    public FragmentElegirLigasFavoritas(Context context, String email, List<Liga> ligas, List<Integer> ligasSeleccionada,boolean isLogin) {
        if (ligas.size() == 0) {
            try {
                ExecutorService es = Executors.newSingleThreadExecutor();
                Future<ArrayList<Liga>> result = es.submit(new LigasCallable(true));
                ligas = result.get();
                Log.e("Intancia List", "");
            } catch (ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }
        }
        this.isLogin=isLogin;
        this.context = context;
        this.email = email;
        listAdapter = new ListAdapterElegirLigasFavoritas(ligas, context, ligasSeleccionada);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lista_ligas_elegir_favoritas, container, false);
        setHasOptionsMenu(true);
        siguienteButton = view.findViewById(R.id.siguienteButton);
        siguienteButton.setOnClickListener(this);

        recyclerView = view.findViewById(R.id.listaElegirLigas);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(listAdapter);

        return view;
    }

    public static void setData(ArrayList<Liga> ligas) {
        listAdapter.setData(ligas);
        listAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        idLigasSeleccionas.clear();
        List<Liga> ligasSeleccionadas = listAdapter.getLigasSeleccionadas();
        System.out.println("Numero ligas antes de insertar" + ligasSeleccionadas.size());
        ligasSeleccionadas.forEach(liga -> idLigasSeleccionas.add(liga.getId()));

        Set<Integer> set = new HashSet<>(idLigasSeleccionas);
        idLigasSeleccionas.clear();
        idLigasSeleccionas.addAll(set);

        //Guardar en la base de datos las ligas
        HashMap<String, Object> datos = new HashMap<>();
        datos.put("ligasFavoritas", idLigasSeleccionas);
        System.out.println("Datos a insertar " + datos.size());
        db.collection("users").document(email).delete();
        db.collection("users").document(email).set(datos);

        if (!isLogin) {
            if (getActivity().getSupportFragmentManager().getFragments().get(getActivity().getSupportFragmentManager().getFragments().size() - 2) instanceof FragmentLigas)
                FragmentLigas.actualizaVista(idLigasSeleccionas);
                MainActivity.asignaLigasFavoritas();
                getActivity().onBackPressed();
        } else {
            Intent mainActivity = new Intent(context, MainActivity.class);
            startActivityForResult(mainActivity, MAINACTIVITY_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MAINACTIVITY_CODE) getActivity().finish();
    }
}