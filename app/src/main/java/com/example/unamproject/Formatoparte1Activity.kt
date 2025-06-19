package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.*
import android.view.LayoutInflater
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import retrofit2.HttpException
import java.io.IOException

class Formatoparte1Activity : AppCompatActivity() {

    private lateinit var siguiente: Button
    private lateinit var guardar: Button

    private lateinit var entidadFederativa: EditText
    private lateinit var ciudadMunicipio: EditText
    private lateinit var apellidoPaterno: EditText
    private lateinit var apellidoMaterno: EditText
    private lateinit var nombres: EditText
    private lateinit var domicilioCalle: EditText
    private lateinit var domicilioCondominio: EditText
    private lateinit var domicilioIT: EditText
    private lateinit var domicilioMz: EditText
    private lateinit var domicilioNoExt: EditText
    private lateinit var domicilioNoInt: EditText
    private lateinit var domicilioEdif: EditText
    private lateinit var domicilioColonia: EditText
    private lateinit var domicilioCp: EditText
    private lateinit var domicilioCurp: EditText

    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte1)

        // Validar idUsuario primero
        idUsuario = intent.getStringExtra("id_usuario").takeIf { !it.isNullOrBlank() }
            ?: run {
                mostrarErrorYCerrar("No se recibió el ID de usuario")
                return
            }

        initViews()
        setupButtons()
    }

    private fun initViews() {
        entidadFederativa = findViewById(R.id.entidad_federativa)
        ciudadMunicipio = findViewById(R.id.ciudad_municipio_delegacion)
        apellidoPaterno = findViewById(R.id.apellido_paterno)
        apellidoMaterno = findViewById(R.id.apellido_materno)
        nombres = findViewById(R.id.nombres)
        domicilioCalle = findViewById(R.id.domicilio_calle)
        domicilioCondominio = findViewById(R.id.domicilio_condominio)
        domicilioIT = findViewById(R.id.domicilio_it)
        domicilioMz = findViewById(R.id.domicilio_mz)
        domicilioNoExt = findViewById(R.id.domicilio_no_ext)
        domicilioNoInt = findViewById(R.id.domicilio_no_int)
        domicilioEdif = findViewById(R.id.domicilio_edif)
        domicilioColonia = findViewById(R.id.domicilio_colonia)
        domicilioCp = findViewById(R.id.domicilio_cp)
        domicilioCurp = findViewById(R.id.domicilio_curp)

        siguiente = findViewById(R.id.btnSiguiente)
        guardar = findViewById(R.id.btnGuardarDatos)
    }

    private fun setupButtons() {
        guardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatos()
            }
        }

        siguiente.setOnClickListener {
            if (idAcreditado == null) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarDatos() }
                )
            } else {
                irSiguienteFormato()
            }
        }
    }

    private fun validarCampos(): Boolean {
        val camposRequeridos = listOf(
            entidadFederativa to "Entidad Federativa",
            ciudadMunicipio to "Ciudad/Municipio/Delegación",
            apellidoPaterno to "Apellido Paterno",
            nombres to "Nombres",
            domicilioCalle to "Domicilio - Calle",
            domicilioColonia to "Domicilio - Colonia",
            domicilioCp to "Domicilio - Código Postal",
            domicilioCurp to "CURP"
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

        // Validación específica para Código Postal (5 dígitos)
        if (!domicilioCp.text.toString().trim().matches(Regex("\\d{5}"))) {
            mostrarDialogo(
                titulo = "Código Postal inválido",
                mensaje = "El código postal debe contener exactamente 5 dígitos",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            domicilioCp.requestFocus()
            return false
        }

        // Validación específica para CURP (18 caracteres)
        val curp = domicilioCurp.text.toString().trim()
        if (curp.length != 18) {
            mostrarDialogo(
                titulo = "CURP inválido",
                mensaje = "El CURP debe contener exactamente 18 caracteres",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            domicilioCurp.requestFocus()
            return false
        }

        return true
    }

    private fun guardarDatos() {
        val acreditado = Acreditado(
            entidad_federativa = entidadFederativa.text.toString().trim(),
            ciudad_municipio_delegacion = ciudadMunicipio.text.toString().trim(),
            apellido_paterno = apellidoPaterno.text.toString().trim(),
            apellido_materno = apellidoMaterno.text.toString().trim(),
            nombres = nombres.text.toString().trim(),
            domicilio_calle = domicilioCalle.text.toString().trim(),
            domicilio_condominio = domicilioCondominio.text.toString().trim(),
            domicilio_it = domicilioIT.text.toString().trim(),
            domicilio_mz = domicilioMz.text.toString().trim(),
            domicilio_no_ext = domicilioNoExt.text.toString().trim(),
            domicilio_no_int = domicilioNoInt.text.toString().trim(),
            domicilio_edif = domicilioEdif.text.toString().trim(),
            domicilio_colonia = domicilioColonia.text.toString().trim(),
            domicilio_cp = domicilioCp.text.toString().trim(),
            domicilio_curp = domicilioCurp.text.toString().trim(),
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarAcreditado(acreditado)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { respuesta ->
                            if (respuesta.success) {
                                idAcreditado = respuesta.id_acreditado
                                mostrarDialogo(
                                    titulo = "Éxito",
                                    mensaje = "Datos guardados correctamente",
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
            500 -> mostrarErrorServidor("Error interno del servidor (500): ${mensajeError ?: "sin detalles"}")
            400 -> mostrarDialogo(
                titulo = "Datos inválidos",
                mensaje = "Verifica la información ingresada: ${mensajeError ?: "Error 400"}",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
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

    private fun irSiguienteFormato() {
        val intent = Intent(this, Formatoparte2Activity::class.java).apply {
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