package com.francisco.futbalmis.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.Clases.Partido;
import com.francisco.futbalmis.R;
import com.francisco.futbalmis.Servicios.Utils;

import java.util.ArrayList;
import java.util.List;


public class ListAdapterPartidos extends RecyclerView.Adapter<ListAdapterPartidos.Holder> {
    private List<Partido> mData;
    private LayoutInflater mInflater;
    private Context context;
    private onClickListnerMiInterfaz onclicklistner;
    private int itemSelected;

    public ListAdapterPartidos(ArrayList<Partido> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        itemSelected = 0;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Partido> partidos) {
        mData = new ArrayList<>(partidos);
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_partidos, parent, false);

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

    public List<Partido> getData() {return mData; }

    public int getItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(int itemSelected) {
        this.itemSelected = itemSelected;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        View view;
        TextView nombreEquipoLocal, nombreEquipoVisitante, estadoPartido, resultadoEquipoLocal, resultadoEquipoVisitante,horaPartido;
        public ImageView escudoEquipoLocal, escudoEquipoVisitante;

        Holder(View itemView) {
            super(itemView);
            view = itemView;
            nombreEquipoLocal = itemView.findViewById(R.id.nombreEquipoLocal);
            nombreEquipoVisitante = itemView.findViewById(R.id.nombreEquipoVisitante);
            estadoPartido = itemView.findViewById(R.id.estadoPartido);
            resultadoEquipoLocal = itemView.findViewById(R.id.resultadoEquipoLocal);
            resultadoEquipoVisitante = itemView.findViewById(R.id.resultadoEquipoVisitante);
            horaPartido=itemView.findViewById(R.id.horaPartido);

            escudoEquipoLocal = itemView.findViewById(R.id.escudoEquipoLocal);
            escudoEquipoVisitante = itemView.findViewById(R.id.escudoEquipoVisitante);
            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        public void bindData(final Partido item, int i) {
            nombreEquipoLocal.setText(item.getEquipoLocal().getNombre());
            nombreEquipoVisitante.setText(item.getEquipoVisitante().getNombre());
            estadoPartido.setText(item.getEstadoPartido());
            if(item.getResultadoEquipoLocal().equals("null")){
                horaPartido.setText(item.getHoraPartido().substring(0,5));
            }else{
                resultadoEquipoLocal.setText(item.getResultadoEquipoLocal());
                resultadoEquipoVisitante.setText(item.getResultadoEquipoVisitate());
            }

            Utils.fetchSvg(context, item.getEquipoLocal().getURLEscudo(), escudoEquipoLocal);
            Utils.fetchSvg(context, item.getEquipoVisitante().getURLEscudo(), escudoEquipoVisitante);
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


