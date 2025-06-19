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

class Formatoparte14SinConexion : AppCompatActivity() {

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L
    private var datosGuardados = false

    // EditTexts
    private lateinit var cobranzaVisitaEt: EditText
    private lateinit var cobranzaNumeroVisitasEt: EditText
    private lateinit var cobranzaUltimaFechaVisitaEt: EditText
    private lateinit var cobranzaDespachoEt: EditText
    private lateinit var cobranzaCalificacionEt: EditText
    private lateinit var cobranzaComentarioEt: EditText

    // Botones
    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte14_sin_conexion)

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
        cobranzaVisitaEt = findViewById(R.id.cobranza_visita)
        cobranzaNumeroVisitasEt = findViewById(R.id.cobranza_numero_visitas)
        cobranzaUltimaFechaVisitaEt = findViewById(R.id.cobranza_ultima_fecha_visita)
        cobranzaDespachoEt = findViewById(R.id.cobranza_despacho)
        cobranzaCalificacionEt = findViewById(R.id.cobranza_calificacion)
        cobranzaComentarioEt = findViewById(R.id.cobranza_comentario)

        // Botones
        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        btnGuardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatosCobranza()
            }
        }

        btnSiguiente.setOnClickListener {
            if (!datosGuardados) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarDatosCobranza() }
                )
            } else {
                irASiguiente()
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar número de visitas si se especificó visita
        if (cobranzaVisitaEt.text.toString().trim().equals("Sí", ignoreCase = true)) {
            if (cobranzaNumeroVisitasEt.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "Debe especificar el número de visitas de cobranza",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                cobranzaNumeroVisitasEt.requestFocus()
                return false
            }

            try {
                val numVisitas = cobranzaNumeroVisitasEt.text.toString().toInt()
                if (numVisitas < 0) {
                    mostrarDialogo(
                        titulo = "Valor inválido",
                        mensaje = "El número de visitas no puede ser negativo",
                        iconoResId = android.R.drawable.ic_dialog_alert,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                    cobranzaNumeroVisitasEt.requestFocus()
                    return false
                }
            } catch (e: NumberFormatException) {
                mostrarDialogo(
                    titulo = "Valor inválido",
                    mensaje = "El número de visitas debe ser un valor numérico",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                cobranzaNumeroVisitasEt.requestFocus()
                return false
            }

            if (cobranzaUltimaFechaVisitaEt.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "Debe especificar la última fecha de visita",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                cobranzaUltimaFechaVisitaEt.requestFocus()
                return false
            }
        }

        // Validar calificación si se especificó
        if (cobranzaCalificacionEt.text.toString().trim().isNotEmpty()) {
            try {
                val calificacion = cobranzaCalificacionEt.text.toString().toInt()
                if (calificacion < 1 || calificacion > 10) {
                    mostrarDialogo(
                        titulo = "Valor inválido",
                        mensaje = "La calificación debe estar entre 1 y 10",
                        iconoResId = android.R.drawable.ic_dialog_alert,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                    cobranzaCalificacionEt.requestFocus()
                    return false
                }
            } catch (e: NumberFormatException) {
                mostrarDialogo(
                    titulo = "Valor inválido",
                    mensaje = "La calificación debe ser un valor numérico",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                cobranzaCalificacionEt.requestFocus()
                return false
            }
        }

        // Validar campo de visita es requerido
        if (cobranzaVisitaEt.text.toString().trim().isEmpty()) {
            mostrarDialogo(
                titulo = "Campo requerido",
                mensaje = "Debe especificar si hubo visita de cobranza",
                iconoResId = android.R.drawable.ic_dialog_alert,
                colorTitulo = 0xFFD32F2F.toInt()
            )
            cobranzaVisitaEt.requestFocus()
            return false
        }

        return true
    }

    private fun guardarDatosCobranza() {
        val datos = DatosCobranzaEntity(
            cobranza_visita = cobranzaVisitaEt.text.toString().trim(),
            cobranza_numero_visitas = cobranzaNumeroVisitasEt.text.toString().trim(),
            cobranza_ultima_fecha_visita = cobranzaUltimaFechaVisitaEt.text.toString().trim(),
            cobranza_despacho = cobranzaDespachoEt.text.toString().trim(),
            cobranza_calificacion = cobranzaCalificacionEt.text.toString().trim(),
            cobranza_comentario = cobranzaComentarioEt.text.toString().trim(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosCobranzaDao().insertDatosCobranza(datos)
                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Datos de cobranza guardados correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado("Error al guardar datos de cobranza: ${e.message ?: "Error desconocido"}")
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte15SinConexion::class.java)
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