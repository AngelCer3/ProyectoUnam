package com.example.unamproject.SinConexion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.unamproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte15SinConexion : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L
    private var datosGuardados = false

    // EditTexts para Credencial para Votar
    private lateinit var docCredencialVotarCuentaEt: EditText
    private lateinit var docCredencialVotarMostroEt: EditText
    private lateinit var docCredencialVotarEntregoCopiaEt: EditText

    // EditTexts para Poder Amplio
    private lateinit var docPoderAmplioCuentaEt: EditText
    private lateinit var docPoderAmplioMostroEt: EditText
    private lateinit var docPoderAmplioEntregoCopiaEt: EditText

    // EditTexts para Comprobante de Ingresos
    private lateinit var docComprobanteIngresosCuentaEt: EditText
    private lateinit var docComprobanteIngresosMostroEt: EditText
    private lateinit var docComprobanteIngresosEntregoCopiaEt: EditText

    // Botones
    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte15_sin_conexion)

        // Obtener ID del acreditado
        idAcreditado = intent.getStringExtra("id_acreditado")?.toLongOrNull() ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }

        // Inicializar base de datos
        database = AppDatabase.getDatabase(this)

        initViews()
        setupButtons()
    }

    private fun initViews() {
        // Inicializar EditTexts para Credencial para Votar
        docCredencialVotarCuentaEt = findViewById(R.id.doc_credencial_votar_cuenta)
        docCredencialVotarMostroEt = findViewById(R.id.doc_credencial_votar_mostro)
        docCredencialVotarEntregoCopiaEt = findViewById(R.id.doc_credencial_votar_entrego_copia)

        // Inicializar EditTexts para Poder Amplio
        docPoderAmplioCuentaEt = findViewById(R.id.doc_poder_amplio_cuenta)
        docPoderAmplioMostroEt = findViewById(R.id.doc_poder_amplio_mostro)
        docPoderAmplioEntregoCopiaEt = findViewById(R.id.doc_poder_amplio_entrego_copia)

        // Inicializar EditTexts para Comprobante de Ingresos
        docComprobanteIngresosCuentaEt = findViewById(R.id.doc_comprobante_ingresos_cuenta)
        docComprobanteIngresosMostroEt = findViewById(R.id.doc_comprobante_ingresos_mostro)
        docComprobanteIngresosEntregoCopiaEt = findViewById(R.id.doc_comprobante_ingresos_entrego_copia)

        // Inicializar botones
        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        btnGuardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatosDocumentos()
            }
        }

        btnSiguiente.setOnClickListener {
            if (!datosGuardados) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarDatosDocumentos() }
                )
            } else {
                irASiguiente()
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar campos de Credencial para Votar
        if (!validarDocumento(
                cuentaEt = docCredencialVotarCuentaEt,
                mostroEt = docCredencialVotarMostroEt,
                entregoCopiaEt = docCredencialVotarEntregoCopiaEt,
                nombreDocumento = "Credencial para Votar"
            )) {
            return false
        }

        // Validar campos de Poder Amplio
        if (!validarDocumento(
                cuentaEt = docPoderAmplioCuentaEt,
                mostroEt = docPoderAmplioMostroEt,
                entregoCopiaEt = docPoderAmplioEntregoCopiaEt,
                nombreDocumento = "Poder Amplio"
            )) {
            return false
        }

        // Validar campos de Comprobante de Ingresos
        if (!validarDocumento(
                cuentaEt = docComprobanteIngresosCuentaEt,
                mostroEt = docComprobanteIngresosMostroEt,
                entregoCopiaEt = docComprobanteIngresosEntregoCopiaEt,
                nombreDocumento = "Comprobante de Ingresos"
            )) {
            return false
        }

        return true
    }

    private fun validarDocumento(
        cuentaEt: EditText,
        mostroEt: EditText,
        entregoCopiaEt: EditText,
        nombreDocumento: String
    ): Boolean {
        val cuenta = cuentaEt.text.toString().trim()
        val mostro = mostroEt.text.toString().trim()
        val entregoCopia = entregoCopiaEt.text.toString().trim()

        // Si no cuenta con el documento, no es necesario validar los demás campos
        if (cuenta.equals("No", ignoreCase = true)) {
            return true
        }

        // Validar que si cuenta con el documento, se especifique si lo mostró
        if (mostro.isEmpty()) {
            mostrarDialogo(
                titulo = "Campo requerido",
                mensaje = "Debe especificar si mostró el documento de $nombreDocumento",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            mostroEt.requestFocus()
            return false
        }

        // Validar que si mostró el documento, se especifique si entregó copia
        if (entregoCopia.isEmpty()) {
            mostrarDialogo(
                titulo = "Campo requerido",
                mensaje = "Debe especificar si entregó copia del documento de $nombreDocumento",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            entregoCopiaEt.requestFocus()
            return false
        }

        return true
    }

    private fun guardarDatosDocumentos() {
        val datos = DatosDocumentosEntity(
            doc_credencial_votar_cuenta = docCredencialVotarCuentaEt.text.toString().trim(),
            doc_credencial_votar_mostro = docCredencialVotarMostroEt.text.toString().trim(),
            doc_credencial_votar_entrego_copia = docCredencialVotarEntregoCopiaEt.text.toString().trim(),

            doc_poder_amplio_cuenta = docPoderAmplioCuentaEt.text.toString().trim(),
            doc_poder_amplio_mostro = docPoderAmplioMostroEt.text.toString().trim(),
            doc_poder_amplio_entrego_copia = docPoderAmplioEntregoCopiaEt.text.toString().trim(),

            doc_comprobante_ingresos_cuenta = docComprobanteIngresosCuentaEt.text.toString().trim(),
            doc_comprobante_ingresos_mostro = docComprobanteIngresosMostroEt.text.toString().trim(),
            doc_comprobante_ingresos_entrego_copia = docComprobanteIngresosEntregoCopiaEt.text.toString().trim(),

            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosDocumentosDao().insertDatosDocumentos(datos)
                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Datos de documentos guardados correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado("Error al guardar datos de documentos: ${e.message ?: "Error desconocido"}")
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte16SinConexion::class.java)
        intent.putExtra("id_acreditado", idAcreditado.toString())
        startActivity(intent)
    }

    private fun mostrarErrorInesperado(mensaje: String) {
        mostrarDialogo(
            titulo = "Error inesperado",
            mensaje = mensaje,
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