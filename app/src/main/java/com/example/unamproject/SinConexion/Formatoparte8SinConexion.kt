package com.example.unamproject.SinConexion

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.*
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.unamproject.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class Formatoparte8SinConexion : AppCompatActivity() {

    private lateinit var integrantesTrabajandoEt: EditText
    private lateinit var solicitanteActivoEt: EditText
    private lateinit var ocupacionActualEt: EditText
    private lateinit var desempleadoTiempoEt: EditText
    private lateinit var empresaPreviaEt: EditText
    private lateinit var antiguedadTrabajoAnteriorEt: EditText
    private lateinit var institucionTrabajoEt: EditText
    private lateinit var actividadRemuneradaEt: EditText
    private lateinit var contratoLaboralEt: EditText
    private lateinit var ingresoMensualEt: EditText
    private lateinit var empresaSolicitanteEt: EditText
    private lateinit var antiguedadSolicitanteEt: EditText
    private lateinit var comprobanteIngresosEt: EditText
    private lateinit var institucionCotizacionEt: EditText
    private lateinit var ingresosConceptosEt: EditText

    private lateinit var guardarBtn: Button
    private lateinit var siguienteBtn: Button

    private lateinit var database: AppDatabase
    private var idAcreditado: Long = 0L
    private var datosGuardados = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_formatoparte8_sin_conexion)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

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
        integrantesTrabajandoEt = findViewById(R.id.hogar_integrantes_trabajando)
        solicitanteActivoEt = findViewById(R.id.solicitante_activo)
        ocupacionActualEt = findViewById(R.id.solicitante_ocupacion_actual)
        desempleadoTiempoEt = findViewById(R.id.solicitante_desempleado_tiempo)
        empresaPreviaEt = findViewById(R.id.solicitante_empresa_previa)
        antiguedadTrabajoAnteriorEt = findViewById(R.id.solicitante_antiguedad_empleo_anterior)
        institucionTrabajoEt = findViewById(R.id.institucion_trabajo_solicitante)
        actividadRemuneradaEt = findViewById(R.id.actividad_remunerada_solicitante)
        contratoLaboralEt = findViewById(R.id.contrato_laboral_solicitante)
        ingresoMensualEt = findViewById(R.id.solicitante_ingreso_mensual)
        empresaSolicitanteEt = findViewById(R.id.solicitante_empresa)
        antiguedadSolicitanteEt = findViewById(R.id.solicitante_antiguedad)
        comprobanteIngresosEt = findViewById(R.id.comprobante_ingresos_solicitante)
        institucionCotizacionEt = findViewById(R.id.institucion_cotizacion_solicitante)
        ingresosConceptosEt = findViewById(R.id.ingreso_conceptos_solicitante)

        guardarBtn = findViewById(R.id.btnGuardar)
        siguienteBtn = findViewById(R.id.btnSiguiente)
    }

    private fun setupButtons() {
        guardarBtn.setOnClickListener {
            if (validarCampos()) {
                guardarDatosSolicitante()
            }
        }

        siguienteBtn.setOnClickListener {
            if (!datosGuardados) {
                mostrarDialogo(
                    titulo = "Advertencia",
                    mensaje = "Debes guardar los datos antes de continuar",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFFFA000.toInt(),
                    onAceptar = { guardarDatosSolicitante() }
                )
            } else {
                irASiguiente()
            }
        }
    }

    private fun validarCampos(): Boolean {
        // Validar campos obligatorios
        val camposRequeridos = listOf(
            integrantesTrabajandoEt to "Integrantes trabajando en el hogar",
            solicitanteActivoEt to "¿El solicitante está activo laboralmente?",
            ocupacionActualEt to "Ocupación actual",
            ingresoMensualEt to "Ingreso mensual"
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

        // Validar que los campos numéricos sean válidos
        val camposNumericos = listOf(
            integrantesTrabajandoEt to "Integrantes trabajando",
            ingresoMensualEt to "Ingreso mensual",
            antiguedadTrabajoAnteriorEt to "Antigüedad en trabajo anterior",
            antiguedadSolicitanteEt to "Antigüedad del solicitante"
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

        // Validación condicional para desempleado
        if (solicitanteActivoEt.text.toString().trim().equals("No", ignoreCase = true)) {
            if (desempleadoTiempoEt.text.toString().trim().isEmpty()) {
                mostrarDialogo(
                    titulo = "Campo requerido",
                    mensaje = "Debe especificar el tiempo de desempleo",
                    iconoResId = android.R.drawable.ic_dialog_alert,
                    colorTitulo = 0xFFD32F2F.toInt()
                )
                desempleadoTiempoEt.requestFocus()
                return false
            }
        }

        return true
    }

    private fun guardarDatosSolicitante() {
        val datos = DatosSolicitanteEntity(
            hogar_integrantes_trabajando = integrantesTrabajandoEt.text.toString().trim(),
            solicitante_activo = solicitanteActivoEt.text.toString().trim(),
            solicitante_ocupacion_actual = ocupacionActualEt.text.toString().trim(),
            solicitante_desempleado_tiempo = desempleadoTiempoEt.text.toString().trim(),
            solicitante_empresa_previa = empresaPreviaEt.text.toString().trim(),
            solicitante_antiguedad_trabajo_anterior = antiguedadTrabajoAnteriorEt.text.toString().trim(),
            institucion_trabajo_solicitante = institucionTrabajoEt.text.toString().trim(),
            actividad_remunerada_solicitante = actividadRemuneradaEt.text.toString().trim(),
            contrato_laboral_solicitante = contratoLaboralEt.text.toString().trim(),
            solicitante_ingreso_mensual = ingresoMensualEt.text.toString().trim(),
            solicitante_empresa = empresaSolicitanteEt.text.toString().trim(),
            solicitante_antiguedad = antiguedadSolicitanteEt.text.toString().trim(),
            comprobante_ingresos_solicitante = comprobanteIngresosEt.text.toString().trim(),
            institucion_cotizacion_solicitante = institucionCotizacionEt.text.toString().trim(),
            ingresos_conceptos_solicitante = ingresosConceptosEt.text.toString().trim(),
            id_acreditado = idAcreditado.toString()
        )

        CoroutineScope(Dispatchers.IO).launch {
            try {
                database.datosSolicitanteDao().insertDatosSolicitante(datos)
                withContext(Dispatchers.Main) {
                    datosGuardados = true
                    mostrarDialogo(
                        titulo = "Éxito",
                        mensaje = "Datos laborales guardados correctamente",
                        iconoResId = android.R.drawable.ic_dialog_info,
                        colorTitulo = 0xFF388E3C.toInt()
                    )
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    mostrarErrorInesperado("Error al guardar datos laborales: ${e.message ?: "Error desconocido"}")
                }
            }
        }
    }

    private fun irASiguiente() {
        val intent = Intent(this, Formatoparte9SinConexion::class.java)
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