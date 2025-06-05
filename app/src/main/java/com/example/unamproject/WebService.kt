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
    ): Response<AcreditadoResponse>

    @POST("/appAgregarFechaVisita")
    suspend fun agregarVisitas(
        @Body visitas: Visitas
    ): Response<AcreditadoResponse>

    @POST("/appAgregarDatosVivienda")
    suspend fun agregarDatosVivienda(
        @Body datosVivienda: datosVivienda
    ): Response<AcreditadoResponse>

    @POST("/appAgregarDatosReestructura")
    suspend fun agregarDatosReestrucutra(
        @Body datosReestructura: DatosReestructura
    ): Response<AcreditadoResponse>

    @POST("/appAgregarDatosFechaCredito")
    suspend fun agregarDatosCredito(
        @Body datosCredito: datosCredito
    ): Response<AcreditadoResponse>

    @POST("/appAgregarDatosGeneralesConyuge")
    suspend fun agregarDatosConyuge(
        @Body datosConyuge: datosConyuge
    ): Response<AcreditadoResponse>

    @POST("/appAgregarDatosFamilia")
    suspend fun agregarDatosFamiliares(
        @Body datosFamiliares: datosFamiliares
    ): Response<AcreditadoResponse>

    @POST("/appAgregarDatosSolicitante")
    suspend fun agregarDatosSolicitante(
        @Body datosSolicitante: datosSolicitante
    ): Response<AcreditadoResponse>

    @POST("/appAgregarDatosEspecificosConyuge")
    suspend fun agregarDatosEspecificosConyuge(
        @Body datosEspecificosConyuge: datosEspecificosConyuge
    ): Response<AcreditadoResponse>

    @POST("/appAgregarDatosOtrosFamiliares")
    suspend fun agregarDatosOtrosFamiliares(
        @Body datosOtrosFamiliares: datosOtrosFamiliares
    ): Response<AcreditadoResponse>

    @POST("/appAgregarDatosGastos")
    suspend fun agregarDatosGastos(
        @Body datosGastos: datosGastos
    ): Response<AcreditadoResponse>

    @POST("/appAgregarDatosFamiliaDeudas")
    suspend fun agregarDatosFamiliaDeudas(
        @Body datosFamiliaDeudas: datosFamiliaDeudas
    ): Response<AcreditadoResponse>

    @POST("/appAgregarDatosTelefono")
    suspend fun agregarDatosTelefonicos(
        @Body datosTelefonos: datosTelefonos
    ): Response<AcreditadoResponse>

    @POST("/appAgregarDatosCobranza")
    suspend fun agregarDatosCobranza(
        @Body datosCobranza: datosCobranza
    ): Response<AcreditadoResponse>

    @POST("/appAgregarDatosDocumentos")
    suspend fun agregarDatosDocumentos(
        @Body datosDocumentos: datosDocumentos
    ): Response<AcreditadoResponse>

    @POST ("/appAgregarDatosEspecificiosVivienda")
    suspend fun agregarDatosEspecificosVivienda(
        @Body datosEspecificosVivienda: datosEspecificosVivienda
    ): Response<AcreditadoResponse>

    @POST("/appAgregarObservaciones")
    suspend fun agregarDatosObservaciones(
        @Body datosObservaciones: datosObservaciones
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