package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosEspecificosVivienda
import com.example.unamproject.datosObservaciones
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparte16 : AppCompatActivity() {

    private lateinit var viviendaNumeroHabitaciones: EditText
    private lateinit var viviendaTipoPiso: EditText
    private lateinit var viviendaTipoPisoOtro: EditText
    private lateinit var viviendaTipoTecho: EditText
    private lateinit var viviendaCuentaBano: EditText
    private lateinit var observacionesEntrevistador: EditText

    private lateinit var btnActualizar: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null

    private var viviendaRegistroExistente = false
    private var observacionesRegistroExistente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte16)

        initViews()
        setupButtons()

        // Validar idAcreditado
        idAcreditado = intent.getStringExtra("id_acreditado") ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }
        idUsuario = intent.getStringExtra("id_usuario").takeIf { !it.isNullOrBlank() }

        cargarDatos()
    }

    private fun initViews() {
        viviendaNumeroHabitaciones = findViewById(R.id.vivienda_numero_habitaciones)
        viviendaTipoPiso = findViewById(R.id.vivienda_tipo_piso)
        viviendaTipoPisoOtro = findViewById(R.id.vivienda_tipo_piso_otro)
        viviendaTipoTecho = findViewById(R.id.vivienda_tipo_techo)
        viviendaCuentaBano = findViewById(R.id.viviendo_cuenta_bano)
        observacionesEntrevistador = findViewById(R.id.observaciones_entrevistador)

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
            if (viviendaRegistroExistente && observacionesRegistroExistente) {
                irASiguiente()
            } else {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar ambos conjuntos de datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarOActualizarDatos() }
                )
            }
        }
    }

    private fun cargarDatos() {
        lifecycleScope.launch {
            try {
                val responseVivienda = RetrofitClient.webService.obtenerDatosEspecificosVivienda(idAcreditado)
                val responseObs = RetrofitClient.webService.obtenerDatosObservaciones(idAcreditado)

                if (responseVivienda.isSuccessful) {
                    responseVivienda.body()?.let { datos ->
                        viviendaRegistroExistente = true
                        viviendaNumeroHabitaciones.setText(datos.vivienda_numero_habitaciones)
                        viviendaTipoPiso.setText(datos.vivienda_tipo_piso)
                        viviendaTipoPisoOtro.setText(datos.vivienda_tipo_piso_otro)
                        viviendaTipoTecho.setText(datos.vivienda_tipo_techo)
                        viviendaCuentaBano.setText(datos.vivienda_cuenta_bano)
                    } ?: run {
                        mostrarDialogo(
                            titulo = "Información",
                            mensaje = "No se encontraron datos de vivienda. Puede crear un nuevo registro.",
                            iconoResId = android.R.drawable.ic_dialog_info,
                            colorTitulo = 0xFF1976D2.toInt()
                        )
                    }
                } else {
                    manejarErrorRespuesta(responseVivienda.code(), responseVivienda.errorBody()?.string())
                }

                if (responseObs.isSuccessful) {
                    responseObs.body()?.let { datos ->
                        observacionesRegistroExistente = true
                        observacionesEntrevistador.setText(datos.observaciones_entrevistador)
                    } ?: run {
                        mostrarDialogo(
                            titulo = "Información",
                            mensaje = "No se encontraron observaciones. Puede crear un nuevo registro.",
                            iconoResId = android.R.drawable.ic_dialog_info,
                            colorTitulo = 0xFF1976D2.toInt()
                        )
                    }
                } else {
                    manejarErrorRespuesta(responseObs.code(), responseObs.errorBody()?.string())
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
        // Validar número de habitaciones
        val numHabitaciones = viviendaNumeroHabitaciones.text.toString().trim()
        if (numHabitaciones.isNotEmpty() && !numHabitaciones.matches(Regex("^\\d+\$"))) {
            viviendaNumeroHabitaciones.error = "Solo números enteros"
            mostrarDialogo(
                titulo = "Validación",
                mensaje = "El número de habitaciones debe ser un valor numérico entero",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        // Validar que si el tipo de piso es "Otro", debe especificar
        if (viviendaTipoPiso.text.toString().equals("Otro", ignoreCase = true) &&
            viviendaTipoPisoOtro.text.toString().isEmpty()) {
            viviendaTipoPisoOtro.error = "Especifique el tipo"
            mostrarDialogo(
                titulo = "Validación",
                mensaje = "Si seleccionó 'Otro' como tipo de piso, debe especificar cuál",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        // Validar que si cuenta con baño, debe especificar cuántos
        if (viviendaCuentaBano.text.toString().equals("Sí", ignoreCase = true) &&
            viviendaNumeroHabitaciones.text.toString().isEmpty()) {
            viviendaNumeroHabitaciones.error = "Especifique cuántos"
            mostrarDialogo(
                titulo = "Validación",
                mensaje = "Si la vivienda cuenta con baño, debe especificar cuántos",
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
            mensaje = "¿Está seguro que desea guardar los datos de vivienda y observaciones?",
            iconoResId = android.R.drawable.ic_dialog_alert,
            colorTitulo = 0xFFFFA000.toInt(),
            onAceptar = { guardarOActualizarDatos() }
        )
    }

    private fun guardarOActualizarDatos() {
        val viviendaData = datosEspecificosVivienda(
            vivienda_numero_habitaciones = viviendaNumeroHabitaciones.text.toString().trim(),
            vivienda_tipo_piso = viviendaTipoPiso.text.toString().trim(),
            vivienda_tipo_piso_otro = viviendaTipoPisoOtro.text.toString().trim(),
            vivienda_tipo_techo = viviendaTipoTecho.text.toString().trim(),
            vivienda_cuenta_bano = viviendaCuentaBano.text.toString().trim(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        val observacionesData = datosObservaciones(
            observaciones_entrevistador = observacionesEntrevistador.text.toString().trim(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        lifecycleScope.launch {
            try {
                val responseVivienda = if (viviendaRegistroExistente) {
                    RetrofitClient.webService.actualizarDatosEspecificosVivienda(idAcreditado, viviendaData)
                } else {
                    RetrofitClient.webService.agregarDatosEspecificosVivienda(viviendaData)
                }

                val responseObs = if (observacionesRegistroExistente) {
                    RetrofitClient.webService.actualizarDatosObservaciones(idAcreditado, observacionesData)
                } else {
                    RetrofitClient.webService.agregarDatosObservaciones(observacionesData)
                }

                if (responseVivienda.isSuccessful && responseObs.isSuccessful) {
                    viviendaRegistroExistente = true
                    observacionesRegistroExistente = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Datos de vivienda y observaciones guardados correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                } else {
                    manejarErrorDobleRespuesta(
                        responseVivienda.code(), responseVivienda.errorBody()?.string(),
                        responseObs.code(), responseObs.errorBody()?.string()
                    )
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
    private fun manejarErrorDobleRespuesta(
        codigoVivienda: Int, errorVivienda: String?,
        codigoObs: Int, errorObs: String?
    ) {
        val mensaje = buildString {
            append("Error al guardar:\n")
            append("Vivienda: ${obtenerMensajeError(codigoVivienda, errorVivienda)}\n")
            append("Observaciones: ${obtenerMensajeError(codigoObs, errorObs)}")
        }

        mostrarDialogo(
            titulo = "Error al guardar",
            mensaje = mensaje,
            iconoResId = android.R.drawable.stat_notify_error,
            colorTitulo = 0xFFD32F2F.toInt()
        )
    }

    private fun obtenerMensajeError(codigo: Int, mensajeError: String?): String {
        return when (codigo) {
            404 -> "Registro no encontrado"
            400 -> "Datos inválidos: ${mensajeError ?: "sin detalles"}"
            500 -> "Error interno del servidor"
            else -> "Error $codigo: ${mensajeError ?: "Error desconocido"}"
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
        val intent = Intent(this, actualizarFormatoparteFinal::class.java).apply {
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