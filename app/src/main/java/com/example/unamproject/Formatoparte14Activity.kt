package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import java.text.SimpleDateFormat
import java.util.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte14Activity : AppCompatActivity() {

    private lateinit var cobranzaVisita: EditText
    private lateinit var cobranzaNumeroVisitas: EditText
    private lateinit var cobranzaUltimaFechaVisita: EditText
    private lateinit var cobranzaDespacho: EditText
    private lateinit var cobranzaCalificacion: EditText
    private lateinit var cobranzaComentario: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button
    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte14)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        initViews()

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

    private fun initViews() {
        cobranzaVisita = findViewById(R.id.cobranza_visita)
        cobranzaNumeroVisitas = findViewById(R.id.cobranza_numero_visitas)
        cobranzaUltimaFechaVisita = findViewById(R.id.cobranza_ultima_fecha_visita)
        cobranzaDespacho = findViewById(R.id.cobranza_despacho)
        cobranzaCalificacion = findViewById(R.id.cobranza_calificacion)
        cobranzaComentario = findViewById(R.id.cobranza_comentario)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun validarCampos(): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false

        // Validar campo de visita
        if (cobranzaVisita.text.toString().isBlank()) {
            mostrarDialogoValidacion(
                "Campo requerido",
                "Debe especificar si hubo visita de cobranza",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            cobranzaVisita.requestFocus()
            return false
        }

        val huboVisita = cobranzaVisita.text.toString().equals("Sí", ignoreCase = true) ||
                cobranzaVisita.text.toString().equals("Si", ignoreCase = true)

        // Validaciones condicionales si hubo visita
        if (huboVisita) {
            if (cobranzaNumeroVisitas.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Debe especificar el número de visitas de cobranza",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                cobranzaNumeroVisitas.requestFocus()
                return false
            }

            if (cobranzaNumeroVisitas.text.toString().toIntOrNull() == null) {
                mostrarDialogoValidacion(
                    "Valor inválido",
                    "El número de visitas debe ser un valor numérico",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                cobranzaNumeroVisitas.requestFocus()
                return false
            }

            if (cobranzaUltimaFechaVisita.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Debe especificar la última fecha de visita",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                cobranzaUltimaFechaVisita.requestFocus()
                return false
            }

            try {
                dateFormat.parse(cobranzaUltimaFechaVisita.text.toString())
            } catch (e: Exception) {
                mostrarDialogoValidacion(
                    "Fecha inválida",
                    "Formato de fecha incorrecto (debe ser dd/MM/yyyy)",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                cobranzaUltimaFechaVisita.requestFocus()
                return false
            }

            if (cobranzaCalificacion.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Debe especificar la calificación de cobranza",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                cobranzaCalificacion.requestFocus()
                return false
            }
        }

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

        return true
    }

    private fun guardarDatos() {
        val datos = datosCobranza(
            cobranza_visita = cobranzaVisita.text.toString(),
            cobranza_numero_visitas = cobranzaNumeroVisitas.text.toString(),
            cobranza_ultima_fecha_visita = cobranzaUltimaFechaVisita.text.toString(),
            cobranza_despacho = cobranzaDespacho.text.toString(),
            cobranza_calificacion = cobranzaCalificacion.text.toString(),
            cobranza_comentario = cobranzaComentario.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosCobranza(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        mostrarDialogoValidacion(
                            "Éxito",
                            "Datos de cobranza guardados correctamente",
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
        val intent = Intent(this, Formatoparte15Activity::class.java)
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