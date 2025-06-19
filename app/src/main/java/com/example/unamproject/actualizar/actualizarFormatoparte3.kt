package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosVivienda
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparte3 : AppCompatActivity() {

    private lateinit var viviendaLocalizada: EditText
    private lateinit var viviendaHabitada: EditText
    private lateinit var verificacionMetodo: EditText
    private lateinit var verificacionOtro: EditText
    private lateinit var vecinoNombre: EditText
    private lateinit var vecinoDireccion: EditText
    private lateinit var acreditadoVive: EditText
    private lateinit var jefeFamiliaNombre: EditText
    private lateinit var jefeFamiliaRelacion: EditText
    private lateinit var fechaOcupacion: EditText
    private lateinit var situacionVivienda: Spinner
    private lateinit var documentoTraspaso: EditText
    private lateinit var tipoDocumentoTraspaso: EditText
    private lateinit var documentoMostrado: EditText
    private lateinit var documentoCopiaEntregada: EditText

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null
    private var registroExistente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte3)

        // Validar idAcreditado primero
        idAcreditado = intent.getStringExtra("id_acreditado") ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }

        idUsuario = intent.getStringExtra("id_usuario").takeIf { !it.isNullOrBlank() }

        initViews()
        setupSpinner()
        cargarDatosPrevios()
        setupButtons()
    }

    private fun initViews() {
        viviendaLocalizada = findViewById(R.id.vivienda_localizada)
        viviendaHabitada = findViewById(R.id.vivienda_habitada)
        verificacionMetodo = findViewById(R.id.verificacion_metodo)
        verificacionOtro = findViewById(R.id.verificacion_otro)
        vecinoNombre = findViewById(R.id.vecino_nombre)
        vecinoDireccion = findViewById(R.id.vecino_direccion)
        acreditadoVive = findViewById(R.id.acreditado_vive)
        jefeFamiliaNombre = findViewById(R.id.jefe_familia_nombre)
        jefeFamiliaRelacion = findViewById(R.id.jefe_familia_relacion)
        fechaOcupacion = findViewById(R.id.fecha_ocupacion)
        situacionVivienda = findViewById(R.id.situacion_vivienda)
        documentoTraspaso = findViewById(R.id.documento_traspaso)
        tipoDocumentoTraspaso = findViewById(R.id.tipo_documento_traspaso)
        documentoMostrado = findViewById(R.id.documento_mostrado)
        documentoCopiaEntregada = findViewById(R.id.documento_copia_entregada)

        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupSpinner() {
        val opciones = resources.getStringArray(R.array.situacion_vivienda_opciones)
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opciones)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        situacionVivienda.adapter = adapter
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

    private fun cargarDatosPrevios() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.obtenerVivienda(idAcreditado)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { vivienda ->
                            registroExistente = true

                            viviendaLocalizada.setText(vivienda.vivienda_localizada)
                            viviendaHabitada.setText(vivienda.vivienda_habitada)
                            verificacionMetodo.setText(vivienda.verificacion_metodo)
                            verificacionOtro.setText(vivienda.verificacion_otro)
                            vecinoNombre.setText(vivienda.vecino_nombre)
                            vecinoDireccion.setText(vivienda.vecino_direccion)
                            acreditadoVive.setText(vivienda.acreditado_vive)
                            jefeFamiliaNombre.setText(vivienda.jefe_familia_nombre)
                            jefeFamiliaRelacion.setText(vivienda.jefe_familia_relacion)
                            fechaOcupacion.setText(vivienda.fecha_ocupacion)

                            val index = (situacionVivienda.adapter as ArrayAdapter<String>)
                                .getPosition(vivienda.situacion_vivienda)
                            if (index >= 0) situacionVivienda.setSelection(index)

                            documentoTraspaso.setText(vivienda.documento_traspaso)
                            tipoDocumentoTraspaso.setText(vivienda.tipo_documento_traspaso)
                            documentoMostrado.setText(vivienda.documento_mostrado)
                            documentoCopiaEntregada.setText(vivienda.documento_copia_entregada)

                        } ?: run {
                            mostrarDialogo(
                                titulo = "Información",
                                mensaje = "No se encontraron datos de vivienda. Puede crear un nuevo registro.",
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
            viviendaLocalizada to "Vivienda localizada",
            viviendaHabitada to "Vivienda habitada",
            verificacionMetodo to "Método de verificación",
            acreditadoVive to "¿El acreditado vive aquí?",
            jefeFamiliaNombre to "Nombre del jefe de familia",
            situacionVivienda to "Situación de la vivienda"
        )

        for ((campo, nombre) in camposRequeridos) {
            if (campo is EditText && campo.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "El campo $nombre es obligatorio",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            } else if (campo is Spinner && campo.selectedItem.toString().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "Debe seleccionar una opción para $nombre",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                return false
            }
        }

        // Validación específica para fecha de ocupación (si está presente)
        if (!fechaOcupacion.text.isNullOrBlank() && !fechaOcupacion.text.toString().matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) {
            mostrarDialogo(
                titulo = "Formato inválido",
                mensaje = "La fecha de ocupación debe tener el formato DD/MM/AAAA",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            fechaOcupacion.requestFocus()
            return false
        }

        return true
    }

    private fun guardarOActualizarDatos() {
        val vivienda = datosVivienda(
            vivienda_localizada = viviendaLocalizada.text.toString().trim(),
            vivienda_habitada = viviendaHabitada.text.toString().trim(),
            verificacion_metodo = verificacionMetodo.text.toString().trim(),
            verificacion_otro = verificacionOtro.text.toString().trim(),
            vecino_nombre = vecinoNombre.text.toString().trim(),
            vecino_direccion = vecinoDireccion.text.toString().trim(),
            acreditado_vive = acreditadoVive.text.toString().trim(),
            jefe_familia_nombre = jefeFamiliaNombre.text.toString().trim(),
            jefe_familia_relacion = jefeFamiliaRelacion.text.toString().trim(),
            fecha_ocupacion = fechaOcupacion.text.toString().trim(),
            situacion_vivienda = situacionVivienda.selectedItem.toString(),
            documento_traspaso = documentoTraspaso.text.toString().trim(),
            tipo_documento_traspaso = tipoDocumentoTraspaso.text.toString().trim(),
            documento_mostrado = documentoMostrado.text.toString().trim(),
            documento_copia_entregada = documentoCopiaEntregada.text.toString().trim(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = if (registroExistente) {
                    RetrofitClient.webService.actualizarDatosVivienda(idAcreditado, vivienda)
                } else {
                    RetrofitClient.webService.agregarDatosVivienda(vivienda)
                }

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { respuesta ->
                            if (respuesta.success) {
                                registroExistente = true
                                mostrarDialogo(
                                    titulo = if (registroExistente) "Actualizado" else "Guardado",
                                    mensaje = if (registroExistente)
                                        "Datos de vivienda actualizados correctamente"
                                    else
                                        "Datos de vivienda guardados correctamente",
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
        val intent = Intent(this, actualizarFormatoparte4::class.java).apply {
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