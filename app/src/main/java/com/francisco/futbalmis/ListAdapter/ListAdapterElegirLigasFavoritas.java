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
import java.util.List;

public class ListAdapterElegirLigasFavoritas extends RecyclerView.Adapter<ListAdapterElegirLigasFavoritas.Holder> {
    private List<Liga> mData;
    private LayoutInflater mInflater;
    private Context context;
    private List<Liga> ligasSeleccionadas;

    public ListAdapterElegirLigasFavoritas(ArrayList<Liga> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        ligasSeleccionadas = new ArrayList<>();
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

