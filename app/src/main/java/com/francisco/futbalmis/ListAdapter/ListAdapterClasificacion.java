package com.francisco.futbalmis.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Clasificacion;
import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.R;
import com.francisco.futbalmis.Servicios.Utils;

import java.util.ArrayList;
import java.util.List;

public class ListAdapterClasificacion extends RecyclerView.Adapter<ListAdapterClasificacion.Holder> {
    private List<Clasificacion> mData;
    private LayoutInflater mInflater;
    private Context context;
    private onClickListnerMiInterfaz onclicklistner;
    private int itemSelected;

    public ListAdapterClasificacion(ArrayList<Clasificacion> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        itemSelected = 0;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Clasificacion> clasificacion) {
        mData = clasificacion;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_clasificacion_equipo, parent, false);

        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.bindData(mData.get(position), position);
    }

    public interface onClickListnerMiInterfaz {
        void onItemLongClick(int position, View v);

        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(onClickListnerMiInterfaz onclicklistner) {
        this.onclicklistner = onclicklistner;
    }

    public List<Clasificacion> getData() {
        return mData;
    }

    public int getItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(int itemSelected) {
        this.itemSelected = itemSelected;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        TextView posicionEquipo, puntos, nombreEquipo, golaverajeEquipo, golesContraEquipo, golesFavorEquipo, partidosJugadosEquipo;
        View view;
        public ImageView escudoEquipo;

        Holder(View itemView) {
            super(itemView);
            view = itemView;
            posicionEquipo = itemView.findViewById(R.id.posicionEquipoClasificacion);
            puntos = itemView.findViewById(R.id.puntosEquipoClasificacion);
            nombreEquipo = itemView.findViewById(R.id.nombreEquipoClasificacion);
            golaverajeEquipo = itemView.findViewById(R.id.golaverajeEquipoClasificacion);
            golesContraEquipo = itemView.findViewById(R.id.golesContraEquipoClasificacion);
            golesFavorEquipo = itemView.findViewById(R.id.golesFavorEquipoClasificacion);
            partidosJugadosEquipo = itemView.findViewById(R.id.partidosJugadosEquipoClasificacion);
            escudoEquipo = itemView.findViewById(R.id.escudoEquipoClasificacion);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        void bindData(final Clasificacion item, int i) {
            posicionEquipo.setText(item.getPosicion()+"");
            puntos.setText(item.getPuntos()+"");
            nombreEquipo.setText(item.getEquipo().getNombre());
            golaverajeEquipo.setText((item.getGolesFavor() - item.getGoleContra())+"");
            golesFavorEquipo.setText(item.getGolesFavor()+"");
            golesContraEquipo.setText(item.getGoleContra()+"");
            partidosJugadosEquipo.setText(item.getPartidosJugados()+"");

            Utils.fetchSvg(context, item.getEquipo().getURLEscudo(), escudoEquipo);
        }


        @Override
        public void onClick(View v) {
            onclicklistner.onItemClick(getAdapterPosition(), v);
            itemSelected = getAdapterPosition();
        }


        @Override
        public boolean onLongClick(View v) {
            onclicklistner.onItemLongClick(getAdapterPosition(), v);
            itemSelected = getAdapterPosition();
            return true;
        }
    }
}

