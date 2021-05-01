package com.francisco.futbalmis.Hilos;

import com.francisco.futbalmis.Clases.Noticia;
import com.francisco.futbalmis.Fragments.FragmentNoticias;
import com.francisco.futbalmis.Servicios.Utils;

import org.w3c.dom.Document;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LectorRssCallable implements Callable<List<Noticia>> {
    private String direccion;

    public LectorRssCallable(String direccion) {
        this.direccion = direccion;
    }

    @Override
    public List<Noticia> call() throws Exception {
        Document datos = obtenerDatos();
        if (datos != null) {
            List<Noticia> noticias = Utils.procesarXml(datos);
            FragmentNoticias.setData(noticias);
            return noticias;
        }

        return new ArrayList<>();
    }

    public Document obtenerDatos() {
        try {
            URL url = new URL(direccion);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            InputStream inputStream = connection.getInputStream();
            DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = builderFactory.newDocumentBuilder();
            return builder.parse(inputStream);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
