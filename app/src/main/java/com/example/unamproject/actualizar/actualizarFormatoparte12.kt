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
import com.example.unamproject.datosFamiliaDeudas
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparte12 : AppCompatActivity() {

    private lateinit var familiaTieneDeudas: EditText
    private lateinit var familiaCantidadDeuda: EditText

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null
    private var registroExistente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte12)
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

        cargarDatosDeudasFamiliares()
    }

    private fun initViews() {
        familiaTieneDeudas = findViewById(R.id.familia_tiene_deudas)
        familiaCantidadDeuda = findViewById(R.id.familia_cantidad_deuda)
        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        btnActualizarDatos.setOnClickListener {
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
                    onAceptar = { guardarOActualizarDatos() }
                )
            }
        }
    }

    private fun cargarDatosDeudasFamiliares() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosFamiliaDeudas(idAcreditado)

                if (response.isSuccessful) {
                    response.body()?.let { datos ->
                        registroExistente = true

                        familiaTieneDeudas.setText(datos.familia_tiene_deudas)
                        familiaCantidadDeuda.setText(datos.familia_cantidad_deudas)
                    } ?: run {
                        mostrarDialogo(
                            titulo = "Información",
                            mensaje = "No se encontraron datos de deudas familiares. Puede crear un nuevo registro.",
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
        // Validar que si tiene deudas, debe especificar la cantidad
        val tieneDeudas = familiaTieneDeudas.text.toString().trim()
        val cantidadDeuda = familiaCantidadDeuda.text.toString().trim()

        if (tieneDeudas.equals("Sí", ignoreCase = true) && cantidadDeuda.isEmpty()) {
            familiaCantidadDeuda.error = "Debe especificar la cantidad"
            mostrarDialogo(
                titulo = "Validación",
                mensaje = "Si la familia tiene deudas, debe especificar la cantidad",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        // Validar que la cantidad de deuda sea un número válido si se especifica
        if (cantidadDeuda.isNotEmpty() && !cantidadDeuda.matches(Regex("^\\d+(\\.\\d{1,2})?$"))) {
            familiaCantidadDeuda.error = "Formato inválido"
            mostrarDialogo(
                titulo = "Validación",
                mensaje = "La cantidad de deuda debe ser un valor monetario válido (ej. 1250 o 1250.50)",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        return true
    }

    private fun mostrarConfirmacionGuardado() {
        mostrarDialogo(
            titulo = "Confirmar",
            mensaje = "¿Está seguro que desea guardar los datos de deudas familiares?",
            iconoResId = android.R.drawable.ic_dialog_alert,
            colorTitulo = 0xFFFFA000.toInt(),
            onAceptar = { guardarOActualizarDatos() }
        )
    }

    private fun guardarOActualizarDatos() {
        val datos = datosFamiliaDeudas(
            familia_tiene_deudas = familiaTieneDeudas.text.toString().trim(),
            familia_cantidad_deudas = familiaCantidadDeuda.text.toString().trim(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        lifecycleScope.launch {
            try {
                val response = if (registroExistente) {
                    RetrofitClient.webService.actualizarDatosFamiliaDeudas(idAcreditado, datos)
                } else {
                    RetrofitClient.webService.agregarDatosFamiliaDeudas(datos)
                }

                if (response.isSuccessful) {
                    response.body()?.let { respuesta ->
                        if (respuesta.success) {
                            registroExistente = true
                            mostrarDialogo(
                                titulo = "Éxito",
                                mensaje = if (registroExistente)
                                    "Datos de deudas familiares actualizados correctamente"
                                else
                                    "Datos de deudas familiares guardados correctamente",
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
        val intent = Intent(this, actualizarFormatoparte13::class.java).apply {
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