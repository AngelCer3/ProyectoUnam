package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class Formatoparte4Activity : AppCompatActivity() {

    private lateinit var guardar: Button
    private lateinit var siguiente: Button

    private lateinit var creditoFechaEntrega: EditText
    private lateinit var creditoMonto: EditText
    private lateinit var creditoSueldoOtorgado: EditText
    private lateinit var creditoFechaUltimoPago: EditText
    private lateinit var creditoReciboPago: EditText
    private lateinit var creditoPagoActual: EditText
    private lateinit var creditoDeudaActual: EditText

    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte4)

        idAcreditado = intent.getStringExtra("id_acreditado")
        idUsuario = intent.getStringExtra("id_usuario")

        initViews()

        guardar.setOnClickListener {
            if (validarCampos()) {
                guardarDatos()
            }
        }

        siguiente.setOnClickListener {
            if (validarCampos()) {
                siguienteFormato()
            }
        }
    }

    private fun initViews() {
        creditoFechaEntrega = findViewById(R.id.credito_fecha_entrega)
        creditoMonto = findViewById(R.id.credito_monto)
        creditoSueldoOtorgado = findViewById(R.id.credito_sueldo_otorgado)
        creditoFechaUltimoPago = findViewById(R.id.credito_fecha_ultimo_pago)
        creditoReciboPago = findViewById(R.id.credito_recibo_pago)
        creditoPagoActual = findViewById(R.id.credito_pago_actual)
        creditoDeudaActual = findViewById(R.id.credito_deuda_actual)

        guardar = findViewById(R.id.btnGuardar)
        siguiente = findViewById(R.id.btnSiguiente)
    }

    private fun validarCampos(): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

        val camposObligatorios = listOf(
            Pair(creditoFechaEntrega, "Fecha de entrega"),
            Pair(creditoMonto, "Monto del crédito"),
            Pair(creditoSueldoOtorgado, "Sueldo otorgado"),
            Pair(creditoFechaUltimoPago, "Fecha último pago"),
            Pair(creditoReciboPago, "Recibo de pago"),
            Pair(creditoPagoActual, "Pago actual"),
            Pair(creditoDeudaActual, "Deuda actual")
        )

        // Validate required fields
        for ((campo, nombre) in camposObligatorios) {
            if (campo.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Por favor, completa el campo: $nombre",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validate date formats
        try {
            dateFormat.isLenient = false
            dateFormat.parse(creditoFechaEntrega.text.toString())
        } catch (e: Exception) {
            mostrarDialogoValidacion(
                "Fecha inválida",
                "Fecha de entrega inválida (usa formato dd/MM/yyyy)",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            creditoFechaEntrega.requestFocus()
            return false
        }

        try {
            dateFormat.parse(creditoFechaUltimoPago.text.toString())
        } catch (e: Exception) {
            mostrarDialogoValidacion(
                "Fecha inválida",
                "Fecha último pago inválida (usa formato dd/MM/yyyy)",
                android.R.drawable.ic_dialog_alert,
                0xFFD32F2F.toInt()
            )
            creditoFechaUltimoPago.requestFocus()
            return false
        }

        // Validate numeric fields
        val camposNumericos = listOf(
            Pair(creditoMonto, "Monto del crédito"),
            Pair(creditoSueldoOtorgado, "Sueldo otorgado"),
            Pair(creditoPagoActual, "Pago actual"),
            Pair(creditoDeudaActual, "Deuda actual")
        )

        for ((campo, nombre) in camposNumericos) {
            if (campo.text.toString().toDoubleOrNull() == null) {
                mostrarDialogoValidacion(
                    "Valor inválido",
                    "El campo '$nombre' debe ser numérico",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

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
        val datos = datosCredito(
            credito_fecha_entrega = creditoFechaEntrega.text.toString(),
            credito_monto = creditoMonto.text.toString(),
            credito_sueldo_otorgado = creditoSueldoOtorgado.text.toString(),
            credito_fecha_ultimo_pago = creditoFechaUltimoPago.text.toString(),
            credito_recibo_pago = creditoReciboPago.text.toString(),
            credito_pago_actual = creditoPagoActual.text.toString(),
            credito_deuda_actual = creditoDeudaActual.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosCredito(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        mostrarDialogoValidacion(
                            "Éxito",
                            "Datos guardados correctamente",
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
                        "No se pudo conectar: ${e.message}",
                        android.R.drawable.stat_notify_error,
                        0xFFD32F2F.toInt()
                    )
                }
            }
        }
    }

    private fun siguienteFormato() {
        val intent = Intent(this, Formatoparte5Activity::class.java)
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