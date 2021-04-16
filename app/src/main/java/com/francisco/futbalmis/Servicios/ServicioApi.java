package com.francisco.futbalmis.Servicios;

import android.util.Log;
import android.view.View;

import com.francisco.futbalmis.Clases.Clasificacion;
import com.francisco.futbalmis.Clases.Fecha;
import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.Clases.Partido;
import com.francisco.futbalmis.Fragments.FragmentClasificacion;
import com.francisco.futbalmis.Fragments.FragmentLigas;
import com.francisco.futbalmis.Fragments.FragmentPartidos;
import com.francisco.futbalmis.MainActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ServicioApi {
    private static String URLBase = "https://futbolapi.azurewebsites.net/api/";
    static ArrayList<Liga> ligas = new ArrayList<>();
    static ArrayList<Partido> partidos = new ArrayList<>();
    static ArrayList<Clasificacion> clasificacion = new ArrayList<>();

    public static ArrayList<Liga> getLigasFecha(String fecha) throws IOException {
        MainActivity.cambiaVisibilidadProgressBar(View.VISIBLE);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLBase)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioApi servicio = retrofit.create(IServicioApi.class);

        Call<ArrayList<Liga>> _ligas = servicio.getLigasFecha(fecha);

        _ligas.enqueue(new Callback<ArrayList<Liga>>() {
            @Override
            public void onResponse(Call<ArrayList<Liga>> call, Response<ArrayList<Liga>> response) {
                ligas = new ArrayList<>(response.body());

                if(ligas.size()>0)
                    FragmentLigas.setData(ligas);
                else{
                    Liga liga= new Liga();
                    liga.setId(-1);
                    ligas.add(liga);
                    FragmentLigas.setData(ligas);
                }
                MainActivity.cambiaVisibilidadProgressBar(View.GONE);
            }

            @Override
            public void onFailure(Call<ArrayList<Liga>> call, Throwable t) {
                Log.e("ERROR OBTENER LIGAS: ", t.getMessage());
            }
        });
        return ligas;
    }

    public static ArrayList<Partido> getPartidosFechaLiga(Fecha fecha, int idLiga) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLBase)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioApi servicio = retrofit.create(IServicioApi.class);

        Call<ArrayList<Partido>> _partidos = servicio.getPartidosFecha(fecha.toString(), idLiga);

        _partidos.enqueue(new Callback<ArrayList<Partido>>() {
            @Override
            public void onResponse(Call<ArrayList<Partido>> call, Response<ArrayList<Partido>> response) {
                partidos = new ArrayList<>(response.body());
                Log.e("CODE", response.code() + "");
                Log.e("Partidos", partidos.size() + "");

                partidos = new ArrayList<>(response.body());
                MainActivity.cambiaVisibilidadProgressBar(View.GONE);
                FragmentPartidos.setData(partidos);
            }

            @Override
            public void onFailure(Call<ArrayList<Partido>> call, Throwable t) {
                Log.e("ERROR OBTENER LIGAS: ", t.getMessage());
            }
        });
        return partidos;
    }

    public static ArrayList<Clasificacion> getClasificacion(int idLiga) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLBase)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        IServicioApi servicio = retrofit.create(IServicioApi.class);

        Call<ArrayList<Clasificacion>> _clasificacion = servicio.getClasificacionIdLiga(idLiga);

        _clasificacion.enqueue(new Callback<ArrayList<Clasificacion>>() {
            @Override
            public void onResponse(Call<ArrayList<Clasificacion>> call, Response<ArrayList<Clasificacion>> response) {
                clasificacion = new ArrayList<>(response.body());
                MainActivity.cambiaVisibilidadProgressBar(View.GONE);
                FragmentClasificacion.setData(clasificacion);
            }

            @Override
            public void onFailure(Call<ArrayList<Clasificacion>> call, Throwable t) {
                Log.e("ERROR OBTENER LIGAS: ", t.getMessage());
            }
        });
        return clasificacion;
    }

    public static String _mensaje = "";

    public static void actualizaPartidosHoy() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.MINUTES)
                .connectTimeout(3, TimeUnit.MINUTES)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLBase)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        IServicioApi servicio = retrofit.create(IServicioApi.class);

        Call<String> mensaje = servicio.actualizaPartidosHoy();

        mensaje.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.e("Partidos actualizados HOY", "Partidos hoy actualizados");
                } else
                    Log.e("Partidos actualizados HOY", "Algo ha pasado " + response.code());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ERROR ACTUALIZAR PARTIDOS HOY: ", t.getMessage());
            }
        });
    }

    public static void actualizaPartidos() {
        final OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .readTimeout(3, TimeUnit.MINUTES)
                .connectTimeout(3, TimeUnit.MINUTES)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLBase)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
        IServicioApi servicio = retrofit.create(IServicioApi.class);

        Call<String> mensaje = servicio.actualizaPartidos();

        mensaje.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.e("Partidos actualizados", "Partidos actualizados");
                } else
                    Log.e("Partidos actualizados", "Algo ha pasado " + response.code());
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.e("ERROR ACTUALIZAR PARTIDOS: ", t.getMessage());
            }
        });
    }

}
