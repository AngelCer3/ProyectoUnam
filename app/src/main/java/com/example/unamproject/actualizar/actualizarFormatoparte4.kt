package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosCredito
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparte4 : AppCompatActivity() {

    private lateinit var creditoFechaEntrega: EditText
    private lateinit var creditoMonto: EditText
    private lateinit var creditoSueldoOtorgado: EditText
    private lateinit var creditoFechaUltimoPago: EditText
    private lateinit var creditoReciboPago: EditText
    private lateinit var creditoPagoActual: EditText
    private lateinit var creditoDeudaActual: EditText

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null
    private var registroExistente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte4)

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
        creditoFechaEntrega = findViewById(R.id.credito_fecha_entrega)
        creditoMonto = findViewById(R.id.credito_monto)
        creditoSueldoOtorgado = findViewById(R.id.credito_sueldo_otorgado)
        creditoFechaUltimoPago = findViewById(R.id.credito_fecha_ultimo_pago)
        creditoReciboPago = findViewById(R.id.credito_recibo_pago)
        creditoPagoActual = findViewById(R.id.credito_pago_actual)
        creditoDeudaActual = findViewById(R.id.credito_deuda_actual)

        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        btnActualizarDatos.setOnClickListener {
            if (validarCampos()) {
                actualizarDatos()
            }
        }

        btnSiguiente.setOnClickListener {
            if (registroExistente) {
                navegarAParteSiguiente()
            } else {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { actualizarDatos() }
                )
            }
        }
    }

    private fun cargarDatosPrevios() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.obtenerCredito(idAcreditado)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { credito ->
                            registroExistente = true

                            creditoFechaEntrega.setText(credito.credito_fecha_entrega)
                            creditoMonto.setText(credito.credito_monto)
                            creditoSueldoOtorgado.setText(credito.credito_sueldo_otorgado)
                            creditoFechaUltimoPago.setText(credito.credito_fecha_ultimo_pago)
                            creditoReciboPago.setText(credito.credito_recibo_pago)
                            creditoPagoActual.setText(credito.credito_pago_actual)
                            creditoDeudaActual.setText(credito.credito_deuda_actual)

                        } ?: run {
                            mostrarDialogo(
                                titulo = "Información",
                                mensaje = "No se encontraron datos de crédito. Puede crear un nuevo registro.",
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

    private fun validarCampos(): Boolean {
        val camposRequeridos = listOf(
            creditoFechaEntrega to "Fecha de entrega",
            creditoMonto to "Monto del crédito",
            creditoSueldoOtorgado to "Sueldo otorgado",
            creditoFechaUltimoPago to "Fecha del último pago",
            creditoPagoActual to "Pago actual",
            creditoDeudaActual to "Deuda actual"
        )

        for ((campo, nombre) in camposRequeridos) {
            if (campo.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "El campo $nombre es obligatorio",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validación de formato de fechas (DD/MM/AAAA)
        val fechas = listOf(
            creditoFechaEntrega to "Fecha de entrega",
            creditoFechaUltimoPago to "Fecha del último pago"
        )

        for ((campo, nombre) in fechas) {
            val fecha = campo.text.toString().trim()
            if (fecha.isNotEmpty() && !fecha.matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) {
                mostrarDialogo(
                    titulo = "Formato inválido",
                    mensaje = "La $nombre debe tener el formato DD/MM/AAAA",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validación de montos numéricos
        val montos = listOf(
            creditoMonto to "Monto del crédito",
            creditoSueldoOtorgado to "Sueldo otorgado",
            creditoPagoActual to "Pago actual",
            creditoDeudaActual to "Deuda actual"
        )

        for ((campo, nombre) in montos) {
            val monto = campo.text.toString().trim()
            if (monto.isNotEmpty() && !monto.matches(Regex("^\\d+(\\.\\d{1,2})?$"))) {
                mostrarDialogo(
                    titulo = "Formato inválido",
                    mensaje = "El campo $nombre debe ser un valor numérico válido",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        return true
    }

    private fun actualizarDatos() {
        val credito = datosCredito(
            credito_fecha_entrega = creditoFechaEntrega.text.toString().trim(),
            credito_monto = creditoMonto.text.toString().trim(),
            credito_sueldo_otorgado = creditoSueldoOtorgado.text.toString().trim(),
            credito_fecha_ultimo_pago = creditoFechaUltimoPago.text.toString().trim(),
            credito_recibo_pago = creditoReciboPago.text.toString().trim(),
            credito_pago_actual = creditoPagoActual.text.toString().trim(),
            credito_deuda_actual = creditoDeudaActual.text.toString().trim(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.actualizarDatosCredito(idAcreditado, credito)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { respuesta ->
                            if (respuesta.success) {
                                registroExistente = true
                                mostrarDialogo(
                                    titulo = "Éxito",
                                    mensaje = "Datos de crédito actualizados correctamente",
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

    private fun navegarAParteSiguiente() {
        val intent = Intent(this, actualizarFormatoparte5::class.java).apply {
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