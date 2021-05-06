package com.francisco.futbalmis.ListAdapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Fecha;
import com.francisco.futbalmis.R;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterDias extends RecyclerView.Adapter<ListAdapterDias.Holder> {
    private List<Fecha> mData;
    private LayoutInflater mInflater;
    private Context context;
    private onClickListnerMiInterfaz onclicklistner;
    private int itemSelected;
    Fecha fechaDiaSeleccionado;

    public ListAdapterDias(ArrayList<Fecha> itemList, Context context,Fecha fechaDiaSeleccionado) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.itemSelected = itemSelected;
        this.fechaDiaSeleccionado=fechaDiaSeleccionado;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Fecha> ligas) {
        mData = ligas;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_dialogo, parent, false);

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

    public class Holder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView diaMes, diaSemana;
        View view;
        LinearLayout contenedor;

        Holder(View itemView) {
            super(itemView);
            view = itemView;
            contenedor = view.findViewById(R.id.contenedorDatosDiaDialogo);
            diaMes = itemView.findViewById(R.id.diaMesDialogo);
            diaSemana = itemView.findViewById(R.id.diaSemanaDialogo);

            itemView.setOnClickListener(this);
        }

        void bindData(final Fecha item, int i) {
            if (item.getDiaSemana().equals("HOY")) {
                diaSemana.setText(item.getDiaSemana());
            } else{
                diaMes.setText(item.getDia() + "/" + item.getMes());
                diaSemana.setText(item.getDiaSemana());
            }
            if(item.equals(fechaDiaSeleccionado))
                contenedor.setBackgroundColor(ContextCompat.getColor(context, R.color.primary));
        }


        @Override
        public void onClick(View v) {
            onclicklistner.onItemClick(getAdapterPosition(), v);
            itemSelected = getAdapterPosition();
        }
    }
}

