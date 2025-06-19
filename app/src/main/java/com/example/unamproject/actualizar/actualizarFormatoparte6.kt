package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosConyuge
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparte6 : AppCompatActivity() {

    private lateinit var nombre: EditText
    private lateinit var sexo: Spinner
    private lateinit var fechaNacimiento: EditText
    private lateinit var edad: EditText
    private lateinit var gradoEstudios: EditText
    private lateinit var compComputo: Spinner

    private lateinit var btnActualizarDatos: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null
    private var registroExistente = false

    private val opcionesSexo = listOf("Masculino", "Femenino", "Otro")
    private val opcionesComp = listOf("Ninguno", "Básico", "Intermedio", "Avanzado")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte6)

        // Validar idAcreditado primero
        idAcreditado = intent.getStringExtra("id_acreditado") ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }

        idUsuario = intent.getStringExtra("id_usuario").takeIf { !it.isNullOrBlank() }

        initViews()
        setupSpinners()
        cargarDatos()
        setupButtons()
    }

    private fun initViews() {
        nombre = findViewById(R.id.conyuge_nombre)
        sexo = findViewById(R.id.conyuge_sexo)
        fechaNacimiento = findViewById(R.id.conyuge_fecha_nacimiento)
        edad = findViewById(R.id.conyuge_edad)
        gradoEstudios = findViewById(R.id.conyuge_grado_estudios)
        compComputo = findViewById(R.id.conyuge_comp_computo)

        btnActualizarDatos = findViewById(R.id.btnActualizar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupSpinners() {
        sexo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcionesSexo)
        compComputo.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, opcionesComp)
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
                val response = RetrofitClient.webService.obtenerDatosConyuge(idAcreditado)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { datos ->
                            registroExistente = true

                            nombre.setText(datos.conyuge_nombre)
                            fechaNacimiento.setText(datos.conyuge_fecha_nacimiento)
                            edad.setText(datos.conyuge_edad)
                            gradoEstudios.setText(datos.conyuge_grado_estudios)

                            val adapterSexo = sexo.adapter as ArrayAdapter<String>
                            val posSexo = adapterSexo.getPosition(datos.conyuge_sexo)
                            if (posSexo >= 0) sexo.setSelection(posSexo)

                            val adapterComp = compComputo.adapter as ArrayAdapter<String>
                            val posComp = adapterComp.getPosition(datos.conyuge_comp_computo)
                            if (posComp >= 0) compComputo.setSelection(posComp)

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
            nombre to "Nombre del cónyuge",
            fechaNacimiento to "Fecha de nacimiento",
            edad to "Edad",
            gradoEstudios to "Grado de estudios"
        )

        for ((campo, nombreCampo) in camposRequeridos) {
            if (campo.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "El campo $nombreCampo es obligatorio",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validación de formato de fecha (DD/MM/AAAA)
        if (!fechaNacimiento.text.toString().trim().matches(Regex("\\d{2}/\\d{2}/\\d{4}"))) {
            mostrarDialogo(
                titulo = "Formato inválido",
                mensaje = "La fecha de nacimiento debe tener el formato DD/MM/AAAA",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            fechaNacimiento.requestFocus()
            return false
        }

        // Validación de edad (número entero positivo)
        val edadText = edad.text.toString().trim()
        if (!edadText.matches(Regex("^\\d+$")) || edadText.toInt() <= 0) {
            mostrarDialogo(
                titulo = "Edad inválida",
                mensaje = "La edad debe ser un número entero positivo",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            edad.requestFocus()
            return false
        }

        return true
    }

    private fun guardarOActualizarDatos() {
        val datos = datosConyuge(
            conyuge_nombre = nombre.text.toString().trim(),
            conyuge_sexo = sexo.selectedItem.toString(),
            conyuge_fecha_nacimiento = fechaNacimiento.text.toString().trim(),
            conyuge_edad = edad.text.toString().trim(),
            conyuge_grado_estudios = gradoEstudios.text.toString().trim(),
            conyuge_comp_computo = compComputo.selectedItem.toString(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = if (registroExistente) {
                    RetrofitClient.webService.actualizarDatosConyuge(idAcreditado, datos)
                } else {
                    RetrofitClient.webService.agregarDatosConyuge(datos)
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
        val intent = Intent(this, actualizarFormatoparte7::class.java).apply {
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