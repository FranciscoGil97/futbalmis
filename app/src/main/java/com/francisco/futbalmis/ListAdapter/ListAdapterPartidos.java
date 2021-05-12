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

import com.francisco.futbalmis.Clases.Fecha;
import com.francisco.futbalmis.Clases.Partido;
import com.francisco.futbalmis.R;
import com.francisco.futbalmis.Servicios.Utils;

import java.util.ArrayList;
import java.util.List;


public class ListAdapterPartidos extends RecyclerView.Adapter<ListAdapterPartidos.Holder> {
    private List<Partido> mData;
    private LayoutInflater mInflater;
    private Context context;
    private boolean sonPartidosUnSoloEquipo = false;

    public ListAdapterPartidos(ArrayList<Partido> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    public ListAdapterPartidos(ArrayList<Partido> itemList, Context context, boolean sonPartidosUnSoloEquipo) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        this.sonPartidosUnSoloEquipo = sonPartidosUnSoloEquipo;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Partido> partidos) {
        mData = new ArrayList<>(partidos);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Holder(mInflater.inflate(R.layout.cardview_partidos, parent, false));
    }

    @Override
    public void onBindViewHolder(final Holder holder, final int position) {
        holder.bindData(mData.get(position), position);
    }

    public class Holder extends RecyclerView.ViewHolder {
        View view;
        TextView nombreEquipoLocal, nombreEquipoVisitante, estadoPartido, resultadoEquipoLocal, resultadoEquipoVisitante, horaPartido;
        public ImageView escudoEquipoLocal, escudoEquipoVisitante;

        Holder(View itemView) {
            super(itemView);
            view = itemView;
            nombreEquipoLocal = itemView.findViewById(R.id.nombreEquipoLocal);
            nombreEquipoVisitante = itemView.findViewById(R.id.nombreEquipoVisitante);
            resultadoEquipoLocal = itemView.findViewById(R.id.resultadoEquipoLocal);
            resultadoEquipoVisitante = itemView.findViewById(R.id.resultadoEquipoVisitante);
            horaPartido = itemView.findViewById(R.id.horaPartido);
            estadoPartido = itemView.findViewById(R.id.estadoPartido);

            escudoEquipoLocal = itemView.findViewById(R.id.escudoEquipoLocal);
            escudoEquipoVisitante = itemView.findViewById(R.id.escudoEquipoVisitante);
        }

        public void bindData(final Partido item, int i) {
            nombreEquipoLocal.setText(item.getEquipoLocal().getNombre());
            nombreEquipoVisitante.setText(item.getEquipoVisitante().getNombre());
            if (!sonPartidosUnSoloEquipo) {
                if (item.getResultadoEquipoLocal().equals("null"))
                    horaPartido.setText(item.getHoraPartido().substring(0, 5));
                else {
                    resultadoEquipoLocal.setText(item.getResultadoEquipoLocal());
                    resultadoEquipoVisitante.setText(item.getResultadoEquipoVisitate());
                }
                if (item.getEstadoPartido().equals("JUGANDO")) {
//                    estadoPartido.setText(Utils.getMinutos(Utils.parseDate(item.getFechaPartido() + " " + item.getHoraPartido())));

                    estadoPartido.setTextColor(Color.YELLOW);
                    resultadoEquipoLocal.setTextColor(Color.YELLOW);
                    resultadoEquipoVisitante.setTextColor(Color.YELLOW);
                }
                estadoPartido.setText(item.getEstadoPartido());
            } else {
                Fecha fechaPartido = new Fecha(item.getFechaPartido());
                horaPartido.setText(fechaPartido.fechaPartido());
                if (!item.getResultadoEquipoLocal().equals("null")){
                    resultadoEquipoLocal.setText(item.getResultadoEquipoLocal());
                    resultadoEquipoVisitante.setText(item.getResultadoEquipoVisitate());
                    horaPartido.setText(fechaPartido.fechaPartido());
                }else{
                    resultadoEquipoLocal.setText("");
                    resultadoEquipoVisitante.setText("");
                    horaPartido.setText(fechaPartido.fechaPartido()+" "+item.getHoraPartido().substring(0, 5));
                }

            }

//            if (item.getResultadoEquipoLocal().equals("null")) {
//                if (!sonPartidosUnSoloEquipo)
//                    horaPartido.setText(item.getHoraPartido().substring(0, 5));
//                resultadoEquipoLocal.setText("");
//                resultadoEquipoVisitante.setText("");
//            } else {
//                resultadoEquipoLocal.setText(item.getResultadoEquipoLocal());
//                resultadoEquipoVisitante.setText(item.getResultadoEquipoVisitate());
//                if(item.getEstadoPartido().equals("JUGANDO") ||item.getEstadoPartido().equals("DESCANSO")){
//                    estadoPartido.setTextColor(Color.RED);
//                    resultadoEquipoLocal.setTextColor(Color.RED);
//                    resultadoEquipoVisitante.setTextColor(Color.RED);
//                }
//            }

            Utils.fetchSvg(context, item.getEquipoLocal().getURLEscudo(), escudoEquipoLocal);
            Utils.fetchSvg(context, item.getEquipoVisitante().getURLEscudo(), escudoEquipoVisitante);
        }
    }
}


