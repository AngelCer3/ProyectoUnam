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

class Formatoparte4SinConexion : AppCompatActivity() {

    private lateinit var creditoFechaEntrega: EditText
    private lateinit var creditoMonto: EditText
    private lateinit var creditoSueldoOtorgado: EditText
    private lateinit var creditoFechaUltimoPago: EditText
    private lateinit var creditoReciboPago: EditText
    private lateinit var creditoPagoActual: EditText
    private lateinit var creditoDeudaActual: EditText

    private lateinit var guardarBtn: Button
    private lateinit var siguienteBtn: Button

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L
    private var datosGuardados = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte4_sin_conexion)

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
        creditoFechaEntrega = findViewById(R.id.credito_fecha_entrega)
        creditoMonto = findViewById(R.id.credito_monto)
        creditoSueldoOtorgado = findViewById(R.id.credito_sueldo_otorgado)
        creditoFechaUltimoPago = findViewById(R.id.credito_fecha_ultimo_pago)
        creditoReciboPago = findViewById(R.id.credito_recibo_pago)
        creditoPagoActual = findViewById(R.id.credito_pago_actual)
        creditoDeudaActual = findViewById(R.id.credito_deuda_actual)

        guardarBtn = findViewById(R.id.btnGuardar)
        siguienteBtn = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        guardarBtn.setOnClickListener {
            if (validarCampos()) {
                guardarDatosCredito()
            }
        }

        siguienteBtn.setOnClickListener {
            if (!datosGuardados) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarDatosCredito() }
                )
            } else {
                irASiguiente()
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar fechas (formato dd/MM/yyyy)
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false

        val fechas = listOf(
            creditoFechaEntrega to "Fecha de entrega del crédito",
            creditoFechaUltimoPago to "Fecha del último pago"
        )

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

        // Validar montos numéricos
        val montos = listOf(
            creditoMonto to "Monto del crédito",
            creditoSueldoOtorgado to "Sueldo otorgado",
            creditoPagoActual to "Pago actual",
            creditoDeudaActual to "Deuda actual"
        )

        for ((campo, nombre) in montos) {
            val montoStr = campo.text.toString().trim()
            if (montoStr.isNotEmpty()) {
                try {
                    montoStr.toDouble()
                } catch (e: NumberFormatException) {
                    mostrarDialogo(
                        titulo = "Valor inválido",
                        mensaje = "$nombre: debe ser un valor numérico válido",
                        iconoResId = android.R.drawable.ic_dialog_alert,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                    campo.requestFocus()
                    return false
                }
            }
        }

        // Validar campos obligatorios
        val camposRequeridos = listOf(
            creditoFechaEntrega to "Fecha de entrega del crédito",
            creditoMonto to "Monto del crédito",
            creditoSueldoOtorgado to "Sueldo otorgado"
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

        return true
    }

    private fun guardarDatosCredito() {
        val datosCredito = DatosCreditoEntity(
            credito_fecha_entrega = creditoFechaEntrega.text.toString().trim(),
            credito_monto = creditoMonto.text.toString().trim(),
            credito_sueldo_otorgado = creditoSueldoOtorgado.text.toString().trim(),
            credito_fecha_ultimo_pago = creditoFechaUltimoPago.text.toString().trim(),
            credito_recibo_pago = creditoReciboPago.text.toString().trim(),
            credito_pago_actual = creditoPagoActual.text.toString().trim(),
            credito_deuda_actual = creditoDeudaActual.text.toString().trim(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosCreditoDao().insertDatosCredito(datosCredito)
                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Datos de crédito guardados correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado("Error al guardar datos de crédito: ${e.message ?: "Error desconocido"}")
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte5SinConexion::class.java)
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