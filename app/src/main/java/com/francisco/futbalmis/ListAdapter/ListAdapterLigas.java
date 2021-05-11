package com.francisco.futbalmis.ListAdapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.MainActivity;
import com.francisco.futbalmis.R;
import com.francisco.futbalmis.Servicios.Utils;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterLigas extends RecyclerView.Adapter<ListAdapterLigas.Holder> {
    private List<Liga> mData;
    private LayoutInflater mInflater;
    private Context context;
    private onClickListnerMiInterfaz onclicklistner;

    public ListAdapterLigas(ArrayList<Liga> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Liga> ligas) {
        mData = ligas;
    }

    @Override
    public int getItemViewType(int position) {
        if (mData.get(0).getId() == -1) return R.layout.no_partidos_dia;
        else return R.layout.cardview_liga;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(getItemViewType(0), parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.bindData(mData.get(position), position);
    }

    public interface onClickListnerMiInterfaz {
        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(onClickListnerMiInterfaz onclicklistner) {
        this.onclicklistner = onclicklistner;
    }

    public List<Liga> getData() {
        return mData;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView pais, nombre, noPartidos;
        View view;
        public ImageView bandera;

        Holder(View itemView) {
            super(itemView);
            view = itemView;

            if (mData.get(0).getId() > 0) {
                nombre = itemView.findViewById(R.id.nombreLiga);
                pais = itemView.findViewById(R.id.paisLiga);
                bandera = itemView.findViewById(R.id.banderaPaisLiga);
            } else
                noPartidos = itemView.findViewById(R.id.noPartidos);
            itemView.setOnClickListener(this);
        }

        void bindData(final Liga item, int i) {
            if (mData.get(0).getId() > 0) {
                nombre.setText(item.getNombre());
                pais.setText(item.getPais() + ":");
                Utils.fetchSvg(context, item.getBanderaURL(), bandera);
            } else {
                MainActivity.cambiaVisibilidadProgressBar(View.GONE);
                noPartidos.setText("NO HAY PARTIDOS ESTE DÍA");
            }
        }

        @Override
        public void onClick(View v) {
            onclicklistner.onItemClick(getAdapterPosition(), v);
        }
    }
}

