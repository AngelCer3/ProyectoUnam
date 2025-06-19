package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosFamiliares
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparte7 : AppCompatActivity() {

    private lateinit var familiaIntegrantes: EditText
    private lateinit var familiaTotalOcupantes: EditText
    private lateinit var familiaTipo: EditText
    private lateinit var edad0_5Hombres: EditText
    private lateinit var edad0_5Mujeres: EditText
    private lateinit var edad6_12Hombres: EditText
    private lateinit var edad6_12Mujeres: EditText
    private lateinit var edad13_18Hombres: EditText
    private lateinit var edad13_18Mujeres: EditText
    private lateinit var edad19_35Hombres: EditText
    private lateinit var edad19_35Mujeres: EditText
    private lateinit var edad36_59Hombres: EditText
    private lateinit var edad36_59Mujeres: EditText
    private lateinit var edad60MasHombres: EditText
    private lateinit var edad60MasMujeres: EditText
    private lateinit var escuelaAsistencia: EditText
    private lateinit var escolaridadNiveles: EditText
    private lateinit var familiaresEnfermedad: EditText
    private lateinit var familiaresEnfermedadCuantos: EditText
    private lateinit var familiaresEnfermedadQuien: EditText
    private lateinit var comprobanteEnfermedad: EditText
    private lateinit var tratamientoRecibido: EditText
    private lateinit var tratamientoLugar: EditText

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null
    private var registroExistente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte7)

        // Validar idAcreditado primero
        idAcreditado = intent.getStringExtra("id_acreditado") ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }

        idUsuario = intent.getStringExtra("id_usuario").takeIf { !it.isNullOrBlank() }

        initViews()
        cargarDatos()
        setupButtons()
    }

    private fun initViews() {
        familiaIntegrantes = findViewById(R.id.familia_integrantes)
        familiaTotalOcupantes = findViewById(R.id.familia_total_ocupantes)
        familiaTipo = findViewById(R.id.familia_tipo)
        edad0_5Hombres = findViewById(R.id.edades_0_5_hombres)
        edad0_5Mujeres = findViewById(R.id.edades_0_5_mujeres)
        edad6_12Hombres = findViewById(R.id.edades_6_12_hombres)
        edad6_12Mujeres = findViewById(R.id.edades_6_12_mujeres)
        edad13_18Hombres = findViewById(R.id.edades_13_18_hombres)
        edad13_18Mujeres = findViewById(R.id.edades_13_18_mujeres)
        edad19_35Hombres = findViewById(R.id.edades_19_35_hombres)
        edad19_35Mujeres = findViewById(R.id.edades_19_35_mujeres)
        edad36_59Hombres = findViewById(R.id.edades_36_59_hombres)
        edad36_59Mujeres = findViewById(R.id.edades_36_59_mujeres)
        edad60MasHombres = findViewById(R.id.edades_60_mas_hombres)
        edad60MasMujeres = findViewById(R.id.edades_60_mas_mujeres)
        escuelaAsistencia = findViewById(R.id.escuela_asistencia)
        escolaridadNiveles = findViewById(R.id.escolaridad_niveles)
        familiaresEnfermedad = findViewById(R.id.familiares_enfermedad)
        familiaresEnfermedadCuantos = findViewById(R.id.familiares_enfermedad_cuantos)
        familiaresEnfermedadQuien = findViewById(R.id.familiares_enfermedad_quien)
        comprobanteEnfermedad = findViewById(R.id.comprobante_enfermedad)
        tratamientoRecibido = findViewById(R.id.tratamiento_recibido)
        tratamientoLugar = findViewById(R.id.tratamiento_lugar)

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

    private fun cargarDatos() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosFamiliares(idAcreditado)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { datos ->
                            registroExistente = true

                            familiaIntegrantes.setText(datos.familia_integrantes)
                            familiaTotalOcupantes.setText(datos.familia_total_ocupantes)
                            familiaTipo.setText(datos.familia_tipo)
                            edad0_5Hombres.setText(datos.edad_0_5_hombres)
                            edad0_5Mujeres.setText(datos.edad_0_5_mujeres)
                            edad6_12Hombres.setText(datos.edad_6_12_hombres)
                            edad6_12Mujeres.setText(datos.edad_6_12_mujeres)
                            edad13_18Hombres.setText(datos.edad_13_18_hombres)
                            edad13_18Mujeres.setText(datos.edad_13_18_mujeres)
                            edad19_35Hombres.setText(datos.edad_19_35_hombres)
                            edad19_35Mujeres.setText(datos.edad_19_35_mujeres)
                            edad36_59Hombres.setText(datos.edad_36_59_hombres)
                            edad36_59Mujeres.setText(datos.edad_36_59_mujeres)
                            edad60MasHombres.setText(datos.edad_60_mas_hombres)
                            edad60MasMujeres.setText(datos.edad_60_mas_mujeres)
                            escuelaAsistencia.setText(datos.escuela_asistencia)
                            escolaridadNiveles.setText(datos.escolaridad_niveles)
                            familiaresEnfermedad.setText(datos.familiares_enfermedad)
                            familiaresEnfermedadCuantos.setText(datos.familiares_enfermedad_cuantos)
                            familiaresEnfermedadQuien.setText(datos.familiares_enfermedad_quien)
                            comprobanteEnfermedad.setText(datos.comprobante_enfermedad)
                            tratamientoRecibido.setText(datos.tratamiento_recibido)
                            tratamientoLugar.setText(datos.tratamiento_lugar)

                        } ?: run {
                            mostrarDialogo(
                                titulo = "Información",
                                mensaje = "No se encontraron datos familiares. Puede crear un nuevo registro.",
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
            familiaIntegrantes to "Número de integrantes familiares",
            familiaTotalOcupantes to "Total de ocupantes",
            familiaTipo to "Tipo de familia",
            escuelaAsistencia to "Asistencia a la escuela"
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

        // Validación de valores numéricos para edades
        val camposNumericos = listOf(
            edad0_5Hombres to "Hombres 0-5 años",
            edad0_5Mujeres to "Mujeres 0-5 años",
            edad6_12Hombres to "Hombres 6-12 años",
            edad6_12Mujeres to "Mujeres 6-12 años",
            edad13_18Hombres to "Hombres 13-18 años",
            edad13_18Mujeres to "Mujeres 13-18 años",
            edad19_35Hombres to "Hombres 19-35 años",
            edad19_35Mujeres to "Mujeres 19-35 años",
            edad36_59Hombres to "Hombres 36-59 años",
            edad36_59Mujeres to "Mujeres 36-59 años",
            edad60MasHombres to "Hombres 60+ años",
            edad60MasMujeres to "Mujeres 60+ años",
            familiaIntegrantes to "Integrantes familiares",
            familiaTotalOcupantes to "Total ocupantes"
        )

        for ((campo, nombre) in camposNumericos) {
            val valor = campo.text.toString().trim()
            if (valor.isNotEmpty() && !valor.matches(Regex("^\\d+$"))) {
                mostrarDialogo(
                    titulo = "Valor inválido",
                    mensaje = "El campo $nombre debe ser un número entero",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validación para campos de enfermedad si se indica que hay enfermos
        if (familiaresEnfermedad.text.toString().trim().equals("Sí", ignoreCase = true)) {
            if (familiaresEnfermedadCuantos.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "Debe especificar cuántos familiares tienen enfermedad",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                familiaresEnfermedadCuantos.requestFocus()
                return false
            }
        }

        return true
    }

    private fun guardarOActualizarDatos() {
        val datos = datosFamiliares(
            familia_integrantes = familiaIntegrantes.text.toString().trim(),
            familia_total_ocupantes = familiaTotalOcupantes.text.toString().trim(),
            familia_tipo = familiaTipo.text.toString().trim(),
            edad_0_5_hombres = edad0_5Hombres.text.toString().trim(),
            edad_0_5_mujeres = edad0_5Mujeres.text.toString().trim(),
            edad_6_12_hombres = edad6_12Hombres.text.toString().trim(),
            edad_6_12_mujeres = edad6_12Mujeres.text.toString().trim(),
            edad_13_18_hombres = edad13_18Hombres.text.toString().trim(),
            edad_13_18_mujeres = edad13_18Mujeres.text.toString().trim(),
            edad_19_35_hombres = edad19_35Hombres.text.toString().trim(),
            edad_19_35_mujeres = edad19_35Mujeres.text.toString().trim(),
            edad_36_59_hombres = edad36_59Hombres.text.toString().trim(),
            edad_36_59_mujeres = edad36_59Mujeres.text.toString().trim(),
            edad_60_mas_hombres = edad60MasHombres.text.toString().trim(),
            edad_60_mas_mujeres = edad60MasMujeres.text.toString().trim(),
            escuela_asistencia = escuelaAsistencia.text.toString().trim(),
            escolaridad_niveles = escolaridadNiveles.text.toString().trim(),
            familiares_enfermedad = familiaresEnfermedad.text.toString().trim(),
            familiares_enfermedad_cuantos = familiaresEnfermedadCuantos.text.toString().trim(),
            familiares_enfermedad_quien = familiaresEnfermedadQuien.text.toString().trim(),
            comprobante_enfermedad = comprobanteEnfermedad.text.toString().trim(),
            tratamiento_recibido = tratamientoRecibido.text.toString().trim(),
            tratamiento_lugar = tratamientoLugar.text.toString().trim(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = if (registroExistente) {
                    RetrofitClient.webService.actualizarDatosFamiliares(idAcreditado, datos)
                } else {
                    RetrofitClient.webService.agregarDatosFamiliares(datos)
                }

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { respuesta ->
                            if (respuesta.success) {
                                registroExistente = true
                                mostrarDialogo(
                                    titulo = "Éxito",
                                    mensaje = if (registroExistente)
                                        "Datos familiares actualizados correctamente"
                                    else
                                        "Datos familiares guardados correctamente",
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
        val intent = Intent(this, actualizarFormatoparte8::class.java).apply {
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