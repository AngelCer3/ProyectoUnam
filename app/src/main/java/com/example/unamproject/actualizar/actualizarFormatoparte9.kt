package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosEspecificosConyuge
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparte9 : AppCompatActivity() {

    private lateinit var conyugeActivo: EditText
    private lateinit var conyugeOcupacionActual: EditText
    private lateinit var institucionTrabajoConyuge: EditText
    private lateinit var conyugeActividadRemunerada: EditText
    private lateinit var conyugeContratoLaboral: EditText
    private lateinit var conyugeIngresoMensual: EditText
    private lateinit var conyugeEmpresa: EditText
    private lateinit var conyugeAntiguedad: EditText
    private lateinit var comprobanteIngresoConyuge: EditText
    private lateinit var institucionCotizacionConyuge: EditText
    private lateinit var ingresosConceptosConyuge: EditText

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null
    private var registroExistente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte9)

        // Validar idAcreditado primero
        idAcreditado = intent.getStringExtra("id_acreditado") ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }

        idUsuario = intent.getStringExtra("id_usuario").takeIf { !it.isNullOrBlank() }

        initViews()
        cargarDatosConyuge()
        setupButtons()
    }

    private fun initViews() {
        conyugeActivo = findViewById(R.id.conyuge_activo)
        conyugeOcupacionActual = findViewById(R.id.conyuge_ocupacion_actual)
        institucionTrabajoConyuge = findViewById(R.id.institucion_trabajo_conyuge)
        conyugeActividadRemunerada = findViewById(R.id.conyuge_actividad_remunerada)
        conyugeContratoLaboral = findViewById(R.id.conyuge_contrato_laboral)
        conyugeIngresoMensual = findViewById(R.id.conyuge_ingreso_mensual)
        conyugeEmpresa = findViewById(R.id.conyuge_empresa)
        conyugeAntiguedad = findViewById(R.id.conyuge_antiguedad)
        comprobanteIngresoConyuge = findViewById(R.id.comprobante_ingreso_conyuge)
        institucionCotizacionConyuge = findViewById(R.id.institucion_cotizacion_conyuge)
        ingresosConceptosConyuge = findViewById(R.id.ingresos_conceptos_conyuge)

        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        btnActualizarDatos.setOnClickListener {
            if (validarCampos()) {
                guardarOActualizarDatos()
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

    private fun cargarDatosConyuge() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosEspecificosConyuge(idAcreditado)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { datos ->
                            registroExistente = true

                            conyugeActivo.setText(datos.conyuge_activo)
                            conyugeOcupacionActual.setText(datos.conyuge_ocupacion_actual)
                            institucionTrabajoConyuge.setText(datos.institucion_trabajo_conyuge)
                            conyugeActividadRemunerada.setText(datos.conyuge_actividad_remunerada)
                            conyugeContratoLaboral.setText(datos.conyuge_contrato_laboral)
                            conyugeIngresoMensual.setText(datos.conyuge_ingreso_mensual)
                            conyugeEmpresa.setText(datos.conyuge_empresa)
                            conyugeAntiguedad.setText(datos.conyuge_antiguedad)
                            comprobanteIngresoConyuge.setText(datos.comprobante_ingreso_conyuge)
                            institucionCotizacionConyuge.setText(datos.institucion_cotizacion_conyuge)
                            ingresosConceptosConyuge.setText(datos.ingresos_conceptos_conyuge)

                        } ?: run {
                            mostrarDialogo(
                                titulo = "Información",
                                mensaje = "No se encontraron datos del cónyuge. Puede crear un nuevo registro.",
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
            conyugeActivo to "Estado laboral del cónyuge",
            conyugeOcupacionActual to "Ocupación actual del cónyuge",
            conyugeIngresoMensual to "Ingreso mensual del cónyuge"
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

        // Validación de campos numéricos
        val camposNumericos = listOf(
            conyugeIngresoMensual to "Ingreso mensual",
            conyugeAntiguedad to "Antigüedad laboral"
        )

        for ((campo, nombre) in camposNumericos) {
            val valor = campo.text.toString().trim()
            if (valor.isNotEmpty() && !valor.matches(Regex("^\\d+(\\.\\d{1,2})?$"))) {
                mostrarDialogo(
                    titulo = "Valor inválido",
                    mensaje = "El campo $nombre debe ser un número válido",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validación condicional si está trabajando
        if (conyugeActivo.text.toString().trim().equals("Sí", ignoreCase = true)) {
            if (conyugeEmpresa.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "Debe especificar la empresa donde trabaja el cónyuge",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                conyugeEmpresa.requestFocus()
                return false
            }
        }

        return true
    }

    private fun guardarOActualizarDatos() {
        val datos = datosEspecificosConyuge(
            conyuge_activo = conyugeActivo.text.toString().trim(),
            conyuge_ocupacion_actual = conyugeOcupacionActual.text.toString().trim(),
            institucion_trabajo_conyuge = institucionTrabajoConyuge.text.toString().trim(),
            conyuge_actividad_remunerada = conyugeActividadRemunerada.text.toString().trim(),
            conyuge_contrato_laboral = conyugeContratoLaboral.text.toString().trim(),
            conyuge_ingreso_mensual = conyugeIngresoMensual.text.toString().trim(),
            conyuge_empresa = conyugeEmpresa.text.toString().trim(),
            conyuge_antiguedad = conyugeAntiguedad.text.toString().trim(),
            comprobante_ingreso_conyuge = comprobanteIngresoConyuge.text.toString().trim(),
            institucion_cotizacion_conyuge = institucionCotizacionConyuge.text.toString().trim(),
            ingresos_conceptos_conyuge = ingresosConceptosConyuge.text.toString().trim(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = if (registroExistente) {
                    RetrofitClient.webService.actualizarDatosEspecificosConyuge(idAcreditado, datos)
                } else {
                    RetrofitClient.webService.agregarDatosEspecificosConyuge(datos)
                }

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { respuesta ->
                            if (respuesta.success) {
                                registroExistente = true
                                mostrarDialogo(
                                    titulo = "Éxito",
                                    mensaje = if (registroExistente)
                                        "Datos del cónyuge actualizados correctamente"
                                    else
                                        "Datos del cónyuge guardados correctamente",
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

    private fun irASiguiente() {
        val intent = Intent(this, actualizarFormatoparte10::class.java).apply {
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