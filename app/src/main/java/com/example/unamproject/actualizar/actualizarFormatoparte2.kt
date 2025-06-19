package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.Visitas
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparte2 : AppCompatActivity() {

    private lateinit var visita1Fecha: EditText
    private lateinit var visita1Hora: EditText
    private lateinit var visita1Resultado: Spinner

    private lateinit var visita2Fecha: EditText
    private lateinit var visita2Hora: EditText
    private lateinit var visita2Resultado: Spinner

    private lateinit var visita3Fecha: EditText
    private lateinit var visita3Hora: EditText
    private lateinit var visita3Resultado: Spinner

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null
    private var registroExistente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte2)

        // Validar idAcreditado primero
        idAcreditado = intent.getStringExtra("id_acreditado") ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }

        idUsuario = intent.getStringExtra("id_usuario").takeIf { !it.isNullOrBlank() }

        initViews()
        cargarDatosPrevios()
        setupButtons()
    }

    private fun initViews() {
        visita1Fecha = findViewById(R.id.visita1_fecha)
        visita1Hora = findViewById(R.id.visita1_hora)
        visita1Resultado = findViewById(R.id.visita1_resultado)

        visita2Fecha = findViewById(R.id.visita2_fecha)
        visita2Hora = findViewById(R.id.visita2_hora)
        visita2Resultado = findViewById(R.id.visita2_resultado)

        visita3Fecha = findViewById(R.id.visita3_fecha)
        visita3Hora = findViewById(R.id.visita3_hora)
        visita3Resultado = findViewById(R.id.visita3_resultado)

        btnActualizarDatos = findViewById(R.id.btnActualizarDatos)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        btnActualizarDatos.setOnClickListener {
            if (validarCampos()) {
                guardarOActualizarDatos()
            }
        }

        btnSiguiente.setOnClickListener {
            navegarAParte3()
        }
    }

    private fun cargarDatosPrevios() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.obtenerVisitas(idAcreditado)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { visitas ->
                            registroExistente = true
                            // Set data for visit 1
                            visita1Fecha.setText(visitas.visita1_fecha)
                            visita1Hora.setText(visitas.visita1_hora)
                            setSpinnerSelection(visita1Resultado, visitas.visita1_resultado)

                            // Set data for visit 2
                            visita2Fecha.setText(visitas.visita2_fecha)
                            visita2Hora.setText(visitas.visita2_hora)
                            setSpinnerSelection(visita2Resultado, visitas.visita2_resultado)

                            // Set data for visit 3
                            visita3Fecha.setText(visitas.visita3_fecha)
                            visita3Hora.setText(visitas.visita3_hora)
                            setSpinnerSelection(visita3Resultado, visitas.visita3_resultado)
                        } ?: run {
                            mostrarDialogo(
                                titulo = "Información",
                                mensaje = "No se encontraron visitas registradas. Puede crear un nuevo registro.",
                                iconoResId = android.R.drawable.ic_dialog_info,
                                colorTitulo = 0xFF1976D2.toInt()
                            )
                        }
                    } else {
                        manejarErrorRespuesta(response.code(), response.errorBody()?.string())
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    manejarErrorRespuesta(e.code(), e.message)
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    mostrarErrorConexion(e.message ?: "Error de red desconocido")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado(e.message ?: "Error desconocido")
                }
            }
        }
    }

    private fun setSpinnerSelection(spinner: Spinner, value: String?) {
        if (value.isNullOrEmpty()) return
        val adapter = spinner.adapter
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString().equals(value, ignoreCase = true)) {
                spinner.setSelection(i)
                break
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar que al menos una visita tenga fecha (es requerido al menos una visita)
        if (visita1Fecha.text.isNullOrBlank() &&
            visita2Fecha.text.isNullOrBlank() &&
            visita3Fecha.text.isNullOrBlank()) {
            mostrarDialogo(
                titulo = "Visita requerida",
                mensaje = "Debe registrar al menos una visita",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        // Validar que si hay hora, haya fecha y viceversa
        val visitas = listOf(
            Triple(visita1Fecha, visita1Hora, "Visita 1"),
            Triple(visita2Fecha, visita2Hora, "Visita 2"),
            Triple(visita3Fecha, visita3Hora, "Visita 3")
        )

        for ((fecha, hora, nombreVisita) in visitas) {
            val tieneFecha = !fecha.text.isNullOrBlank()
            val tieneHora = !hora.text.isNullOrBlank()

            if (tieneFecha != tieneHora) {
                mostrarDialogo(
                    titulo = "Datos incompletos",
                    mensaje = "Para $nombreVisita debe ingresar tanto fecha como hora",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                if (tieneFecha) hora.requestFocus() else fecha.requestFocus()
                return false
            }
        }

        return true
    }

    private fun guardarOActualizarDatos() {
        val visitas = Visitas(
            visita1_fecha = visita1Fecha.text.toString().trim(),
            visita1_hora = visita1Hora.text.toString().trim(),
            visita1_resultado = visita1Resultado.selectedItem?.toString()?.trim() ?: "",
            visita2_fecha = visita2Fecha.text.toString().trim(),
            visita2_hora = visita2Hora.text.toString().trim(),
            visita2_resultado = visita2Resultado.selectedItem?.toString()?.trim() ?: "",
            visita3_fecha = visita3Fecha.text.toString().trim(),
            visita3_hora = visita3Hora.text.toString().trim(),
            visita3_resultado = visita3Resultado.selectedItem?.toString()?.trim() ?: "",
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = if (registroExistente) {
                    RetrofitClient.webService.actualizarVisitas(idAcreditado, visitas)
                } else {
                    RetrofitClient.webService.agregarVisitas(visitas)
                }

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { respuesta ->
                            if (respuesta.success) {
                                registroExistente = true
                                mostrarDialogo(
                                    titulo = if (registroExistente) "Actualizado" else "Guardado",
                                    mensaje = if (registroExistente)
                                        "Visitas actualizadas correctamente"
                                    else
                                        "Visitas guardadas correctamente",
                                    iconoResId = android.R.drawable.ic_dialog_info,
                                    colorTitulo = 0xFF388E3C.toInt()
                                )
                            } else {
                                mostrarErrorServidor("Error al procesar la solicitud")
                            }
                        } ?: mostrarErrorServidor("Respuesta vacía del servidor")
                    } else {
                        manejarErrorRespuesta(response.code(), response.errorBody()?.string())
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    manejarErrorRespuesta(e.code(), e.message)
                }
            } catch (e: IOException) {
                withContext(Dispatchers.Main) {
                    mostrarErrorConexion(e.message ?: "Error de red desconocido")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado(e.message ?: "Error desconocido")
                }
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

    private fun navegarAParte3() {
        val intent = Intent(this, actualizarFormatoparte3::class.java).apply {
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