package com.francisco.futbalmis.Servicios;

import com.francisco.futbalmis.Clases.Clasificacion;
import com.francisco.futbalmis.Clases.Equipo;
import com.francisco.futbalmis.Clases.Liga;
import com.francisco.futbalmis.Clases.Partido;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface IServicioApi {
    @GET("ligas")
    Call<ArrayList<Liga>> getLigas();

    @GET("clasificacion/{idLiga}")
    Call<ArrayList<Clasificacion>> getClasificacionIdLiga(@Path("idLiga") int idLiga);

    @GET("ligas/{fecha}")
    Call<ArrayList<Liga>> getLigasFecha(@Path("fecha") String fecha);

    @GET("partidos/{idEquipo}")
    Call<ArrayList<Partido>> getPartidosEquipo(@Path("idEquipo") int idEquipo);

    @GET("partidos/fecha/{fecha}/{idLiga}")
    Call<ArrayList<Partido>> getPartidosFecha(@Path("fecha") String fecha, @Path("idLiga") int idLiga);

    @GET("partidos/liga/{idLiga}")
    Call<ArrayList<Partido>> getTodosPartidosLiga(@Path("idLiga") int idLiga);

    @PUT("partidos/actualizahoy")
    Call<String> actualizaPartidosHoy();

    @PUT("partidos/actualiza")
    Call<String> actualizaPartidos();
}
