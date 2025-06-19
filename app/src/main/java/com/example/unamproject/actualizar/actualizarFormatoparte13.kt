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
import com.example.unamproject.datosTelefonos
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparte13 : AppCompatActivity() {

    private lateinit var telefono1Lada: EditText
    private lateinit var telefono1Numero: EditText
    private lateinit var telefono1Extension: EditText
    private lateinit var telefono1Tipo: EditText
    private lateinit var telefono2Lada: EditText
    private lateinit var telefono2Numero: EditText
    private lateinit var telefono2Extension: EditText
    private lateinit var telefono2Tipo: EditText

    private lateinit var btnActualizar: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null
    private var registroExistente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte13)
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

        cargarDatosTelefonicos()
    }

    private fun initViews() {
        telefono1Lada = findViewById(R.id.telefono1_lada)
        telefono1Numero = findViewById(R.id.telefono1_numero)
        telefono1Extension = findViewById(R.id.telefono1_extension)
        telefono1Tipo = findViewById(R.id.telefono1_tipo)
        telefono2Lada = findViewById(R.id.telefono2_lada)
        telefono2Numero = findViewById(R.id.telefono2_numero)
        telefono2Extension = findViewById(R.id.telefono2_extension)
        telefono2Tipo = findViewById(R.id.telefono2_tipo)

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
                    onAceptar = { guardarOActualizarDatosTelefonicos() }
                )
            }
        }
    }

    private fun cargarDatosTelefonicos() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosTelefonos(idAcreditado)

                if (response.isSuccessful) {
                    response.body()?.let { datos ->
                        registroExistente = true

                        telefono1Lada.setText(datos.telefono1_lada)
                        telefono1Numero.setText(datos.telefono1_numero)
                        telefono1Extension.setText(datos.telefono1_extension)
                        telefono1Tipo.setText(datos.telefono1_tipo)
                        telefono2Lada.setText(datos.telefono2_lada)
                        telefono2Numero.setText(datos.telefono2_numero)
                        telefono2Extension.setText(datos.telefono2_extension)
                        telefono2Tipo.setText(datos.telefono2_tipo)
                    } ?: run {
                        mostrarDialogo(
                            titulo = "Información",
                            mensaje = "No se encontraron datos telefónicos. Puede crear un nuevo registro.",
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
        // Validar que al menos un teléfono esté completo
        val telefono1Completo = telefono1Numero.text.toString().isNotEmpty()
        val telefono2Completo = telefono2Numero.text.toString().isNotEmpty()

        if (!telefono1Completo && !telefono2Completo) {
            mostrarDialogo(
                titulo = "Validación",
                mensaje = "Debe proporcionar al menos un número telefónico completo",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        // Validar formatos de teléfonos
        val telefonos = listOf(
            Pair(telefono1Numero, "Teléfono 1"),
            Pair(telefono2Numero, "Teléfono 2")
        )

        for ((campo, nombre) in telefonos) {
            val numero = campo.text.toString().trim()
            if (numero.isNotEmpty() && !numero.matches(Regex("^[0-9]{7,15}$"))) {
                campo.error = "Formato inválido"
                mostrarDialogo(
                    titulo = "Validación",
                    mensaje = "El $nombre debe contener solo números (7-15 dígitos)",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                return false
            }
        }

        // Validar que si hay número, debe haber tipo
        val telefonosConTipo = listOf(
            Triple(telefono1Numero, telefono1Tipo, "Teléfono 1"),
            Triple(telefono2Numero, telefono2Tipo, "Teléfono 2")
        )

        for ((numero, tipo, nombre) in telefonosConTipo) {
            if (numero.text.toString().isNotEmpty() && tipo.text.toString().isEmpty()) {
                tipo.error = "Debe especificar el tipo"
                mostrarDialogo(
                    titulo = "Validación",
                    mensaje = "Si proporciona un número para $nombre, debe indicar el tipo (ej. Celular, Casa, Oficina)",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                return false
            }
        }

        // Validar que las ladas sean numéricas si existen
        val ladas = listOf(
            Pair(telefono1Lada, "Lada Teléfono 1"),
            Pair(telefono2Lada, "Lada Teléfono 2")
        )

        for ((campo, nombre) in ladas) {
            val lada = campo.text.toString().trim()
            if (lada.isNotEmpty() && !lada.matches(Regex("^[0-9]+$"))) {
                campo.error = "Solo números"
                mostrarDialogo(
                    titulo = "Validación",
                    mensaje = "La $nombre debe contener solo números",
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
            mensaje = "¿Está seguro que desea guardar los datos telefónicos?",
            iconoResId = android.R.drawable.ic_dialog_alert,
            colorTitulo = 0xFFFFA000.toInt(),
            onAceptar = { guardarOActualizarDatosTelefonicos() }
        )
    }

    private fun guardarOActualizarDatosTelefonicos() {
        val datos = datosTelefonos(
            telefono1_lada = telefono1Lada.text.toString().trim(),
            telefono1_numero = telefono1Numero.text.toString().trim(),
            telefono1_extension = telefono1Extension.text.toString().trim(),
            telefono1_tipo = telefono1Tipo.text.toString().trim(),
            telefono2_lada = telefono2Lada.text.toString().trim(),
            telefono2_numero = telefono2Numero.text.toString().trim(),
            telefono2_extension = telefono2Extension.text.toString().trim(),
            telefono2_tipo = telefono2Tipo.text.toString().trim(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        lifecycleScope.launch {
            try {
                val response = if (registroExistente) {
                    RetrofitClient.webService.actualizarDatosTelefonicos(idAcreditado, datos)
                } else {
                    RetrofitClient.webService.agregarDatosTelefonicos(datos)
                }

                if (response.isSuccessful) {
                    response.body()?.let { respuesta ->
                        if (respuesta.success) {
                            registroExistente = true
                            mostrarDialogo(
                                titulo = "Éxito",
                                mensaje = if (registroExistente)
                                    "Datos telefónicos actualizados correctamente"
                                else
                                    "Datos telefónicos guardados correctamente",
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
        val intent = Intent(this, actualizarFormatoparte14::class.java).apply {
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