package com.francisco.futbalmis.ListAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.francisco.futbalmis.Clases.Noticia;
import com.francisco.futbalmis.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ListAdapterNoticias extends RecyclerView.Adapter<ListAdapterNoticias.Holder> {
    private List<Noticia> mData;
    private LayoutInflater mInflater;
    private Context context;
    private onClickListnerMiInterfaz onclicklistner;

    public ListAdapterNoticias(Context context, List<Noticia> itemList) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public void setData(List<Noticia> partidos) {
        mData = new ArrayList<>(partidos);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.cardview_noticias, parent, false);
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
        View view;
        ImageView imagenNoticia;
        TextView titular, descripcion, fecha;

        Holder(View itemView) {
            super(itemView);
            view = itemView;

            imagenNoticia = view.findViewById(R.id.imagenNoticia);
            titular = view.findViewById(R.id.titularNoticia);
            descripcion = view.findViewById(R.id.descripcionNoticia);
            fecha = view.findViewById(R.id.fechaPublicacionNoticia);
            itemView.setOnClickListener(this);
        }

        public void bindData(final Noticia item, int i) {

            titular.setText(item.getTitulo());
            descripcion.setText(item.getDescripcion());
            fecha.setText(item.getFechaPublicacion().fechaPartido());
            Picasso.get().load(item.getUrlImagenNoticia()).into(imagenNoticia);
        }

        @Override
        public void onClick(View v) {
            onclicklistner.onItemClick(getAdapterPosition(), v);
        }
    }
}


