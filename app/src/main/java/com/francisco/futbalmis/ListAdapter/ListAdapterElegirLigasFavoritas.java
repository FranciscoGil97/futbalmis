package com.francisco.futbalmis.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.R;
import com.francisco.futbalmis.Servicios.Utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAdapterElegirLigasFavoritas extends RecyclerView.Adapter<ListAdapterElegirLigasFavoritas.Holder> {
    private List<Liga> mData;
    private LayoutInflater mInflater;
    private Context context;
    private List<Liga> ligasSeleccionadas;
    List<Integer> idLigasSeleccionadas;
//    Map<Integer, Liga> ligas;

    public ListAdapterElegirLigasFavoritas(ArrayList<Liga> itemList, Context context, List<Integer> idLigasSeleccionadas) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        ligasSeleccionadas = new ArrayList<>();
//        ligas = new HashMap<>();
        this.idLigasSeleccionadas = idLigasSeleccionadas;

//        if (!mData.isEmpty())
//            rellenaMap(mData);
    }

//    private void rellenaMap(List<Liga> ligas) {
//        ligas.forEach(liga -> this.ligas.put(liga.getId(), liga));
//    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Liga> ligas) {
//        if (mData.isEmpty())
//            rellenaMap(ligas);
        mData = ligas;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_liga_elegir_fav, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.bindData(mData.get(position), position);
    }

    public List<Liga> getData() {
        return mData;
    }

    public List<Liga> getLigasSeleccionadas() {
        return ligasSeleccionadas;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView pais, nombre;
        View view;
        public ImageView bandera;
        ImageView ligaElegida;

        Holder(View itemView) {
            super(itemView);
            view = itemView;
            ligaElegida = itemView.findViewById(R.id.favoritaLiga);
            nombre = itemView.findViewById(R.id.nombreLigaE);
            pais = itemView.findViewById(R.id.paisLigaE);
            bandera = itemView.findViewById(R.id.banderaPaisLigaE);

            ligaElegida.setOnClickListener(this);
        }

        void bindData(final Liga item, int i) {
            nombre.setText(item.getNombre());
            pais.setText(item.getPais().concat(":"));
            Utils.fetchSvg(context, item.getBanderaURL(), bandera);

            if (idLigasSeleccionadas.contains(item.getId()))
                ligaElegida.setImageResource(R.drawable.favorito_seleccionado);

        }

        @Override
        public void onClick(View v) {
            Liga liga = mData.get(getAdapterPosition());
            if (ligasSeleccionadas.contains(liga)) {
                ligasSeleccionadas.remove(liga);
                ligaElegida.setImageResource(R.drawable.favorito_no_seleccionado);
            } else {
                ligasSeleccionadas.add(liga);
                ligaElegida.setImageResource(R.drawable.favorito_seleccionado);
            }
        }
    }
}

