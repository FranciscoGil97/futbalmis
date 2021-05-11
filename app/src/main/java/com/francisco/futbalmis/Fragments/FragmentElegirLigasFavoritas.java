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
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.Hilos.LigasCallable;
import com.francisco.futbalmis.ListAdapter.ListAdapterElegirLigasFavoritas;
import com.francisco.futbalmis.LoginActivity;
import com.francisco.futbalmis.MainActivity;
import com.francisco.futbalmis.R;
import com.francisco.futbalmis.Servicios.Utils;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FragmentElegirLigasFavoritas extends Fragment implements View.OnClickListener {
    private View view;
    private Context context;
    private RecyclerView recyclerView;
    private static ListAdapterElegirLigasFavoritas listAdapter;
    private ArrayList<Liga> ligas = new ArrayList<>();
    Button siguienteButton;
    String email;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private final int MAINACTIVITY_CODE = 101;
    List<Integer> ligasSeleccionada = new ArrayList<>();
    String ligasFavoritasString = "", prueba;

    public FragmentElegirLigasFavoritas(Context context, String email) {
        this.context = context;

//        this.email = email;
        this.email = "fj.gil16@iesdoctorbalmis.com";

        db.collection("users").get().addOnCompleteListener(task -> {
            for (DocumentSnapshot document : task.getResult()) {
                Log.e("DOCUMENTO", document.getId() + " => " + document.getData());
            }
        });
        db.collection("users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ligasFavoritasString = task.getResult().getData().get("ligasFavoritas").toString();
                ligasFavoritasString = ligasFavoritasString.replace('[', ' ');
                ligasFavoritasString = ligasFavoritasString.replace(']', ' ');
                ligasFavoritasString = ligasFavoritasString.replaceAll(" ", "");
                List<String> aux = Arrays.asList(ligasFavoritasString.split(","));
                aux.forEach(s -> ligasSeleccionada.add(Integer.parseInt(s)));

            }
        });
        Log.e("prueba ", (prueba == null) + "");
        try {
            ExecutorService es = Executors.newSingleThreadExecutor();
            Future<ArrayList<Liga>> result = es.submit(new LigasCallable());
            ligas = result.get();
            listAdapter = new ListAdapterElegirLigasFavoritas(ligas, context, ligasSeleccionada);
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
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
        List<Liga> ligasSeleccionadas = listAdapter.getLigasSeleccionadas();
        List<Integer> idLigasSeleccionas = new ArrayList<>();
        ligasSeleccionadas.forEach(liga -> idLigasSeleccionas.add(liga.getId()));

        //Guardar en la base de datos las ligas
        HashMap<String, Object> datos = new HashMap<>();
        datos.put("ligasFavoritas", idLigasSeleccionas);
        db.collection("users").document(email).set(datos);

        Intent elegirLigas = new Intent(context, MainActivity.class);
        startActivityForResult(elegirLigas, MAINACTIVITY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == MAINACTIVITY_CODE) getActivity().finish();
    }
}