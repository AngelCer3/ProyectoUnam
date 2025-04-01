package com.example.unamproject

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST


object AppConstantes{
    const val BASE_URL = "http://10.0.2.2:3000/"
}

interface WebService{
    @POST("/appAgregarDatosGenerales")
    suspend fun agregarAcreditado(
        @Body acreditado: Acreditado
    ): Response<String>

    @POST("/appAgregarFechaVisita")
    suspend fun agregarVisitas(
        @Body visitas: Visitas
    ): Response<String>

    @POST("/appAgregarDatosVivienda")
    suspend fun agregarDatosVivienda(
        @Body datosVivienda: datosVivienda
    ): Response<String>
}

object RetrofitClient{
    val webService : WebService by lazy {
        Retrofit.Builder()
            .baseUrl(AppConstantes.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build().create(WebService::class.java)
    }
}