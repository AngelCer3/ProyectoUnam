package com.example.unamproject.actualizar

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import com.example.unamproject.R
import com.example.unamproject.RetrofitClient
import com.example.unamproject.datosDocumentos
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException

class actualizarFormatoparte15 : AppCompatActivity() {

    private lateinit var docCredencialCuenta: EditText
    private lateinit var docCredencialMostro: EditText
    private lateinit var docCredencialEntrego: EditText
    private lateinit var docPoderCuenta: EditText
    private lateinit var docPoderMostro: EditText
    private lateinit var docPoderEntrego: EditText
    private lateinit var docComprobanteCuenta: EditText
    private lateinit var docComprobanteMostro: EditText
    private lateinit var docComprobanteEntrego: EditText

    private lateinit var btnActualizar: Button
    private lateinit var btnSiguiente: Button

    private lateinit var idAcreditado: String
    private var idUsuario: String? = null
    private var registroExistente = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_formatoparte15)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        initViews()
        setupButtons()

        // Validar idAcreditado
        idAcreditado = intent.getStringExtra("id_acreditado") ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }
        idUsuario = intent.getStringExtra("id_usuario").takeIf { !it.isNullOrBlank() }

        cargarDatosDocumentos()
    }

    private fun initViews() {
        docCredencialCuenta = findViewById(R.id.doc_credencial_votar_cuenta)
        docCredencialMostro = findViewById(R.id.doc_credencial_votar_mostro)
        docCredencialEntrego = findViewById(R.id.doc_credencial_votar_entrego_copia)
        docPoderCuenta = findViewById(R.id.doc_poder_amplio_cuenta)
        docPoderMostro = findViewById(R.id.doc_poder_amplio_mostro)
        docPoderEntrego = findViewById(R.id.doc_poder_amplio_entrego_copia)
        docComprobanteCuenta = findViewById(R.id.doc_comprobante_ingresos_cuenta)
        docComprobanteMostro = findViewById(R.id.doc_comprobante_ingresos_mostro)
        docComprobanteEntrego = findViewById(R.id.doc_comprobante_ingresos_entrego_copia)

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
            if (registroExistente) {
                irASiguiente()
            } else {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarOActualizarDatosDocumentos() }
                )
            }
        }
    }

    private fun cargarDatosDocumentos() {
        lifecycleScope.launch {
            try {
                val response = RetrofitClient.webService.obtenerDatosDocumentos(idAcreditado)

                if (response.isSuccessful) {
                    response.body()?.let { datos ->
                        registroExistente = true

                        docCredencialCuenta.setText(datos.doc_credencial_votar_cuenta)
                        docCredencialMostro.setText(datos.doc_credencial_votar_mostro)
                        docCredencialEntrego.setText(datos.doc_credencial_votar_entrego_copia)
                        docPoderCuenta.setText(datos.doc_poder_amplio_cuenta)
                        docPoderMostro.setText(datos.doc_poder_amplio_mostro)
                        docPoderEntrego.setText(datos.doc_poder_amplio_entrego_copia)
                        docComprobanteCuenta.setText(datos.doc_comprobante_ingresos_cuenta)
                        docComprobanteMostro.setText(datos.doc_comprobante_ingresos_mostro)
                        docComprobanteEntrego.setText(datos.doc_comprobante_ingresos_entrego_copia)
                    } ?: run {
                        mostrarDialogo(
                            titulo = "Información",
                            mensaje = "No se encontraron datos de documentos. Puede crear un nuevo registro.",
                            iconoResId = android.R.drawable.ic_dialog_info,
                            colorTitulo = 0xFF1976D2.toInt()
                        )
                    }
                } else {
                    manejarErrorRespuesta(response.code(), response.errorBody()?.string())
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
        // Validar que los campos de "mostró" y "entregó" sean "Sí" o "No"
        val camposSiNo = listOf(
            Triple(docCredencialMostro, "Mostró credencial para votar", "doc_credencial_votar_mostro"),
            Triple(docCredencialEntrego, "Entregó copia credencial", "doc_credencial_votar_entrego_copia"),
            Triple(docPoderMostro, "Mostró poder notarial", "doc_poder_amplio_mostro"),
            Triple(docPoderEntrego, "Entregó copia poder", "doc_poder_amplio_entrego_copia"),
            Triple(docComprobanteMostro, "Mostró comprobante ingresos", "doc_comprobante_ingresos_mostro"),
            Triple(docComprobanteEntrego, "Entregó copia comprobante", "doc_comprobante_ingresos_entrego_copia")
        )

        for ((campo, nombreCampo, nombreTecnico) in camposSiNo) {
            val valor = campo.text.toString().trim()
            if (valor.isNotEmpty() && !valor.equals("Sí", ignoreCase = true) && !valor.equals("No", ignoreCase = true)) {
                campo.error = "Debe ser Sí o No"
                mostrarDialogo(
                    titulo = "Validación",
                    mensaje = "El campo '$nombreCampo' ($nombreTecnico) debe ser 'Sí' o 'No'",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                return false
            }
        }

        // Validar que si se mostró un documento, debe especificar si se cuenta con él
        val documentos = listOf(
            Triple(docCredencialMostro, docCredencialCuenta, "credencial para votar"),
            Triple(docPoderMostro, docPoderCuenta, "poder notarial"),
            Triple(docComprobanteMostro, docComprobanteCuenta, "comprobante de ingresos")
        )

        for ((mostro, cuenta, nombreDoc) in documentos) {
            if (mostro.text.toString().equals("Sí", ignoreCase = true) && cuenta.text.toString().isEmpty()) {
                cuenta.error = "Debe especificar"
                mostrarDialogo(
                    titulo = "Validación",
                    mensaje = "Si mostró el documento de $nombreDoc, debe especificar si cuenta con él",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                return false
            }
        }

        return true
    }

    private fun mostrarConfirmacionGuardado() {
        mostrarDialogo(
            titulo = "Confirmar",
            mensaje = "¿Está seguro que desea guardar los datos de documentos?",
            iconoResId = android.R.drawable.ic_dialog_alert,
            colorTitulo = 0xFFFFA000.toInt(),
            onAceptar = { guardarOActualizarDatosDocumentos() }
        )
    }

    private fun guardarOActualizarDatosDocumentos() {
        val datos = datosDocumentos(
            doc_credencial_votar_cuenta = docCredencialCuenta.text.toString().trim(),
            doc_credencial_votar_mostro = docCredencialMostro.text.toString().trim(),
            doc_credencial_votar_entrego_copia = docCredencialEntrego.text.toString().trim(),
            doc_poder_amplio_cuenta = docPoderCuenta.text.toString().trim(),
            doc_poder_amplio_mostro = docPoderMostro.text.toString().trim(),
            doc_poder_amplio_entrego_copia = docPoderEntrego.text.toString().trim(),
            doc_comprobante_ingresos_cuenta = docComprobanteCuenta.text.toString().trim(),
            doc_comprobante_ingresos_mostro = docComprobanteMostro.text.toString().trim(),
            doc_comprobante_ingresos_entrego_copia = docComprobanteEntrego.text.toString().trim(),
            id_acreditado = idAcreditado,
            id_usuario = idUsuario!!
        )

        lifecycleScope.launch {
            try {
                val response = if (registroExistente) {
                    RetrofitClient.webService.actualizarDatosDocumentos(idAcreditado, datos)
                } else {
                    RetrofitClient.webService.agregarDatosDocumentos(datos)
                }

                if (response.isSuccessful) {
                    response.body()?.let { respuesta ->
                        if (respuesta.success) {
                            registroExistente = true
                            mostrarDialogo(
                                titulo = "Éxito",
                                mensaje = if (registroExistente)
                                    "Datos de documentos actualizados correctamente"
                                else
                                    "Datos de documentos guardados correctamente",
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
            } catch (e: HttpException) {
                manejarErrorRespuesta(e.code(), e.message)
            } catch (e: IOException) {
                mostrarErrorConexion(e.message ?: "Error de red desconocido")
            } catch (e: Exception) {
                mostrarErrorInesperado(e.message ?: "Error desconocido")
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
        val intent = Intent(this, actualizarFormatoparte16::class.java).apply {
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