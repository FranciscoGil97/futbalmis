package com.francisco.futbalmis.ListAdapter;

import android.content.Context;
import android.util.Log;
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

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListAdapterElegirLigasFavoritas extends RecyclerView.Adapter<ListAdapterElegirLigasFavoritas.Holder> {
    private List<Liga> mData;
    private LayoutInflater mInflater;
    private Context context;
    private List<Liga> ligasSeleccionadas;
    private List<Integer> idLigasSeleccionadas;

    public ListAdapterElegirLigasFavoritas(List<Liga> itemList, Context context, List<Integer> idLigasSeleccionadas) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        ligasSeleccionadas = new ArrayList<>();

        this.idLigasSeleccionadas = idLigasSeleccionadas;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Liga> ligas) {
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

    public void setIdLigasSeleccionadas(List<Integer> idLigasSeleccionadas) {
        this.idLigasSeleccionadas = idLigasSeleccionadas;
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
            if (item != null) {
                nombre.setText(item.getNombre());
                pais.setText(item.getPais()+":");
                if(item.getBanderaURL()!=null)
                    Utils.fetchSvg(context, item.getBanderaURL(), bandera);

                if (idLigasSeleccionadas.contains(item.getId())) {
                    if (!ligasSeleccionadas.contains(item))
                        ligasSeleccionadas.add(item);
                    ligaElegida.setImageResource(R.drawable.favorito_seleccionado);
                    System.out.println("Numero de ligas seleccionadas  " + ligasSeleccionadas.size() + "    " + idLigasSeleccionadas.size());
                }
            }
        }

        @Override
        public void onClick(View v) {
            System.out.println("CLICK");
            Liga liga = mData.get(getAdapterPosition());
            if (ligasSeleccionadas.contains(liga)) {
                ligasSeleccionadas.remove(liga);
                ligaElegida.setImageResource(R.drawable.favorito_no_seleccionado);
                System.out.println("ELIMINA");
            } else {
                ligasSeleccionadas.add(liga);
                ligaElegida.setImageResource(R.drawable.favorito_seleccionado);
                System.out.println("AÑADE");
            }
        }
    }
}

