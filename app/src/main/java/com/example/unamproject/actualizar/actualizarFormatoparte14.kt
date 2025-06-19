package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosCobranza
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class actualizarFormatoparte14 : AppCompatActivity() {

    private lateinit var cobranzaVisita: EditText
    private lateinit var cobranzaNumeroVisitas: EditText
    private lateinit var cobranzaUltimaFechaVisita: EditText
    private lateinit var cobranzaDespacho: EditText
    private lateinit var cobranzaCalificacion: EditText
    private lateinit var cobranzaComentario: EditText

    private lateinit var btnActualizar: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null
    private var registroExistente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte14)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupButtons()

        // Validar idAcreditado
        idAcreditado = intent.getStringExtra("id_acreditado") ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }
        idUsuario = intent.getStringExtra("id_usuario").takeIf { !it.isNullOrBlank() }

        cargarDatosCobranza()
    }

    private fun initViews() {
        cobranzaVisita = findViewById(R.id.cobranza_visita)
        cobranzaNumeroVisitas = findViewById(R.id.cobranza_numero_visitas)
        cobranzaUltimaFechaVisita = findViewById(R.id.cobranza_ultima_fecha_visita)
        cobranzaDespacho = findViewById(R.id.cobranza_despacho)
        cobranzaCalificacion = findViewById(R.id.cobranza_calificacion)
        cobranzaComentario = findViewById(R.id.cobranza_comentario)

        btnActualizar = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        btnActualizar.setOnClickListener {
            if (validarFormulario()) {
                mostrarConfirmacionGuardado()
            }
        }

        btnSiguiente.setOnClickListener {
            if (registroExistente) {
                irASiguiente()
            } else {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarOActualizarDatosCobranza() }
                )
            }
        }
    }

    private fun cargarDatosCobranza() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosCobranza(idAcreditado)

                if (response.isSuccessful) {
                    response.body()?.let { datos ->
                        registroExistente = true

                        cobranzaVisita.setText(datos.cobranza_visita)
                        cobranzaNumeroVisitas.setText(datos.cobranza_numero_visitas)
                        cobranzaUltimaFechaVisita.setText(datos.cobranza_ultima_fecha_visita)
                        cobranzaDespacho.setText(datos.cobranza_despacho)
                        cobranzaCalificacion.setText(datos.cobranza_calificacion)
                        cobranzaComentario.setText(datos.cobranza_comentario)
                    } ?: run {
                        mostrarDialogo(
                            titulo = "Información",
                            mensaje = "No se encontraron datos de cobranza. Puede crear un nuevo registro.",
                            iconoResId = android.R.drawable.ic_dialog_info,
                            colorTitulo = 0xFF1976D2.toInt()
                        )
                    }
                } else {
                    manejarErrorRespuesta(response.code(), response.errorBody()?.string())
                }
            } catch (e: HttpException) {
                manejarErrorRespuesta(e.code(), e.message)
            } catch (e: IOException) {
                mostrarErrorConexion(e.message ?: "Error de red desconocido")
            } catch (e: Exception) {
                mostrarErrorInesperado(e.message ?: "Error desconocido")
            }
        }
    }

    private fun validarFormulario(): Boolean {
        // Validar que si hay visita, debe haber número de visitas
        if (cobranzaVisita.text.toString().equals("Sí", ignoreCase = true) &&
            cobranzaNumeroVisitas.text.toString().isEmpty()) {
            cobranzaNumeroVisitas.error = "Debe especificar el número"
            mostrarDialogo(
                titulo = "Validación",
                mensaje = "Si hubo visita de cobranza, debe especificar el número de visitas",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        // Validar formato de número de visitas
        val numeroVisitas = cobranzaNumeroVisitas.text.toString().trim()
        if (numeroVisitas.isNotEmpty() && !numeroVisitas.matches(Regex("^\\d+$"))) {
            cobranzaNumeroVisitas.error = "Solo números enteros"
            mostrarDialogo(
                titulo = "Validación",
                mensaje = "El número de visitas debe ser un valor numérico entero",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        // Validar formato de fecha si existe
        val fechaVisita = cobranzaUltimaFechaVisita.text.toString().trim()
        if (fechaVisita.isNotEmpty()) {
            try {
                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(fechaVisita)
            } catch (e: Exception) {
                cobranzaUltimaFechaVisita.error = "Formato inválido"
                mostrarDialogo(
                    titulo = "Validación",
                    mensaje = "La fecha debe tener el formato DD/MM/AAAA",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                return false
            }
        }

        // Validar calificación si existe
        // Validar calificación si existe
        val calificacionStr = cobranzaCalificacion.text.toString().trim()
        if (calificacionStr.isNotEmpty()) {
            try {
                val calificacionNum = calificacionStr.toInt()
                if (calificacionNum < 1 || calificacionNum > 10) {
                    cobranzaCalificacion.error = "1-10"
                    mostrarDialogo(
                        titulo = "Validación",
                        mensaje = "La calificación debe ser un número entre 1 y 10",
                        iconoResId = android.R.drawable.ic_dialog_alert,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                    return false
                }
            } catch (e: NumberFormatException) {
                cobranzaCalificacion.error = "Número inválido"
                mostrarDialogo(
                    titulo = "Validación",
                    mensaje = "La calificación debe ser un valor numérico",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                return false
            }
        }
        return true
    }

    private fun mostrarConfirmacionGuardado() {
        mostrarDialogo(
            titulo = "Confirmar",
            mensaje = "¿Está seguro que desea guardar los datos de cobranza?",
            iconoResId = android.R.drawable.ic_dialog_alert,
            colorTitulo = 0xFFFFA000.toInt(),
            onAceptar = { guardarOActualizarDatosCobranza() }
        )
    }

    private fun guardarOActualizarDatosCobranza() {
        val datos = datosCobranza(
            cobranza_visita = cobranzaVisita.text.toString().trim(),
            cobranza_numero_visitas = cobranzaNumeroVisitas.text.toString().trim(),
            cobranza_ultima_fecha_visita = cobranzaUltimaFechaVisita.text.toString().trim(),
            cobranza_despacho = cobranzaDespacho.text.toString().trim(),
            cobranza_calificacion = cobranzaCalificacion.text.toString().trim(),
            cobranza_comentario = cobranzaComentario.text.toString().trim(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        lifecycleScope.launch {
            try {
                val response = if (registroExistente) {
                    RetrofitClient.webService.actualizarDatosCobranza(idAcreditado, datos)
                } else {
                    RetrofitClient.webService.agregarDatosCobranza(datos)
                }

                if (response.isSuccessful) {
                    response.body()?.let { respuesta ->
                        if (respuesta.success) {
                            registroExistente = true
                            mostrarDialogo(
                                titulo = "Éxito",
                                mensaje = if (registroExistente)
                                    "Datos de cobranza actualizados correctamente"
                                else
                                    "Datos de cobranza guardados correctamente",
                                iconoResId = android.R.drawable.ic_dialog_info,
                                colorTitulo = 0xFF388E3C.toInt()
                            )
                        } else {
                            mostrarErrorServidor("El servidor no pudo procesar la solicitud")
                        }
                    } ?: mostrarErrorServidor("Respuesta vacía del servidor")
                } else {
                    manejarErrorRespuesta(response.code(), response.errorBody()?.string())
                }
            } catch (e: HttpException) {
                manejarErrorRespuesta(e.code(), e.message)
            } catch (e: IOException) {
                mostrarErrorConexion(e.message ?: "Error de red desconocido")
            } catch (e: Exception) {
                mostrarErrorInesperado(e.message ?: "Error desconocido")
            }
        }
    }

    private fun manejarErrorRespuesta(codigo: Int, mensajeError: String?) {
        when (codigo) {
            404 -> mostrarDialogo(
                titulo = "No encontrado",
                mensaje = "El registro no fue encontrado",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            400 -> mostrarDialogo(
                titulo = "Datos inválidos",
                mensaje = "Verifique la información ingresada: ${mensajeError ?: "Error 400"}",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            500 -> mostrarErrorServidor("Error interno del servidor: ${mensajeError ?: "sin detalles"}")
            else -> mostrarErrorServidor("Error $codigo: ${mensajeError ?: "Error desconocido"}")
        }
    }

    private fun mostrarErrorServidor(mensaje: String) {
        mostrarDialogo(
            titulo = "Error del servidor",
            mensaje = mensaje,
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        )
    }

    private fun mostrarErrorConexion(mensaje: String) {
        mostrarDialogo(
            titulo = "Error de conexión",
            mensaje = "No se pudo conectar al servidor: $mensaje",
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        )
    }

    private fun mostrarErrorInesperado(mensaje: String) {
        mostrarDialogo(
            titulo = "Error inesperado",
            mensaje = "Ocurrió un error: $mensaje",
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        )
    }

    private fun mostrarErrorYCerrar(mensaje: String) {
        mostrarDialogo(
            titulo = "Error crítico",
            mensaje = mensaje,
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        ) {
            finish()
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, actualizarFormatoparte15::class.java).apply {
            putExtra("id_acreditado", idAcreditado)
            putExtra("id_usuario", idUsuario)
        }
        startActivity(intent)
    }

    private fun mostrarDialogo(
        titulo: String,
        mensaje: String,
        iconoResId: Int,
        colorTitulo: Int,
        onAceptar: (() -> Unit)? = null
    ) {
        val view = LayoutInflater.from(this).inflate(R.layout.custom_alert_dialog, null)

        view.findViewById<ImageView>(R.id.ivIcon).apply {
            setImageResource(iconoResId)
            setColorFilter(colorTitulo)
        }

        view.findViewById<TextView>(R.id.tvTitle).apply {
            text = titulo
            setTextColor(colorTitulo)
        }

        view.findViewById<TextView>(R.id.tvMessage).text = mensaje

        AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()
            .apply {
                view.findViewById<Button>(R.id.btnOk).setOnClickListener {
                    dismiss()
                    onAceptar?.invoke()
                }
                show()
            }
    }
}