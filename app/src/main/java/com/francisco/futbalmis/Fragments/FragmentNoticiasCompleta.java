package com.francisco.futbalmis.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.francisco.futbalmis.Clases.Noticia;
import com.francisco.futbalmis.R;
import com.squareup.picasso.Picasso;

public class FragmentNoticiasCompleta extends Fragment {
    private View view;
    private Noticia noticia;
    ImageView imagenNoticia;
    TextView titularNoticia, contenidoNoticia,fechaPublicado;

    public FragmentNoticiasCompleta(Noticia noticia) {
        this.noticia=noticia;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.noticia_completa, container, false);
        imagenNoticia=view.findViewById(R.id.imagenNoticiaCompleta);
        titularNoticia=view.findViewById(R.id.titularNoticiaCompleta);
        contenidoNoticia=view.findViewById(R.id.contenidoNoticiaCompleta);
        fechaPublicado=view.findViewById(R.id.fechaPublicacionNoticiaCompleta);

        fechaPublicado.setText(noticia.getFechaPublicacion().fechaPartido());
        titularNoticia.setText(noticia.getTitulo());
        contenidoNoticia.setText(noticia.getContenido());

        Picasso.get().load(noticia.getUrlImagenNoticia()).into(imagenNoticia);

        return view;
    }
}
