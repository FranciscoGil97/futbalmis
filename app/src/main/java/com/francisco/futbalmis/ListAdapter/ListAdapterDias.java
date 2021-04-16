package com.francisco.futbalmis.ListAdapter;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    public ListAdapterDias(ArrayList<Fecha> itemList, Context context) {
        this.mInflater = LayoutInflater.from(context);
        this.context = context;
        this.mData = itemList;
        itemSelected = 0;
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
        void onItemLongClick(int position, View v);

        void onItemClick(int position, View v);
    }

    public void setOnItemClickListener(onClickListnerMiInterfaz onclicklistner) {
        this.onclicklistner = onclicklistner;
    }

    public List<Fecha> getData() {
        return mData;
    }

    public int getItemSelected() {
        return itemSelected;
    }

    public void setItemSelected(int itemSelected) {
        this.itemSelected = itemSelected;
    }

    public class Holder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {
        TextView diaMes, diaSemana;
        View view;

        Holder(View itemView) {
            super(itemView);
            view = itemView;
            diaMes = itemView.findViewById(R.id.diaMesDialogo);
            diaSemana = itemView.findViewById(R.id.diaSemanaDialogo);

            itemView.setOnLongClickListener(this);
            itemView.setOnClickListener(this);
        }

        void bindData(final Fecha item, int i) {
            if (!item.getDiaSemana().equals("HOY")) {
                diaMes.setText(item.getDia() + "/" + item.getMes());
                diaSemana.setText(item.getDiaSemana());
            } else diaSemana.setText(item.getDiaSemana());
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

