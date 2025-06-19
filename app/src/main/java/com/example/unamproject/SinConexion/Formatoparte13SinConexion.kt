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

class Formatoparte13SinConexion : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L
    private var datosGuardados = false

    // EditTexts
    private lateinit var telefono1LadaEt: EditText
    private lateinit var telefono1NumeroEt: EditText
    private lateinit var telefono1ExtensionEt: EditText
    private lateinit var telefono1TipoEt: EditText
    private lateinit var telefono2LadaEt: EditText
    private lateinit var telefono2NumeroEt: EditText
    private lateinit var telefono2ExtensionEt: EditText
    private lateinit var telefono2TipoEt: EditText

    // Buttons
    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte13_sin_conexion)

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
        // Inicializar EditTexts
        telefono1LadaEt = findViewById(R.id.telefono1_lada)
        telefono1NumeroEt = findViewById(R.id.telefono1_numero)
        telefono1ExtensionEt = findViewById(R.id.telefono1_extension)
        telefono1TipoEt = findViewById(R.id.telefono1_tipo)
        telefono2LadaEt = findViewById(R.id.telefono2_lada)
        telefono2NumeroEt = findViewById(R.id.telefono2_numero)
        telefono2ExtensionEt = findViewById(R.id.telefono2_extension)
        telefono2TipoEt = findViewById(R.id.telefono2_tipo)

        // Inicializar botones
        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        btnGuardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatosTelefonos()
            }
        }

        btnSiguiente.setOnClickListener {
            if (!datosGuardados) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarDatosTelefonos() }
                )
            } else {
                irASiguiente()
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar que al menos un teléfono esté completo
        val telefono1Completo = telefono1NumeroEt.text.toString().trim().isNotEmpty()
        val telefono2Completo = telefono2NumeroEt.text.toString().trim().isNotEmpty()

        if (!telefono1Completo && !telefono2Completo) {
            mostrarDialogo(
                titulo = "Datos requeridos",
                mensaje = "Debe registrar al menos un número de teléfono",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            telefono1NumeroEt.requestFocus()
            return false
        }

        // Validar formatos de teléfonos
        if (telefono1Completo && !validarTelefono(telefono1NumeroEt.text.toString().trim())) {
            mostrarDialogo(
                titulo = "Teléfono inválido",
                mensaje = "El teléfono 1 debe tener 10 dígitos",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            telefono1NumeroEt.requestFocus()
            return false
        }

        if (telefono2Completo && !validarTelefono(telefono2NumeroEt.text.toString().trim())) {
            mostrarDialogo(
                titulo = "Teléfono inválido",
                mensaje = "El teléfono 2 debe tener 10 dígitos",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            telefono2NumeroEt.requestFocus()
            return false
        }

        // Validar que si hay extensión, sea numérica
        if (telefono1ExtensionEt.text.toString().trim().isNotEmpty()) {
            try {
                telefono1ExtensionEt.text.toString().trim().toInt()
            } catch (e: NumberFormatException) {
                mostrarDialogo(
                    titulo = "Extensión inválida",
                    mensaje = "La extensión del teléfono 1 debe ser numérica",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                telefono1ExtensionEt.requestFocus()
                return false
            }
        }

        if (telefono2ExtensionEt.text.toString().trim().isNotEmpty()) {
            try {
                telefono2ExtensionEt.text.toString().trim().toInt()
            } catch (e: NumberFormatException) {
                mostrarDialogo(
                    titulo = "Extensión inválida",
                    mensaje = "La extensión del teléfono 2 debe ser numérica",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                telefono2ExtensionEt.requestFocus()
                return false
            }
        }

        return true
    }

    private fun validarTelefono(numero: String): Boolean {
        // Validar que el número tenga 10 dígitos (sin contar lada)
        return numero.matches(Regex("\\d{10}"))
    }

    private fun guardarDatosTelefonos() {
        val datos = DatosTelefonosEntity(
            telefono1_lada = telefono1LadaEt.text.toString().trim(),
            telefono1_numero = telefono1NumeroEt.text.toString().trim(),
            telefono1_extension = telefono1ExtensionEt.text.toString().trim(),
            telefono1_tipo = telefono1TipoEt.text.toString().trim(),
            telefono2_lada = telefono2LadaEt.text.toString().trim(),
            telefono2_numero = telefono2NumeroEt.text.toString().trim(),
            telefono2_extension = telefono2ExtensionEt.text.toString().trim(),
            telefono2_tipo = telefono2TipoEt.text.toString().trim(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosTelefonosDao().insertDatosTelefonos(datos)
                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Datos de teléfonos guardados correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado("Error al guardar datos de teléfonos: ${e.message ?: "Error desconocido"}")
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte14SinConexion::class.java)
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