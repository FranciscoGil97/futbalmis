package com.francisco.futbalmis.Servicios;

import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.widget.ImageView;

import androidx.core.app.ComponentActivity;
import androidx.fragment.app.FragmentTransaction;

import com.francisco.futbalmis.Clases.Fecha;
import com.francisco.futbalmis.MainActivity;
import com.francisco.futbalmis.R;
import com.pixplicity.sharp.Sharp;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;

import okhttp3.Cache;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Utils {
    private static OkHttpClient httpClient;
    public static final int DIAS_SEMANA=7;
    // this method is used to fetch svg and load it into target imageview.
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
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DATE, intervaloDias);
        String dia = addCeroDigitoFecha(calendar.get(Calendar.DAY_OF_MONTH));
        String mes = addCeroDigitoFecha(calendar.get(Calendar.MONTH)+1);
        String diaSemana = getDiaSemana(calendar.get(Calendar.DAY_OF_WEEK));
        String anyo=calendar.get(Calendar.YEAR)+"";
        Fecha fecha = new Fecha(dia, mes, anyo, diaSemana);
        return fecha;

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
        String mes = addCeroDigitoFecha(calendar.get(Calendar.MONTH)+1);
        String diaSemana = getDiaSemana(calendar.get(Calendar.DAY_OF_WEEK));
        String anyo=calendar.get(Calendar.YEAR)+"";
        return new Fecha(dia, mes, anyo, diaSemana);

    }

    private static String addCeroDigitoFecha(int fecha) {
        return (fecha < 10 ? "0" + fecha : "" + fecha);
    }


}
