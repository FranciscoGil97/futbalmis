package com.francisco.futbalmis.Servicios;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.core.text.HtmlCompat;

import com.francisco.futbalmis.Clases.Fecha;
import com.francisco.futbalmis.Clases.Noticia;
import com.francisco.futbalmis.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.pixplicity.sharp.Sharp;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Utils {
    private static OkHttpClient httpClient;
    public static final int DIAS_SEMANA = 7;
    private static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public static String ligasFavoritasString = "";

    public static void fetchSvg(Context context, String url, final ImageView target) {
        if (httpClient == null) {
            httpClient = new OkHttpClient.Builder()
                    .cache(new Cache(context.getCacheDir(), 5 * 1024 * 1014))
                    .build();
        }

        // here we are making HTTP call to fetch data from URL.
        Request request = new Request.Builder().url(url).build();
        httpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // we are adding a default image if we gets any error.
                target.setImageResource(R.drawable.ic_launcher_background);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // sharp is a library which will load stream which we generated
                // from url in our target imageview.
                InputStream stream = response.body().byteStream();
                Sharp.loadInputStream(stream).into(target);
                stream.close();
            }
        });
    }

    private static String getDiaSemana(int dia) {
        switch (dia) {
            case 1:
                return "Domingo";
            case 2:
                return "Lunes";
            case 3:
                return "Martes";
            case 4:
                return "Miércoles";
            case 5:
                return "Jueves";
            case 6:
                return "Viernes";
            case 7:
                return "Sábado";

        }
        return "";
    }

    public static Fecha getFecha(int intervaloDias) {
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, intervaloDias);
        String dia = addCeroDigitoFecha(calendar.get(Calendar.DAY_OF_MONTH));
        String mes = addCeroDigitoFecha(calendar.get(Calendar.MONTH) + 1);
        String diaSemana = getDiaSemana(calendar.get(Calendar.DAY_OF_WEEK));
        String anyo = calendar.get(Calendar.YEAR) + "";
        return new Fecha(dia, mes, anyo, diaSemana);
    }

    public static Fecha getParseFecha(String fechaString) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = formatoFecha.parse(fechaString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        String dia = addCeroDigitoFecha(calendar.get(Calendar.DAY_OF_MONTH));
        String mes = addCeroDigitoFecha(calendar.get(Calendar.MONTH) + 1);
        String diaSemana = getDiaSemana(calendar.get(Calendar.DAY_OF_WEEK));
        String anyo = calendar.get(Calendar.YEAR) + "";
        return new Fecha(dia, mes, anyo, diaSemana);

    }

    private static String addCeroDigitoFecha(int fecha) {
        return (fecha < 10 ? "0" + fecha : "" + fecha);
    }

    public static Date parseDate(String fechaHora) {
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = formatoFecha.parse(fechaHora);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getMinutos(Date fechPartido) {
        String minutos = "";
        Date fechaActual = new Date();
        int tiempoDescanso = 15; //parece que en algunas ligas es de 20 minutos(italia, portugal y...)

        long diferenciaMinutos = TimeUnit.MILLISECONDS.toMinutes(fechaActual.getTime() - fechPartido.getTime());
        minutos = diferenciaMinutos + "'";
        if (diferenciaMinutos > 45 && diferenciaMinutos < 48)
            minutos = "45+" + (diferenciaMinutos - 45) + "'";
        if (diferenciaMinutos >= 60)
            minutos = (diferenciaMinutos - tiempoDescanso) + "'";
        if (diferenciaMinutos > 90)
            minutos = "90+" + (diferenciaMinutos - 90 - tiempoDescanso) + "'";
        return minutos;
    }

    public static Fecha obtenFechaXml(String fechaString) {
        try {
            Pattern patronFecha = Pattern.compile("\\s(?<dia>([0-9])|([1-2][0-9])|(3[0-1]))\\s(?<mes>[A-Z][a-z]{2})\\s(?<year>20\\d{2})");

            Matcher matcher = patronFecha.matcher(fechaString);
            if (matcher.find()) {
                String dia = matcher.group("dia");
                String mes = matcher.group("mes");
                String year = matcher.group("year");

                String mesNumero = "";
                int posicionMes = Arrays.asList(new DateFormatSymbols(Locale.ENGLISH).getShortMonths()).indexOf(mes);
                if (posicionMes > 0)
                    mesNumero = addCeroDigitoFecha(++posicionMes);

                dia=addCeroDigitoFecha(Integer.parseInt(dia));



                return new Fecha(dia,mesNumero,year,"");
            } else return null;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Noticia rellenaNoticia(NodeList itemsChild) {
        Noticia noticia = new Noticia();
        noticia.setUrlImagenNoticia(null);
        for (int i = 0; i < itemsChild.getLength(); i++) {
            Node item = itemsChild.item(i);
            if (item.getNodeName().equalsIgnoreCase("title"))
                noticia.setTitulo(item.getTextContent());
            else if (item.getNodeName().equalsIgnoreCase("description"))
                noticia.setDescripcion(item.getTextContent());
            else if (item.getNodeName().equalsIgnoreCase("pubDate")) {
                Fecha fecha = obtenFechaXml(item.getTextContent());
                noticia.setFechaPublicacion(fecha);
            } else if (item.getNodeName().equalsIgnoreCase("content:encoded")) {
                String contenido = item.getTextContent();
                if (contenido.equalsIgnoreCase("<p>.</p>"))
                    noticia.setContenido(null);
                else
                    noticia.setContenido(HtmlCompat.fromHtml(contenido, HtmlCompat.FROM_HTML_MODE_LEGACY).toString());
            } else if (item.getNodeName().equalsIgnoreCase("enclosure") && noticia.getUrlImagenNoticia() == null) { //se asigna a null porque hay dos elementos de tipo enclosure
                String urlImagen = item.getAttributes().item(0).getTextContent();
                noticia.setUrlImagenNoticia(urlImagen);
            }
        }
        return noticia;
    }

    public static List<Noticia> procesarXml(Document data) {
        List<Noticia> noticias = new ArrayList<>();
        if (data != null) {
            Element root = data.getDocumentElement();
            Node channel = root.getChildNodes().item(1);
            NodeList items = channel.getChildNodes();

            for (int i = 0; i < items.getLength(); i++) {
                Node hijoActual = items.item(i);
                if (hijoActual.getNodeName().equalsIgnoreCase("item")) {
                    NodeList itemsChild = hijoActual.getChildNodes();
                    Noticia noticia = rellenaNoticia(itemsChild);
                    if (noticia.getContenido() != null)
                        noticias.add(noticia);
                }

            }
        }
        return noticias;
    }

    public static void getLigasFavoritas(String email, List<Integer> ligasFavoritas){
        db.collection("users").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.getResult().exists()) {
                    ligasFavoritasString = task.getResult().getData().get("ligasFavoritas").toString();
                    ligasFavoritasString = ligasFavoritasString.replace('[', ' ');
                    ligasFavoritasString = ligasFavoritasString.replace(']', ' ');
                    ligasFavoritasString = ligasFavoritasString.replaceAll(" ", "");
                    if (ligasFavoritasString.length() > 0) {

                        List<String> aux = Arrays.asList(ligasFavoritasString.split(","));
                        aux.forEach(s -> ligasFavoritas.add(Integer.parseInt(s)));

                    }
                }
            }

        });
    }

}
