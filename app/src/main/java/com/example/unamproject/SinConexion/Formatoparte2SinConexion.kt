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
import java.text.SimpleDateFormat
import java.util.*

class Formatoparte2SinConexion : AppCompatActivity() {

    private lateinit var visita1Fecha: EditText
    private lateinit var visita1Hora: EditText
    private lateinit var visita1Resultado: Spinner
    private lateinit var visita2Fecha: EditText
    private lateinit var visita2Hora: EditText
    private lateinit var visita2Resultado: Spinner
    private lateinit var visita3Fecha: EditText
    private lateinit var visita3Hora: EditText
    private lateinit var visita3Resultado: Spinner

    private lateinit var guardarBtn: Button
    private lateinit var siguienteBtn: Button

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L
    private var datosGuardados = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte2_sin_conexion)

        // Obtener ID del acreditado
        idAcreditado = intent.getStringExtra("id_acreditado")?.toLongOrNull() ?: run {
            mostrarErrorYCerrar("No se recibió el ID del acreditado")
            return
        }

        // Inicializa la base de datos Room
        database = AppDatabase.getDatabase(this)

        initViews()
        setupButtons()
    }

    private fun initViews() {
        visita1Fecha = findViewById(R.id.visita1_fecha)
        visita1Hora = findViewById(R.id.visita1_hora)
        visita1Resultado = findViewById(R.id.visita1_resultado)
        visita2Fecha = findViewById(R.id.visita2_fecha)
        visita2Hora = findViewById(R.id.visita2_hora)
        visita2Resultado = findViewById(R.id.visita2_resultado)
        visita3Fecha = findViewById(R.id.visita3_fecha)
        visita3Hora = findViewById(R.id.visita3_hora)
        visita3Resultado = findViewById(R.id.visita3_resultado)

        guardarBtn = findViewById(R.id.btnGuardarDatos)
        siguienteBtn = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        guardarBtn.setOnClickListener {
            if (validarCampos()) {
                guardarVisitas()
            }
        }

        siguienteBtn.setOnClickListener {
            if (!datosGuardados) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarVisitas() }
                )
            } else {
                irASiguiente()
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar fechas (formato dd/MM/yyyy)
        val fechas = listOf(
            visita1Fecha to "Fecha de visita 1",
            visita2Fecha to "Fecha de visita 2",
            visita3Fecha to "Fecha de visita 3"
        )

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false

        for ((campo, nombre) in fechas) {
            val fechaStr = campo.text.toString().trim()
            if (fechaStr.isNotEmpty()) {
                try {
                    dateFormat.parse(fechaStr)
                } catch (e: Exception) {
                    mostrarDialogo(
                        titulo = "Formato inválido",
                        mensaje = "$nombre: debe tener formato dd/MM/yyyy",
                        iconoResId = android.R.drawable.ic_dialog_alert,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                    campo.requestFocus()
                    return false
                }
            }
        }

        // Validar horas (formato HH:mm)
        val horas = listOf(
            visita1Hora to "Hora de visita 1",
            visita2Hora to "Hora de visita 2",
            visita3Hora to "Hora de visita 3"
        )

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        timeFormat.isLenient = false

        for ((campo, nombre) in horas) {
            val horaStr = campo.text.toString().trim()
            if (horaStr.isNotEmpty()) {
                try {
                    timeFormat.parse(horaStr)
                } catch (e: Exception) {
                    mostrarDialogo(
                        titulo = "Formato inválido",
                        mensaje = "$nombre: debe tener formato HH:mm",
                        iconoResId = android.R.drawable.ic_dialog_alert,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                    campo.requestFocus()
                    return false
                }
            }
        }

        // Validar que al menos una visita esté completa
        if (visita1Fecha.text.isBlank() && visita2Fecha.text.isBlank() && visita3Fecha.text.isBlank()) {
            mostrarDialogo(
                titulo = "Datos requeridos",
                mensaje = "Debes registrar al menos una visita",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            return false
        }

        return true
    }

    private fun guardarVisitas() {
        val visitas = VisitasEntity(
            visita1_fecha = visita1Fecha.text.toString().trim(),
            visita1_hora = visita1Hora.text.toString().trim(),
            visita1_resultado = visita1Resultado.selectedItem.toString(),
            visita2_fecha = visita2Fecha.text.toString().trim(),
            visita2_hora = visita2Hora.text.toString().trim(),
            visita2_resultado = visita2Resultado.selectedItem.toString(),
            visita3_fecha = visita3Fecha.text.toString().trim(),
            visita3_hora = visita3Hora.text.toString().trim(),
            visita3_resultado = visita3Resultado.selectedItem.toString(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.visitasDao().insertVisita(visitas)
                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Visitas guardadas correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado("Error al guardar visitas: ${e.message ?: "Error desconocido"}")
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte3SinConexion::class.java)
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