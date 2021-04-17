package com.francisco.futbalmis.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Clasificacion;
import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.Hilos.ClasificacionCallable;
import com.francisco.futbalmis.ListAdapter.ListAdapterClasificacion;
import com.francisco.futbalmis.MainActivity;
import com.francisco.futbalmis.R;
import com.francisco.futbalmis.Servicios.Utils;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class FragmentClasificacion extends Fragment {
    private View view;
    private static Context context;
    private static RecyclerView recyclerView;
    private static ListAdapterClasificacion listAdapter;
    private ArrayList<Clasificacion> clasificacion = new ArrayList<>();
    private Liga ligaPartidos;
    TextView nombreLiga, paisLiga;
    public ImageView banderaPaisLiga;
    FragmentTransaction FT;

    public FragmentClasificacion(Context context, Liga ligaPartidos, FragmentTransaction FT) {
        this.FT = FT;
        this.context = context;
        this.ligaPartidos = new Liga(ligaPartidos.getId(), ligaPartidos.getNombre(), ligaPartidos.getCodigoPais(), ligaPartidos.getPais(), ligaPartidos.getBanderaURL());
        ExecutorService es = Executors.newSingleThreadExecutor();
        Future<ArrayList<Clasificacion>> result = es.submit(new ClasificacionCallable(ligaPartidos.getId()));
        listAdapter = new ListAdapterClasificacion(clasificacion, context);

        try {
            clasificacion = result.get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lista_clasificacion, container, false);
        recyclerView = view.findViewById(R.id.listaClasificacion);

        banderaPaisLiga = view.findViewById(R.id.banderaPaisLigaClasificacion);
        nombreLiga = view.findViewById(R.id.nombreLigaClasificacion);
        paisLiga = view.findViewById(R.id.paisLigaClasificacion);

        Utils.fetchSvg(context, ligaPartidos.getBanderaURL(), banderaPaisLiga);
        paisLiga.setText(ligaPartidos.getPais() + ":");
        nombreLiga.setText(ligaPartidos.getNombre());

//        view.setFocusableInTouchMode(true);
//        view.requestFocus();
//        view.setOnKeyListener(new View.OnKeyListener() {
//            @Override
//            public boolean onKey(View v, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//                    Toast.makeText(context, "ADIOS", Toast.LENGTH_SHORT).show();
//                    getActivity().onBackPressed();
//                    return true;
//                }
//                return false;
//            }
//        });

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(listAdapter);

        if (listAdapter != null) {
            listAdapter.setOnItemClickListener(new ListAdapterClasificacion.onClickListnerMiInterfaz() {
                @Override
                public void onItemLongClick(int position, View v) {

                }

                @Override
                public void onItemClick(int position, View v) {
                    //Mostrar partidos de un equipo
                    if (getActivity().getSupportFragmentManager().getFragments().get(getActivity().getSupportFragmentManager().getFragments().size() - 1) instanceof FragmentClasificacion)
                        Toast.makeText(context, listAdapter.getData().get(position).getEquipo().getNombre() + "", Toast.LENGTH_SHORT).show();


                    MainActivity.cambiaVisibilidadProgressBar(View.VISIBLE);
                    cargarFragment(new FragmentPartidosEquipo(context, listAdapter.getData().get(position).getEquipo()));
                }
            });
        }
        return view;
    }

    public static void setData(ArrayList<Clasificacion> clasificacion) {
        listAdapter.setData(clasificacion);
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
