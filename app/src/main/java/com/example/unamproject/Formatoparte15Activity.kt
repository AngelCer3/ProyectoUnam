package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte15Activity : AppCompatActivity() {

    // Declaración de EditTexts
    private lateinit var docCredencialVotarCuenta: EditText
    private lateinit var docCredencialVotarMostro: EditText
    private lateinit var docCredencialVotarEntregoCopia: EditText
    private lateinit var docPoderAmplioCuenta: EditText
    private lateinit var docPoderAmplioMostro: EditText
    private lateinit var docPoderAmplioEntregoCopia: EditText
    private lateinit var docComprobanteIngresosCuenta: EditText
    private lateinit var docComprobanteIngresosMostro: EditText
    private lateinit var docComprobanteIngresosEntregoCopia: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button
    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte15)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Obtener el ID del acreditado del intent
        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        // Vincular vistas
        bindViews()

        btnGuardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatos()
            }
        }

        btnSiguiente.setOnClickListener {
            if (validarCampos()) {
                irSiguiente()
            }
        }
    }

    private fun bindViews() {
        docCredencialVotarCuenta = findViewById(R.id.doc_credencial_votar_cuenta)
        docCredencialVotarMostro = findViewById(R.id.doc_credencial_votar_mostro)
        docCredencialVotarEntregoCopia = findViewById(R.id.doc_credencial_votar_entrego_copia)
        docPoderAmplioCuenta = findViewById(R.id.doc_poder_amplio_cuenta)
        docPoderAmplioMostro = findViewById(R.id.doc_poder_amplio_mostro)
        docPoderAmplioEntregoCopia = findViewById(R.id.doc_poder_amplio_entrego_copia)
        docComprobanteIngresosCuenta = findViewById(R.id.doc_comprobante_ingresos_cuenta)
        docComprobanteIngresosMostro = findViewById(R.id.doc_comprobante_ingresos_mostro)
        docComprobanteIngresosEntregoCopia = findViewById(R.id.doc_comprobante_ingresos_entrego_copia)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun validarCampos(): Boolean {
        // Validar IDs
        if (idAcreditado.isNullOrBlank() || idUsuario.isNullOrBlank()) {
            mostrarDialogoValidacion(
                "Datos faltantes",
                "Faltan datos del acreditado o usuario",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            return false
        }

        // Validar documentos Credencial para Votar
        if (docCredencialVotarCuenta.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar si cuenta con Credencial para Votar",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            docCredencialVotarCuenta.requestFocus()
            return false
        }

        if (docCredencialVotarMostro.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar si mostró la Credencial para Votar",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            docCredencialVotarMostro.requestFocus()
            return false
        }

        if (docCredencialVotarEntregoCopia.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar si entregó copia de la Credencial para Votar",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            docCredencialVotarEntregoCopia.requestFocus()
            return false
        }

        // Validar documentos Poder Amplio
        if (docPoderAmplioCuenta.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar si cuenta con Poder Amplio",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            docPoderAmplioCuenta.requestFocus()
            return false
        }

        if (docPoderAmplioMostro.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar si mostró el Poder Amplio",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            docPoderAmplioMostro.requestFocus()
            return false
        }

        if (docPoderAmplioEntregoCopia.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar si entregó copia del Poder Amplio",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            docPoderAmplioEntregoCopia.requestFocus()
            return false
        }

        // Validar documentos Comprobante de Ingresos
        if (docComprobanteIngresosCuenta.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar si cuenta con Comprobante de Ingresos",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            docComprobanteIngresosCuenta.requestFocus()
            return false
        }

        if (docComprobanteIngresosMostro.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar si mostró el Comprobante de Ingresos",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            docComprobanteIngresosMostro.requestFocus()
            return false
        }

        if (docComprobanteIngresosEntregoCopia.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar si entregó copia del Comprobante de Ingresos",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            docComprobanteIngresosEntregoCopia.requestFocus()
            return false
        }

        return true
    }

    private fun guardarDatos() {
        val datos = datosDocumentos(
            doc_credencial_votar_cuenta = docCredencialVotarCuenta.text.toString(),
            doc_credencial_votar_mostro = docCredencialVotarMostro.text.toString(),
            doc_credencial_votar_entrego_copia = docCredencialVotarEntregoCopia.text.toString(),
            doc_poder_amplio_cuenta = docPoderAmplioCuenta.text.toString(),
            doc_poder_amplio_mostro = docPoderAmplioMostro.text.toString(),
            doc_poder_amplio_entrego_copia = docPoderAmplioEntregoCopia.text.toString(),
            doc_comprobante_ingresos_cuenta = docComprobanteIngresosCuenta.text.toString(),
            doc_comprobante_ingresos_mostro = docComprobanteIngresosMostro.text.toString(),
            doc_comprobante_ingresos_entrego_copia = docComprobanteIngresosEntregoCopia.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosDocumentos(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        mostrarDialogoValidacion(
                            "Éxito",
                            "Datos de documentos guardados correctamente",
                            android.R.drawable.ic_dialog_info,
                            0xFF388E3C.toInt()
                        )
                    } else {
                        mostrarDialogoValidacion(
                            "Error",
                            "Error al guardar los datos: ${response.message()}",
                            android.R.drawable.stat_notify_error,
                            0xFFD32F2F.toInt()
                        )
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarDialogoValidacion(
                        "Error de conexión",
                        "No se pudo conectar al servidor: ${e.message}",
                        android.R.drawable.stat_notify_error,
                        0xFFD32F2F.toInt()
                    )
                }
            }
        }
    }

    private fun irSiguiente() {
        val intent = Intent(this, Formatoparte16Activity::class.java)
        intent.putExtra("id_acreditado", idAcreditado)
        intent.putExtra("id_usuario", idUsuario)
        startActivity(intent)
    }

    private fun mostrarDialogoValidacion(
        titulo: String,
        mensaje: String,
        iconoResId: Int,
        colorTitulo: Int,
        onAceptar: (() -> Unit)? = null
    ) {
        val view = layoutInflater.inflate(R.layout.custom_alert_dialog, null)

        val icon = view.findViewById<ImageView>(R.id.ivIcon)
        val title = view.findViewById<TextView>(R.id.tvTitle)
        val message = view.findViewById<TextView>(R.id.tvMessage)
        val btnOk = view.findViewById<Button>(R.id.btnOk)

        icon.setImageResource(iconoResId)
        icon.setColorFilter(colorTitulo)
        title.text = titulo
        title.setTextColor(colorTitulo)
        message.text = mensaje

        val dialog = AlertDialog.Builder(this)
            .setView(view)
            .setCancelable(false)
            .create()

        btnOk.setOnClickListener {
            dialog.dismiss()
            onAceptar?.invoke()
        }

        dialog.show()
    }
}