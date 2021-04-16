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
    @GET("partidos")
    Call<ArrayList<Partido>> getPartidosHoy();

    @GET("numeropartidos/fecha/{fecha}")
    Call<Integer> getNumeroPartidos(@Path("fecha") String fecha);

    @GET("partidoshoy/{idLiga}")
    Call<ArrayList<Partido>> getPartidosHoyLiga(@Path("idLiga") int idLiga);

    @GET("partidos/{idLiga}")
    Call<ArrayList<Partido>> getPartidosIdLiga(@Path("idLiga") int idLiga);

    @GET("clasificacion/{idLiga}")
    Call<ArrayList<Clasificacion>> getClasificacionIdLiga(@Path("idLiga") int idLiga);

    @GET("ligas")
    Call<ArrayList<Liga>> getLigas();

    @GET("ligas/{fecha}")
    Call<ArrayList<Liga>> getLigasFecha(@Path("fecha") String fecha);

    @GET("equipos/{idLiga}")
    Call<ArrayList<Equipo>> getEquipos(@Path("idLiga") int idLiga);

    @GET("partidos/fecha/{fecha}/{idLiga}")
    Call<ArrayList<Partido>> getPartidosFecha(@Path("fecha") String fecha, @Path("idLiga") int idLiga);

    @PUT("partidos/actualizahoy")
    Call<String> actualizaPartidosHoy();

    @PUT("partidos/actualiza")
    Call<String> actualizaPartidos();

    @PUT("partidos/actualiza/{idLiga}")
    Call<String> actualizaPartidosLiga(@Path("idLiga") int idLiga);
}
