package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosSolicitante
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparte8 : AppCompatActivity() {

    private lateinit var hogarIntegrantesTrabajando: EditText
    private lateinit var solicitanteActivo: EditText
    private lateinit var solicitanteOcupacionActual: EditText
    private lateinit var solicitanteDesempleadoTiempo: EditText
    private lateinit var solicitanteEmpresaPrevia: EditText
    private lateinit var solicitanteAntiguedadEmpleoAnterior: EditText
    private lateinit var institucionTrabajoSolicitante: EditText
    private lateinit var actividadRemuneradaSolicitante: EditText
    private lateinit var contratoLaboralSolicitante: EditText
    private lateinit var solicitanteIngresoMensual: EditText
    private lateinit var solicitanteEmpresa: EditText
    private lateinit var solicitanteAntiguedad: EditText
    private lateinit var comprobanteIngresosSolicitante: EditText
    private lateinit var institucionCotizacionSolicitante: EditText
    private lateinit var ingresoConceptosSolicitante: EditText

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null
    private var registroExistente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte8)

        // Validar idAcreditado primero
        idAcreditado = intent.getStringExtra("id_acreditado") ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }

        idUsuario = intent.getStringExtra("id_usuario").takeIf { !it.isNullOrBlank() }

        initViews()
        cargarDatosSolicitante()
        setupButtons()
    }

    private fun initViews() {
        hogarIntegrantesTrabajando = findViewById(R.id.hogar_integrantes_trabajando)
        solicitanteActivo = findViewById(R.id.solicitante_activo)
        solicitanteOcupacionActual = findViewById(R.id.solicitante_ocupacion_actual)
        solicitanteDesempleadoTiempo = findViewById(R.id.solicitante_desempleado_tiempo)
        solicitanteEmpresaPrevia = findViewById(R.id.solicitante_empresa_previa)
        solicitanteAntiguedadEmpleoAnterior = findViewById(R.id.solicitante_antiguedad_empleo_anterior)
        institucionTrabajoSolicitante = findViewById(R.id.institucion_trabajo_solicitante)
        actividadRemuneradaSolicitante = findViewById(R.id.actividad_remunerada_solicitante)
        contratoLaboralSolicitante = findViewById(R.id.contrato_laboral_solicitante)
        solicitanteIngresoMensual = findViewById(R.id.solicitante_ingreso_mensual)
        solicitanteEmpresa = findViewById(R.id.solicitante_empresa)
        solicitanteAntiguedad = findViewById(R.id.solicitante_antiguedad)
        comprobanteIngresosSolicitante = findViewById(R.id.comprobante_ingresos_solicitante)
        institucionCotizacionSolicitante = findViewById(R.id.institucion_cotizacion_solicitante)
        ingresoConceptosSolicitante = findViewById(R.id.ingreso_conceptos_solicitante)

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

    private fun cargarDatosSolicitante() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosSolicitante(idAcreditado)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { datos ->
                            registroExistente = true

                            hogarIntegrantesTrabajando.setText(datos.hogar_integrantes_trabajando)
                            solicitanteActivo.setText(datos.solicitante_activo)
                            solicitanteOcupacionActual.setText(datos.solicitante_ocupacion_actual)
                            solicitanteDesempleadoTiempo.setText(datos.solicitante_desempleado_tiempo)
                            solicitanteEmpresaPrevia.setText(datos.solicitante_empresa_previa)
                            solicitanteAntiguedadEmpleoAnterior.setText(datos.solicitante_antiguedad_trabajo_anterior)
                            institucionTrabajoSolicitante.setText(datos.institucion_trabajo_solicitante)
                            actividadRemuneradaSolicitante.setText(datos.actividad_remunerada_solicitante)
                            contratoLaboralSolicitante.setText(datos.contrato_laboral_solicitante)
                            solicitanteIngresoMensual.setText(datos.solicitante_ingreso_mensual)
                            solicitanteEmpresa.setText(datos.solicitante_empresa)
                            solicitanteAntiguedad.setText(datos.solicitante_antiguedad)
                            comprobanteIngresosSolicitante.setText(datos.comprobante_ingresos_solicitante)
                            institucionCotizacionSolicitante.setText(datos.institucion_cotizacion_solicitante)
                            ingresoConceptosSolicitante.setText(datos.ingresos_conceptos_solicitante)

                        } ?: run {
                            mostrarDialogo(
                                titulo = "Información",
                                mensaje = "No se encontraron datos del solicitante. Puede crear un nuevo registro.",
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
            hogarIntegrantesTrabajando to "Integrantes trabajando en el hogar",
            solicitanteActivo to "Estado laboral del solicitante",
            solicitanteOcupacionActual to "Ocupación actual del solicitante",
            solicitanteIngresoMensual to "Ingreso mensual del solicitante"
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
            hogarIntegrantesTrabajando to "Integrantes trabajando",
            solicitanteIngresoMensual to "Ingreso mensual",
            solicitanteAntiguedad to "Antigüedad laboral"
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

        // Validación condicional si está desempleado
        if (solicitanteActivo.text.toString().trim().equals("No", ignoreCase = true)) {
            if (solicitanteDesempleadoTiempo.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "Debe especificar el tiempo de desempleo",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                solicitanteDesempleadoTiempo.requestFocus()
                return false
            }
        }

        return true
    }

    private fun guardarOActualizarDatos() {
        val datos = datosSolicitante(
            hogar_integrantes_trabajando = hogarIntegrantesTrabajando.text.toString().trim(),
            solicitante_activo = solicitanteActivo.text.toString().trim(),
            solicitante_ocupacion_actual = solicitanteOcupacionActual.text.toString().trim(),
            solicitante_desempleado_tiempo = solicitanteDesempleadoTiempo.text.toString().trim(),
            solicitante_empresa_previa = solicitanteEmpresaPrevia.text.toString().trim(),
            solicitante_antiguedad_trabajo_anterior = solicitanteAntiguedadEmpleoAnterior.text.toString().trim(),
            institucion_trabajo_solicitante = institucionTrabajoSolicitante.text.toString().trim(),
            actividad_remunerada_solicitante = actividadRemuneradaSolicitante.text.toString().trim(),
            contrato_laboral_solicitante = contratoLaboralSolicitante.text.toString().trim(),
            solicitante_ingreso_mensual = solicitanteIngresoMensual.text.toString().trim(),
            solicitante_empresa = solicitanteEmpresa.text.toString().trim(),
            solicitante_antiguedad = solicitanteAntiguedad.text.toString().trim(),
            comprobante_ingresos_solicitante = comprobanteIngresosSolicitante.text.toString().trim(),
            institucion_cotizacion_solicitante = institucionCotizacionSolicitante.text.toString().trim(),
            ingresos_conceptos_solicitante = ingresoConceptosSolicitante.text.toString().trim(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = if (registroExistente) {
                    RetrofitClient.webService.actualizarDatosSolicitante(idAcreditado, datos)
                } else {
                    RetrofitClient.webService.agregarDatosSolicitante(datos)
                }

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { respuesta ->
                            if (respuesta.success) {
                                registroExistente = true
                                mostrarDialogo(
                                    titulo = "Éxito",
                                    mensaje = if (registroExistente)
                                        "Datos del solicitante actualizados correctamente"
                                    else
                                        "Datos del solicitante guardados correctamente",
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
        val intent = Intent(this, actualizarFormatoparte9::class.java).apply {
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