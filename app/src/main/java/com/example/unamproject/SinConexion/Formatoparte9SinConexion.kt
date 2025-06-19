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

class Formatoparte9SinConexion : AppCompatActivity() {

    private lateinit var conyugeActivoEt: EditText
    private lateinit var ocupacionActualEt: EditText
    private lateinit var institucionTrabajoEt: EditText
    private lateinit var actividadRemuneradaEt: EditText
    private lateinit var contratoLaboralEt: EditText
    private lateinit var ingresoMensualEt: EditText
    private lateinit var empresaEt: EditText
    private lateinit var antiguedadEt: EditText
    private lateinit var comprobanteIngresoEt: EditText
    private lateinit var institucionCotizacionEt: EditText
    private lateinit var conceptosIngresoEt: EditText

    private lateinit var guardarBtn: Button
    private lateinit var siguienteBtn: Button

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L
    private var datosGuardados = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_formatoparte9_sin_conexion)

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
        conyugeActivoEt = findViewById(R.id.conyuge_activo)
        ocupacionActualEt = findViewById(R.id.conyuge_ocupacion_actual)
        institucionTrabajoEt = findViewById(R.id.institucion_trabajo_conyuge)
        actividadRemuneradaEt = findViewById(R.id.conyuge_actividad_remunerada)
        contratoLaboralEt = findViewById(R.id.conyuge_contrato_laboral)
        ingresoMensualEt = findViewById(R.id.conyuge_ingreso_mensual)
        empresaEt = findViewById(R.id.conyuge_empresa)
        antiguedadEt = findViewById(R.id.conyuge_antiguedad)
        comprobanteIngresoEt = findViewById(R.id.comprobante_ingreso_conyuge)
        institucionCotizacionEt = findViewById(R.id.institucion_cotizacion_conyuge)
        conceptosIngresoEt = findViewById(R.id.ingresos_conceptos_conyuge)

        guardarBtn = findViewById(R.id.btnGuardar)
        siguienteBtn = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        guardarBtn.setOnClickListener {
            if (validarCampos()) {
                guardarDatosConyuge()
            }
        }

        siguienteBtn.setOnClickListener {
            if (!datosGuardados) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarDatosConyuge() }
                )
            } else {
                irASiguiente()
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar campos obligatorios
        val camposRequeridos = listOf(
            conyugeActivoEt to "Estado laboral del cónyuge",
            ingresoMensualEt to "Ingreso mensual del cónyuge"
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

        // Validar campos numéricos
        val camposNumericos = listOf(
            ingresoMensualEt to "Ingreso mensual",
            antiguedadEt to "Antigüedad laboral"
        )

        for ((campo, nombre) in camposNumericos) {
            val valor = campo.text.toString().trim()
            if (valor.isNotEmpty()) {
                try {
                    valor.toInt()
                } catch (e: NumberFormatException) {
                    mostrarDialogo(
                        titulo = "Valor inválido",
                        mensaje = "El campo $nombre debe ser un número entero",
                        iconoResId = android.R.drawable.ic_dialog_alert,
                        colorTitulo = 0xFFD32F2F.toInt()
                    )
                    campo.requestFocus()
                    return false
                }
            }
        }

        // Validación condicional para cónyuge activo
        if (conyugeActivoEt.text.toString().trim().equals("Sí", ignoreCase = true)) {
            if (ocupacionActualEt.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "Debe especificar la ocupación actual del cónyuge",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                ocupacionActualEt.requestFocus()
                return false
            }

            if (empresaEt.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "Debe especificar la empresa donde trabaja el cónyuge",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                empresaEt.requestFocus()
                return false
            }
        }

        return true
    }

    private fun guardarDatosConyuge() {
        val datos = DatosEspecificosConyugeEntity(
            conyuge_activo = conyugeActivoEt.text.toString().trim(),
            conyuge_ocupacion_actual = ocupacionActualEt.text.toString().trim(),
            institucion_trabajo_conyuge = institucionTrabajoEt.text.toString().trim(),
            conyuge_actividad_remunerada = actividadRemuneradaEt.text.toString().trim(),
            conyuge_contrato_laboral = contratoLaboralEt.text.toString().trim(),
            conyuge_ingreso_mensual = ingresoMensualEt.text.toString().trim(),
            conyuge_empresa = empresaEt.text.toString().trim(),
            conyuge_antiguedad = antiguedadEt.text.toString().trim(),
            comprobante_ingreso_conyuge = comprobanteIngresoEt.text.toString().trim(),
            institucion_cotizacion_conyuge = institucionCotizacionEt.text.toString().trim(),
            ingresos_conceptos_conyuge = conceptosIngresoEt.text.toString().trim(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosEspecificosConyugeDao().insertDatosEspecificosConyuge(datos)
                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Datos laborales del cónyuge guardados correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado("Error al guardar datos del cónyuge: ${e.message ?: "Error desconocido"}")
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte10SinConexion::class.java)
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