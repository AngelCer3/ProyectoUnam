package com.example.unamproject

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte9Activity : AppCompatActivity() {

    private lateinit var conyugeActivo: EditText
    private lateinit var conyugeOcupacionActual: EditText
    private lateinit var institucionTrabajoConyuge: EditText
    private lateinit var conyugeActividadRemunerada: EditText
    private lateinit var conyugeContratoLaboral: EditText
    private lateinit var conyugeIngresoMensual: EditText
    private lateinit var conyugeEmpresa: EditText
    private lateinit var conyugeAntiguedad: EditText
    private lateinit var comprobanteIngresoConyuge: EditText
    private lateinit var institucionCotizacionConyuge: EditText
    private lateinit var ingresosConceptosConyuge: EditText

    private lateinit var btnGuardar: Button
    private lateinit var btnSiguiente: Button

    private var idAcreditado: String? = null
    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte9)
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
        conyugeActivo = findViewById(R.id.conyuge_activo)
        conyugeOcupacionActual = findViewById(R.id.conyuge_ocupacion_actual)
        institucionTrabajoConyuge = findViewById(R.id.institucion_trabajo_conyuge)
        conyugeActividadRemunerada = findViewById(R.id.conyuge_actividad_remunerada)
        conyugeContratoLaboral = findViewById(R.id.conyuge_contrato_laboral)
        conyugeIngresoMensual = findViewById(R.id.conyuge_ingreso_mensual)
        conyugeEmpresa = findViewById(R.id.conyuge_empresa)
        conyugeAntiguedad = findViewById(R.id.conyuge_antiguedad)
        comprobanteIngresoConyuge = findViewById(R.id.comprobante_ingreso_conyuge)
        institucionCotizacionConyuge = findViewById(R.id.institucion_cotizacion_conyuge)
        ingresosConceptosConyuge = findViewById(R.id.ingresos_conceptos_conyuge)

        btnGuardar = findViewById(R.id.btnGuardar)
        btnSiguiente = findViewById(R.id.btnSiguiente)
    }

    private fun validarCampos(): Boolean {
        // Campos obligatorios principales
        val camposObligatorios = listOf(
            Pair(conyugeActivo, "¿Cónyuge activo laboralmente?"),
            Pair(conyugeOcupacionActual, "Ocupación actual del cónyuge"),
            Pair(conyugeIngresoMensual, "Ingreso mensual del cónyuge"),
            Pair(conyugeEmpresa, "Empresa del cónyuge"),
            Pair(conyugeAntiguedad, "Antigüedad del cónyuge")
        )

        for ((campo, nombre) in camposObligatorios) {
            if (campo.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Por favor, complete el campo: $nombre",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                campo.requestFocus()
                return false
            }
        }

        // Validar campos numéricos
        val camposNumericos = listOf(
            Pair(conyugeIngresoMensual, "Ingreso mensual del cónyuge"),
            Pair(conyugeAntiguedad, "Antigüedad del cónyuge")
        )

        for ((campo, nombre) in camposNumericos) {
            if (campo.text.toString().isNotBlank() && campo.text.toString().toDoubleOrNull() == null) {
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

        // Validación condicional si el cónyuge está activo
        if (conyugeActivo.text.toString().equals("Sí", ignoreCase = true) ||
            conyugeActivo.text.toString().equals("Si", ignoreCase = true)) {

            if (institucionTrabajoConyuge.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Debe especificar la institución donde trabaja el cónyuge",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                institucionTrabajoConyuge.requestFocus()
                return false
            }

            if (conyugeActividadRemunerada.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Debe especificar la actividad remunerada del cónyuge",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                conyugeActividadRemunerada.requestFocus()
                return false
            }

            if (conyugeContratoLaboral.text.toString().isBlank()) {
                mostrarDialogoValidacion(
                    "Campo requerido",
                    "Debe especificar el tipo de contrato laboral del cónyuge",
                    android.R.drawable.ic_dialog_alert,
                    0xFFD32F2F.toInt()
                )
                conyugeContratoLaboral.requestFocus()
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
        val datos = datosEspecificosConyuge(
            conyuge_activo = conyugeActivo.text.toString(),
            conyuge_ocupacion_actual = conyugeOcupacionActual.text.toString(),
            institucion_trabajo_conyuge = institucionTrabajoConyuge.text.toString(),
            conyuge_actividad_remunerada = conyugeActividadRemunerada.text.toString(),
            conyuge_contrato_laboral = conyugeContratoLaboral.text.toString(),
            conyuge_ingreso_mensual = conyugeIngresoMensual.text.toString(),
            conyuge_empresa = conyugeEmpresa.text.toString(),
            conyuge_antiguedad = conyugeAntiguedad.text.toString(),
            comprobante_ingreso_conyuge = comprobanteIngresoConyuge.text.toString(),
            institucion_cotizacion_conyuge = institucionCotizacionConyuge.text.toString(),
            ingresos_conceptos_conyuge = ingresosConceptosConyuge.text.toString(),
            id_acreditado = idAcreditado!!,
            id_usuario = idUsuario!!
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = RetrofitClient.webService.agregarDatosEspecificosConyuge(datos)
                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        mostrarDialogoValidacion(
                            "Éxito",
                            "Datos del cónyuge guardados correctamente",
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
        val intent = Intent(this, Formatoparte10Activity::class.java)
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