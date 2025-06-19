package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.identificarAcreditado
import kotlinx.coroutines.*
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparte1 : AppCompatActivity() {

    private lateinit var entidadFederativa: EditText
    private lateinit var ciudadMunicipioDelegacion: EditText
    private lateinit var apellidoPaterno: EditText
    private lateinit var apellidoMaterno: EditText
    private lateinit var nombres: EditText
    private lateinit var calle: EditText
    private lateinit var condominio: EditText
    private lateinit var it: EditText
    private lateinit var mz: EditText
    private lateinit var noExt: EditText
    private lateinit var noInt: EditText
    private lateinit var edif: EditText
    private lateinit var colonia: EditText
    private lateinit var cp: EditText
    private lateinit var curp: EditText

    private lateinit var acreditado: identificarAcreditado
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte1)

        // Validar idUsuario primero
        idUsuario = intent.getStringExtra("id_usuario").takeIf { !it.isNullOrBlank() }
            ?: run {
                mostrarErrorYCerrar("No se recibió el ID de usuario")
                return
            }

        // Obtener datos del acreditado
        acreditado = intent.getSerializableExtra("acreditado") as? identificarAcreditado ?: run {
            mostrarErrorYCerrar("No se recibieron los datos del acreditado")
            return
        }

        initViews()
        cargarDatos()
        setupButtons()
    }

    private fun initViews() {
        entidadFederativa = findViewById(R.id.entidad_federativa)
        ciudadMunicipioDelegacion = findViewById(R.id.ciudad_municipio_delegacion)
        apellidoPaterno = findViewById(R.id.apellido_paterno)
        apellidoMaterno = findViewById(R.id.apellido_materno)
        nombres = findViewById(R.id.nombres)
        calle = findViewById(R.id.domicilio_calle)
        condominio = findViewById(R.id.domicilio_condominio)
        it = findViewById(R.id.domicilio_it)
        mz = findViewById(R.id.domicilio_mz)
        noExt = findViewById(R.id.domicilio_no_ext)
        noInt = findViewById(R.id.domicilio_no_int)
        edif = findViewById(R.id.domicilio_edif)
        colonia = findViewById(R.id.domicilio_colonia)
        cp = findViewById(R.id.domicilio_cp)
        curp = findViewById(R.id.domicilio_curp)
    }

    private fun cargarDatos() {
        entidadFederativa.setText(acreditado.entidad_federativa)
        ciudadMunicipioDelegacion.setText(acreditado.ciudad_municipio_delegacion)
        apellidoPaterno.setText(acreditado.apellido_paterno)
        apellidoMaterno.setText(acreditado.apellido_materno)
        nombres.setText(acreditado.nombres)
        calle.setText(acreditado.domicilio_calle)
        condominio.setText(acreditado.domicilio_condominio)
        it.setText(acreditado.domicilio_it)
        mz.setText(acreditado.domicilio_mz)
        noExt.setText(acreditado.domicilio_no_ext)
        noInt.setText(acreditado.domicilio_no_int)
        edif.setText(acreditado.domicilio_edif)
        colonia.setText(acreditado.domicilio_colonia)
        cp.setText(acreditado.domicilio_cp)
        curp.setText(acreditado.domicilio_curp)
    }

    private fun setupButtons() {
        findViewById<Button>(R.id.btnActualizarDatos).setOnClickListener {
            if (validarCampos()) {
                actualizarDatos()
            }
        }

        findViewById<Button>(R.id.btnSiguiente).setOnClickListener {
            navegarAParte2()
        }
    }

    private fun validarCampos(): Boolean {
        val camposRequeridos = listOf(
            entidadFederativa to "Entidad Federativa",
            ciudadMunicipioDelegacion to "Ciudad/Municipio/Delegación",
            apellidoPaterno to "Apellido Paterno",
            nombres to "Nombres",
            calle to "Domicilio - Calle",
            colonia to "Domicilio - Colonia",
            cp to "Domicilio - Código Postal",
            curp to "CURP"
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
        if (!cp.text.toString().trim().matches(Regex("\\d{5}"))) {
            mostrarDialogo(
                titulo = "Código Postal inválido",
                mensaje = "El código postal debe contener exactamente 5 dígitos",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            cp.requestFocus()
            return false
        }

        // Validación específica para CURP (18 caracteres)
        val curpText = curp.text.toString().trim()
        if (curpText.length != 18) {
            mostrarDialogo(
                titulo = "CURP inválido",
                mensaje = "El CURP debe contener exactamente 18 caracteres",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            curp.requestFocus()
            return false
        }

        return true
    }

    private fun actualizarDatos() {
        val acreditadoActualizado = identificarAcreditado(
            id_acreditado = acreditado.id_acreditado,
            entidad_federativa = entidadFederativa.text.toString().trim(),
            ciudad_municipio_delegacion = ciudadMunicipioDelegacion.text.toString().trim(),
            apellido_paterno = apellidoPaterno.text.toString().trim(),
            apellido_materno = apellidoMaterno.text.toString().trim(),
            nombres = nombres.text.toString().trim(),
            domicilio_calle = calle.text.toString().trim(),
            domicilio_condominio = condominio.text.toString().trim(),
            domicilio_it = it.text.toString().trim(),
            domicilio_mz = mz.text.toString().trim(),
            domicilio_no_ext = noExt.text.toString().trim(),
            domicilio_no_int = noInt.text.toString().trim(),
            domicilio_edif = edif.text.toString().trim(),
            domicilio_colonia = colonia.text.toString().trim(),
            domicilio_cp = cp.text.toString().trim(),
            domicilio_curp = curp.text.toString().trim(),
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.actualizarAcreditado(
                    acreditado.id_acreditado.toString(),
                    acreditadoActualizado
                )

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        response.body()?.let { respuesta ->
                            if (respuesta.success) {
                                acreditado = acreditadoActualizado
                                mostrarDialogo(
                                    titulo = "Éxito",
                                    mensaje = "Datos actualizados correctamente",
                                    iconoResId = android.R.drawable.ic_dialog_info,
                                    colorTitulo = 0xFF388E3C.toInt()
                                )
                            } else {
                                mostrarErrorServidor("El servidor no pudo procesar la actualización")
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
            404 -> mostrarDialogo(
                titulo = "No encontrado",
                mensaje = "El registro a actualizar no fue encontrado",
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

    private fun navegarAParte2() {
        val intent = Intent(this, actualizarFormatoparte2::class.java).apply {
            putExtra("id_acreditado", acreditado.id_acreditado)
            putExtra("id_usuario", acreditado.id_usuario)
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