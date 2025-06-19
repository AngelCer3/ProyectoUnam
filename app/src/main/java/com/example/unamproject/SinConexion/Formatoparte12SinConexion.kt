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

class Formatoparte12SinConexion : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L
    private var datosGuardados = false

    // EditTexts
    private lateinit var familiaTieneDeudasEt: EditText
    private lateinit var familiaCantidadDeudaEt: EditText

    // Buttons
    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte12_sin_conexion)

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
        familiaTieneDeudasEt = findViewById(R.id.familia_tiene_deudas)
        familiaCantidadDeudaEt = findViewById(R.id.familia_cantidad_deuda)

        // Inicializar botones
        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        btnGuardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatosFamiliaDeudas()
            }
        }

        btnSiguiente.setOnClickListener {
            if (!datosGuardados) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarDatosFamiliaDeudas() }
                )
            } else {
                irASiguiente()
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar campo obligatorio
        if (familiaTieneDeudasEt.text.toString().trim().isEmpty()) {
            mostrarDialogo(
                titulo = "Campo requerido",
                mensaje = "Debe especificar si la familia tiene deudas",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            familiaTieneDeudasEt.requestFocus()
            return false
        }

        // Validación condicional para cantidad de deuda
        if (familiaTieneDeudasEt.text.toString().trim().equals("Sí", ignoreCase = true)) {
            if (familiaCantidadDeudaEt.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "Debe especificar la cantidad de deuda familiar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                familiaCantidadDeudaEt.requestFocus()
                return false
            }

            // Validar que la cantidad sea numérica y no negativa
            try {
                val cantidad = familiaCantidadDeudaEt.text.toString().trim().toDouble()
                if (cantidad < 0) {
                    mostrarDialogo(
                        titulo = "Valor inválido",
                        mensaje = "La cantidad de deuda no puede ser negativa",
                        iconoResId = android.R.drawable.ic_dialog_alert,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                    familiaCantidadDeudaEt.requestFocus()
                    return false
                }
            } catch (e: NumberFormatException) {
                mostrarDialogo(
                    titulo = "Valor inválido",
                    mensaje = "La cantidad de deuda debe ser un valor numérico",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                familiaCantidadDeudaEt.requestFocus()
                return false
            }
        }

        return true
    }

    private fun guardarDatosFamiliaDeudas() {
        val datos = DatosFamiliaDeudasEntity(
            familia_tiene_deudas = familiaTieneDeudasEt.text.toString().trim(),
            familia_cantidad_deudas = familiaCantidadDeudaEt.text.toString().trim(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosFamiliaDeudasDao().insertDatosFamiliaDeudas(datos)
                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Datos de deudas familiares guardados correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado("Error al guardar datos de deudas familiares: ${e.message ?: "Error desconocido"}")
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte13SinConexion::class.java)
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