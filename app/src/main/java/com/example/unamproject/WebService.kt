package com.example.unamproject

import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path


object AppConstantes{
    const val BASE_URL = "http://10.0.2.2:3000/"
}

interface WebService{

    @POST("/appInicioSesion")
    fun iniciarSesion(@Body request: LoginRequest): Call<LoginResponse>

    @GET("/appObtenerAcreditados")
    suspend fun ObtenerAcreditados(): List<identificarAcreditado>

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

    @PUT("/appActualizarDatosGenerales/{id}")
    suspend fun actualizarAcreditado(
        @Path("id") idAcreditado: String,
        @Body acreditado: Acreditado
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarFechaVisita/{id}")
    suspend fun actualizarVisitas(
        @Path("id") idAcreditado: String,
        @Body visitas: Visitas
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarDatosVivienda/{id}")
    suspend fun actualizarDatosVivienda(
        @Path("id") idAcreditado: String,
        @Body datosVivienda: datosVivienda
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarDatosReestructura/{id}")
    suspend fun actualizarDatosReestructura(
        @Path("id") idAcreditado: String,
        @Body datosReestructura: DatosReestructura
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarDatosFechaCredito/{id}")
    suspend fun actualizarDatosCredito(
        @Path("id") idAcreditado: String,
        @Body datosCredito: datosCredito
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarDatosGeneralesConyuge/{id}")
    suspend fun actualizarDatosConyuge(
        @Path("id") idAcreditado: String,
        @Body datosConyuge: datosConyuge
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarDatosFamilia/{id}")
    suspend fun actualizarDatosFamiliares(
        @Path("id") idAcreditado: String,
        @Body datosFamiliares: datosFamiliares
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarDatosSolicitante/{id}")
    suspend fun actualizarDatosSolicitante(
        @Path("id") idAcreditado: String,
        @Body datosSolicitante: datosSolicitante
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarDatosEspecificosConyuge/{id}")
    suspend fun actualizarDatosEspecificosConyuge(
        @Path("id") idAcreditado: String,
        @Body datosEspecificosConyuge: datosEspecificosConyuge
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarDatosOtrosFamiliares/{id}")
    suspend fun actualizarDatosOtrosFamiliares(
        @Path("id") idAcreditado: String,
        @Body datosOtrosFamiliares: datosOtrosFamiliares
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarDatosGastos/{id}")
    suspend fun actualizarDatosGastos(
        @Path("id") idAcreditado: String,
        @Body datosGastos: datosGastos
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarDatosFamiliaDeudas/{id}")
    suspend fun actualizarDatosFamiliaDeudas(
        @Path("id") idAcreditado: String,
        @Body datosFamiliaDeudas: datosFamiliaDeudas
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarDatosTelefono/{id}")
    suspend fun actualizarDatosTelefonicos(
        @Path("id") idAcreditado: String,
        @Body datosTelefonos: datosTelefonos
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarDatosCobranza/{id}")
    suspend fun actualizarDatosCobranza(
        @Path("id") idAcreditado: String,
        @Body datosCobranza: datosCobranza
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarDatosDocumentos/{id}")
    suspend fun actualizarDatosDocumentos(
        @Path("id") idAcreditado: String,
        @Body datosDocumentos: datosDocumentos
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarDatosEspecificiosVivienda/{id}")
    suspend fun actualizarDatosEspecificosVivienda(
        @Path("id") idAcreditado: String,
        @Body datosEspecificosVivienda: datosEspecificosVivienda
    ): Response<AcreditadoResponse>

    @PUT("/appActualizarObservaciones/{id}")
    suspend fun actualizarDatosObservaciones(
        @Path("id") idAcreditado: String,
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