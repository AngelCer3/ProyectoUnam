package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.DatosReestructura
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparte5 : AppCompatActivity() {

    // Campos de entrada
    private lateinit var motivo: EditText
    private lateinit var documento: EditText
    private lateinit var tipoDocumento: EditText
    private lateinit var solicitanteEsAcred: EditText
    private lateinit var solicitanteNombre: EditText
    private lateinit var parentesco: EditText
    private lateinit var motivoPersonal: EditText
    private lateinit var sexo: EditText
    private lateinit var fechaNacimiento: EditText
    private lateinit var edad: EditText
    private lateinit var lugarNacimiento: EditText
    private lateinit var gradoEstudios: EditText
    private lateinit var conocimientoComp: EditText
    private lateinit var discapacidad: EditText
    private lateinit var dictamen: EditText
    private lateinit var institucionDictamen: EditText
    private lateinit var fechaDictamen: EditText
    private lateinit var porcentajeDiscapacidad: EditText
    private lateinit var estadoCivil: EditText
    private lateinit var fechaEstadoCivil: EditText
    private lateinit var exesposoAportacion: EditText
    private lateinit var exesposoMonto: EditText
    private lateinit var regimenConyugal: EditText
    private lateinit var viveConConyuge: EditText
    private lateinit var fechaNoConvive: EditText

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null
    private var registroExistente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte5)

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
        motivo = findViewById(R.id.reestructura_motivo)
        documento = findViewById(R.id.reestructura_documento)
        tipoDocumento = findViewById(R.id.reestructura_tipo_documento)
        solicitanteEsAcred = findViewById(R.id.reestructura_solicitante_es_acred)
        solicitanteNombre = findViewById(R.id.reestructura_solicitante_nombre)
        parentesco = findViewById(R.id.reestructura_parentesco)
        motivoPersonal = findViewById(R.id.reestructura_motivo_personal)
        sexo = findViewById(R.id.reestructura_sexo)
        fechaNacimiento = findViewById(R.id.reestructura_fecha_nacimiento)
        edad = findViewById(R.id.reestructura_edad)
        lugarNacimiento = findViewById(R.id.reestructura_lugar_nacimiento)
        gradoEstudios = findViewById(R.id.reestructura_grado_estudios)
        conocimientoComp = findViewById(R.id.reestructura_conocimiento_comp)
        discapacidad = findViewById(R.id.reestructura_discapacidad)
        dictamen = findViewById(R.id.reestructura_dictamen)
        institucionDictamen = findViewById(R.id.reestructura_institucion_dictamen)
        fechaDictamen = findViewById(R.id.reestructura_fecha_dictamen)
        porcentajeDiscapacidad = findViewById(R.id.reestructura_porcentaje_discapacidad)
        estadoCivil = findViewById(R.id.reestructura_estado_civil)
        fechaEstadoCivil = findViewById(R.id.reestructura_fecha_estado_civil)
        exesposoAportacion = findViewById(R.id.reestructura_exesposo_aportacion)
        exesposoMonto = findViewById(R.id.reestructura_exesposo_monto)
        regimenConyugal = findViewById(R.id.reestructura_regimen_conyugal)
        viveConConyuge = findViewById(R.id.reestructura_vive_con_conyuge)
        fechaNoConvive = findViewById(R.id.reestructura_fecha_no_convive)

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
                navegarASiguienteParte()
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
                val response = RetrofitClient.webService.obtenerReestructura(idAcreditado)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { data ->
                            registroExistente = true

                            motivo.setText(data.reestructura_motivo)
                            documento.setText(data.reestructura_documento)
                            tipoDocumento.setText(data.reestructura_tipo_documento)
                            solicitanteEsAcred.setText(data.reestructura_solicitante_es_acred)
                            solicitanteNombre.setText(data.reestructura_solicitante_nombre)
                            parentesco.setText(data.reestructura_parentesco)
                            motivoPersonal.setText(data.reestructura_motivo_personal)
                            sexo.setText(data.reestructura_sexo)
                            fechaNacimiento.setText(data.reestructura_fecha_nacimiento)
                            edad.setText(data.reestructura_edad)
                            lugarNacimiento.setText(data.reestructura_lugar_nacimiento)
                            gradoEstudios.setText(data.reestructura_grado_estudios)
                            conocimientoComp.setText(data.reestructura_conocimiento_comp)
                            discapacidad.setText(data.reestructura_discapacidad)
                            dictamen.setText(data.reestructura_dictamen)
                            institucionDictamen.setText(data.reestructura_institucion_dictamen)
                            fechaDictamen.setText(data.reestructura_fecha_dictamen)
                            porcentajeDiscapacidad.setText(data.reestructura_porcentaje_discapacidad)
                            estadoCivil.setText(data.reestructura_estado_civil)
                            fechaEstadoCivil.setText(data.reestructura_fecha_estado_civil)
                            exesposoAportacion.setText(data.reestructura_exesposo_aportacion)
                            exesposoMonto.setText(data.reestructura_exesposo_monto)
                            regimenConyugal.setText(data.reestructura_regimen_conyugal)
                            viveConConyuge.setText(data.reestructura_vive_con_conyuge)
                            fechaNoConvive.setText(data.reestructura_fecha_no_convive)

                        } ?: run {
                            mostrarDialogo(
                                titulo = "Información",
                                mensaje = "No se encontraron datos de reestructura. Puede crear un nuevo registro.",
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
            motivo to "Motivo de reestructura",
            documento to "Documento",
            tipoDocumento to "Tipo de documento",
            solicitanteEsAcred to "¿El solicitante es el acreditado?",
            fechaNacimiento to "Fecha de nacimiento",
            edad to "Edad",
            estadoCivil to "Estado civil"
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

        // Validación de fechas (DD/MM/AAAA)
        val fechas = listOf(
            fechaNacimiento to "Fecha de nacimiento",
            fechaDictamen to "Fecha de dictamen",
            fechaEstadoCivil to "Fecha de estado civil",
            fechaNoConvive to "Fecha de no convivencia"
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

        // Validación de edad y porcentaje (números)
        val numeros = listOf(
            edad to "Edad",
            porcentajeDiscapacidad to "Porcentaje de discapacidad",
            exesposoMonto to "Monto de aportación del exesposo"
        )

        for ((campo, nombre) in numeros) {
            val valor = campo.text.toString().trim()
            if (valor.isNotEmpty() && !valor.matches(Regex("^\\d+$"))) {
                mostrarDialogo(
                    titulo = "Formato inválido",
                    mensaje = "El campo $nombre debe ser un número entero",
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
        val datos = DatosReestructura(
            reestructura_motivo = motivo.text.toString().trim(),
            reestructura_documento = documento.text.toString().trim(),
            reestructura_tipo_documento = tipoDocumento.text.toString().trim(),
            reestructura_solicitante_es_acred = solicitanteEsAcred.text.toString().trim(),
            reestructura_solicitante_nombre = solicitanteNombre.text.toString().trim(),
            reestructura_parentesco = parentesco.text.toString().trim(),
            reestructura_motivo_personal = motivoPersonal.text.toString().trim(),
            reestructura_sexo = sexo.text.toString().trim(),
            reestructura_fecha_nacimiento = fechaNacimiento.text.toString().trim(),
            reestructura_edad = edad.text.toString().trim(),
            reestructura_lugar_nacimiento = lugarNacimiento.text.toString().trim(),
            reestructura_grado_estudios = gradoEstudios.text.toString().trim(),
            reestructura_conocimiento_comp = conocimientoComp.text.toString().trim(),
            reestructura_discapacidad = discapacidad.text.toString().trim(),
            reestructura_dictamen = dictamen.text.toString().trim(),
            reestructura_institucion_dictamen = institucionDictamen.text.toString().trim(),
            reestructura_fecha_dictamen = fechaDictamen.text.toString().trim(),
            reestructura_porcentaje_discapacidad = porcentajeDiscapacidad.text.toString().trim(),
            reestructura_estado_civil = estadoCivil.text.toString().trim(),
            reestructura_fecha_estado_civil = fechaEstadoCivil.text.toString().trim(),
            reestructura_exesposo_aportacion = exesposoAportacion.text.toString().trim(),
            reestructura_exesposo_monto = exesposoMonto.text.toString().trim(),
            reestructura_regimen_conyugal = regimenConyugal.text.toString().trim(),
            reestructura_vive_con_conyuge = viveConConyuge.text.toString().trim(),
            reestructura_fecha_no_convive = fechaNoConvive.text.toString().trim(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = if (registroExistente) {
                    RetrofitClient.webService.actualizarDatosReestructura(idAcreditado, datos)
                } else {
                    RetrofitClient.webService.agregarDatosReestrucutra(datos)
                }

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { respuesta ->
                            if (respuesta.success) {
                                registroExistente = true
                                mostrarDialogo(
                                    titulo = "Éxito",
                                    mensaje = if (registroExistente)
                                        "Datos de reestructura actualizados correctamente"
                                    else
                                        "Datos de reestructura guardados correctamente",
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

    private fun navegarASiguienteParte() {
        val intent = Intent(this, actualizarFormatoparte6::class.java).apply {
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